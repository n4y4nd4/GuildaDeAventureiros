package com.guilda.audit.entity;

/**
 * Constraint ck_audit_action: valores permitidos na coluna action de audit_entries.
 */
public enum AuditAction {
    CREATE, UPDATE, DELETE, LOGIN, LOGOUT, EXPORT, IMPORT, ERROR
}
