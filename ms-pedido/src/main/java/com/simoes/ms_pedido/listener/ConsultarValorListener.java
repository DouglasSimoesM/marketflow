package com.simoes.ms_pedido.listener;

import com.simoes.ms_pedido.entity.Carrinho;
import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.repository.PedidoRepository;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import com.simoes.ms_pedido.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarValorListener {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    // Receber pedido de ms-vendedor para retornar valor ao cliente
    @RabbitListener(queues = "${rabbitmq.queue.consultar.valor}")
    public void consultarValor(Pedido pedido){
        try {
            Usuario usuario = usuarioRepository.findById(pedido.getIdUsuario()).orElseThrow(()-> new RuntimeException("Usuario não encontrado !!!"));

            // Caso todas validações tenham dado certo, informar o cliente com valor do produto e valor total de sua possivel compra
            if(pedido.isAprovado()){
                pedido.setUsuario(usuario);
                pedido.setCarrinho(usuario.getCarrinho());
                pedido.setValorTotal(pedido.getQuantidade() * pedido.getValor());
                usuarioRepository.save(usuario);
                pedidoRepository.save(pedido);
            } else { // Caso exista algum erro, finalizar pedido adicionando em PedidoProcessado
                pedidoService.adicionarPedidoProcessado(usuario, List.of(pedido));
            }
        } catch (RuntimeException e){

        }
    }
}
