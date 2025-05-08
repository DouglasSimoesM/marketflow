package com.simoes.ms_notificacao.entity;

import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Carrinho {

    private Long id;

    private Usuario usuario;

    private List<Pedido> pedidos = new ArrayList<>();

    private List<PedidoProcessado> pedidoProcessado = new ArrayList<>();
}

