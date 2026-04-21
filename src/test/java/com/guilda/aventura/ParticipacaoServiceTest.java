package com.guilda.aventura;

import com.guilda.audit.entity.Organizacao;
import com.guilda.audit.entity.Usuario;
import com.guilda.audit.entity.UsuarioStatus;
import com.guilda.aventura.entity.Aventureiro;
import com.guilda.aventura.entity.Missao;
import com.guilda.aventura.entity.ParticipacaoMissao;
import com.guilda.aventura.enums.ClasseAventureiro;
import com.guilda.aventura.enums.NivelPerigo;
import com.guilda.aventura.enums.PapelMissao;
import com.guilda.aventura.enums.StatusMissao;
import com.guilda.aventura.repository.AventureiroRepository;
import com.guilda.aventura.repository.MissaoRepository;
import com.guilda.aventura.repository.ParticipacaoMissaoRepository;
import com.guilda.aventura.service.ParticipacaoService;
import com.guilda.common.exception.RequisicaoInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ParticipacaoServiceTest {

    @Mock AventureiroRepository aventureiroRepository;
    @Mock MissaoRepository missaoRepository;
    @Mock ParticipacaoMissaoRepository participacaoRepository;

    @InjectMocks ParticipacaoService service;

    private Organizacao org;
    private Organizacao outraOrg;
    private Aventureiro aventureiroAtivo;
    private Aventureiro aventureiroInativo;
    private Missao missaoAberta;
    private Missao missaoConcluida;
    private Missao missaoOutraOrg;

    @BeforeEach
    void setup() {
        org = criarOrg(1L, "Guilda Principal");
        outraOrg = criarOrg(2L, "Outra Guilda");

        Usuario usuario = criarUsuario(org);

        aventureiroAtivo = new Aventureiro(org, usuario, "Aldric", ClasseAventureiro.GUERREIRO, 10);
        setId(aventureiroAtivo, 1L);

        aventureiroInativo = new Aventureiro(org, usuario, "Zorn", ClasseAventureiro.LADINO, 3);
        aventureiroInativo.setAtivo(false);
        setId(aventureiroInativo, 2L);

        missaoAberta = new Missao(org, "Missão Aberta", NivelPerigo.MEDIO);
        setId(missaoAberta, 10L);

        missaoConcluida = new Missao(org, "Missão Concluída", NivelPerigo.ALTO);
        missaoConcluida.setStatus(StatusMissao.CONCLUIDA);
        setId(missaoConcluida, 11L);

        missaoOutraOrg = new Missao(outraOrg, "Missão Outra Org", NivelPerigo.BAIXO);
        setId(missaoOutraOrg, 12L);
    }

    // ── Teste 1: aventureiro inativo não pode participar ──────────────────────

    @Test
    void associar_aventureiroInativo_lancaExcecao() {
        when(aventureiroRepository.findById(2L)).thenReturn(Optional.of(aventureiroInativo));

        assertThatThrownBy(() ->
            service.associar(10L, 2L, PapelMissao.SUPORTE, BigDecimal.ZERO, false))
            .isInstanceOf(RequisicaoInvalidaException.class)
            .hasMessageContaining("inativo");

        // Garante que nenhuma persistência foi tentada
        verify(participacaoRepository, never()).save(any());

        System.out.println("[ParticipacaoServiceTest] Aventureiro inativo rejeitado corretamente.");
    }

    // ── Teste 2: organizações diferentes não podem se misturar ───────────────

    @Test
    void associar_organizacoesDiferentes_lancaExcecao() {
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiroAtivo));
        when(missaoRepository.findById(12L)).thenReturn(Optional.of(missaoOutraOrg));

        assertThatThrownBy(() ->
            service.associar(12L, 1L, PapelMissao.LIDER, BigDecimal.TEN, false))
            .isInstanceOf(RequisicaoInvalidaException.class)
            .hasMessageContaining("organizações diferentes");

        verify(participacaoRepository, never()).save(any());

        System.out.println("[ParticipacaoServiceTest] Organização cruzada rejeitada corretamente.");
    }

    // ── Teste 3: missão concluída não aceita novos participantes ─────────────

    @Test
    void associar_missaoConcluida_lancaExcecao() {
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiroAtivo));
        when(missaoRepository.findById(11L)).thenReturn(Optional.of(missaoConcluida));

        assertThatThrownBy(() ->
            service.associar(11L, 1L, PapelMissao.COMBATENTE, BigDecimal.ZERO, false))
            .isInstanceOf(RequisicaoInvalidaException.class)
            .hasMessageContaining("CONCLUIDA");

        verify(participacaoRepository, never()).save(any());

        System.out.println("[ParticipacaoServiceTest] Missão concluída rejeitada corretamente.");
    }

    // ── Teste 4: caminho feliz — participação válida é persistida ────────────

    @Test
    void associar_dadosValidos_persisteParticipacao() {
        when(aventureiroRepository.findById(1L)).thenReturn(Optional.of(aventureiroAtivo));
        when(missaoRepository.findById(10L)).thenReturn(Optional.of(missaoAberta));
        when(participacaoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ParticipacaoMissao resultado = service.associar(
            10L, 1L, PapelMissao.LIDER, new BigDecimal("150.00"), true);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getPapel()).isEqualTo(PapelMissao.LIDER);
        assertThat(resultado.getRecompensaOuro()).isEqualByComparingTo("150.00");
        assertThat(resultado.isDestaque()).isTrue();

        verify(participacaoRepository).save(any());

        System.out.println("[ParticipacaoServiceTest] Participação válida persistida: "
            + resultado.getPapel() + " | recompensa=" + resultado.getRecompensaOuro());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Organizacao criarOrg(Long id, String nome) {
        Organizacao org = new Organizacao(nome);
        setId(org, id);
        return org;
    }

    private Usuario criarUsuario(Organizacao org) {
        return new Usuario(org, "Admin", "admin@test.com", "hash", UsuarioStatus.ATIVO);
    }

    private void setId(Object obj, Long id) {
        try {
            var field = obj.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(obj, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
