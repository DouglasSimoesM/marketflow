# 🛒 MarketFlow - Microsserviço de Processamento de Pedidos no Marketplace

Este projeto foi concebido, desenvolvido e implementado por mim, **Douglas Simões Maciel**, com o objetivo de criar um microsserviço robusto e escalável para processamento de pedidos em marketplaces.

**MarketFlow** utiliza **Java 21 + Spring Boot**, garantindo mensageria assíncrona via **RabbitMQ**, notificações dinâmicas com **AWS SNS** e um fluxo eficiente de controle de estoque e interação com vendedores.

---

## 🔹 Arquitetura do Sistema

O projeto segue uma arquitetura baseada em **microsserviços**, garantindo **alta escalabilidade**, **baixa latência** e **separação de responsabilidades**:

### 1️⃣ 📦 MS Pedido (Gestão de Pedidos & Usuários)
- ✔ Registra usuários e permite que realizem pedidos via API REST  
- ✔ Solicita preços de produtos e envia consulta ao `ms-vendedor` via RabbitMQ  
- ✔ Salva os pedidos, lista de compras e histórico de transações no PostgreSQL  

### 2️⃣ 🚚 MS Vendedor (Gestão de Vendedores & Estoque)
- ✔ Consulta produtos no estoque antes de aprovar pedidos  
- ✔ Caso o item não exista, notifica `ms-notificação` para alertar sobre a demanda  
- ✔ Armazena dados do vendedor, loja, catálogo de produtos e estoque no PostgreSQL  

#### 🏷️ **📣 Destaque: Integração Especial com a Magazine Luiza**
> Quando o vendedor é da **Magazine Luiza**, o `ms-vendedor` aplica uma lógica diferenciada:
>
> 🔹 Retorna **nome e telefone do vendedor** na resposta  
> 🔹 Permite **negociação personalizada** entre o cliente e o vendedor  
> 🔹 Abre espaço para **ajuste de preços**, **ofertas exclusivas** e **maior taxa de conversão de vendas**
>
> 💼 Essa estratégia comercial reforça o diferencial competitivo da Magazine Luiza, aumentando o potencial de **engajamento e fidelização de clientes**.

### 3️⃣ 📢 MS Notificação (Gestão de Notificações & Histórico de Estoque)
- ✔ Envia notificações aos clientes via AWS SNS  
- ✔ Salva em MongoDB todos os produtos que ficaram sem estoque para análise futura  
- ✔ Notifica vendedores que há demanda para produtos sem estoque  
- ✔ Notifica clientes quando seus pedidos são aceitos e encaminhados para transporte  

---

## 🔄 Fluxo de Pedido

1️⃣ Usuário solicita preço → Enviado ao `ms-vendedor` via RabbitMQ  
2️⃣ `ms-vendedor` consulta estoque → Se não existir, avisa `ms-notificação`  
3️⃣ `ms-notificação` alerta vendedores via AWS SNS sobre clientes interessados  
4️⃣ Usuário finaliza compra → Enviado novamente ao `ms-vendedor`  
5️⃣ `ms-vendedor` reconfirma estoque → Se existir, pedido é confirmado  
6️⃣ `ms-notificação` avisa cliente que pedido foi aceito e enviado à transportadora  

---

## 💡 Tecnologias Utilizadas

Este projeto foi desenvolvido com as melhores tecnologias do mercado, proporcionando **escalabilidade, performance e robustez**:

### 🚀 Back-end
- ✔ Java 21  
- ✔ Spring Boot  
- ✔ Maven  

### 📡 Mensageria & Comunicação
- ✔ RabbitMQ  
- ✔ AWS SNS  

### 🗄 Banco de Dados & Persistência
- ✔ PostgreSQL  
- ✔ MongoDB  

### 📦 Infraestrutura & Deploy
- ✔ Docker  
- ✔ Swagger  

---

## 🤝 Contato & LinkedIn

📱 **WhatsApp**: [+55 15 99871-2209](https://wa.me/5515998712209)  
🔹 **LinkedIn**: [linkedin.com/in/douglassimoes-maciel](https://www.linkedin.com/in/douglassimoes-maciel)

---

> 🚀 **Este projeto foi 100% desenvolvido por mim e está aberto para contribuições e sugestões!**  
> Conecte-se comigo para discutir melhorias ou novas ideias. 😊

---

> 🏆 **Diferencial do Projeto:**  
> A integração especial com a **Magazine Luiza** proporciona uma experiência única para o cliente final, com comunicação direta e estratégias comerciais que **impulsionam as vendas de forma personalizada**.
