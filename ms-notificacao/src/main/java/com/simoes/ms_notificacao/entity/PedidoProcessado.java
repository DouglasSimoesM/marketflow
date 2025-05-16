package com.simoes.ms_notificacao.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PedidoProcessado {

        private Long id;

        private Long idUsuario;
        private String nomeItem;
        private int quantidade;
        private BigDecimal valor;
        private BigDecimal valorTotal;
        private String status;
        private boolean aprovado;
        private String observacao;

        private Usuario usuario;

        private Carrinho carrinho;

}
