# VitaLink Backend

**Paquete con soporte completo para el JSON del dashboard.** Para cargar los datos, sigue [importacion/LEEME.md](importacion/LEEME.md). No se importan automáticamente ni se borran los datos anteriores.


Backend académico ejecutable para VitaLink, adaptado del ZIP Catch-Up Platform. Es un monolito modular con IAM, Providers, Care y Shared; sigue sus convenciones de comandos, consultas, servicios de aplicación, recursos REST, assemblers y persistencia JPA.

## Tecnologías

- Java **26** (JDK, no solo JRE, para compilar).
- Maven **3.9.16**, incluido como Maven Wrapper: `mvnw` y `mvnw.cmd`.
- Spring Boot **4.0.6**, Spring Web, Validation, Spring Data JPA y Hibernate.
- MySQL **8.4** en Compose (el driver está administrado por Spring Boot).
- SpringDoc OpenAPI **3.0.3**, Swagger UI, Lombok y la estrategia de nombres del proyecto original.
- Spring Security, contraseñas BCrypt y sesiones bearer opacas persistidas con hash y vencimiento de ocho horas.
- JUnit y pruebas HTTP con servidor real. H2 se usa únicamente en las pruebas; no reemplaza MySQL para la ejecución normal.
- Dockerfile multietapa con Maven/JDK 26 y JRE 26.

## Opción A: ejecutar todo con Docker Desktop

1. Extraer el ZIP y abrir una terminal en la carpeta que contiene `pom.xml` y `compose.yaml`.
2. Iniciar **Docker Desktop** y esperar a que el motor Linux esté disponible.
3. Ejecutar:

```sh
docker compose up --build
```

Compose inicia MySQL, espera su healthcheck y levanta el backend en modo de desarrollo con datos ficticios. La primera ejecución descarga imágenes y dependencias, por lo que puede tardar varios minutos.

- Swagger: http://localhost:8080/swagger-ui/index.html
- Contrato OpenAPI: http://localhost:8080/v3/api-docs
- API: http://localhost:8080/api/v1

Detener con `Ctrl+C` o `docker compose down`. Los datos se conservan en el volumen de MySQL. Los puertos 3306 y 8080 deben estar libres; no ejecutar el backend nativo simultáneamente en el mismo puerto. Las contraseñas predeterminadas de Compose son exclusivamente ficticias para esta demostración local.

## Opción B: Java y Maven Wrapper en Windows

Necesitas JDK 26 y MySQL. Puedes iniciar únicamente la base de datos con:

```powershell
docker compose up -d mysql
```

Con MySQL ya disponible, configurar la terminal PowerShell. Si usas MySQL de Compose desde Java nativo, establece DATABASE_PORT con el puerto externo indicado en tu compose.yaml (por ejemplo 3307); dentro de Docker el backend sigue usando 3306:

```powershell
$env:JAVA_HOME = 'C:\ruta\a\tu\jdk-26'
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$env:DATABASE_USER = 'vitalink'
$env:DATABASE_PASSWORD = 'vitalink_demo_password'
$env:SPRING_PROFILES_ACTIVE = 'dev'
.\mvnw.cmd spring-boot:run
```

Si el JDK ya está en PATH y JAVA_HOME está configurado, omitir sus dos primeras líneas. IntelliJ también puede importar `pom.xml`, seleccionar JDK 26 y ejecutar `VitaLinkPlatformApplication` con esas variables de entorno. No se necesita instalar Maven aparte.

Para MySQL instalado sin Docker, crear previamente la base `vitalink` y un usuario con permisos sobre ella, y usar sus credenciales. No es necesario ejecutar un script de tablas: el perfil `dev` utiliza `ddl-auto=update`.

## Linux / macOS

Con JDK 26 y MySQL disponibles:

```sh
chmod +x mvnw
export DATABASE_USER=vitalink
export DATABASE_PASSWORD=vitalink_demo_password
export SPRING_PROFILES_ACTIVE=dev
./mvnw spring-boot:run
```

La inicialización solo se activa en perfiles `dev` y `test`, una vez, cuando DR-01 no existe. Los médicos quedan con verificación profesional `pending`. No se verifica colegiatura ni pertenencia real a una clínica.

## Primer recorrido en Swagger

1. En `POST /api/v1/auth/sign-in`, enviar el correo de Ana y `Demo12345`.
2. Copiar el campo `token` de la respuesta. Pulsar **Authorize** en Swagger y pegar solo el token.
3. Consultar `GET /api/v1/patients`: aparece P-001. Consultar alertas y mediciones.
4. Atender A-001 con `PATCH /api/v1/alerts/A-001`, indicando `status: ATTENDED`, una `note` y el `expectedVersion` obtenido del GET. El historial se registra en la misma transacción.
5. Ingresar como Luis y solicitar vínculo con P-002 mediante `POST /care-relationships`.
6. Ingresar como María y aceptar la solicitud. Luis obtiene acceso al paciente; puede finalizarse después desde cualquiera de las dos cuentas.

Los tokens vencen a las ocho horas. `POST /auth/sign-out` los revoca en la base de datos. Las respuestas nunca incluyen contraseñas ni hashes.

## Registro

`POST /api/v1/auth/sign-up` admite `doctor` o `patient`. Los familiares se registran junto a su paciente mediante `POST /api/v1/enrollment/families`.

- Todos: nombre, correo, contraseña de 8 a 72 caracteres y teléfono. El correo es único.
- Médico: especialidad, colegiatura de 4 a 10 dígitos y `practiceType` (`clinic` o `independent`). Si es de clínica, requiere una `clinicId` válida. La colegiatura es única. Un independiente se guarda sin clínica.
- Familiar: nombre, correo, contraseña y teléfono; además los datos del paciente, médico elegido, consentimiento y cuatro signos vitales completos. El nuevo endpoint guarda la cuenta, el paciente y su seguimiento inicial en una sola transacción.
- Paciente: edad, ubicación, `doctorId`, `serviceType` y `consent: true`. Para modalidad de clínica exige una clínica que coincida con el médico elegido. La cuenta, ficha y consentimiento se guardan en una transacción. El paciente solo ve su propia ficha.

Ejemplos completos de peticiones en [docs/API.md](docs/API.md) y `requests.http`.

## JAR ejecutable incluido

`release/vitalink-backend-1.0.0.jar` ya está compilado con Java 26. Con MySQL disponible y las variables de entorno de la opción B configuradas, puedes ejecutarlo sin compilar de nuevo:

```sh
java -jar release/vitalink-backend-1.0.0.jar
```

El JAR no incluye la base de datos. Necesita Java 26 o un entorno compatible; para modificar el código, usa el JDK 26 y Maven Wrapper. El archivo `.sha256` permite verificar su integridad. La carpeta release se excluye del contexto Docker; Compose construye a partir del código fuente.

## Compilar y probar

```powershell
.\mvnw.cmd clean verify
```

En Linux/macOS: `./mvnw clean verify`. Las pruebas no necesitan MySQL ni Docker: levantan H2 en modo de compatibilidad MySQL y un servidor HTTP temporal. Esto no sustituye una validación adicional con MySQL real.

El JAR ejecutable queda en `target/vitalink-backend-1.0.0.jar`. Con las variables de MySQL configuradas:

```sh
java -jar target/vitalink-backend-1.0.0.jar
```

## Frontend Angular actual

Este backend **no es un reemplazo directo de la fake API sin cambios en Angular**. Conserva la forma de los recursos de seguimiento, pero introduce autenticación real y operaciones transaccionales. Los cambios puntuales están en [docs/ANGULAR-INTEGRATION.md](docs/ANGULAR-INTEGRATION.md). El frontend no se modifica ni se incluye en este ZIP.

## Configuración y alcance

Variables: `DATABASE_HOST` (localhost por defecto), `DATABASE_PORT` (3306), `DATABASE_NAME` (vitalink), `DATABASE_USER`, `DATABASE_PASSWORD`, `FRONTEND_ORIGIN` (http://localhost:4200), `PORT` (8080) y `SPRING_PROFILES_ACTIVE`.

Compose usa el perfil `dev` para que funcione la demostración sin preparación adicional. `prod` desactiva la semilla y valida el esquema; requiere tablas previamente creadas y conexión MySQL con SSL. No se incluye todavía una estrategia de migraciones para producción. Este paquete es una base académica local, no una entrega certificada para manejar historias clínicas reales.

Incluye autenticación, registro, perfiles, catálogo, pacientes, autorización, solicitudes de vínculo, mediciones, alertas de ayuda e intervenciones. No incluye envío de correos/SMS, dispositivos IoT, verificación profesional, recuperación de contraseña ni generación automática de alertas clínicas a partir de mediciones. Los rangos de la semilla son datos demostrativos, no recomendaciones médicas.

## Procedencia

Se conservaron las convenciones y componentes compartidos reutilizables del proyecto Catch-Up Platform y su [licencia MIT](LICENSE.md). Se reemplazó el negocio de noticias por los módulos de VitaLink. Detalles de arquitectura en [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md). Evidencia de validación en [docs/VALIDATION.md](docs/VALIDATION.md).


## Registro de médicos y de familiares con pacientes

La interfaz Angular ofrece dos tipos de alta. Los médicos usan `POST /api/v1/auth/sign-up`: `practiceType=clinic` requiere un `clinicId` del catálogo; `independent` guarda `clinicId=null`. Clínica y colegiatura son declaradas en esta simulación académica; `verificationStatus` permanece pendiente.

El familiar usa `POST /api/v1/enrollment/families` con sus datos de cuenta y un objeto `patient`: nombres, apellidos, edad, ubicación, nombre familiar opcional, modalidad, clínica si corresponde, médico, consentimiento e `initialVitals` (heartRate, oxygen, glucose, systolic, diastolic). Todos los signos son obligatorios; la presión sistólica debe superar a la diastólica. Los límites de entrada son de formato de simulación, no rangos clínicos de normalidad.

El bounded context `enrollment` contiene comandos y objetos de valor de dominio, puertos, servicios de aplicación, recursos REST, assemblers y adaptadores de infraestructura. Su aplicación depende de los puertos; los adaptadores coordinan IAM, Providers y Care. Una sola transacción crea la cuenta, el contacto familiar, el paciente, su vínculo autorizado y cuatro mediciones simuladas. Un error revierte el alta completa. El familiar inicia sesión con su propia cuenta; el paciente no necesita una cuenta adicional.

Los accesos del JSON se agregan como alias de las mismas identidades, conservando las contraseñas originales. Los alias también se consideran ocupados al registrar cuentas. Para cargar el dataset y consultar las tablas, consulta [importacion/LEEME.md](importacion/LEEME.md).
