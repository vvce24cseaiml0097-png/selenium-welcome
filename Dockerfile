FROM maven:3.9-eclipse-temurin-17

RUN apt-get update && \
    apt-get install -y --no-install-recommends chromium chromium-driver && \
    rm -rf /var/lib/apt/lists/*

ENV CHROME_BIN=/usr/bin/chromium \
    CHROMEDRIVER_PATH=/usr/bin/chromedriver

WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src

CMD ["mvn", "test"]