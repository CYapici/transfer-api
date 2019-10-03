#  The transfer service api
## Features
Rest service that allows:
* Creation of monetary accounts with an initial non negative balance
* Transfer money from remitter to beneficiary

## The Model
 It has been based on the model definition
 Minimum model definition
 o Account
 ▪ Account number
 ▪ Balance
 o Transaction
 ▪ Source account number
 ▪ Destination account number
 ▪ Amount
 
## Implementation Rules
1. An Account can't have a negative Balance;
1. A transfer cannot make the source Account negative, that is, the source account must have enough funds for the transfer to complete;
1. The application must support simultaneous (concurrent) account creation and transfer requests;
1. The application must keep data consistent between requests, even in a case of an invalid request or execution error.

## Solution Description
### Programming Language
* Java
###  REST Framework
* Spring Boot 2.1.9.RELEASE
### Persistence Medium
* H2 Embedded Database
### Data Persistence Layer and the  ORM
* JPA - spring-boot-starter-data-jpa
* Hibernate JPA
### BDD Testing
* Cucumber Cukes JVM has been implemented 
* Has a small feature with the following instructions,
  
  Scenario: Conform the maths of the Transfer Api
    Given api populates the beneficiary  and the remitter with  100 currency
    When remitter sends 10 currency to remitter
    Then remitter has 90 currencies and the beneficiary has 110
    
    
### Unit Testing
* Junit 4.12
* Mockito Core 1.10.19
### Dependency Management tool
* Gradle Wrapper
 
### API URI Map

Api url listing is as follows :

http://localhost:{port}/accounts/transfer
http://localhost:{port}/accounts/new
http://localhost:{port}/accounts/
http://localhost:{port}/transfers/

curl -X PUT \
  http://localhost:{port}/accounts/transfer \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 50' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8092' \
  -H 'Postman-Token: 7f6b5322-ce3d-4e3b-b8f3-e779b9ff8ac2,69204277-a1fd-4927-b77c-88c36c9bd403' \
  -H 'User-Agent: PostmanRuntime/7.17.1' \
  -H 'cache-control: no-cache' \
  -d '{ 
	"sourceId":2,
 	"destId":1,
 	 "amount":1
}'

curl -X PUT \
  http://localhost:{port}/accounts/new \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 45' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:{port}' \ 
  -H 'User-Agent: PostmanRuntime/7.17.1' \
  -H 'cache-control: no-cache' \
  -d '{
	
	
	"name":"jack",
 	"initialBalance":10
}'

curl -X GET \
  http://localhost:{port}/accounts/ \
  -H 'Content-Type: application/json' \ 
  -H 'cache-control: no-cache'
  
  curl -X GET \
    http://localhost:8092/transfers/ \
    -H 'Accept: */*' \
    -H 'Accept-Encoding: gzip, deflate' \
    -H 'Cache-Control: no-cache' \
    -H 'Connection: keep-alive' \
    -H 'Host: localhost:{port}' \
    -H 'User-Agent: PostmanRuntime/7.17.1' \
    -H 'cache-control: no-cache'


## Deployment Instructions
### System Requirements
   * Java version: 11.0.4  

## Execution
   ### Build command 
      ./gradlew build   
      
   ## running the application
         ./gradlew bootRun
         
   ## running the cucumber testcases
   Cucumber requires a running instance of the application therefore running the app as instance
   and the cucumber container as a seperate instance is the way to pass the cucumber test cases  
            ./gradlew bootRun then ./gradlew test
             