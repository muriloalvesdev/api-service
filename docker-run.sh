mvn clean install -DskipTests -Pprod && docker build -t muriloalvesdev/api . && docker-compose up

