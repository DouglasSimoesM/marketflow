package com.simoes.ms_notificacao.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pedido {

    private Long id;
    private Long produtoId;
    private Long idUsuario;
    private String nomeItem;
    private int quantidade;
    private double valor;
    private double valorTotal;
    private String status;
    private boolean aprovado;
    private String observacao;

    private Usuario usuario;

    private Carrinho carrinho;

}

