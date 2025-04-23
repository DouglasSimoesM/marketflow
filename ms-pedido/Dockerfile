# Usa a imagem oficial do Java 21
FROM eclipse-temurin:21-jdk-alpine

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR gerado pelo Spring Boot para o contêiner
COPY target/ms-pedido.jar /app/ms-pedido.jar

# Expõe a porta onde o microsserviço rodará
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "/app/ms-pedido.jar"]
