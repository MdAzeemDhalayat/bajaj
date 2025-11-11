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
bfhl.name=John Doe
bfhl.regNo=REG12347
bfhl.email=john@example.com

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

### Notes
- Authorization header uses the raw token (spec shows no `Bearer` prefix).
- All network calls are via `WebClient`. No controllers are exposed.
- Logs will show the submission result or any errors.
- For Odd SRN (Dept question), `src/main/resources/sql/odd.sql` contains the final query.

### Direct downloadable JAR link
- Preferred: GitHub Release asset
  - Create a Release (e.g., tag `v1.0.0`) and upload `target/bajaj-qualifier-java-0.0.1-SNAPSHOT.jar`
  - Copy the asset URL like:
    - `https://github.com/<username>/<repo>/releases/download/v1.0.0/bajaj-qualifier-java-0.0.1-SNAPSHOT.jar`
- Alternative: Raw link from repo (if you commit the JAR)
  - `https://raw.githubusercontent.com/<username>/<repo>/main/target/bajaj-qualifier-java-0.0.1-SNAPSHOT.jar`
  - Replace `main` with your branch if needed.

### Submission checklist
- Public GitHub repo URL
  - Contains source, final SQL file, and the built JAR (or Release)
- Direct downloadable JAR link (Release asset or raw link)
- Submit the form: `https://forms.office.com/r/2cSHMsAbgr`


"# bajaj" 
