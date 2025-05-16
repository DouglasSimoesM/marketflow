package com.simoes.ms_notificacao.service;

import com.simoes.ms_notificacao.entity.LogsPedido;
import com.simoes.ms_notificacao.entity.Pedido;
import com.simoes.ms_notificacao.exception.StrategyException;
import com.simoes.ms_notificacao.repository.LogsPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogsService {

    private final LogsPedidoRepository logsPedidoRepository;

    public LogsService(LogsPedidoRepository logsPedidoRepository) {
        this.logsPedidoRepository = logsPedidoRepository;
    }

    public void salvarLog(Pedido pedido){

        Optional.ofNullable(pedido).ifPresent(p -> {
            try {
                LogsPedido logsPedido = new LogsPedido();
                logsPedido.setPedidoId(p.getId());
                logsPedido.setIdUsuario(p.getIdUsuario());
                logsPedido.setNome(p.getUsuario().getNome());
                logsPedido.setStatus(p.getStatus());
                logsPedido.setObservacao(p.getObservacao());

                logsPedidoRepository.save(logsPedido);

            } catch (StrategyException e) {
                System.out.println("Erro ao salvar log");
            }
        });
    }

    public List<LogsPedido> todosLogs(){
        return logsPedidoRepository.findAll();
    }

    public List<LogsPedido> buscarLogsPorId(Long clienteId){
        List<LogsPedido> idCliente =  logsPedidoRepository.findByIdUsuario(clienteId);
        if (idCliente == null){
            throw new StrategyException("O id do cliente esta invalido, tente novamente !!! ");
        }

        return idCliente;
    }
}
