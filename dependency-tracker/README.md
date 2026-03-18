# Dependency Tracker

## Run the server

### Prerequisites
- Java 21 installed

### Start the app
From the project root (`dependency-tracker`), run:

```powershell
.\mvnw.cmd spring-boot:run
```

Wait until you see logs similar to `Tomcat started on port 8080` and `Started Application`.

## Open the website

After the server starts, open:
- Main app: [http://localhost:8080](http://localhost:8080)
- H2 console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

H2 JDBC URL:

```text
jdbc:h2:mem:trackerdb
```

## Stop the server

In the terminal where the app is running, press `Ctrl + C`.
