package com.simoes.ms_notificacao.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoSnsService {

    private final AmazonSNS amazonSNS;

    @Autowired
    public NotificacaoSnsService(AmazonSNS amazonSNS) {
        this.amazonSNS = amazonSNS;
    }

    public void notificar(String telefone, String mensagem){


        PublishRequest publishRequest = new PublishRequest()
                .withMessage(mensagem)
                .withPhoneNumber("55"+telefone);

        amazonSNS.publish(publishRequest);  // Notifica apenas se o número for válido
    }
}
