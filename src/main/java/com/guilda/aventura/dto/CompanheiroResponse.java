package com.guilda.aventura.dto;

import com.guilda.aventura.entity.Companheiro;
import com.guilda.aventura.enums.EspecieCompanheiro;

public record CompanheiroResponse(
        String nome,
        EspecieCompanheiro especie,
        Integer lealdade
) {
    public static CompanheiroResponse de(Companheiro c) {
        if (c == null) return null;
        return new CompanheiroResponse(c.getNome(), c.getEspecie(), c.getLealdade());
    }
}
