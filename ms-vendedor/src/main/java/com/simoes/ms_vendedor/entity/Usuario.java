package com.simoes.ms_vendedor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Usuario {

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;


    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonBackReference
    private Carrinho carrinho;
}


