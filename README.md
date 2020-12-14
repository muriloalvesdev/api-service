[![codecov](https://codecov.io/gh/muriloalvesdev/api-service/branch/main/graph/badge.svg?token=GEX15Z6X6Y)](https://codecov.io/gh/muriloalvesdev/api-service)
[![Build Status](https://travis-ci.com/muriloalvesdev/api-service.svg?branch=main)](https://travis-ci.com/muriloalvesdev/api-service)
[![Java Code Style](https://img.shields.io/badge/code%20style-eclipse-brightgreen.svg?style=flat)](https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml "Eclipse/STS Code Style")
[![Java Code Style](https://img.shields.io/badge/code%20style-intellij-brightgreen.svg?style=flat)](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml "Intellij Code Style")
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Tecnologias 
- [Spring Boot](https://spring.io/projects/spring-boot) - Framework de Desenvolvimento para a Linguagem Java.

- [Lombok](https://projectlombok.org/) - Biblioteca Java focada em produtividade e redução de código boilerplate que, por meio de anotações adicionadas ao nosso código, ensinamos o compilador (maven ou gradle) durante o processo de compilação a criar código Java.

- [JUnit5](https://junit.org/junit5/) - Framework facilita a criação e manutenção do código para a automação de testes com apresentação dos resultados.

- [Mockito](https://site.mockito.org/) - Estrutura de teste de código aberto para Java liberada sob a licença MIT. A estrutura permite a criação de objetos duplos de teste em testes de unidade automatizados com o objetivo de desenvolvimento orientado a teste ou desenvolvimento orientado a comportamento.

- [Docker](https://www.docker.com/) - Plataforma open source que facilita a criação e administração de ambientes isolados. Ele possibilita o empacotamento de uma aplicação ou ambiente dentro de um container, se tornando portátil para qualquer outro host que contenha o Docker instalado.

- [H2 database](https://www.h2database.com/html/main.html) - Banco em memória

- [Swagger](https://swagger.io/) - Essencialmente uma linguagem de descrição de interface para descrever APIs RESTful expressas usando JSON.


## Pré requisitos
 - [Maven](https://maven.apache.org/) - Ferramenta de automação de compilação utilizada primariamente em projetos Java.
 - [Docker](https://docs.docker.com/get-docker/) - Execução de aplicativos de containers.

## Iniciando projeto
 - Clone o projeto ` $ git clone https://github.com/muriloalvesdev/api-service.git` e acesse o diretório do projeto `cd api-service`.
 - Basicamente, você só precisa executar o script `docker-run.sh`. Dê permissão de execução para o arquivo `$ chmod +x docker-run.sh`
 - Execute o script `$ ./docker-run.sh`
 - O script vai gerar uma imagem do projeto e criar um container do projeto.
 - Para testar se o projeto está ativo, veja a documentação das APIs do projeto [clicando aqui](http://localhost:8082/swagger-ui.html)
 
## Requisições
- As APIs tem autenticação JWT, então é preciso realizar um cadastro do seu usuário. O status (response) desta requisição é `201 CREATED` Abaixo o JSON para se registrar na aplicação:

##### endpoint: `http://localhost:8082/api/auth/register` (HTTP POST)
```
{
    "name": "Murilo",
    "lastname": "Alves",
    "role": ["admin"],
    "email": "murilohenrique.ti@outlook.com.br",
    "password": "1234567890"
}
```

- Após se registrar, é necessário realizar um login para obter seu token de autenticação. JSON para realizar login:

##### endpoint: `http://localhost:8082/api/auth/login` (HTTP POST)

```
{
    "email": "murilohenrique.ti@outlook.com.br",
    "password": "1234567890"
}
```

-  O status (response) da requisição de login é `200 OK` e também é retornado um JSON:
```
{
    "access_token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtdXJpbG9oZW5yaXF1ZS50aUBvdXRsb29rLmNvbS5iciIsImlhdCI6MTYwNzg5NjQ0NCwidXNlcm5hbWUiOiJNdXJpbG8iLCJleHAiOjE2MDc5Mzk2NDR9.vg6J8k2Y8qaIlKkhpFaGwvw-1UMgNpnY-L5FKqEpb70Z4KWHRRIQkkdl-PwxyZ3d3aAHPkvSMGMFTtw4WqvTCQ"
}
```

### Calcular digito unico 
- Endpoint: `http://localhost:8082/api/digit/{single-digit}/{quantity}` (HTTP GET)
- Tipo de autenticação `Bearer Token`

#### Contexto
Dado um número não decimal, precisamos encontrar o dígito único deste número.
- Se `{single-digit}` tem apenas um dígito, então seu dígito único é `{single-digit}`.
- Caso contrário, o dígito único de `{single-digit}` é igual ao dígito único da soma dos dígitos de `{single-digit}`.

Exemplo, o dígito 9875 será calculado como:
digitoUnico(9875) 9+8+7+5 = `29`
digitoUnico(29) 2+9 = `11`
digitoUnico(11) 1+1= `2`

A partir de dois números `{single-digit}` e `{quantity}`, P deverá ser criado da concatenação da string `{single-digit}` * `{quantity}`.

Exemplo:
- `{single-digit}` = 9875 e `{quantity}` = 4 -> p=9875 9875 9875 9875
- 9+8+7+5+9+8+7+5+9+8+7+5+9+8+7+5=116
- digitoUnico(116) =8

Exemplo requisição:
```
http://localhost:8082/api/digit/9875/4
```

Exemplo response:
```
{
    "response": 8
}
```
