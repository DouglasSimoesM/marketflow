package com.simoes.ms_notificacao.repository;

import com.simoes.ms_notificacao.entity.LogsPedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsPedidoRepository extends MongoRepository<LogsPedido, String> {
    List<LogsPedido> findByIdUsuario(Long idUsuario);
}
