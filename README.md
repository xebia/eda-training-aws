# eda-training-aws

###Startup environment
#### Application environment
- `docker-compose up`

#### AWS (localstack)
- `git clone https://github.com/localstack/localstack.git`
- `cd localstack`
- `TMPDIR=/private$TMPDIR docker-compose up -d`

 
###Build Docker
## Push Docker image 

1. On Mac only: Expose demon without TLS

See: https://forums.docker.com/t/spotify-docker-maven-plugin-cant-connect-to-localhost-2375/9093/16
```
docker run -d -v /var/run/docker.sock:/var/run/docker.sock -p 2375:2375 bobrik/socat TCP4-LISTEN:2375,fork,reuseaddr UNIX-CONNECT:/var/run/docker.sock
```

2. Add docker.io registry credentials in `~/.m2/settings.xml`
```xml
        <server>
            <id>docker.io</id>
            <username>docker-account-name</username>
            <password>pwd</password>
        </server>
```
The `docker-account-name` is defined as env variable in the parent `pom.xml` as:
```xml
<properties>
    <docker.image.prefix>upeter</docker.image.prefix>>
</properties>

```

3a. Build and push an image in a sub-project

- `cd crm-system`
- ` mvn install dockerfile:build dockerfile:push`

3b. Push all images
- from project root:
```properties
mvn -Dmaven.deploy.skip deploy
```
