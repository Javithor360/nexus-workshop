# NEXUS WORKSHOP

A technical solution that provides a ticket system to easily track work progress and manage tasks.

# IMPORTANT NOTE
- At the moment the project only provides a RESTful API to demonstrate database entities and their relationships.
  - Keep in mind that web module will come on the second phase of the project.
  - Authentication will be implemented when actual conflicts with monorepo are solved.
- Some of the other features are not implemented yet, but they might come in the future.
- Some of the actual features also might be changed or removed in the future according to the project's needs.
- This project is still in development and is not ready for production use. It is intended for educational purposes only.

## Technologies Stack
<table align="center">
    <tr>
        <td align="center"><img src="https://www.oracle.com/a/ocom/img/cb71-java-logo.png" alt="Java" width="64" height="64"></td>
        <td align="center"><img src="https://miro.medium.com/v2/resize:fit:600/1*ljHUhFnaBissdRBe7DIo6g.png" alt="Jakarta EE" width="64" height="64"></td>
        <td align="center"><img src="https://cdn.clever-cloud.com/uploads/2023/03/mysql.svg" alt="Payara" width="64" height="64"></td>
    </tr>
    <tr>
        <td align="center"><strong><a href="https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html">Java 21</a></strong></td>
        <td align="center"><strong><a href="https://spring.io/projects/spring-boot">Spring Boot v3.3.3</a></strong></td>
        <td align="center"><strong><a href="https://www.mysql.com/">MySQL v8</a></strong></td>
    </tr>
</table>

## Features at the moment
- [x] Spring Boot project setup
- [x] MySQL database connection
- [x] RESTful API

## Installation
1. Clone the repository
    ```bash
    git clone https://github.com/Javithor360/spring-fullstack-app.git
    ```
2. Open the project in your favorite IDE
3. Adjust database connection settings in `src/main/resources/application.properties`
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:<port>/<database_name>
    spring.datasource.username=<username>
    spring.datasource.password=<password>
    ```
4. Make sure to create the database in MySQL
5. Run the project
6. Open POSTMAN and go to `http://localhost:8080/users`
7. Enjoy!
