package com.simoes.ms_pedido.repository;

import com.simoes.ms_pedido.entity.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
}
