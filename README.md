# Micro Picpay Challenge

Este projeto foi desenvolvido através do [víde: Picpay simplificado com Java e Spring Boot!](https://www.youtube.com/watch?v=YcuscoiIN14) da Giuliana Bezerra. 

Ele consiste duas funcionalidades de realizar transações de pagamento entre dois usuários distintos, sendo um do tipo pagador e outro do tipo recebedor.

## Diagrama de arquitetura 
 ![Diagrama](Doc/Diagram.png)

 Tem dois endpoints um GET e POST para a entidade *Transaction*.

 Ambos enviam as solicitação a camada service que se conectam com o banco de dados e os serviços externos(autorização e notificação).

 ## Tecnologias utilizadas
<p>
     <a>
        <img alt="Java" src="https://img.shields.io/badge/Java-v17-blue.svg" />
    </a>
    <a>
        <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-v3.2.4-brightgreen.svg" />
    </a>
    <a>
        <img alt="Maven" src="https://img.shields.io/badge/Maven-v4.0-lightgreen.svg" />
    </a>
    <a >
        <img alt="H2" src="https://img.shields.io/badge/H2-v2.2.224-darkblue.svg" />
    </a>
    <a >
        <img alt="Kafka" src="https://img.shields.io/badge/Kafka-v3.6.1-red.svg">
    </a>
</p>


## Aprendizados


## Cobertura de testes
