package com.simoes.ms_notificacao.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Vendedor {

    private Long id;
    private String nome;
    private String loja;
    private String cpf;
    private String telefone;

    private List<Produto> produto;

}
