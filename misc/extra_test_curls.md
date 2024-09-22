___

The `Catalog Microservice` is simulated with MockServer, the predefined data is load from: [catalog-ms-mock-data.json](https://github.com/rkuroki/insurance-quote-ms/blob/main/catalog-ms-mockserver/catalog-ms-mock-data.json)

**The predefined data ids**:

- Produto do desafio (Seguro de Vida): `1b2da7cc-b367-4196-8a78-9cfeec21f587`
    - Oferta 1 (Seguro de Vida Familiar): `adc56d77-348c-4bf0-908f-22d402ee715c`

- Produto 2 (Seguro Viagem): `8c71f0a2-7351-4a90-a84c-ef376f481cb5`
    - Oferta 1(Seguro Viagem Nacional): `41e54e5a-f5de-4560-bc4b-5fa43a61cb29`
    - Oferta 2 (Seguro Viagem Interacional): `ae91fc82-e408-41db-a657-3b1f2d5cfa2c`
    - Oferta 2 (Seguro Viagem Espacial) - inativo: `21d7b9b2-f6d3-49b5-a697-6e5c5c2e4b9d`

- Produto 3 (Seguro Veicular) - inativo: `5417122d-2336-422d-b07e-457f186bbf3e`
    - Oferta 1 (Seguro Veicular Passeio) - inativo: `41e54e5a-f5de-4560-bc4b-5fa43a61cb29`

___

### Registrando Cotação de 'Seguro Viagem' (Sucesso):

``` bash
curl --request POST \
  --url http://localhost:8080/insurance-quote \
  --header 'Content-Type: application/json' \
  --data '{
	"product_id": "8c71f0a2-7351-4a90-a84c-ef376f481cb5",
	"offer_id": "ae91fc82-e408-41db-a657-3b1f2d5cfa2c",
	"category": "TRAVEL",
	"total_monthly_premium_amount": 90.50,
	"total_coverage_amount": 15000.46,
	"coverages": {
		"Acidentes pessoais": 10000.12,
		"Extravio de bagagem": 5000.34
	},
	"assistances": [
		"Suporte em viagem",
		"Assistência em emergências"
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
```

### Buscando Cotação salva por ID:

``` bash
curl --request GET \
  --url http://localhost:8080/insurance-quote/1
```

### Buscando todas Cotações salvas:

``` bash
curl --request GET \
  --url http://localhost:8080/insurance-quote
```

### Registrando Cotação (Erro de payload invalido):

``` bash
curl --request POST \
  --url http://localhost:8080/insurance-quote \
  --header 'Content-Type: application/json' \
  --data '{ }'
```

### Registrando Cotação (Erro de regra de validação):

``` bash
curl --request POST \
  --url http://localhost:8080/insurance-quote \
  --header 'Content-Type: application/json' \
  --data '{
	"product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
	"offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
	"total_monthly_premium_amount": 75.25,
	"total_coverage_amount": 2000000.00,
	"coverages": {
		"Incêndio": 1000000.00,
		"Desastres naturais": 1000000.00
	},
	"assistances": [
		"Encanador"
	]
}'
```

### Registrando Cotação (Erro de Produto selecionado não encontrado):

``` bash
curl --request POST \
  --url http://localhost:8080/insurance-quote \
  --header 'Content-Type: application/json' \
  --data '{
	"product_id": "8c71f0a2-7351-4a90-a84c-ef376f481cb5 bla bla",
	"offer_id": "ae91fc82-e408-41db-a657-3b1f2d5cfa2c",
	"category": "TRAVEL",
	"total_monthly_premium_amount": 90.50,
	"total_coverage_amount": 10000.00,
	"coverages": {
		"Acidentes pessoais": 10000.00
	},
	"assistances": [
		"Suporte em viagem"
	],
	"customer": {
		"document_number": "36205578900",
		"name": "John Wick",
	}
}'
```

### Registrando Cotação de 'Seguro Viagem' (Oferta de 'Seguro Viagem Espacial' inativa):

``` bash
curl --request POST \
  --url http://localhost:8080/insurance-quote \
  --header 'Content-Type: application/json' \
  --data '{
	"product_id": "8c71f0a2-7351-4a90-a84c-ef376f481cb5",
	"offer_id": "21d7b9b2-f6d3-49b5-a697-6e5c5c2e4b9d",
	"category": "TRAVEL",
	"total_monthly_premium_amount": 90.50,
	"total_coverage_amount": 15000.46,
	"coverages": {
		"Acidentes pessoais": 10000.12,
		"Extravio de bagagem": 5000.34
	},
	"assistances": [
		"Suporte em viagem",
		"Assistência em emergências"
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
```

### Registrando Cotação de 'Seguro Veicular' (Produto de 'Seguro Veicular' inativa):

``` bash
curl --request POST \
  --url http://localhost:8080/insurance-quote \
  --header 'Content-Type: application/json' \
  --data '{
	"product_id": "5417122d-2336-422d-b07e-457f186bbf3e",
	"offer_id": "40fe5d53-e5ba-4905-a350-b5c391b0435b",
	"category": "CAR",
	"total_monthly_premium_amount": 15.50,
	"total_coverage_amount": 600.46,
	"coverages": {
		"Colisão": 500.0,
		"Incêndio": 700.0
	},
	"assistances": [
		"Guincho",
		"Desastres naturais"
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
```



