package com.simoes.ms_pedido.entity.dto;

import java.math.BigDecimal;

public record PedidoDTO(Long clienteId, String nome, int quantidade, String item, BigDecimal valorTotal, String observacao) {
}