# dai-lab-HTTP
Kevin Auberson, Adrian Rogner
## Step 1: Static Web Site
### Dockerfile

Content of the dockerfile :
```Dockerfile
FROM nginx:1.10.1-alpine
 COPY nginx.conf /etc/nginx/nginx.conf
 COPY ./www/ /usr/share/nginx/html
```
In the dockerfile we have the command "FROM" to indicates the image we want to use.
The "COPY" instruction copies the files or directories from first argument "source" to the filesystem of the container at the path indicates by the second argument "destination".

### nginx.conf

Content of the nginx.conf :
```nginx.conf
http {
    # Define a simple server block to serve static content
    server {
        listen 80; # Listen on port 80

        # Set the root directory where static files are served from
        root /usr/share/nginx/html;

        # Specify default index files
        index index.html;
    }
}
```

### Run server

Commands used to run the server :
```bash
docker build -t nginx .
docker run -d -p 8080:80 nginx
```

## Step 2: Docker compose
### Docker compose file

Content of the docker compose file :
```docker-compose.yml
# docker-compose.yml
version: '3.8'
services:
  nginx:
    build:
        context: .
    ports:
      - "8080:80"  # Map port 8080 on the host to port 80 on the container
```
In the docker compose file we specify the version used.
As services we use a nginx server built with the docker file created in the previous step.
And we map the port 8080 on the host to port 80 on the container.

### nginx.conf

New Content of the nginx.conf :
```nginx.conf
# nginx.conf

# Configuration of our Nginx static server

events {
    worker_connections 1024; # Define maximum number of simultaneous connections
}

http {
    # Define a simple server block to serve static content
    server {
        listen 80; # Listen on port 80

        # Set the root directory where static files are served from
        root /usr/share/nginx/html;

        # Specify default index files
        index index.html;
    }
}
```
In the nginx.con file we just added the lines event with the specification included in the brackets.
Before this change when we started the container we had an error saying : "[emerg] 1#1: no "events" section in configuration"
After some research on the web the solution is to add these lines.

### Run Server

Command used to create and start container :
```bash
docker compose up
```
Command used to build, start and stop server :
```bash
docker compose build
docker compose start
docker compose stop
```

## Step 3: HTTP API server
### API to manage a list of people
The code is a concise implementation of a RESTful API using the Javalin framework in Java. It consists of three files: PersonController.java, Person.java, and Main.java. The Person class represents a person with first and last names. The PersonController class manages CRUD operations on a list of persons and handles HTTP requests. The Main class sets up the Javalin application with routes for listing, retrieving, adding, updating, and deleting persons on port 7777.

#### Test of creation (POST)
![POST](https://github.com/kevinAuberson/dai-lab-HTTP/assets/114987481/d2ed7608-41a8-4d9e-b755-c822c9e0e7fe)

#### Test of read (GET)
![GET](https://github.com/kevinAuberson/dai-lab-HTTP/assets/114987481/35697fd9-bdc7-48c0-81b8-abca9e226222)

#### Test of update (PUT)
![PUT](https://github.com/kevinAuberson/dai-lab-HTTP/assets/114987481/d7c22d94-57a1-4b5d-9959-e4db89c287f4)

#### Test of delete (DELETE)
![DELETE](https://github.com/kevinAuberson/dai-lab-HTTP/assets/114987481/ec703d6c-a1ee-49b7-88e4-036009c41053)


### Dockerfile for Javalin
Content of the Dockerfile for Javalin :
```Dockerfile
FROM eclipse-temurin:latest
COPY app.jar /app.jar
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "/app.jar"]
```
In this Dockerfile for Javalin we use an image of eclipse-temurin for JRE, we COPY our app.jar which contains our API server into the destination.
The command EXPOSE 7777 will permit our application to listen on this port and ENTRYPOINT Specify the default executable.

### Docker Compose file
We changed the directory tree for the docker part so the Docker compose changed a little bit.
Content of the Docker compose file :
```docker-compose.yml
# docker-compose.yml
version: '3.8'
services:
  nginx:
    build:
        context: Nginx/
    ports:
      - "8080:80"  # Map port 8080 on the host to port 80 on the container
      
  javalin:
    build:
        context: Javalin/
    ports:
      - "7777:7777"
```
The part for Javalin is added, we build the container with the dockerfile provided in the context part and we map it to the port 7777.
For nginx we moved the dockerfile in a subfolder so we separated the static server and API parts.

### Run Servers
Command used to create and start container :
```bash
docker compose up
```
Command used to build, start and stop server :
```bash
docker compose build
docker compose start
docker compose stop
```
Command used to delete container :
```bash
docker compose down
```

## Step 4: Reverse proxy with Traefik
### Docker Compose file
Content of the Docker compose file :
```docker-compose.yml
version: '3.8'
services:

  # Nginx service configuration
  nginx:
    build:
      context: Nginx/  # Set the build context to the Nginx directory
    deploy:
      replicas: 1  # Deploy 1 replica
    labels:
      - traefik.http.routers.nginx.rule=Host(`nginx.localhost`)  # Traefik routing rule for Nginx

  # Javalin service configuration
  javalin:
    build:
      context: Javalin/  # Set the build context to the Javalin directory
    deploy:
      replicas: 1  # Deploy 1 replica
    labels:
      - traefik.http.routers.javalin.rule=Host(`javalin.localhost`)  # Traefik routing rule for Javalin

  # Traefik service configuration
  traefik:
    image: traefik:latest  # Use the latest Traefik image
    command:
      - --api.insecure=true  # Enable Traefik dashboard (insecure mode)
      - --providers.docker  # Use Docker as the provider
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock  # Mount Docker socket for communication
    ports:
      - "80:80"  # Expose port 80 for web sites (nginx and javalin)
      - "8080:8080"  # Expose port 8080 for Traefik dashboard
```
The command to start and stop the container are the same in the other step.

### Dockerfile file for nginx
Content of the Docker file :
```Dockerfile
FROM nginx:latest
 COPY ./nginx.conf /etc/nginx/nginx.conf
 COPY ./www/ /usr/share/nginx/html
EXPOSE 80
```
We added the line EXPOSE 80 to make this service listen on the port 80 for traefik

### Javalin application
```java
// Create and start Javalin application on port 80
        Javalin app = Javalin.create().start(80);
```
Our Javalin application now listen on the port 80 instead of the port 7777.

### Reverse proxy

Reverse proxy can handle traffic from all user to access the web sites and can act as a load balancer, security gateway, web cache, etc...
It's also a good element of protection for infrastructures because only the proxy is exposed to the internet and hides the internal web servers.
The HTTPS will be implemented in the step 7.

### Traefik

We can access to the Traefik dashboard by using the port 8080 on a web browser.

<img width="1555" alt="Capture d’écran 2024-01-13 à 15 59 32" src="https://github.com/kevinAuberson/dai-lab-HTTP/assets/100291212/ef6cf7fa-ea96-40dc-98e1-642d25c021c7">

## Step 5: Scalability and load balancing
### Docker Compose file
Content of the Docker compose file :
```docker-compose.yml
version: '3.8'
services:

  # Nginx service configuration
  nginx:
    build:
      context: Nginx/  # Set the build context to the Nginx directory
    deploy:
      replicas: 2  # Deploy 2 replica
    labels:
      - traefik.http.routers.nginx.rule=Host(`nginx.localhost`)  # Traefik routing rule for Nginx
      - traefik.http.services.nginx.loadbalancer.server.port=80  # Specify the port for load balancing

  # Javalin service configuration
  javalin:
    build:
      context: Javalin/  # Set the build context to the Javalin directory
    deploy:
      replicas: 2  # Deploy 2 replica
    labels:
      - traefik.http.routers.javalin.rule=Host(`javalin.localhost`)  # Traefik routing rule for Javalin
      - traefik.http.services.javalin.loadbalancer.server.port=80  # Specify the port for load balancing

  # Traefik service configuration
  traefik:
    image: traefik:latest  # Use the latest Traefik image
    command:
      - --api.insecure=true  # Enable Traefik dashboard (insecure mode)
      - --providers.docker  # Use Docker as the provider
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock  # Mount Docker socket for communication
    ports:
      - "80:80"  # Expose port 80 for web sites (nginx and javalin)
      - "8080:8080"  # Expose port 8080 for Traefik dashboard
```
The number of replicas has been chaged to 2 to launch the docker compose file in a load blancing environment.

### Run servers with scalability
We can start several instance of the services by simply using this command :
```bash
docker compose up
```

Once the services are started we can use this command to extend the number of servers of each services.
The number specify the total servers which will be running.
We have to specify the command by using the argument --no-recreate because we don't want to recreate the container.
```bash
docker compose up -d --scale nginx=3 --scale javalin=3 --no-recreate
```

To deacrese the number of servers use the same command by specifying an inferior number of servers
```bash
docker compose up -d --scale nginx=2 --scale javalin=2 --no-recreate
```

This command stop all the services running.
```bash
docker compose down
```

Example of how it works :

<img width="811" alt="Capture d’écran 2024-01-13 à 15 51 13" src="https://github.com/kevinAuberson/dai-lab-HTTP/assets/100291212/395a11d3-7357-4f48-a7a2-6fcaad482431">

#### Test load balancer with scalability

<img width="1460" alt="Capture d’écran 2024-01-13 à 16 13 12" src="https://github.com/kevinAuberson/dai-lab-HTTP/assets/100291212/a02e39a2-4ea3-4319-92ad-52d62af4df5b">

### Javalin application
In our application we added some log information to understand which service is responding to our command.
