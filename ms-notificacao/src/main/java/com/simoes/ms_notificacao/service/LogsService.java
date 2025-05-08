package com.simoes.ms_notificacao.service;

import com.simoes.ms_notificacao.entity.Logs;
import com.simoes.ms_notificacao.entity.Pedido;
import com.simoes.ms_notificacao.exception.StrategyException;
import com.simoes.ms_notificacao.repository.LogsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogsService {

    private final LogsRepository logsRepository;

    public LogsService(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    public void salvarLog(Pedido pedido){

        Optional.ofNullable(pedido).ifPresent(p -> {
            try {
                Logs logs = new Logs();
                logs.setPedidoId(p.getId());
                logs.setIdUsuario(p.getIdUsuario());
                logs.setNome(p.getUsuario().getNome());
                logs.setStatus(p.getStatus());
                logs.setObservacao(p.getObservacao());

                logsRepository.save(logs);

            } catch (StrategyException e) {
                System.out.println("Erro ao salvar log");
            }
        });
    }

    public List<Logs> todosLogs(){
        return logsRepository.findAll();
    }

    public List<Logs> buscarLogsPorId(Long clienteId){
        List<Logs> idCliente =  logsRepository.findByIdUsuario(clienteId);
        if (idCliente == null){
            throw new StrategyException("O id do cliente esta invalido, tente novamente !!! ");
        }

        return idCliente;
    }
}
