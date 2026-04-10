package com.guilda.audit.repository;

import com.guilda.audit.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Parte 1 — listar roles com suas permissions
    @Query("SELECT DISTINCT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.organizacao.id = :orgId")
    List<Role> findByOrganizacaoIdWithPermissions(@Param("orgId") Long orgId);
}
