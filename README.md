# 🛒 MarketFlow - Microsserviço de Processamento de Pedidos no Marketplace

Este projeto foi concebido, desenvolvido e implementado por mim, **Douglas**, com o objetivo de criar um microsserviço **robusto** e **escalável** para o processamento de pedidos em marketplaces.

O **MarketFlow** utiliza **Java 21 + Spring Boot**, garantindo **mensageria assíncrona** via **RabbitMQ** e **notificações dinâmicas** com **AWS SNS**.

---

## 🔹 Arquitetura do Sistema

O projeto segue uma arquitetura baseada em **microsserviços**, proporcionando:
- Alta escalabilidade
- Baixa latência
- Separação clara de responsabilidades

---

## 📦 Microsserviços

### 1️⃣ MS Pedido (Gestão de Pedidos & Usuários)
- Registra usuários e permite pedidos via API REST
- Solicita preços de produtos e envia consulta ao `ms-vendedor` via RabbitMQ
- Armazena pedidos, lista de compras e histórico de transações no PostgreSQL

### 🚚 2️⃣ MS Vendedor (Gestão de Vendedores & Estoque)
- Consulta produtos em estoque antes de aprovar pedidos
- Caso o item não exista, notifica o `ms-notificação` para alertar sobre a demanda
- Armazena dados de vendedor, loja, catálogo de produtos e estoque no PostgreSQL

#### 🏷️📣 Integração Especial com a Magazine Luiza
Quando o vendedor é da **Magazine Luiza**, o `ms-vendedor` aplica uma lógica diferenciada:

- Retorna **nome** e **telefone** do vendedor na resposta
- Permite **negociação personalizada** com o cliente
- Possibilita **ajuste de preços**, **ofertas exclusivas** e **maior taxa de conversão**

💼 Essa estratégia reforça o diferencial competitivo da Magazine Luiza, aumentando o potencial de **engajamento** e **fidelização** de clientes.

### 📢 3️⃣ MS Notificação (Notificações via AWS SNS)
- Envia notificações aos clientes via **AWS SNS**
- Notifica vendedores sobre demanda de produtos sem estoque
- Notifica clientes sobre o status dos pedidos (aceitos, em transporte, etc.)

---

## 🔄 Fluxo de Pedido (Com RabbitMQ)

1. **Usuário solicita o preço de um produto**
   🔁 Mensagem é enviada para a exchange `consultar-valor` com a routing key `consultar-valor.ms-vendedor`
   📩 `ms-vendedor` consome da fila `consultar-valor.ms-vendedor`

2. **ms-vendedor consulta o estoque**
   🔁 Caso o produto **não exista**, envia mensagem para a exchange `consultar-valor` com a routing key `consulta-concluida.ms-notificacao`
   📩 `ms-notificação` consome da fila `consulta-concluida.ms-notificacao` e notifica os vendedores sobre o interesse no produto

3. **ms-vendedor envia resposta com preço ao ms-pedido**
   🔁 Via exchange `consultar-valor` com a routing key `consultar-valor.ms-pedido`
   📩 `ms-pedido` consome da fila `consultar-valor.ms-pedido` e apresenta o valor ao usuário

4. **Usuário finaliza o pedido**
   🔁 Pedido é enviado via exchange `pedido-pendente` com a routing key `pedido-pendente.ms-vendedor`
   📩 `ms-vendedor` consome da fila `pedido-pendente.ms-vendedor`, reconfirma o estoque

5. **ms-vendedor valida e aceita o pedido**
   🔁 Envia evento de status para a exchange `situacao-pedido` (Fanout)
   📩 `ms-pedido` e `ms-notificação` consomem das filas `situacao-pedido.ms-pedido` e `situacao-pedido.ms-notificacao`

6. **ms-notificação avisa o cliente via AWS SNS**
   📬 Confirma que o pedido foi aceito e encaminhado à transportadora

---

## 💡 Tecnologias Utilizadas

### 🚀 Back-end
- **Java 21** – Alta performance e segurança
- **Spring Boot** – Framework moderno para APIs REST e microsserviços
- **Maven** – Gerenciamento de dependências

### 📡 Mensageria & Comunicação
- **RabbitMQ** – Comunicação assíncrona entre microsserviços
- **AWS SNS** – Notificações automáticas

### 🗄 Banco de Dados
- **PostgreSQL** – Banco relacional confiável e eficiente

### 📦 Infraestrutura & Deploy
- **Docker** – Conteinerização e deploy automatizado
- **Swagger** – Documentação interativa das APIs

---

## 🤝 Contato

- 📱 WhatsApp: [+55 15 99871-2209](https://wa.me/5515998712209)
- 🔗 LinkedIn: [linkedin.com/in/douglassimoes-maciel](https://linkedin.com/in/douglassimoes-maciel)

---

## 🏆 Diferencial do Projeto

> A integração especial com a **Magazine Luiza** proporciona uma **experiência única** ao cliente, com **comunicação direta**, **estratégias comerciais personalizadas**, e **impulso real nas vendas**.

---
