# eda-training-aws

### Install required software
Git: https://git-scm.com/downloads
Docker: https://docs.docker.com/install/ (requires an account and system restart)
AWS CLI: https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html
JDK (> version 7): https://www.oracle.com/technetwork/java/javase/downloads/index.html
Follow the installation instructions in the given links for your operating system. 

### Setup labs project:
From a directory of your choice type: git clone https://github.com/xebia/eda-training-aws
Go into the newly created directory: cd eda-training-aws

### Setup localstack, which is a local AWS environment and databases
From the project root <eda-training-aws> go into the <scripts> directory:  `cd scripts`
- Run: `start-middleware.bat` for windows or `./start-middleware.sh` for mac, linux

### Compile project
Move back to the project root directory <eda-training-aws>: cd ..
- Run: `mvnw.bat install` for windows or `./mvnw install` for mac, linux
All projects must compile successfully

### Start Applications
From your IDE then start the applications:
- order-system: `com.xebia.OrderApplication`
- inventory-system: `com.xebia.InventoryApplication`
- crm-system: `com.xebia.CRMApplication`

### Insert an order
- Open browser on URL: `http://localhost:9000/` to insert an order via the web-interface

- Manually insert an order with `curl`: 
```
curl -X POST --data '{"customerId": 1,"shippingAddress": {"street": "Sesamstreet","number": "5b","zipCode": "3456AB","city": "Amsterdam","country": "Netherlands"   },   "lines": [{"productId": 1001,"productName": "Fancy Gadget #1","itemCount": 1,"priceCents": 100},{"productId": 1002,"productName": "Fancy Gadget #2","itemCount": 2,"priceCents": 200},{"productId": 1003,"productName": "Fancy Gadget #3","itemCount": 3,"priceCents": 300},{"productId": 1004,"productName": "Fancy Gadget #4","itemCount": 4,"priceCents": 400},{"productId": 1005,"productName": "Fancy Gadget #5","itemCount": 5,"priceCents": 500}   ] }' http://localhost:9000/order-api/v1/orders --header "Content-Type:application/json"
```

### Stop localstack and databases