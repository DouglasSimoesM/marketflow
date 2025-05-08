package com.simoes.ms_notificacao.repository;

import com.simoes.ms_notificacao.entity.Logs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRepository extends MongoRepository<Logs, String> {
    List<Logs> findByIdUsuario(Long idUsuario);
}
