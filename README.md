
```
docker-compose up -d
./gradlew bootRun
```

```
curl --request POST \
  --url http://localhost:8080/insurance-quote \
  --header 'Content-Type: application/json' \
  --data '{
	"product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
	"offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
	"category": "HOME",
	"total_monthly_premium_amount": 75.25,
	"total_coverage_amount": 825000.00,
	"coverages": {
		"Incêndio": 250000.00,
		"Desastres naturais": 500000.00,
		"Responsabiliadade civil": 75000.00
	},
	"assistances": [
		"Encanador",
		"Eletricista",
		"Chaveiro 24h"
	],
	"customer": {
		"document_number": "36205578900",
		"name": "John Wick",
		"type": "NATURAL",
		"gender": "MALE",
		"date_of_birth": "1973-05-02",
		"email": "johnwick@gmail.com",
		"phone_number": 11950503030
	}
}'

curl --request GET \
  --url http://localhost:8080/insurance-quote/1
```

The controller testcontainer: [InsuranceQuoteControllerContainerTest.java](https://github.com/rkuroki/insurance-quote-ms/blob/main/src/test/java/com/insurance/insurancequote/controller/InsuranceQuoteControllerContainerTest.java)

### TODO list

- [x] criar projeto pelo Spring Initializr
- [ ] configurar projeto
    - [x] postgresql, h2 (component tests)
    - [x] kafka
    - [ ] rest mock server (`catalog-ms`)
        - [ ] criar 'small nodejs app'?
        - [ ] remover mocks (package `xxx`)
    - [ ] kafka pub-sub "mock client" (`insurance-policy-ms`)
        - [ ] criar 'small nodejs app'?
        - [ ] remover mocks (package `xxx`)
    - [x] docker-compose
    - [ ] Dockerfile
- [ ] criar entidade InsuranceQuote (persistencia simples)
    - [x] entity, service, controller, dtos, mappers
    - [ ] criar tests
        - [ ] units:
            - [ ] entity
            - [x] service
            - [x] mappers
        - [ ] integration:
            - [X] controller
            - [ ] service (criar mais cenarios) ***
        - [x] integration (container)
            - [x] controller
- [ ] adicionar validações da entidade
    - [ ] fetch Product e Offer (`catalog-ms`)
        - [ ] criar um controller temporario para mockar o `catalog-ms` (package `xxx`)
            - [x] criar .json com massa
            - [ ] fix: fazer funcionar no testcontainer
        - [x] criar serviços de integração externa
        - [ ] configurar mock server ***
            - [ ] utilizar o mesmo .json já criado
            - [ ] remover controller mockado
            - [ ] trocar config URLs usadas nos servicos
        - [ ] criar contract tests (integration container) *
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
    - [ ] criar tests
- [ ] configurar Cache service que consomem o catalog-ms
    - [x] usar cache simples do spring
    - [ ] trocar para cache distribuido: Redis (optional)
        - [ ] config:
            - [ ] testcontainer
            - [ ] applications yaml
            - [ ] docker-compose

- [ ] escrever README.md
    - [ ] descricao
    - [ ] tecnologias utilizadas
    - [ ] abordagens
        - [ ] objetivo do projeto
        - [ ] abordagens
            - [ ] implmentacao simples de chain de validadores
            - [ ] adição do campo 'status' para identificar erros (principalmente de mensageria)
    - [ ] instrucoes
        - [ ] build
        - [ ] bootRun (com docker-compose)
        - [ ] massas de test (jsons samples)
            - [ ] lista dos Products e Offers disponiveis no mock
            - [ ] exemplos de InsuranteQuote Requests:
                - [ ] sucesso
                - [ ] erro por campo do payload invalido (400)
                - [ ] erro por validação de regra (422)
    - [ ] diagrama da arquitetura
    - [ ] diagrama de sequencia do fluxo principal (PlantUML)
    - [ ] arvore de diretorios e principais arquivos
    - [ ] descrever testes
    - [ ] adicionar observacoes gerais
- [ ] configurar CI/CD
- [ ] adicionar "logs, traces e metrics" distribuido
- [ ] teste de carga
