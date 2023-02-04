# Share Account
Share Account With Friends Application

# Objetivo:
A aplicação foi desenvolvida mediante uma necessidade de resolver um problema do dia-a-dia que é dividir o almoço/lanche com amigos ou equipe de trabalho de uma forma justa, levando em consideração o consumo de cada indivíduo na compra. Esse serviço além de calcular o total a pagar de cada indivíduo, ainda gera um link de uma carteira de cobrança, com o valor que cada um tem que pagar.

* ### O ponto de partida foi o desafio: [link](https://github.com/ArturSch/DesafioBackendSE) ###
* ### [Miro](https://miro.com/app/board/uXjVPuBa2vM=/) ###
* ### [Doc De Integração](https://drive.google.com/file/d/1pa3nhwha25mmNthUEADZEuKN_UPTGh5R/view?usp=sharing) ###
* ### [AWS Swagger(Endereço EC2)](http://ec2-15-228-82-5.sa-east-1.compute.amazonaws.com:8080/swagger-ui/index.html) ###

### :pushpin: Features

- [x] Divide de forma justa o montante da conta para cada indivíduo.
- [x] Gera chave Pix com o valor da conta, para facilitar o acerto de contas.
- [x] Permite entrada de valores de porcentagem quando especificados(Apenas para estabelecimentos).

### Iniciando projeto pela primeira vez

- Requisitos minimos:
    * Java 11 SDK
    * Maven

```bash
# Na raiz do projeto execute o seguinte comando:
mvn compile

# Logo depois de finalizar a compilação, execute:
mvn package

# Entrar na pasta target onde foi gerado o .jar:
cd target

# Execute o .jar com o seguinte comando:
java -jar shareAccount-0.0.1-SNAPSHOT.jar

# Acesse o seguinte endereço no navegador para utilizar as apis documentadas:
http://localhost:8080/shareAccount/swagger-ui/index.html

# Se preferir tem o Dockerfile, é só executar os comandos abaixo:
docker build -t springio/shareaccount-spring-boot-docker .
docker run -p 8080:8080 springio/gs-spring-boot-docker
```

### 🛠 Detalhes Tecnicos

- Java 11
- Testes unitários
- Testes de controller

## Tecnologias

<div style="display: inline_block">

  <img align="center" alt="java" src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white" />
  <img align="center" alt="spring" src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white" />
  <img align="center" alt="swagger" src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white" />

</div>


### :sunglasses: Autor
Criado por Johonatan Seibel Da Silva.

