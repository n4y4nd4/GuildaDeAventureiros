package com.guilda.aventura.repository;

import com.guilda.aventura.entity.ParticipacaoMissao;
import com.guilda.aventura.entity.ParticipacaoMissaoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipacaoMissaoRepository
        extends JpaRepository<ParticipacaoMissao, ParticipacaoMissaoId> {
}
