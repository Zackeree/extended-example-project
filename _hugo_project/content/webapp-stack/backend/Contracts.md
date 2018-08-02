---
title: "Radio Telescope Contracts"
---

## What do we mean by "Contracts"
Whenever we refer to the "business logic" or "contracts" part of an application, we are
referring to the 
part of the application responsible for validating any incoming request from the client-side.
This validation will cover 
anything from making sure the required fields for a given request are filled in to making sure
the person accessing 
a certain endpoint has the permission to do so.

## 1. The Command Object
There are two things that make up the centerpiece of the contracts architecture: The Command Object
and The Responder interface (see next section).
The Command Object is an interface with a single execute method that any sort of request from the 
client-side will implement.
Each concretely-implemented class that extends the Command Object will concretely implement the execute
method. This method
is the one that is actually responsible for querying/inserting/deleting items from the database.

## 2. The Responder Interface
The second part that makes up the centerpiece of the contracts architecture is the Responder interface.
It is in charge of responding 
back to the client-side with the information they requested via our API. The Responder object itself currently
has 6 other interfaces that
extend from it. 

Each other responder interface is just in charge of concretely implementing what will be sent
back to the client-side on success.

By default, each responder will always return a Multimap<ErrorTag, String> where ErrorTag is just an Entity-specific
enum containing 
the different constraints that could fail

## 3. The Factory Interface
For each Spring Entity we have, in our contracts layer we will also need a Factory Interface for it. The purpose 
of the factory interface
is more or less just to wrap up all of our command objects for an Entity into one interface, that way we can just
concretely implement the factory interface
and have all of our methods in one consolidated class.

## 4. The UserContextWrapper Interface
The UserContextWrapper Interface is in charge of tying in to Spring Security and validating that the person making
a request has the roles/permissions
to do so. As with the Factory Interface each set of contracts for a specific entity will need a concrete UserWrapper 
implementation.

One important thing to note is under the scenario that a client-side request does not require any sort of
roles/permission checking, 
the UserWrapper should just return the concrete factory's command object instead. Spring Security only has
an Authentication token created 
once a user has been validated and logged in. All endpoints that can be accessed by an anonymous user will
not have any Spring Security authentication.

## 5. Testing the Contracts layer
One of the main reasons this layer in web-app architecture is referred to as the "business logic" is due to 
the fact that the majority of our application's logic exists here.

It is exactly because of this that we must ensure our Contracts layer is very well tested. Each command object
is concretely implemented 
should have a unit test the checks all possible validation scenarios.

Examples of unit tests for the Spring Framework have been provided in the example application.


