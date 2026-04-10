package com.guilda.marketplace.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.guilda.marketplace.dto.ProdutoDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Questão 3 — Service com todas as buscas e agregações no índice guilda_loja.
 */
@Service
public class ElasticsearchService {

    private static final String INDEX = "guilda_loja";
    private final ElasticsearchClient client;

    public ElasticsearchService(ElasticsearchClient client) {
        this.client = client;
    }

    // ── Parte A: Buscas Textuais ──────────────────────────────────────────────

    /** GET /produtos/busca/nome — match query no campo nome */
    public List<ProdutoDTO> buscarPorNome(String termo) throws IOException {
        if (isBlank(termo)) return List.of();
        var response = client.search(s -> s
            .index(INDEX)
            .query(q -> q.match(m -> m.field("nome").query(termo))),
            Map.class);
        return mapHits(response);
    }

    /** GET /produtos/busca/descricao — match query no campo descricao */
    public List<ProdutoDTO> buscarPorDescricao(String termo) throws IOException {
        if (isBlank(termo)) return List.of();
        var response = client.search(s -> s
            .index(INDEX)
            .query(q -> q.match(m -> m.field("descricao").query(termo))),
            Map.class);
        return mapHits(response);
    }

    /** GET /produtos/busca/frase — match_phrase query */
    public List<ProdutoDTO> buscarPorFrase(String termo) throws IOException {
        if (isBlank(termo)) return List.of();
        var response = client.search(s -> s
            .index(INDEX)
            .query(q -> q.matchPhrase(m -> m.field("descricao").query(termo))),
            Map.class);
        return mapHits(response);
    }

    /** GET /produtos/busca/fuzzy — fuzzy query no campo nome */
    public List<ProdutoDTO> buscarFuzzy(String termo) throws IOException {
        if (isBlank(termo)) return List.of();
        var response = client.search(s -> s
            .index(INDEX)
            .query(q -> q.fuzzy(f -> f.field("nome").value(termo))),
            Map.class);
        return mapHits(response);
    }

    /** GET /produtos/busca/multicampos — multi_match em nome e descricao */
    public List<ProdutoDTO> buscarMultiCampos(String termo) throws IOException {
        if (isBlank(termo)) return List.of();
        var response = client.search(s -> s
            .index(INDEX)
            .query(q -> q.multiMatch(m -> m
                .fields("nome", "descricao")
                .query(termo))),
            Map.class);
        return mapHits(response);
    }

    // ── Parte B: Buscas com Filtros ───────────────────────────────────────────

    /** GET /produtos/busca/com-filtro — match na descricao + filter por categoria */
    public List<ProdutoDTO> buscarComFiltroCategoria(String termo, String categoria) throws IOException {
        if (isBlank(termo)) return List.of();
        var response = client.search(s -> s
            .index(INDEX)
            .query(q -> q.bool(b -> b
                .must(m -> m.match(mt -> mt.field("descricao").query(termo)))
                .filter(f -> f.term(t -> t.field("categoria.keyword").value(categoria))))),
            Map.class);
        return mapHits(response);
    }

    /** GET /produtos/busca/faixa-preco — range query no campo preco */
    public List<ProdutoDTO> buscarPorFaixaPreco(BigDecimal min, BigDecimal max) throws IOException {
        var response = client.search(s -> s
            .index(INDEX)
            .query(q -> q.range(r -> r
                .field("preco")
                .gte(co.elastic.clients.json.JsonData.of(min))
                .lte(co.elastic.clients.json.JsonData.of(max)))),
            Map.class);
        return mapHits(response);
    }

    /** GET /produtos/busca/avancada — bool com categoria, raridade e faixa de preço */
    public List<ProdutoDTO> buscarAvancada(String categoria, String raridade,
                                            BigDecimal min, BigDecimal max) throws IOException {
        var response = client.search(s -> s
            .index(INDEX)
            .query(q -> q.bool(b -> {
                BoolQuery.Builder bool = b;
                if (!isBlank(categoria))
                    bool = bool.filter(f -> f.term(t -> t.field("categoria.keyword").value(categoria)));
                if (!isBlank(raridade))
                    bool = bool.filter(f -> f.term(t -> t.field("raridade.keyword").value(raridade)));
                if (min != null && max != null)
                    bool = bool.filter(f -> f.range(r -> r
                        .field("preco")
                        .gte(co.elastic.clients.json.JsonData.of(min))
                        .lte(co.elastic.clients.json.JsonData.of(max))));
                return bool;
            })),
            Map.class);
        return mapHits(response);
    }

    // ── Parte C: Agregações ───────────────────────────────────────────────────

    /** GET /produtos/agregacoes/por-categoria — terms aggregation */
    public Map<String, Long> agregarPorCategoria() throws IOException {
        var response = client.search(s -> s
            .index(INDEX)
            .size(0)
            .aggregations("por_categoria", a -> a
                .terms(t -> t.field("categoria.keyword"))),
            Map.class);
        return extrairTermsBuckets(response, "por_categoria");
    }

    /** GET /produtos/agregacoes/por-raridade — terms aggregation */
    public Map<String, Long> agregarPorRaridade() throws IOException {
        var response = client.search(s -> s
            .index(INDEX)
            .size(0)
            .aggregations("por_raridade", a -> a
                .terms(t -> t.field("raridade.keyword"))),
            Map.class);
        return extrairTermsBuckets(response, "por_raridade");
    }

    /** GET /produtos/agregacoes/preco-medio — avg aggregation */
    public Double calcularPrecoMedio() throws IOException {
        var response = client.search(s -> s
            .index(INDEX)
            .size(0)
            .aggregations("preco_medio", a -> a
                .avg(avg -> avg.field("preco"))),
            Map.class);

        var agg = response.aggregations().get("preco_medio");
        if (agg == null) return 0.0;
        return agg.avg().value();
    }

    /** GET /produtos/agregacoes/faixas-preco — range aggregation */
    public Map<String, Long> distribuirPorFaixaPreco() throws IOException {
        var response = client.search(s -> s
            .index(INDEX)
            .size(0)
            .aggregations("faixas_preco", a -> a
                .range(r -> r
                    .field("preco")
                    .ranges(rng -> rng.to("100"))
                    .ranges(rng -> rng.from("100").to("300"))
                    .ranges(rng -> rng.from("300").to("700"))
                    .ranges(rng -> rng.from("700")))),
            Map.class);

        Map<String, Long> resultado = new LinkedHashMap<>();
        var buckets = response.aggregations().get("faixas_preco").range().buckets().array();
        String[] labels = {"abaixo_de_100", "100_a_300", "300_a_700", "acima_de_700"};
        for (int i = 0; i < buckets.size(); i++) {
            resultado.put(labels[i], buckets.get(i).docCount());
        }
        return resultado;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private List<ProdutoDTO> mapHits(SearchResponse<Map> response) {
        return response.hits().hits().stream()
            .map(Hit::source)
            .filter(src -> src != null)
            .map(src -> new ProdutoDTO(
                str(src, "nome"),
                str(src, "descricao"),
                str(src, "categoria"),
                str(src, "raridade"),
                src.get("preco") != null
                    ? new BigDecimal(src.get("preco").toString())
                    : null))
            .toList();
    }

    private Map<String, Long> extrairTermsBuckets(SearchResponse<Map> response, String aggName) {
        Map<String, Long> resultado = new LinkedHashMap<>();
        var buckets = response.aggregations().get(aggName).sterms().buckets().array();
        for (StringTermsBucket bucket : buckets) {
            resultado.put(bucket.key().stringValue(), bucket.docCount());
        }
        return resultado;
    }

    private String str(Map<?, ?> map, String key) {
        Object v = map.get(key);
        return v != null ? v.toString() : null;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
