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
