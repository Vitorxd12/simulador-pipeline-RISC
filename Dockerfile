# Simulador Pipeline RISC — frontend web + motor Java ORIGINAL (src/, sem alterações).
# Build:  docker build -t simulador-risc .
# Run:    docker run --rm -p 8080:8080 simulador-risc   ->  http://localhost:8080

FROM docker.io/library/eclipse-temurin:21-jdk AS build
WORKDIR /build
COPY src ./src
COPY web/*.java ./web/
RUN mkdir classes \
 && javac --release 21 -encoding UTF-8 -d classes $(find src web -name '*.java') \
 && jar --create --file simulador.jar -C classes .

FROM docker.io/library/eclipse-temurin:21-jre
RUN useradd --system --uid 10001 --home-dir /app app
WORKDIR /app
COPY --from=build /build/simulador.jar ./simulador.jar
COPY docs ./docs
USER app
ENV PORT=8080 STATIC_DIR=/app/docs MAX_CONCORRENTES=4
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s CMD ["sh", "-c", "exec 3<>/dev/tcp/127.0.0.1/$PORT && printf 'GET /healthz HTTP/1.0\\r\\n\\r\\n' >&3 && grep -q ok <&3"]
CMD ["java", "-Xmx128m", "-cp", "simulador.jar", "Servidor"]
