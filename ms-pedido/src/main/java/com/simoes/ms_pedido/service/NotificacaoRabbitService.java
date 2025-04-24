package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Pedido;

public interface NotificacaoRabbitService {
    void notificar(Pedido pedido, String exchange);
}
