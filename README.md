# ACME Seguros - Insurance Quote Microservice

## Description

This microservice module is responsible for registering and retrieving **Insurance Quotes**.
It provides two endpoints: one for registering a quote and another for querying a registered quote by its ID.

<details>
<summary>The insurance quote registration process steps <b>[expand]</b> </summary>

1. Receiving the insurance quote request (`POST /insurance-quote`);
1. Validate the request:
    - Request field validation (must comply with [the endpoint specification](#Rest-API-Endpoints-descriptions));
    - Business rule validation:
        - Fetching data from the `Catalog Microservices`, which defines the characteristics of the available `Products` and `Offers` for Quotes;
        - Validating the requested quote, based on the rules of the selected Product and Offer;
1. Storing the Insurance Quote request in the database;
1. Notifying the reception of the Insurance Quote;
1. (Asynchronously) Generating the `Insurance Policy` related to the requested Quote:
    - The `Insurance Policy Microservice` consumes the notification of the received Quote;
    - It creates the Insurance Policy related to the requested Quote;
    - It notifies the generation of the Policy;
1. This microservice consumes the notification of the Policy generation;
1. It updates the Insurance Quote record in the database, adding the generated Insurance Policy ID;
1. By querying the Insurance Quote via the provided endpoint (`GET /insurance-quote/{id}`), it's possible to retrieve the Insurance Quote record;

![image](https://github.com/rkuroki/insurance-quote-ms/tree/main/misc/diagrams/insurance-quote-registration-process_sequence-diagram.png)
([diagram source](https://github.com/rkuroki/insurance-quote-ms/tree/main/misc/diagrams/insurance-quote-registration-process_sequence-diagram.puml))

</details>

## Technologies

- Java 17
- Spring Boot 3.3.3
    - Web, Data JPA, Validation, Cache, Actuator, Testcontainers
- PostgreSQL 16
- H2 (for some component tests)
- Kafka 3.6.2

## Simply Build, Run and Test

Clone the project, go to the folder and run:

``` bash
# To build:
docker-compose build
./gradlew build

# To run
docker-compose up -d
./gradlew bootRun
```

All the application variables are defined in the [application.properties](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/main/resources/application.properties) file.

#### Registering a Quote (Success):

``` bash
curl --request POST \
  --url http://localhost:8080/insurance-quote \
  --header 'Content-Type: application/json' \
  --data '{
	"product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
	"offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
	"category": "HOME",
	"total_monthly_premium_amount": 75.25,
	"total_coverage_amount": 825000.00,
	"coverages": { "Incêndio": 250000.00, "Desastres naturais": 500000.00, "Responsabiliadade civil": 75000.00 },
	"assistances": ["Encanador", "Eletricista", "Chaveiro 24h" ],
	"customer": { "document_number": "36205578900", "name": "John Wick", "type": "NATURAL", "gender": "MALE", "date_of_birth": "1973-05-02", "email": "johnwick@gmail.com", "phone_number": 11950503030 }
}'
```

#### Getting a registered Quote by ID:

``` bash
curl --request GET \
  --url http://localhost:8080/insurance-quote/1
```

#### Another tests instructions

- The Endpoints descriptions: [here](#Rest-API-Endpoints-descriptions)
- More curls examples: [extra_test_curls](https://github.com/rkuroki/insurance-quote-ms/blob/main/misc/extra_test_curls.md)
- All predefined data tests available (Products and Offers): [catalog-ms-mock-data.json](https://github.com/rkuroki/insurance-quote-ms/blob/main/catalog-ms-mockserver/catalog-ms-mock-data.json)
- Insomnia Collection Cases calls: [Insomnia_Collection_2024-09-22.json](https://github.com/rkuroki/insurance-quote-ms/blob/main/misc/Insomnia_Collection_2024-09-22.json)
- Swagger is available: http://localhost:8080/swagger-ui/index.html
- Test Coverage Report (after building): `insurance-quote-ms/build/reports/jacoco/test/html/index.html`

<br/>
<br/>

## Project Conception Details

### Approaches

The project was built using a simple and clean Layered Architecture structure (Presentation, Business, and Persistence
layers), guided by the application of SOLID principles. The primary objective is to develop code with high cohesion and low coupling.

Some specific decisions:

- Implemented a simple "chain of validators" ([RuleCompositeValidator](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/main/java/com/insurance/insurancequote/validator/RuleCompositeValidator.java))
  to handle all necessary business rule validations for an insurance quote. Isolating the details of each validation rule, making the code more cohesive and testable.
- Added a ['status' field to the Insurance Quote entity](https://github.com/rkuroki/insurance-quote-ms/blob/130f18bdc4b87d34b0ce17c1dbc27c8de96cf216/src/main/java/com/insurance/insurancequote/entity/InsuranceQuote.java#L51)
  to track its processing state, providing better visibility into the asynchronous message processing.

<br/>

### Main flow sequence diagrams

#### Insurance Quote Registration:

![image](https://github.com/rkuroki/insurance-quote-ms/tree/main/misc/diagrams/insurance-quote-registration-flow-sequence_diagram.png)
([diagram source](https://github.com/rkuroki/insurance-quote-ms/tree/main/misc/diagrams/insurance-quote-registration-flow-sequence_diagram.puml))

- IQController: [InsuranceQuoteController](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/main/java/com/insurance/insurancequote/controller/InsuranceQuoteController.java)
- IQService: [InsuranceQuoteService](src/main/java/com/insurance/insurancequote/service/InsuranceQuoteServiceImpl.java)
- IQValidator: [InsuranceQuoteValidator](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/main/java/com/insurance/insurancequote/validator/RuleCompositeValidator.java)
- IQReceivedPub: [InsuranceQuoteReceivedPub](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/main/java/com/insurance/insurancequote/messaging/publisher/InsuranceQuoteReceivedPubImpl.java)

<br/>

#### Insurance Policy Creation (`PolicyMockSub` is mocking the `Insurance Policy Microservice`):

![image](https://github.com/rkuroki/insurance-quote-ms/tree/main/misc/diagrams/Insurance-policy-creation-mock-sequence_diagram.png)
([diagram source](https://github.com/rkuroki/insurance-quote-ms/tree/main/misc/diagrams/Insurance-policy-creation-mock-sequence_diagram.puml))

- PolicyMockSub: [PolicyMockSub](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/main/java/com/insurance/insurancequote/xxx/insuransepolicyms/PolicyMockSub.java) (simulating the Insurance Policy Microservice)
- InsurancePolicyCreationSub: [InsurancePolicyCreationSub](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/main/java/com/insurance/insurancequote/messaging/subscriber/InsurancePolicyCreationSubImpl.java)
- InsuranceQuoteService: [InsuranceQuoteService](src/main/java/com/insurance/insurancequote/service/InsuranceQuoteServiceImpl.java)

<br/>

### Simulating the 'Insurance Policy Microservice'

To facilitate initial development, I created a subscriber ([PolicyMockSub](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/main/java/com/insurance/insurancequote/xxx/insuransepolicyms/PolicyMockSub.java))
for the `insurance-quote-received` topic, which generates a random ID for the Insurance Policy and publishes it to the `insurance-policy-created` topic.

**This PolicyMockSub is active and starts with the application.** So it is responsible for assigning an insurancePolicyId to the Insurance Quote...

To replace this mock subscriber, I attempted to create a small **NodeJS application ([insurance-policy-ms-mock](https://github.com/rkuroki/insurance-quote-ms/tree/main/insurance-policy-ms-mock))**
to simulate the policy service, but I encountered issues connecting to the Kafka container (connection library problems).
I couldn't resolve it in time, but I will keep trying.

<br/>

### Test Types Implemented:

- **Unit Tests**: For most of the classes and methods.
- **Component Tests**: For the controller, services, repositories, validators, etc. Using the profile 'test', it runs with H2 in memory database and mocking all other external dependencies.
- **Integration Tests with Testcontainers**: Mainly for the controller ([InsuranceQuoteControllerContainerTest.java](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/test/java/com/insurance/insurancequote/controller/InsuranceQuoteControllerContainerTest.java)), starting all the external dependencies.
- **Contract Tests**: For the external services, starting the MockServer with Testcontainer to simulate the `Catalog Microservice`.

<br/>
<br/>

## Rest API Endpoints descriptions

### 1. Insurance Quote Request Creation

#### Path: `POST /insurance-quote`

#### Request Body (`application/json`):

| Field                          | Data Type             | Required | Json Sample                              |
|--------------------------------|:----------------------|:---------|------------------------------------------|
| `product_id`                   | `String`              | `true`   | `"1b2da7cc-b367-4196-8a78-9cfeec21f587"` |
| `offer_id`                     | `String`              | `true`   | `"adc56d77-348c-4bf0-908f-22d402ee715c"` |
| `category`                     | `String`              | `false`  | `"HOME"`                                 |
| `total_monthly_premium_amount` | `Double`              | `true`   | `75.25`                                  |
| `total_coverage_amount`        | `Double`              | `true`   | `825000.00`                              |
| `coverages`                    | `Map<String, Double>` | `true`   | `{ "Incêndio": 250000.00 }`              |
| `assistances`                  | `String[]`            | `true`   | `[ "Encanador" ]`                        |
| `customer`                     | `Object`              | `false`  | `{ }`                                    |
| `customer.document_number`     | `String`              | `false`  | `"36205578900"`                          |
| `customer.name`                | `String`              | `false`  | `"John Wick"`                            |
| `customer.type`                | `String`              | `false`  | `"NATURAL"`                              |
| `customer.gender`              | `String`              | `false`  | `"MALE"`                                 |
| `customer.date_of_birth`       | `Date`                | `false`  | `"1973-05-02"`                           |
| `customer.email`               | `String`              | `false`  | `"johnwick@gmail.com"`                   |
| `customer.phone_number`        | `String`              | `false`  | `"11950503030"`                          |

#### Business rules:

    1. The product and offer exist and are active.
    2. The provided coverages are within the list of coverages for the offer and below the maximum allowed values.
    3. The provided assistances are within the list of assistances for the offer.
    4. The total monthly premium amount is between the maximum and minimum defined for the offer.
    5. The total coverage amount corresponds to the sum of the provided coverages, matching the total coverage amount specified in the request body.

#### Responses:

1. Created (status `201`)
    ``` javascript
    {
      "id": 123456,
      "insurance_policy_id": null, // Long or null
      // all sent fields...
      "status": "RECEIVED", // "RECEIVED", "POLICY_CREATED" or "FAILED"
      "created_at": "2024-09-22T07:59:48Z",
      "updated_at": "2024-09-22T07:59:48Z"
    }
    ```

2. Bad Request (status `400`): invalid payload
    ``` javascript
    {
      "statusCode": "BAD_REQUEST",
      "message": "Invalid payload, check 'details'.",
      "details": {
        "productId": "must not be null",
        "offerId": "must not be null"
      }
    }
    ```

3. Unprocessable Entity (status `422`): validation rule error

    ``` javascript
    {
      "statusCode": "UNPROCESSABLE_ENTITY",
      "message": "The coverage value for [Incêndio] exceeds the maximum allowed.",
      "details": null
    }
    ```

<br/>

### 2. Insurance Quote Get by ID

#### Path: GET /insurance-quote/{id}

#### Responses:

1. Ok (status `200`)

    ``` javascript
    {
      "id": 123456,
      "insurance_policy_id": null, // Long or null
      // all sent fields...
      "status": "RECEIVED", // "RECEIVED", "POLICY_CREATED" or "FAILED"
      "created_at": "2024-09-22T07:59:48Z",
      "updated_at": "2024-09-22T07:59:48Z"
    }
   ```


1. Not found (status `404`)

    ``` javascript
    {
      "statusCode": "string",
      "message": "Insurance Quote not found with id: ",
      "details": null
    }
    ```

<br/>
<br/>

### Directory Structure (main files)

<details>

<summary>Show File tree</summary>

<pre><code class="less">
│   build.gradle.kts
│   compose.yaml
│
├───catalog-ms-mockserver
│       catalog-ms-mock-data.json
│
├───insurance-policy-ms-mock
│       Dockerfile
│       index.js
│       package.json
│
└───src
    ├───main
    │   ├───java
    │   │   └───com
    │   │       └───insurance
    │   │           └───insurancequote
    │   │               │   InsuranceQuoteMsApplication.java
    │   │               │
    │   │               ├───controller
    │   │               │       GlobalExceptionResponseHandler.java
    │   │               │       InsuranceQuoteController.java                // *
    │   │               │
    │   │               ├───dto
    │   │               │   │   CustomerDTO.java
    │   │               │   │   InsuranceQuoteDTO.java
    │   │               │   │   InsuranceQuoteRequestDTO.java
    │   │               │   │   SimpleErrorResponseDTO.java
    │   │               │   │
    │   │               │   └───mapper
    │   │               │           CustomerMapper.java
    │   │               │           InsuranceQuoteMapper.java
    │   │               │
    │   │               ├───entity
    │   │               │       Customer.java
    │   │               │       InsuranceQuote.java                          // *
    │   │               │       InsuranceQuoteStatus.java
    │   │               │
    │   │               ├───exception
    │   │               │       InsuranceQuoteRuleValidationException.java
    │   │               │
    │   │               ├───external
    │   │               │   │   OfferService.java
    │   │               │   │   ProductService.java
    │   │               │   │
    │   │               │   ├───catalogms
    │   │               │   │       CatalogMsOfferService.java               // *
    │   │               │   │       CatalogMsProductService.java             // *
    │   │               │   │
    │   │               │   └───dto
    │   │               │           MonthlyPremiumAmountDTO.java
    │   │               │           OfferDTO.java
    │   │               │           ProductDTO.java
    │   │               │
    │   │               ├───messaging
    │   │               │   │   MessagingConstants.java
    │   │               │   │
    │   │               │   ├───dto
    │   │               │   │       InsurancePolicyCreatedEvent.java
    │   │               │   │       InsuranceQuoteReceivedEvent.java
    │   │               │   │
    │   │               │   ├───publisher
    │   │               │   │       InsuranceQuoteReceivedPub.java
    │   │               │   │       InsuranceQuoteReceivedPubImpl.java       // *
    │   │               │   │
    │   │               │   └───subscriber
    │   │               │           InsurancePolicyCreationSub.java
    │   │               │           InsurancePolicyCreationSubImpl.java      // *
    │   │               │
    │   │               ├───repository
    │   │               │       InsuranceQuoteRepository.java
    │   │               │
    │   │               ├───service
    │   │               │       InsuranceQuoteService.java
    │   │               │       InsuranceQuoteServiceImpl.java               // *
    │   │               │
    │   │               ├───validator
    │   │               │   │   InsuranceQuoteValidator.java
    │   │               │   │   RuleCompositeValidator.java                  // *
    │   │               │   │
    │   │               │   └───rule
    │   │               │           OfferActiveAndValidRuleHandler.java
    │   │               │           OfferAssitancesRuleHandler.java
    │   │               │           OfferCoveragesRuleHandler.java
    │   │               │           OfferMonthlyPremiumRuleHandler.java
    │   │               │           ProductActiveRuleHandler.java
    │   │               │           RuleHandler.java
    │   │               │
    │   │               └───xxx
    │   │                   └───insuransepolicyms
    │   │                           PolicyMockSub.java                       // * temporary
    │   │
    │   └───resources
    │           application.properties                                       // *
    │
    └───test
        ├───java
        │   └───com
        │       └───insurance
        │           └───insurancequote
        │               │   InsuranceQuoteMsApplicationTests.java
        │               │   TestcontainersConfiguration.java
        │               │   TestInsuranceQuoteMsApplication.java
        │               │
        │               ├───config
        │               │       MockCatalogServicesConfig.java
        │               │       MockKafkaConfig.java
        │               │       TestcontaionerAppConstants.java
        │               │
        │               ├───controller
        │               │       InsuranceQuoteControllerContainerTest.java   // *
        │               │       InsuranceQuoteControllerTest.java
        │               │
        │               ├───dto
        │               │   └───mapper
        │               │           CustomerMapperTest.java
        │               │           InsuranceQuoteMapperTest.java
        │               │
        │               ├───entity
        │               │       InsuranceQuoteTest.java
        │               │
        │               ├───external
        │               │   └───catalogms
        │               │           CatalogMsContainerTest.java
        │               │
        │               ├───service
        │               │       InsuranceQuoteServiceTest.java
        │               │
        │               └───validator
        │                   │   InsuranceQuoteValidatorTest.java
        │                   │
        │                   └───rule
        │                           OfferActiveAndValidRuleHandlerTest.java
        │                           OfferAssitancesRuleHandlerTest.java
        │                           OfferCoveragesRuleHandlerTest.java
        │                           OfferMonthlyPremiumRuleHandlerTest.java
        │                           ProductActiveRuleHandlerTest.java
        │
        └───resources
            │   application-test.properties
            │
            └───testcontainer
                └───catalog-ms-mockserver
                        catalog-ms-mock-data.json
</code></pre>

</details>

<br/>

___

### TODO List

I used this TODO list below to organize myself while developing the project:

- [x] criar projeto pelo Spring Initializr
- [ ] configurar projeto
    - [x] postgresql, h2 (component tests)
    - [x] kafka
    - [x] rest mock server (`catalog-ms`)
        - [x] criar 'small nodejs app'?
        - [x] remover mocks (package `xxx`)
    - [ ] kafka pub-sub "mock client" (`insurance-policy-ms`)
        - [x] criar 'small nodejs app'?
        - [ ] problema pra conectar no kafka
        - [ ] remover mocks (package `xxx`)
    - [x] docker-compose
    - [ ] Dockerfile
- [x] criar entidade InsuranceQuote (persistencia simples)
    - [x] entity, service, controller, dtos, mappers
    - [x] criar tests
        - [x] units:
            - [x] entity
            - [x] service
            - [x] mappers
        - [x] integration:
            - [x] controller
            - [x] service
        - [x] integration (container)
            - [x] controller
- [x] adicionar validações da entidade
    - [x] fetch Product e Offer (`catalog-ms`)
        - [x] criar um controller temporario para mockar o `catalog-ms` (package `xxx`)
            - [x] criar .json com massa
            - [x] fix: fazer funcionar no testcontainer
        - [x] criar serviços de integração externa
        - [X] configurar mock server ***
            - [x] utilizar o mesmo .json já criado
            - [x] remover controller mockado (package `xxx`)
            - [x] configurar URLs usadas nos servicos
        - [x] criar contract tests (integration container)
    - [x] criar validador de regras
        - [x] criar chain de validadores de regras
        - [x] criar composite para percorrer o chain
        - [x] criar exception de validações de regras
    - [x] configurar error responses, com mensagem e status code de acordo com o tipo de erro
        - [x] validar nos testes de controller
- [ ] notificar recebimento da cotação (Kafka)
    - [x] criar publisher `insurance-quote-received`
        - [x] usar publisher no service, publica msg: `{insuranceQuoteId}`
    - [x] criar consumer MOCK `insurance-quote-received` (mock do `insurance-policy-ms`)
        - [x] consome msg, gera um policy id aleatorio, coloca no topico `insurance-policy-created`:
          `{insuranceQuoteId,insurancePolicyId}`
    - [x] criar consumer `insurance-policy-created`
        - [x] chamar service para salvar o `insurancePolicyId`
    - [x] configurar Kafta:
        - [x] docker-compose.yaml
        - [x] application.yaml
        - [x] testcontainer
    - [x] criar tests
- [ ] configurar Cache service que consomem o catalog-ms
    - [x] usar cache simples do spring
    - [ ] trocar para cache distribuido: Redis (optional)
        - [ ] config:
            - [ ] testcontainer
            - [ ] applications yaml
            - [ ] docker-compose

- [ ] escrever README.md
    - [x] descricao
    - [x] tecnologias utilizadas
    - [x] abordagens
        - [x] objetivo do projeto
        - [x] abordagens
            - [X] implmentacao simples de chain de validadores
            - [x] adição do campo 'status' para identificar erros (principalmente de mensageria)
    - [x] instrucoes
        - [x] build
        - [x] bootRun (com docker-compose)
        - [x] massas de test (jsons samples)
            - [x] lista dos Products e Offers disponiveis no mock
            - [x] exemplos de InsuranteQuote Requests:
                - [x] sucesso
                - [x] erro por campo do payload invalido (400)
                - [x] erro por validação de regra (422)
    - [x] diagrama de sequencia do fluxo principal (PlantUML)
    - [x] arvore de diretorios e principais arquivos, descrever principais
    - [ ] adicionar observacoes gerais
- [ ] configurar CI/CD (github actions)
- [ ] adicionar "logs, traces e metrics" distribuido
- [ ] teste de carga

___
