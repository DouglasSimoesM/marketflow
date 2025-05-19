# Usa a imagem oficial do Java 21
FROM eclipse-temurin:21-jdk-alpine

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR gerado pelo Spring Boot para o contêiner
COPY target/ms-notificacao-0.0.1-SNAPSHOT.jar /app/ms-notificacao.jar

# Expõe a porta onde o microsserviço rodará
EXPOSE 8081

# Comando para iniciar a aplicação
ENTRYPOINT ["sh", "-c", "sleep 5 && java -jar /app/ms-notificacao.jar"]
