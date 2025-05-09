package com.simoes.ms_vendedor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PedidoProcessado {

        private Long id;

        private Long idUsuario;
        private String item;
        private int quantidade;
        private BigDecimal valor;
        private BigDecimal valorTotal;
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
