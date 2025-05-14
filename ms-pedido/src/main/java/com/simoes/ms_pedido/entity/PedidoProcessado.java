package com.simoes.ms_pedido.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_pedido_finalizado")
public class PedidoProcessado {

        @Id
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

        public String getValorTotalFmt() {
                NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                return formatador.format(this.valorTotal);
        }
}
