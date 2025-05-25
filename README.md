# My github Project -- POC

When I first started my software development career I came across a programming pearl from the ACM and one of them said, "*It is easier to build a 6 inch mirror and then a 10 inch mirror, than it is to build a 10 inch mirror*." This project is my 6" mirror.

I have an idea for a project that will use some github APIs and so to get an idea if what I want to do is feasible, and since I haven't used any github APIs before this will allow me to experiment with them, get an handle on working with them, and see if my project is feasible.

In addition to github APIs there are some other tools and technologies that I will use for the first time to use and add them to my toolbox. Namely I will use a Azure Cosmos DB for the data repository to learn about that. Additionally I will create a spring boot backend and will use the Spring Cloud Azure project to integrate with the various Azure services.  

## Architecture Overview 

This project will be built with an Angular 19 front end, springboot backend, and an Azure cosmos DB. The Angular code will be built alongside the spring boot and will be deployed as one unit. The embedded springboot server will serve the static Angular content for the app. All of this will be deployed in an Azure app service.

Optionally this may make use of an Azure function app for some serverless functions that may be needed. We will see.