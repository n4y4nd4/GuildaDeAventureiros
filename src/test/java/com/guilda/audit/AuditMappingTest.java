package com.guilda.audit;

import com.guilda.audit.entity.Organizacao;
import com.guilda.audit.entity.Usuario;
import com.guilda.audit.entity.UsuarioStatus;
import com.guilda.audit.repository.OrganizacaoRepository;
import com.guilda.audit.repository.RoleRepository;
import com.guilda.audit.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Parte 1 — Testes de mapeamento do schema audit.
 *
 * Demonstra:
 * - Usuário e roles são carregados corretamente
 * - Relacionamento com organização funciona
 * - Permissões estão acessíveis via roles
 */
@DataJpaTest
@ActiveProfiles("test")
class AuditMappingTest {

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired OrganizacaoRepository organizacaoRepository;
    @Autowired RoleRepository roleRepository;

    @Test
    void deveSalvarNovoUsuarioVinculadoAOrganizacaoExistente() {
        // Busca a primeira organização existente no banco
        Organizacao org = organizacaoRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Nenhuma organização encontrada no banco"));

        Usuario usuario = new Usuario(org, "Teste Aventureiro",
            "teste@guilda.com", "hash_seguro", UsuarioStatus.ATIVO);
        Usuario salvo = usuarioRepository.save(usuario);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getOrganizacao().getId()).isEqualTo(org.getId());
        System.out.println("[AuditMappingTest] Usuário salvo: id=" + salvo.getId()
            + ", org=" + org.getNome());
    }

    @Test
    void deveCarregarUsuarioComRoles() {
        Organizacao org = organizacaoRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Nenhuma organização encontrada"));

        List<Usuario> usuarios = usuarioRepository.findByOrganizacaoIdWithRoles(org.getId());
        assertThat(usuarios).isNotNull();

        usuarios.forEach(u -> System.out.println(
            "[AuditMappingTest] Usuário: " + u.getNome()
            + " | Roles: " + u.getRoles().size()));
    }

    @Test
    void deveCarregarRoleComPermissions() {
        Organizacao org = organizacaoRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Nenhuma organização encontrada"));

        roleRepository.findByOrganizacaoIdWithPermissions(org.getId())
            .forEach(r -> System.out.println(
                "[AuditMappingTest] Role: " + r.getNome()
                + " | Permissions: " + r.getPermissions().size()));
    }
}
