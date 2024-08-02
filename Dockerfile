FROM openjdk:17-alpine

ADD target/rekomendasos-0.0.1-SNAPSHOT.jar rekomendasos.jar

CMD ["java", "-jar", "rekomendasos.jar"]