package com.simoes.ms_notificacao.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.simoes.ms_notificacao.exception.StrategyException;
import com.simoes.ms_notificacao.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoSnsService {

    @Autowired
    private AmazonSNS amazonSNS;

    private LogsRepository logsRepository;

    public void notificar(String telefone, String mensagem){
        //Validando numero de telefone
        if ((telefone == null) || !telefone.equals("5515998712209")) {
            System.out.println("Numero invalido: "+ telefone);
        }
        System.out.println("nnnnn");
//
//        PublishRequest publishRequest = new PublishRequest().withMessage(mensagem).withPhoneNumber(telefone);
//        amazonSNS.publish(publishRequest);
    }

}
