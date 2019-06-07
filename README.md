# eda-training-aws

### Startup environment
#### Application environment
- `docker-compose up`

#### AWS (localstack)
- `git clone https://github.com/localstack/localstack.git`
- `cd localstack`
- `TMPDIR=/private$TMPDIR docker-compose up -d`
- For faster startup minimize localstack components via `SERVICES` env variable, only selecting the necessary services:
    - `TMPDIR=/private$TMPDIR && SERVICES="sns,sqs,kinesis" docker-compose up -d`



### Start Application
#### UI
- Open browser on URL: `http://localhost:9000/`
- Manually insert an order: 
```
curl -X POST --data '{"customerId": 1,"shippingAddress": {"street": "Sesamstreet","number": "5b","zipCode": "3456AB","city": "Amsterdam","country": "Netherlands"   },   "lines": [{"productId": 1001,"productName": "Fancy Gadget #1","itemCount": 1,"priceCents": 100},{"productId": 1002,"productName": "Fancy Gadget #2","itemCount": 2,"priceCents": 200},{"productId": 1003,"productName": "Fancy Gadget #3","itemCount": 3,"priceCents": 300},{"productId": 1004,"productName": "Fancy Gadget #4","itemCount": 4,"priceCents": 400},{"productId": 1005,"productName": "Fancy Gadget #5","itemCount": 5,"priceCents": 500}   ] }' http://localhost:9000/order-api/v1/orders --header "Content-Type:application/json"
```


 
### Build Docker
#### Push Docker image 

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

3b. Build and push all images
From project root:
- Build images locally
```properties
mvn install
```

- Push images
```properties
mvn -Dmaven.deploy.skip deploy
```
