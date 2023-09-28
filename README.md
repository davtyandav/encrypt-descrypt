Pre-requirements:
- Java 16
- Maven 3 

# Build
`mvn clean package`

# Run
`java -jar target/app.jar <encrypt|decrypt> <inputFilePath> <outputFilePath>` 

# Example
* `java -jar target/app.jar encrypt test.jpg encrypted.jpg`
* `java -jar target/app.jar decrypt encrypted.jpg decrypted.jpg`