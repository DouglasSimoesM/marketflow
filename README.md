# 🛒 MarketFlow - Microsserviço de Processamento de Pedidos no Marketplace
 
 Este projeto foi **concebido, desenvolvido e implementado por mim, Douglas Simões Maciel**, com o objetivo de criar um **microsserviço robusto e escalável** para processamento de pedidos em marketplaces.  
 
 MarketFlow utiliza **Java 21 + Spring Boot**, garantindo **mensageria assíncrona via RabbitMQ** e **notificações dinâmicas com AWS SNS**. O sistema oferece **gestão eficiente de pedidos, controle de estoque e logs centralizados**.
 
 ## 🔹 Arquitetura do Sistema
 
 O projeto é baseado em **microsserviços independentes**, cada um com sua responsabilidade bem definida:
 
 ### 1️⃣ **📦 MS Pedido ()**
 ✔ Desenvolvido para receber solicitações de compra via **API REST**  
 ✔ Publica mensagens na **fila ** do RabbitMQ  
 
 ### 2️⃣ **🚚 MS Vendedor ()**
 ✔ Processa pedidos recebidos do RabbitMQ  
 ✔ Valida disponibilidade de estoque  
 ✔ Publica pedidos confirmados na **fila **  
 
 ### 3️⃣ **📢 MS Notificação ()**
 ✔ Envia notificações aos clientes via **AWS SNS**  
 ✔ Armazena histórico de notificações no **MongoDB**  
 
 ---
 
 ## 💡 Tecnologias Utilizadas
 
 ✔ **Java 21**  
 ✔ **Spring Boot** (Spring Web, Spring Data JPA, Spring Cloud)  
 ✔ **RabbitMQ** (Mensageria assíncrona)  
 ✔ **AWS SNS** (Notificação por SMS)  
 ✔ **PostgreSQL** (Banco de dados para pedidos e vendedores)  
 ✔ **MongoDB** (Banco NoSQL para logs e notificações)  
 ✔ **Docker** (Conteinerização e fácil deploy)  
 ✔ **Swagger** (Documentação interativa da API)  
 
 ---
 
 ### 🤝 Contato & LinkedIn
 
 📱 WhatsApp: +55 15 99871-2209
 
 🔹 LinkedIn: linkedin.com/in/douglassimoes-maciel
 
 🚀 Este projeto foi 100% desenvolvido por mim e está aberto para contribuições e sugestões! Conecte-se comigo para discutir melhorias ou novas ideias. 😊
