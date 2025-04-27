package com.simoes.ms_vendedor.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;

}
