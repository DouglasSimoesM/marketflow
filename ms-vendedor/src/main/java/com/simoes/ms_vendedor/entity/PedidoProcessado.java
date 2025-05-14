package com.simoes.ms_vendedor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoProcessado {

        private Long id;

        private Long idUsuario;
        private String nomeItem;
        private int quantidade;
        private double valor;
        private double valorTotal;
        private String status;
        private boolean aprovado;
        private String observacao;

        @ManyToOne
        @JoinColumn(name = "usuario_id", nullable = false)
        private Usuario usuario;

        @ManyToOne
        @JoinColumn(name = "carrinho_id")
        @JsonBackReference
        private Carrinho carrinho;

}
