package com.simoes.ms_notificacao.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_logs")
public class Logs {

    @Id
    private String id;
    private Long clienteId;
    private Long pedidoId;
    private String status;
    private String observacao;

}
