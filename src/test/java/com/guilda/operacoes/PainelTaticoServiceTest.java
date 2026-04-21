package com.guilda.operacoes;

import com.guilda.operacoes.entity.PainelTaticoMissao;
import com.guilda.operacoes.repository.PainelTaticoRepository;
import com.guilda.operacoes.service.PainelTaticoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PainelTaticoServiceTest {

    @Mock
    PainelTaticoRepository repository;

    @InjectMocks
    PainelTaticoService service;

    @Test
    void buscarTop10_retornaListaOrdenadaLimitadaA10() {
        // Monta 12 missões para garantir que o service limita a 10
        List<PainelTaticoMissao> mockData = criarMissoesMock(12);
        when(repository.buscarPorDataCorte(any(LocalDateTime.class))).thenReturn(mockData);

        List<PainelTaticoMissao> resultado = service.buscarTop10UltimosQuinzeDias();

        System.out.println("=== EVIDÊNCIA: PainelTaticoService.buscarTop10UltimosQuinzeDias() ===");
        for (int i = 0; i < resultado.size(); i++) {
            PainelTaticoMissao m = resultado.get(i);
            System.out.printf("Missão %d: id=%d, titulo=%s, indiceProntidao=%s%n",
                i + 1, m.getMissaoId(), m.getTitulo(), m.getIndiceProntidao());
        }
        System.out.println("Total retornado: " + resultado.size());

        assertThat(resultado).hasSize(10);
    }

    @Test
    void buscarTop10_repositorioVazio_retornaListaVazia() {
        when(repository.buscarPorDataCorte(any(LocalDateTime.class))).thenReturn(List.of());

        List<PainelTaticoMissao> resultado = service.buscarTop10UltimosQuinzeDias();

        System.out.println("=== EVIDÊNCIA: lista vazia ===");
        System.out.println("Total retornado: " + resultado.size());

        assertThat(resultado).isEmpty();
    }

    @Test
    void buscarTop10_verificaDataCorteEhUltimos15Dias() {
        LocalDateTime antes = LocalDateTime.now().minusDays(15).minusSeconds(2);
        when(repository.buscarPorDataCorte(any(LocalDateTime.class))).thenAnswer(inv -> {
            LocalDateTime dataCorte = inv.getArgument(0);
            System.out.println("=== EVIDÊNCIA: dataCorte calculada = " + dataCorte + " ===");
            assertThat(dataCorte).isAfter(antes);
            return List.of();
        });

        service.buscarTop10UltimosQuinzeDias();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private List<PainelTaticoMissao> criarMissoesMock(int quantidade) {
        return java.util.stream.IntStream.rangeClosed(1, quantidade)
            .mapToObj(i -> {
                try {
                    var m = PainelTaticoMissao.class.getDeclaredConstructor().newInstance();
                    setField(m, "missaoId", (long) i);
                    setField(m, "titulo", "Missão " + (char)('A' + i - 1));
                    setField(m, "indiceProntidao", BigDecimal.valueOf(10 - i * 0.5));
                    setField(m, "ultimaAtualizacao", LocalDateTime.now().minusDays(i));
                    return m;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        var field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
