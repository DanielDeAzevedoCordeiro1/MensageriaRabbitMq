# Sistema de Registro de Usuários com Confirmação por Email

Uma aplicação baseada em microsserviços que gerencia o registro de usuários e envia emails de confirmação usando Spring Boot, CloudAMQP (RabbitMQ como serviço) e Java Mail.

## Arquitetura do Sistema

Este projeto consiste em dois microsserviços:

1. **Serviço CRUD** - Lida com o registro de usuários e gerenciamento de dados
2. **Serviço de Email** - Processa eventos de registro e envia emails de confirmação

Os serviços se comunicam de forma assíncrona via CloudAMQP (RabbitMQ como serviço na nuvem), seguindo uma arquitetura orientada a eventos.

![mermaid-diagram-2025-04-01-083558](https://github.com/user-attachments/assets/a24a7a5f-be28-422f-8d71-3551039a1c09)



```
┌────────────────┐     ┌────────────┐     ┌────────────────┐
│                │     │            │     │                │
│ Serviço CRUD   │────▶│ CloudAMQP  │────▶│ Serviço Email  │
│                │     │ (RabbitMQ) │     │                │
└────────────────┘     └────────────┘     └────────────────┘
```

## Tecnologias utilizadas

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **Spring AMQP** (para RabbitMQ)
- **CloudAMQP** (RabbitMQ como serviço)
- **Spring Mail**
- **Lombok**
- **Banco de Dados Relacional** (configurado com JPA)

## Estrutura do Projeto

### Serviço CRUD

Gerencia o registro e manutenção dos usuários:

- **Controllers**:
  - `RegistrationController` - Fornece endpoints REST para registro e recuperação de usuários
  
- **Models**:
  - `User` - Entidade que representa os dados do usuário
  
- **Services**:
  - `UserService` - Lógica de negócio para gerenciamento de usuários
  - `ConfirmRegisterProducer` - Envia eventos de registro para o CloudAMQP

- **Configuração**:
  - `RabbitMqConfig` - Configuração de exchanges, filas e bindings do CloudAMQP

### Serviço de Email

Processa eventos de registro de usuários e envia emails de confirmação:

- **Services**:
  - `EmailService` - Envia emails de confirmação usando JavaMailSender
  - `ConfirmRegisterConsumer` - Ouve eventos de registro do CloudAMQP

- **Configuração**:
  - `RabbitMqConfig` - Configuração do consumidor CloudAMQP
  - `MailConfig` - Configuração do JavaMailSender

## Configuração e Instalação

### Pré-requisitos

1. JDK 17 ou superior
2. Maven
3. Conta no CloudAMQP (RabbitMQ como serviço)
4. Acesso a Servidor SMTP (configuração do Gmail incluída)

### Configuração CloudAMQP

Para configurar o CloudAMQP:

1. Crie uma conta em [CloudAMQP](https://www.cloudamqp.com/)

![Captura de tela de 2025-04-01 08-22-55](https://github.com/user-attachments/assets/c4da981a-8f19-4997-a2b5-8c02499d2f9f)

2. Crie uma instância do RabbitMQ
3. Obtenha a URL de conexão AMQP fornecida pelo CloudAMQP
4. Configure a URL de conexão no arquivo `application.properties` ou `application.yml` de cada serviço:
5. Gerencie via GUI seu message broker

![Captura de tela de 2025-04-01 08-23-28](https://github.com/user-attachments/assets/74191a2d-acba-4596-9f69-5dacd135e63a)


```properties
spring.rabbitmq.addresses=amqps://seu-usuario:sua-senha@seu-servidor.cloudamqp.com/seu-vhost
```

Ambos os serviços compartilham configurações do RabbitMQ para troca de mensagens:

- **Exchange**: `imo.user.exchange` (Exchange do tipo Topic)
- **Fila**: `email-service.user.confirmation.queue`
- **Routing Key**: `user.registered.success`

### Configuração de Email

O Serviço de Email está configurado para usar SMTP do Gmail:

```properties
mail.host=smtp.gmail.com
mail.port=587
mail.username=seu-email@gmail.com
mail.password=sua-senha-de-app
```

**Observação**: Por segurança, substitua as credenciais do Gmail em `MailConfig.java` pelas suas próprias ou use variáveis de ambiente/arquivos de configuração.

## Como Funciona

1. **Registro de Usuário**:
   - O cliente envia uma requisição POST para `/api/register/create` com os detalhes do usuário
   - O Serviço CRUD salva o usuário e cria um `UserRegisteredEvent`
   - O evento é publicado no CloudAMQP com a routing key `user.registered.success`

2. **Confirmação por Email**:
   - O Serviço de Email escuta a fila `email-service.user.confirmation.queue`
   - Ao receber um evento, processa os dados do usuário
   - Um email de confirmação é enviado para o endereço de email registrado

## Endpoints da API

### API de Registro

- **Criar Usuário**:
  - `POST /api/register/create`
  - Corpo da Requisição: Objeto User com nome, email e senha
  - Resposta: UserRegisteredEvent com HTTP 201 (Created)

- **Obter Todos os Usuários**:
  - `GET /api/register/getAll`
  - Resposta: Lista de objetos UserDTO com HTTP 200 (OK)

## Executando a Aplicação

1. **Configure o CloudAMQP**:
   - Certifique-se de que sua instância CloudAMQP está ativa
   - Verifique se as credenciais de conexão estão corretas nos arquivos de configuração

2. **Configurar o Serviço de Email**:
   - Atualize as credenciais de email em `MailConfig.java` ou use variáveis de ambiente

3. **Execute Cada Serviço**:
   ```bash
   # Iniciar o Serviço CRUD
   cd crud-service
   ./mvnw spring-boot:run
   
   # Iniciar o Serviço de Email
   cd ../email-service
   ./mvnw spring-boot:run
   ```

## Monitoramento e Gerenciamento do CloudAMQP

O CloudAMQP fornece um painel de controle para monitorar:
- Status das filas e mensagens
- Taxas de publicação e consumo
- Conexões ativas
- Logs e alertas

Acesse o painel através da sua conta CloudAMQP para gerenciar sua instância RabbitMQ.

