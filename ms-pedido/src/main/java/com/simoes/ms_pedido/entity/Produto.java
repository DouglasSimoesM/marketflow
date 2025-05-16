package com.simoes.ms_pedido.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long produtoId;
    private String nomeVendedor;
    private String telefone;
    private String loja;
    private String nomeItem;
    private int quantidade;
    private double valor;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    @JsonBackReference
    private Vendedor vendedor;
}
