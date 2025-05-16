package com.simoes.ms_notificacao.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Produto {

    private Long id;
    private Long produtoId;
    private String nomeVendedor;
    private String telefone;
    private String loja;
    private String nomeItem;
    private int quantidade;
    private double valor;

    private Vendedor vendedor;
}
