# My github Project -- POC

When I first started my software development career I came across a programming pearl from the ACM and one of them said, "*It is easier to build a 6 inch mirror and then a 10 inch mirror, than it is to build a 10 inch mirror*." This project is my 6" mirror.

I have an idea for a project that will use some github APIs and so to get an idea if what I want to do is feasible, and since I haven't used any github APIs before this will allow me to experiment with them, get an handle on working with them, and see if my project is feasible.

In addition to github APIs there are some other tools and technologies that I will use for the first time to use and add them to my toolbox. Namely I will use a Azure Cosmos DB for the data repository to learn about that. Additionally I will create a spring boot backend and will use the Spring Cloud Azure project to integrate with the various Azure services.  

## Architecture Overview 

This project will be built with an Angular 19 front end, springboot backend, and an Azure cosmos DB. The Angular code will be built alongside the spring boot and will be deployed as one unit. The embedded springboot server will serve the static Angular content for the app. All of this will be deployed in an Azure app service.

Optionally this may make use of an Azure function app for some serverless functions that may be needed. We will see.

## One Month

I made the initial commit for this on 05/25 over Memorial day weekend at my MIL's house. Today, 06/25/2025, is one month since I started and feel that I have accomplished the goals I set out to accomplish. I get data from GitHub for my projects, I have a webhook collect relevant information when commits are pushed, and I have (manually) deployed it to Azure.

I learned a bit about Cosmos DB and NoSQL databases. I created an Angular 19 front end and learned quite a bit about modern angular. I created the springboot backed to tie to together. I did not need an azure function, but that's ok. Nor did I use any feature of Spring Cloud Azure beyond the Cosmos DB packages. I could possible take some more time to add key vault support.

When I started I didn't know about MapStruct, but discovered it via Medium which I subscribed to on June 1. I really like MapStruct and avoid some of the drudgery of using DTOs.

The CD/CI action in GitHub isn't working (it is all maven), though the command works locally. All in all a successful quick sort of project. 

It is functional for what I needed, but it definitely isn't pretty despite my attempts at wrangling some CSS. I think the next steps are to get some framework help in that department with Material or Tailwind.