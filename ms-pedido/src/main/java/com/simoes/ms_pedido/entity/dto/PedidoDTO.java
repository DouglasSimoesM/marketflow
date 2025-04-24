package com.simoes.ms_pedido.entity.dto;

import java.math.BigDecimal;

public record PedidoDTO(Long clienteId, String nome, String item, BigDecimal valorTotal) {
}
