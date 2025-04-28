package com.simoes.ms_notificacao.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoSnsService {

    @Autowired
    private AmazonSNS amazonSNS;

    public void notificar(String telefone, String mensagem){
        System.out.println(telefone);
        telefone = null;
        System.out.println(telefone);

        PublishRequest publishRequest = new PublishRequest().withMessage(mensagem).withPhoneNumber(telefone);
        amazonSNS.publish(publishRequest);
    }
}
