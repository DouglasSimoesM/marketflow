package com.simoes.ms_pedido.repository;

import com.simoes.ms_pedido.entity.PedidoProcessado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoProcessadoRepository extends JpaRepository<PedidoProcessado, Long> {
}
