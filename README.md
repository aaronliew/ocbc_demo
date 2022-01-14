# Documentation

This project is separated into two parts: **command-line interfaces(CLI)** and **spring boot application**

All command-line interfaces are located in package com.example.demo.command. All CLIs are using UNIREST library to perform api call (http://localhost:8080).

All test cases only cover command-line interfaces, and REST apis. All scenario are covered in com.example.demo.command.CommandIntegrationTest.

### Guides
Please make sure com.example.demo.DemoApplication is up and running before perform any CLIs action. In order to perform CLIs action, just run **mvn clean package**. It will be packaged into a jar file. Here is the example to call CLI command:
```bash
java -jar "target/demo-0.0.1-SNAPSHOT.jar" login bob
 ```
Here are the lists of commands 
```bash
java -jar "target/demo-0.0.1-SNAPSHOT.jar" login <username>
java -jar "target/demo-0.0.1-SNAPSHOT.jar" topup <amount>
java -jar "target/demo-0.0.1-SNAPSHOT.jar" pay <recipientName> <amount>
 ```
### Assumptions
1. The clients can only perform transactions between one another.
2. All username is in lower case.
3. TopUp and payment amount are in cents. Cannot contain decimals 

