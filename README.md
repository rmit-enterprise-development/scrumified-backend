# scrumified-backend

## Setup
To setup the project, you need to have the following requirement;
- Heroku (responsible for hosting server and database)
- Intellij (Env File extension)
- SDK 17.0.1
- Postgre SQL 14
- Maven
- Postman

## Package details
### Java EE
With the package of Java EE, we can use the HTTP/2 support, binding API for JSON converting between POJO and JSON.
We also use this for Server-Send Event support. So that our application can be real-time interaction between users.
For instance, dragging cards, or updating cards of a user in project 1 will notify the change to all user on that project.

### Spring Boot
A design that follows the MVC pattern (Model-View-Controller).
It allows us to have dependency injection which make the design loosely coupled.
This framework provides embedded Tomcat which allows us to deploy the REST API.
### Hibernate
An implementation of JPA which allows the project to communicate with the database by generate query and executes using JDBC and mapping between Object and Relational
Model mapper
It is a tool for the project to convert quickly between a POJO and a DTO class.
With abstraction level of converting, this will make the application scalable when there are more fields in a class to be convert or more entities to be converted to DTO. 
### Lombok
It is a tool for the project to reduce the manual writing code of getter, setter, or constructor.
We only need to use notation provided @Data @Getter @Setter @AllArgsConstructor @NoArgsConstructor.
This will help developer focus on create the main logic of the project.
### Spring-boot-starter-hateoas
It is a tool to make the REST API become RESTful API.
It will indicate the object that return by the method to construct the related link and return with the Object.
So that the API’s user can easily navigate the next API call base on the current receive JSON.
For instance, if user call creates a project, he or she can then see the API suggestion of navigate the project details, or API to update the project, or API to delete the project or API to create stories (in our case) in the backlog.
### Springdoc-openapi-ui
For further usability of the REST API, the project uses this tool to provide a document of the API so that further web-developer can utilize our API so that they can intergrade our application into their application.
### Postman
A tool that helps the developer to testing the APIs by typing the REST API URI.
It will provide, keys params, body request, formatting for JSON return or body request, save the URI and share the collection of requests.
So that developer can collaborate with each other in Back end.
### PostgreSQL
A relational database management system that required for this project. It will store and manage the data in this website.

## API usage
You can use the following documentation to further understand the API usage
https://scrumified-dev-bakend.herokuapp.com/swagger-ui/index.html#/

As you can see, each controller will responsible for a certain area of the page in Front-end scrumified
- Page landing will use the API of user controller
- Page dashboard will use the API of project controller, story controller, and user controller
- Page backlog will use the API of the story controller, sprint controller, and user controller
- Page sprint will use the API of the story controller, sprint controller, and user controller
- Page roadmap will use the API of the story controller, and sprint controller

The special thing about our API is that we can use the API GET "/backlog" to subscribe to the server.
This will allow the page to receive the information of refresh if needed whenever there is a changing in the project.
In our project, this includes the udpate of the stories or sprints.

## Deployment
To deploy the project, you only need to register to Heroku, and create a application to deploy.
To make sure the project will work, you need to link project via the Heroku CLI instead of Github because there is an error up to May 18th 2022 related to github deployment.
Furthermore, the design of the Back-end application also prevent the cold call in the server where it will take a long time for the server to response if there is no recent activity.
We try to do so by providing a schedule call by the server to itself in a constant interval.


## Class diagram
[Enterprise Diagram - Class Diagram.pdf](https://github.com/rmit-enterprise-development/scrumified-backend/files/8710530/Enterprise.Diagram.-.Class.Diagram.pdf)

