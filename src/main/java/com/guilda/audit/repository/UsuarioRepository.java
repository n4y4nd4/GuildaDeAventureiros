package com.guilda.audit.repository;

import com.guilda.audit.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Parte 1 — listar usuários com suas roles
    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.organizacao.id = :orgId")
    List<Usuario> findByOrganizacaoIdWithRoles(@Param("orgId") Long orgId);
}
