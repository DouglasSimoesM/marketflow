package com.simoes.ms_notificacao.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Estoque {

    private Long id;
    private Long produtoId;
    private String nomeItem;
    private int quantidade;
}
