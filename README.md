# 4yourlife

Versión final lista para entrega del backend de la aplicación 4yourlife — aplicación Spring Boot basada en Java. Este README está pensado para un entregable: incluye requisitos, preparación del entorno, instrucciones de construcción y despliegue, variables de entorno, ejemplos de uso de la API (payloads), y resolución de problemas frecuentes.

Contenido
- Descripción y stack
- Requisitos y versiones
- Variables de entorno (.env.example)
- Configuración rápida (dev y producción)
- Construcción y despliegue (jar y Docker)
- Migraciones de base de datos (Flyway)
- Ejecución de pruebas
- Ejemplos de requests JSON
- Errores comunes y cómo resolverlos (PDF, Thymeleaf, fechas)
- Excel: recomendaciones estéticas
- Contacto, licencia y entregables

--------------------------------------------------------------------------------

Descripción y stack
-------------------

Proyecto backend REST construido con Spring Boot. Puntos principales:

- Lenguaje: Java 17+
- Framework: Spring Boot (JPA, Flyway, Spring MVC)
- Base de datos: PostgreSQL
- Build: Gradle (wrapper incluido `gradlew.bat`)
- Plantillas: Thymeleaf (usado para generar HTML/PDF en algunas rutas)
- Exportación a Excel: Apache POI (u otra librería configurable)

Requisitos
----------

- Java 17 o superior
- Docker y Docker Compose (para entorno local con contenedores)
- Espacio en disco suficiente para build/logs
- (Opcional) Cliente HTTP (curl, httpie, Postman)

Variables de entorno (ejemplo `.env`)
----------------------------------

Coloca estas variables en un archivo `.env` en la raíz del proyecto o en tu entorno de ejecución. A continuación un ejemplo que puedes adaptar:

```env
# Database
PGHOST=localhost
PGPORT=5432
PGDATABASE=4yourlife_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Application
API_URL=http://localhost:8081
SERVER_PORT=8081

# SendGrid (si se usa)
COM_EMAIL_SENDGRID_APIKEY=SG.xxxxxxxxxxxxxxxxxxxxx
COM_EMAIL_SENDGRID_FROMEMAIL=example@example.com
COM_EMAIL_SENDGRID_FROMNAME=4YourLife

# Otras propiedades según src/main/resources/application.properties
```

Nota: En `application.properties` algunas propiedades usan placeholders; verifica que los nombres coincidan con tu `.env` o variables de entorno del sistema.

Configuración rápida (desarrollo)
--------------------------------

1) Levantar PostgreSQL con Docker Compose (si deseas usar contenedor):

```powershell
docker compose up -d
```

2) Ejecutar la aplicación en modo desarrollo (usando variables de entorno):

```powershell
# En Windows PowerShell
.\gradlew.bat bootRun
```

3) Alternativamente, compilar y ejecutar jar:

```powershell
.\gradlew.bat clean bootJar
java -jar build/libs/4yourlife-<version>.jar
```

Construcción y despliegue con Docker (producción)
-----------------------------------------------

1) Crear jar:

```powershell
.\gradlew.bat clean bootJar
```

2) Construir imagen Docker (si `Dockerfile` está presente en la raíz):

```powershell
docker build -t 4yourlife:latest .
```

3) Ejecutar imagen:

```powershell
docker run -e PGHOST=... -e POSTGRES_USER=... -e POSTGRES_PASSWORD=... -p 8081:8081 4yourlife:latest
```

Migraciones de base de datos
---------------------------

Se usa Flyway; las migraciones se encuentran en `src/main/resources/db/migrations`. Flyway se ejecuta automáticamente al iniciar la aplicación (según `application.properties`). Para forzar migraciones desde línea de comandos, puedes usar las tareas de Flyway o arrancar la aplicación con la base de datos disponible.

Ejecución de pruebas
--------------------

```powershell
.\gradlew.bat test
```

Logging
-------

Los logs se escriben en `./logs/application.log` por configuración en `application.properties`. Ajusta niveles en ese archivo si necesitas más información en desarrollo.

API — ejemplos y payloads
-------------------------

Base path en esta aplicación: `/api` (ajustable en `application.properties` con `server.servlet.context-path`).

Ejemplo: `OrganizationalChartRequest` (JSON listo para enviar)

```json
{
  "teamId": "team-123",
  "masterLives": [
    { "id": "ml-1", "name": "Master Life 1" }
  ],
  "visionaries": [
    { "id": "v-1", "name": "Visionary 1" }
  ],
  "staff": [
    { "id": "s-1", "name": "Staff 1" }
  ]
}
```

Ejemplo: `CallLogRequest` (DateTime en formato ISO-8601)

```json
{
  "callType": "OUTGOING",
  "callStatus": "COMPLETED",
  "notes": "Llamada de seguimiento",
  "calledById": "user-123",
  "calledUserId": "user-456",
  "startTime": "2026-03-12T10:15:30",
  "endTime": "2026-03-12T10:25:00"
}
```

Ejemplo mapeo de categoría/producto (JSON ya proporcionado)

```json
{
  "id": "nRMdRgrN2ig9Zel6",
  "nombre": "Entrenamientos",
  "padre_id": null,
  "agrupar": false,
  "tipo_producto": "SERV",
  "cuenta_venta": null,
  "cuenta_compra": null,
  "cuenta_inventario": null
}
```

Nota de mapeo: en Java preferimos `padreId` en camelCase; usa `@JsonProperty("padre_id")` si el JSON mantiene snake_case.

Errores comunes y cómo resolverlos
--------------------------------

1) SAXParseException / TrAX transformer — "The markup in the document following the root element must be well-formed"

- Causa: se está transformando HTML/XHTML a XML/FO para generar PDF y el HTML no es well-formed (por ejemplo hay contenido fuera del `<html>` raíz, etiquetas sin cerrar, o se concatenan fragmentos sin contenedor raíz).
- Solución:
  - Asegúrate de que el documento tenga un único `<html>...</html>` y que todo esté correctamente cerrado.
  - Si usas fragmentos Thymeleaf, envuélvelos en un contenedor (por ejemplo `<div>` o `<html>` si vas a transformar a FO).
  - Valida el HTML resultante (puedes imprimirlo y revisarlo con un validador XML/HTML).

2) Formatear `LocalDateTime` en Thymeleaf

Si tu variable es `LocalDateTime` y quieres mostrarla en la plantilla:

```html
<td class="line" style="width: 150px">
  <span th:text="${payment != null ? payment.createdDate.format(T(java.time.format.DateTimeFormatter).ofPattern('dd/MM/yyyy HH:mm')) : 'N/A'}"></span>
</td>
```

O usando utilidades de Thymeleaf:

```html
<span th:text="${payment != null ? #temporals.format(payment.createdDate, 'dd/MM/yyyy HH:mm') : 'N/A'}"></span>
```

3) Iterar con índice en Thymeleaf

```html
<div th:each="data, iterStat : ${contracts}">
  <span th:text="${iterStat.index}"></span> <!-- índice empieza en 0 -->
  <span th:text="${iterStat.count}"></span> <!-- posición empieza en 1 -->
  <!-- contenido -->
</div>
```

Excel — recomendaciones para mejorar presentación
-------------------------------------------------

Si generas reportes XLSX con Apache POI, estas prácticas mejoran la presentación:

- Definir estilos para encabezados (negrita, color de fondo, bordes).
- Usar `sheet.autoSizeColumn(i)` después de poblar datos.
- Aplicar formatos de celda: fechas, moneda, porcentajes con `DataFormat`.
- Congelar encabezado: `sheet.createFreezePane(0, 1)`.
- Usar filtros: `sheet.setAutoFilter(new CellRangeAddress(0, lastRow, 0, lastCol))`.

Snippet Java (POI) para crear estilo de encabezado básico:

```java
// ...existing code...
CellStyle headerStyle = workbook.createCellStyle();
Font headerFont = workbook.createFont();
headerFont.setBold(true);
headerStyle.setFont(headerFont);
headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
// apply headerStyle to header cells
// ...existing code...
```

Checklist pre-entrega
---------------------

- [ ] Asegurar que las migraciones Flyway estén en orden y aplicadas.
- [ ] Probar endpoints críticos y documentar ejemplos (POST, GET, auth si aplica).
- [ ] Verificar generación de PDFs (abrir PDF final) y corregir plantillas Thymeleaf si hay errores SAX.
- [ ] Ejecutar tests: `./gradlew test` y corregir fallos.
- [ ] Ver logs y quitar datos sensibles (API keys) del repositorio.

Contacto y entrega
------------------

Si necesitas que prepare:

- Un `docker-compose.yml` listo (BD + app) respetando variables de entorno.
- Un archivo `.env.example` con todas las variables utilizadas.
- Documentación de endpoints (Postman collection o OpenAPI spec).

Licencia
--------

Incluye aquí la licencia del proyecto (por ejemplo MIT) o la nota del propietario del código.

-------------------------------------------------------------------------------

Este README está pensado para ser entregado junto al código fuente. Si quieres, genero también:

- `docker-compose.yml` y `.env.example` listos para correr la aplicación localmente.
- Colección Postman o especificación OpenAPI con los endpoints más usados.

Indícame cuál de esas tareas quieres que haga ahora y lo preparo inmediatamente.
