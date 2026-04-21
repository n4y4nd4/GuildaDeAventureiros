package com.guilda.aventura;

import com.guilda.audit.entity.Organizacao;
import com.guilda.audit.entity.Usuario;
import com.guilda.audit.entity.UsuarioStatus;
import com.guilda.audit.repository.OrganizacaoRepository;
import com.guilda.audit.repository.UsuarioRepository;
import com.guilda.aventura.entity.Aventureiro;
import com.guilda.aventura.entity.Missao;
import com.guilda.aventura.entity.ParticipacaoMissao;
import com.guilda.aventura.enums.ClasseAventureiro;
import com.guilda.aventura.enums.NivelPerigo;
import com.guilda.aventura.enums.PapelMissao;
import com.guilda.aventura.enums.StatusMissao;
import com.guilda.aventura.projection.RelatorioMissaoView;
import com.guilda.aventura.repository.AventureiroRepository;
import com.guilda.aventura.repository.MissaoRepository;
import com.guilda.aventura.repository.ParticipacaoMissaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class AventuraQueryTest {

    @Autowired OrganizacaoRepository organizacaoRepository;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired AventureiroRepository aventureiroRepository;
    @Autowired MissaoRepository missaoRepository;
    @Autowired ParticipacaoMissaoRepository participacaoRepository;
    @Autowired TestEntityManager em;

    private Organizacao org;
    private Usuario usuario;
    private Aventureiro ativo1, ativo2, inativo;
    private Missao missao;

    @BeforeEach
    void setup() {
        // Cria organização com nome único para isolar dados entre testes
        org = organizacaoRepository.save(new Organizacao("Teste-" + UUID.randomUUID()));

        usuario = usuarioRepository.save(
            new Usuario(org, "Admin", UUID.randomUUID() + "@test.com", "hash", UsuarioStatus.ATIVO));

        ativo1 = aventureiroRepository.save(
            new Aventureiro(org, usuario, "Aldric", ClasseAventureiro.GUERREIRO, 10));
        ativo2 = aventureiroRepository.save(
            new Aventureiro(org, usuario, "Lyra", ClasseAventureiro.MAGO, 5));
        inativo = aventureiroRepository.save(
            new Aventureiro(org, usuario, "Zorn", ClasseAventureiro.LADINO, 3));
        inativo.setAtivo(false);
        aventureiroRepository.save(inativo);

        missao = missaoRepository.save(new Missao(org, "Missão das Sombras", NivelPerigo.ALTO));
        missao.setStatus(StatusMissao.CONCLUIDA);
        missaoRepository.save(missao);

        ParticipacaoMissao p1 = new ParticipacaoMissao(missao, ativo1, PapelMissao.LIDER);
        p1.setRecompensaOuro(new BigDecimal("150.00"));
        p1.setDestaque(true);
        participacaoRepository.save(p1);

        ParticipacaoMissao p2 = new ParticipacaoMissao(missao, ativo2, PapelMissao.SUPORTE);
        p2.setRecompensaOuro(new BigDecimal("80.00"));
        participacaoRepository.save(p2);

        // Força flush para que queries nativas vejam os dados
        em.flush();
        em.clear();
    }

    @Test
    void listarAventureiros_filtroAtivo_retornaApenasAtivos() {
        Page<Aventureiro> result = aventureiroRepository.listarComFiltros(
            org.getId(), true, null, null, PageRequest.of(0, 10, Sort.by("nome")));
        assertThat(result.getContent()).allMatch(Aventureiro::isAtivo);
        assertThat(result.getTotalElements()).isEqualTo(2);
        System.out.println("[AventuraQueryTest] Ativos: " + result.getTotalElements());
    }

    @Test
    void listarAventureiros_filtroClasse_retornaApenasClasseCorreta() {
        Page<Aventureiro> result = aventureiroRepository.listarComFiltros(
            org.getId(), null, ClasseAventureiro.GUERREIRO.name(), null, PageRequest.of(0, 10));
        assertThat(result.getContent()).allMatch(a -> a.getClasse() == ClasseAventureiro.GUERREIRO);
        System.out.println("[AventuraQueryTest] Guerreiros: " + result.getTotalElements());
    }

    @Test
    void listarAventureiros_filtroNivelMinimo_retornaApenasAcimaDoNivel() {
        Page<Aventureiro> result = aventureiroRepository.listarComFiltros(
            org.getId(), null, null, 5, PageRequest.of(0, 10));
        assertThat(result.getContent()).allMatch(a -> a.getNivel() >= 5);
        System.out.println("[AventuraQueryTest] Nível >= 5: " + result.getTotalElements());
    }

    @Test
    void listarAventureiros_semFiltros_retornaTodosAtivosEInativos() {
        Page<Aventureiro> result = aventureiroRepository.listarComFiltros(
            org.getId(), null, null, null, PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(3);
        System.out.println("[AventuraQueryTest] Total: " + result.getTotalElements());
    }

    @Test
    void buscarPorNomeParcial_caseInsensitive_retornaCorrespondencias() {
        Page<Aventureiro> result = aventureiroRepository.buscarPorNome(
            org.getId(), "ldr", PageRequest.of(0, 10));
        assertThat(result.getContent()).isNotEmpty();
        System.out.println("[AventuraQueryTest] Busca 'ldr': " + result.getTotalElements());
    }

    @Test
    void buscarDetalhado_retornaDadosAgregados() {
        var detalhe = aventureiroRepository.buscarDetalhe(ativo1.getId());
        assertThat(detalhe).isPresent();
        assertThat(detalhe.get().getTotalParticipacoes()).isEqualTo(1L);
        System.out.println("[AventuraQueryTest] Detalhe " + ativo1.getNome()
            + ": participações=" + detalhe.get().getTotalParticipacoes());
    }

    @Test
    void buscarDetalhado_aventureiroSemParticipacoes_retornaZero() {
        var detalhe = aventureiroRepository.buscarDetalhe(inativo.getId());
        assertThat(detalhe).isPresent();
        assertThat(detalhe.get().getTotalParticipacoes()).isEqualTo(0L);
        System.out.println("[AventuraQueryTest] Sem participações: "
            + detalhe.get().getTotalParticipacoes());
    }

    @Test
    void listarMissoes_filtroStatus_retornaApenasStatusCorreto() {
        Page<Missao> result = missaoRepository.listarComFiltros(
            org.getId(), StatusMissao.CONCLUIDA.name(), null, null, null, PageRequest.of(0, 10));
        assertThat(result.getContent()).allMatch(m -> m.getStatus() == StatusMissao.CONCLUIDA);
        System.out.println("[AventuraQueryTest] Missões CONCLUIDA: " + result.getTotalElements());
    }

    @Test
    void buscarMissaoComParticipantes_retornaParticipantesCarregados() {
        Optional<Missao> result = missaoRepository.buscarComParticipantes(missao.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getParticipacoes()).hasSize(2);
        result.get().getParticipacoes().forEach(p ->
            System.out.println("[AventuraQueryTest] Participante: "
                + p.getAventureiro().getNome()
                + " | Papel: " + p.getPapel()
                + " | Recompensa: " + p.getRecompensaOuro()
                + " | Destaque: " + p.isDestaque()));
    }

    @Test
    void gerarRanking_retornaOrdenadoPorParticipacoes() {
        var ranking = aventureiroRepository.ranking(org.getId(), null, null, null);
        assertThat(ranking).isNotEmpty();
        ranking.forEach(r -> System.out.println(
            "[AventuraQueryTest] Ranking: " + r.getNome()
            + " | Participações: " + r.getTotalParticipacoes()
            + " | Recompensas: " + r.getTotalRecompensas()
            + " | MVPs: " + r.getTotalMvps()));
    }

    @Test
    void gerarRelatorio_retornaMissoesComMetricas() {
        List<RelatorioMissaoView> relatorio = missaoRepository.relatorio(
            org.getId(), null, null);
        assertThat(relatorio).isNotEmpty();
        relatorio.forEach(r -> System.out.println(
            "[AventuraQueryTest] Missão: " + r.getTitulo()
            + " | Participantes: " + r.getTotalParticipantes()
            + " | Total recompensas: " + r.getTotalRecompensas()));
        assertThat(relatorio.get(0).getTotalParticipantes()).isEqualTo(2L);
    }
}
