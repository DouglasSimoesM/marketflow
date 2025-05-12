package com.simoes.ms_pedido.entity.dto;

public record PedidoDto(Long idUsuario, int quantidade, String item, String getValorTotalFmt, String observacao) {
}