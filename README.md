## Bajaj Finserv Health | Qualifier 1 | JAVA

Spring Boot app that:
- On startup, generates a webhook and JWT via POST
- Decides Odd/Even via last two digits of `regNo`
- Loads the corresponding SQL solution and submits it to the webhook with the JWT in the `Authorization` header

### Tech
- Java 17
- Spring Boot 3 (WebClient)
- No controllers; flow runs on startup

### Configure
Edit `src/main/resources/application.properties`:
```
bfhl.name=MD AZEEM FAYAZ AHMED DHALAYAT
bfhl.regNo=PES1UG22CS341
bfhl.email=azeemfd7869@gmail.com

# Run headless (no web server/port)
spring.main.web-application-type=none

# Option A: inline
# bfhl.finalQueryOdd=SELECT ...;
# bfhl.finalQueryEven=SELECT ...;

# Option B: resource files
bfhl.finalQueryOddPath=classpath:sql/odd.sql
bfhl.finalQueryEvenPath=classpath:sql/even.sql
```

Place your final SQL in:
- `src/main/resources/sql/odd.sql` for Question 1 (Odd)
- `src/main/resources/sql/even.sql` for Question 2 (Even)

The app uses inline SQL if provided; otherwise it reads the resource files.

### Build (with Maven Wrapper)
```
.\mvnw.cmd -DskipTests package
```
Jar output:
```
target/bajaj-qualifier-java-0.0.1-SNAPSHOT.jar
```

### Run
```
java -jar target/bajaj-qualifier-java-0.0.1-SNAPSHOT.jar
```

### What it does on startup
1) POST `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
```
{
  "name": "<name>",
  "regNo": "<regNo>",
  "email": "<email>"
}
```
2) Receives `{ webhook, accessToken }`
3) Computes Odd/Even from last two digits of `regNo`
4) Loads the final SQL
5) POSTs to the returned `webhook` (or fallback to `https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA`) with header:
```
Authorization: <accessToken>
Content-Type: application/json
```
Body:
```
{ "finalQuery": "YOUR_SQL_QUERY_HERE" }
```
