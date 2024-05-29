FROM azul/zulu-openjdk:17

WORKDIR /app

COPY target/meme-memelandia-1.0.0.jar /app/meme-ms.jar

EXPOSE 8083

CMD ["java", "-jar", "/app/meme-ms.jar"]