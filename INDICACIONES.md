
# Booking Service

## Descripción

Este es un servicio de reserva de alojamiento que permite a los usuarios realizar reservas, aplicando descuentos si corresponden. El servicio valida los códigos de descuento a través de una API externa y maneja posibles fallos con Resilience4j para retry y timeout.

## Requisitos

- Java 17
- Spring Boot 3.3.2
- PostgreSQL
- Docker

## Configuración

### Configuración de la Base de Datos

Puedes usar PostgreSQL en Docker para la base de datos. Asegúrate de tener Docker instalado en tu máquina.

1. **Crea un contenedor Docker para PostgreSQL**:

```bash
docker run --name booking-postgres -e POSTGRES_PASSWORD=Oracle11g -e POSTGRES_USER=postgres -e POSTGRES_DB=booking -p 5432:5432 -d postgres
```

2. **Accede al contenedor**:

```bash
docker exec -it booking-postgres psql -U postgres -d booking
```

3. **Verifica las tablas en la base de datos**:

```sql
\dt
```

### Configuración de la Aplicación

1. **Clona el repositorio**:

```bash
git clone https://github.com/tu-usuario/booking-service.git
cd booking-service
```

2. **Configura las propiedades de la base de datos en `application.properties`**:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booking
spring.datasource.username=postgres
spring.datasource.password=Oracle11g
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
resilience4j.retry.instances.discountService.maxAttempts=3
resilience4j.retry.instances.discountService.waitDuration=1000ms
resilience4j.timelimiter.instances.discountService.timeoutDuration=2s
```

### Ejecución de la Aplicación

1. **Compila y ejecuta la aplicación**:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Endpoints

#### Crear una Reserva

- **URL**: `/book`
- **Método**: `POST`
- **Request Body**:
  ```json
  {
      "userId": "string",
      "name": "string",
      "lastname": "string",
      "age": 0,
      "phoneNumber": "string",
      "startDate": "yyyy-MM-dd",
      "endDate": "yyyy-MM-dd",
      "houseId": "string",
      "discountCode": "string"
  }
  ```
- **Response**:
    - **HTTP 200 OK**:
      ```json
      {
          "code": 200,
          "message": "Book Accepted"
      }
      ```
    - **HTTP 400 BAD REQUEST**:
      ```json
      {
          "code": 400,
          "message": "Invalid Discount"
      }
      ```

### Pruebas

Para ejecutar las pruebas unitarias, usa el siguiente comando:

```bash
./mvnw test
```

### Estructura del Proyecto

- **controller**: Contiene el controlador `BookingController`.
- **entity**: Contiene la entidad `Booking`.
- **model**: Contiene la clase de respuesta `ResponseMessage`.
- **repository**: Contiene el repositorio `BookingRepository`.
- **service**: Contiene el servicio `DiscountService`.

### Notas

- El servicio de descuento utiliza `RestTemplate` para realizar la validación del código de descuento contra un servicio externo.
- Resilience4j se utiliza para manejar reintentos y límites de tiempo en las llamadas al servicio de descuento.

## Contacto

Para cualquier consulta, por favor contacta a [arauzocode@ejemplo.com](mailto:arauzocode@ejemplo.com).
