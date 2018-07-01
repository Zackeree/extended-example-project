# example-project

1. ORM Layer
* This project utilizes the Spring Framework to communicate with a MySQL Database. For more information on Spring, refer to https://projects.spring.io/spring-data

2. Install IntelliJ
* https://www.jetbrains.com/idea/download/#section=windows
* You should be able to sign up to get IntelliJ Idea Ultimate Edition for free using your school email address via their student license

3. Install MySQL
* https://dev.mysql.com/downloads/mysql
* start mysql - "mysql.server start" ** use "stop" and "restart" when needed

4. Install Gradle 
* https://gradle.org/install

5. Create example Database
    1. Open the Database tab in IntelliJ
    2. Add New MySQL Datasource and Test Connection
    3. Open SQL Console (that little terminal icon in the Database Tabe)
    4. Run the init.sql file
    
6. Set up the Gradle Wrapper
* Open IntelliJ terminal in the project directory and type "gradle wrapper"

7. Contracts Layer
* The "contract" layer refers to the business logic of the application. 

8. Kotlin
* The server code for this application is written using the kotlin programming language made by the folks at JetBrains. It compiles to java and runs on the JVM. For information regarding kotlin, it is highly recommended to check out https://kotlinlang.org/docs/reference/ and https://try.kotlinlang.org/ to help familiarize yourself with the language