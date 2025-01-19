# SubastALL BackEnd

This project is a Spring Boot application that demonstrates JWT authentication and includes endpoints for user authentication, auctions (subastas), and bids (ofertas).

## Front End

The front end for this project is available in a separate repository. You can find it [here](https://github.com/juancursed/fronted_subastall.git).

## Installation

Follow these steps to install and run the project:

1. Clone the repository: `git clone https://github.com/Ceball0s/SpringSubastAll.git`
2. Navigate to the project directory: `cd SpringSubastAll`
3. Build the project using Maven: `mvn clean install`
4. Run the project: `mvn spring-boot:run`
5. Test the API Rest using Postman or another application at `http://localhost:8080`.

## API Endpoints

### Authentication Endpoints
- **Login**: `POST /auth/login`
  - Request Body: `LoginRequest`
  - Response: `AuthResponse`
- **Register**: `POST /auth/register`
  - Request Body: `RegisterRequest`
  - Response: `AuthResponse`

### Auction (Subasta) Endpoints
- **Add Auction**: `POST /api/subasta/agregar`
  - Request Body: `AgregarRequest`
  - Response: `SubastaDTO`
- **Get Auction by ID**: `GET /api/subasta/{subastaId}`
  - Response: `SubastaDTO`
- **Modify Auction**: `PUT /api/subasta/{subastaId}`
  - Request Body: `AgregarRequest`
  - Response: `SubastaDTO`
- **Close Auction**: `PUT /api/subasta/finalizar/{subastaId}`
  - Response: `SubastaDTO`
- **Cancel Auction**: `PUT /api/subasta/cancelar/{subastaId}`
  - Response: `SubastaDTO`
- **Get User Auctions**: `GET /api/subasta/usuario`
  - Response: List of `SubastaDTO`
- **Get Auction Photo**: `GET /api/subasta/foto/{nombreArchivo}`
  - Response: `byte[]`
- **Get Recommendations**: `GET /api/subasta/recomendaciones`
  - Response: List of `SubastaDTO`

### Bid (Oferta) Endpoints
- **Create Bid**: `POST /api/ofertas/crear`
  - Request Body: `CrearOfertaRequest`
  - Response: `OfertaDTO`
- **Get Bid by ID**: `GET /api/ofertas/{id}`
  - Response: `OfertaDTO`
- **Get User Bids**: `GET /api/ofertas/usuario`
  - Response: List of `OfertaDTO`
- **Get Bids by Auction**: `GET /api/ofertas/subasta/{subastaId}`
  - Response: List of `OfertaDTO`
- **Get Best Bid by Auction**: `GET /api/ofertas/subasta/mejor/{subastaId}`
  - Response: `OfertaDTO`
- **Cancel Bid**: `GET /api/ofertas/cancelar/{ofertaid}`
  - Response: `void`

### User Endpoints
- **Get User by ID**: `GET /api/v1/user/{id}`
  - Response: `UserDTO`
- **Update User**: `PUT /api/v1/user`
  - Request Body: `UserRequest`
  - Response: `UserResponse`

## Running Tests

To run the tests, use the following command:

```sh
mvn test
```
# Configuration
The application properties are configured in the application.properties file. Update the database connection details as needed.

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:postgresql://localhost:5432/subasta
spring.datasource.username=administrador
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```
