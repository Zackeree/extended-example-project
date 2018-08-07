---
title: "Backend Bootcamp"
---

# Backend Bootcamp

The purpose of this section is to help you get hands on with the structure and work-flow of the architecture. If you plan on working on
the backend, it is **highly recommended** to work through the bootcamp section to get a good understanding of what the backend architecture
will look like, how to use Kotlin efficienty, and how all of the different pieces of the architecture fit with each other.

By this point, you should have access to the repo, if not (email me)[mailto:cspath1@ycp.edu] and I will add you. The first thing you are 
going to want to do after cloning the repo, is take a look at the branches. You should see a branch named "master-YourGitHubUsername". This
branch will effectively act as your master branch. It is protected so that only users that have been given the privilege to are allowed to
push changes to it.

After you have IntelliJ opened and the project loaded, use IntelliJ's built-in terminal to checkout your master branch. From there, create
a new branch (you can name it whatever you want, but I would recommend something like develop-YourGitHubUsername). This will be the branch
that will act as your working branch. Anything you do will be pushed to this branch. 

## Entities
Your task is to think about all of the different pieces you will need in order for a Person Entity to have **one to many** phone numbers 
and **one to many** addresses. For both addresses and phone numbers, what fields do you think you will need? Feel free to think it through
and come up with your own class structure. If you want to (for some reason) see what I came up with, check out the UML Diagram in the CS481 
Team Drive. Maybe you thought of something I didn't or vice versa.

## Repositories
As part of coming up with what the the Entity object will look like, you will also need to figure out what repository methods (if any) you will 
need for the Entity's Spring Repository. You may not know this right away, and may figure out when you are building out the contracts for the 
Entity, which is perfectly fine.

## Contracts
When you are done working in the repository layer for a new Entity, it is time to move to the contracts layer, which is responsible for ensuring
a request from the client has valid constraints before communicating with the database. Think about what commands you will need to implement for 
each Entity (such as Create, Retrieve, Update, Delete), but what other actions should the a User be able to do? Think about it and if you come up 
with any others, feel free to implement them as well. For each command, be sure to think of all possible scenarios you may need to test and
include it in your validation.

After you are done with create **and testing** your command objects, it's time to make the BaseFactory for the Entity. The BaseFactory allows us to
consoldiate all of the API for an Entity into once class (one that just has methods to return each command type). When testing a concrete implementation 
of a BaseFactory interface, all you need to do (since you should have already tested each individual command) is make sure that each method returns the
expected command object.

After you complete this, all you have left to do in the contracts is implement the UserEntityWrapper for the Entity you are implementing, which
allows us to use Spring Security to authenticate any request that a user makes. This will require a little bit more thinking, and a bit more 
testing too. For this, you will need to know **what type** of user should have the ability to use that part of the API. Examples include
only allowing the creator of a record to view, update, and delete it, ensuring a user is logged in and has a user account, making sure you
have the required role or permission. When testing this layer, you should test each authentication scenario that could occur to make sure 
the logic was correctly implemented.

After that you are done with the Contracts layer (well for that give Entity at the very least)!

## Controllers

At this point, we only have one more layer in the background, and that is the layer that is in charge of actually exposing your API to the client!
For this layer, we will be implementing form adapters and our Spring Rest Controllers so we can communicate with the client-side HTML/JS.

To start, you will want to make want is known as a "form adapter". Basically, since there is a separation between the controller layer and the 
contracts layer, we could potentially have server-side changes that the front-end will be completely unaware of. In a situation where we don't use 
form adapters to essentially bridge the gap between these two layers, and sort of changes that were made to the contracts layer would not be 
detected until runtime when someone accesses the endpoint that was changed. With form adapters, any sort of errors we get are going to be compile-time
errors since the controller layer is aware of changes made to the contracts layer.

Form adapters are going to look exactly like the Request data class they will be adapted to, except for the fact that they also have nullable values.
Because of this, we can perform some very basic validations on the client-side request before passing it down to the contracts layer to save on 
network overhead ever so slightly.

It is worth mentioning that I came up with this design after I had already implemented the contracts layer (just part of coming up with a design
as you're working on it) so I have an unnecessary amount of validation in the contracts layer. I have not personally decided which approach I think
is better, and am totally fine with just checking for null values in the form adapter if you want.

After we have successfully made our form adapters, our last step in the backend structure is to implement a Spring Rest Controller for each command
object. I have seen a lot of people that prefer making one controller for each entity rather than each command object, but I personally have found
that doing this can (and with enough time, will) result in an overly complicated and beefy controller. Instead, I like to just have one controller 
for one endpoint, that way I know exactly where to look and I don't end up having a 400 line controller. There is a abstract controller class for 
each type of operation (Create, Retrieve, Update, Delete, Page) which ties into what you are returning to the client-side. For each of the controller
types (as they appear respectively), I have been returning the following (Long, Info class, Long, Long, Page of Info classes). I would recommend 
following this as well.

If you have any comments or concerns, again feel free to (email me)[mailto:cspath1@ycp.edu] or text me at 717-823-2216.