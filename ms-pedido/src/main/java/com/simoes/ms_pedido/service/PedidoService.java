package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.dto.PedidoDTO;

import java.util.List;

public interface PedidoService {

    Pedido criarPedido(Long usuarioId, Pedido pedido);
    List<PedidoDTO> obterPedido(Long id);
}
