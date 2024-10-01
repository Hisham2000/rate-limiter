# Rate Limiter ![Rate Limiter Icon](https://img.icons8.com/ios-filled/50/000000/rate-limiting.png)
![GitHub](https://img.shields.io/github/license/Hisham2000/rate-limiter) ![Maven](https://img.shields.io/badge/Maven-3.6.3-blue) ![Java](https://img.shields.io/badge/Java-17-orange)

# Rate Limiter ![Rate Limiter Icon](https://img.icons8.com/ios-filled/50/000000/rate-limiting.png)

![GitHub](https://img.shields.io/github/license/Hisham2000/rate-limiter) ![Maven](https://img.shields.io/badge/Maven-3.6.3-blue) ![Java](https://img.shields.io/badge/Java-17-orange)

## Description 📜

The **Rate Limiter** is a Spring Boot application designed to manage and limit the number of API requests, ensuring fair usage of resources. It implements a **Token Bucket Algorithm** to effectively control the rate of requests to your backend APIs.

### Features ✨

- 📈 **Token Bucket Algorithm**: Efficiently manages request rates.
- 🔒 **Rate Limiting**: Prevents abuse by returning an HTTP 429 status code when limits are exceeded.
- 🛠️ **Configurable**: Easily configure rate limits and refill intervals.

## Table of Contents 📋

1. [Getting Started](#getting-started)
2. [Prerequisites](#prerequisites)
3. [Installation](#installation)
4. [Configuration](#configuration)
5. [Usage](#usage)
6. [Dockerization](#dockerization)
7. [License](#license)
8. [Authors](#authors)

## Getting Started 🚀

To get started with the Rate Limiter application, follow the steps below to set it up locally.

## Prerequisites ⚙️

Ensure you have the following installed:

- [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-downloads.html)
- [Maven 3.6.3](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/get-started)

Installation 📦
1. **Clone the repository:**
   ```bash
    git clone https://github.com/Hisham2000/rate-limiter.git
   ```
2. **Navigate to the project directory:**
   ```bash
    cd rate-limiter
   ```
3. **Build the project:**
   ```bash
    mvn clean package
   ```

## Configuration ⚙️

The application configuration is specified in the `application.properties` file located in `src/main/resources`.

```properties
spring.application.name=rate limiter
rate.limit.capacity=2
rate.limit.refillTokens=1
rate.limit.refillInterval=200000000000
real.api.url=http://localhost:9021
```

## Usage 🏗️
To run the application, use the following command:
```bash
   mvn spring-boot:run
   ```
The application will start, and you can begin making requests to your API endpoints.
## Dockerization 🐳
You can also run the Rate Limiter application in a Docker container. Follow these steps:

Create a Dockerfile in the root of your project:

```bash
# Step 1: Use a base image
FROM openjdk:17-jdk-slim as builder

# Step 2: Set the working directory
WORKDIR /app

# Step 3: Copy the Maven build output
COPY target/rate-limiter-0.0.1-SNAPSHOT.jar /app/rate-limiter.jar

# Step 4: Specify the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/rate-limiter.jar"]
```


Build the Docker image:

```bash
   docker build -t rate-limiter .
```
Run the Docker container:

```bash
  docker run -p 8080:8080 rate-limiter
```
## License 📄

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Authors 👨‍💻

- **Hisham Anwar** - [hishamanwar72@gmail.com](mailto:hishamanwar72@gmail.com)

Feel free to contribute to this project by creating issues, suggesting enhancements, or submitting pull requests. Enjoy using the Rate Limiter! 🎉
