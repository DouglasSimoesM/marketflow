package com.simoes.ms_pedido.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Entity(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long idUsuario;
    @NotBlank(message = "O nomeItem não pode estar vazio")
    private String nomeItem;
    @NotNull(message = "A quantidade não pode ser nulo")
    private int quantidade;
//    @NotNull(message = "O valor não pode ser nulo")
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

    // Metodo para formatar `valorTotal`
    public String getValorTotalFmt() {
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatador.format(this.valorTotal);
    }
}


