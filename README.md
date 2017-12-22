# WalletHubParser

README.md

## Documentation:
The folder named site was generated using mvn site shell command. There you can find the project dependency tree and more information. (Not all information was set in the pom.xml, some of the documentation was generated with default data and it's not relevant)

## Source code:
The folder WalletHubParser is the eclipse project.
I've decided to use spring boot.
The project arquictecture is pretty straight forward and light as possible, I've decided to use jdbc and not jpa to avoid unnecessary overhead.

## Project Build:
The project was built on eclipse using maven.
To build by command line execute mvn clean package.


## Project Execution:
DB Configuration
- Update the application.propertys file with the database settings and it should be good to go

### MAVEN
The project is built using spring boot, you can just run it with spring-boot:run and pass the expected arguments
Example:

mvn spring-boot:run --accesslog=access.log --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200

--accesslog Filename and location
--startDate Query logs beggining at this point
--duration Query logs from startDate to this point
--threshold Query requests in the logs that originated from the same ip more them the number of times defined by this argument

### ECLIPSE
- Go to run-> run configurations-> arguments
- Add the application arguments
	--accesslog=access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
- Run the project

### COMMAND LINE
java -jar "parser.jar" com.ef.Parser --accesslog=access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
