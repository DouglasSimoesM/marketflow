package com.simoes.ms_vendedor.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Carrinho {

    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonManagedReference
    private Usuario usuario;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Pedido> pedidos=new ArrayList<>();

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PedidoProcessado> pedidoProcessado = new ArrayList<>();
}

