package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Pedido;

public interface PedidoService {

    Pedido criarPedido(Long usuarioId, Pedido pedido);
    Pedido obterPedido(Long id);
}
