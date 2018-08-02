---
title: "Spring Framework"
---

## Spring Data JPA - Entities and Repositories
Spring Data JPA (Java Persistence API), part of the Spring Data family, allows us to abstract away much
of the SQL Layer by acting as
a wrapper overtop of the SQL ORM layer. What Spring does is allows us to create Entity objects (indicated via
the @Entity annotation, 
which model our database tables and allows us to perform CRUD operations on our MySQL database without actually 
ever needing to write a 
SQL statement.

This is made possible by Spring Repositories (indicated via the @Repository annotation), which are interfaces that 
essentially are able to adapt the
entity objects into SQL statements for us. Each Spring Repository is in charge of managing a single Spring Entity

### Create
For persisting new values, what happens is this. A request from the client-side is sent to the server.
Our "business logic" (more on this later) 
will be in charge of validating any constraints with the request. Once a request gets validated, we will adapt 
the client-side request into an Entity object.
Then all we need to do is use the Entity's Repository to persist the new entry (done with the save() method)

### Read
For retrieving existing values from the database, a request from the client-side is sent to the server,
often only containing the id of the record
we wish to pull up. Then all we need to do is call the Entity's Repository method aptly named findById
and it will retrieve the record for us.

### Update
In order to grab an existing entry in the database and update it, we basically combine the processes
for creating and reading. 
A request from the client-side will be sent to the server containing all of the new values we wish to 
persist, along with the id of the
entry we wish to update.

### Delete
Deleting an entry is as easy as retrieving it. Pretty much the same exact thing occurs, except we will
be deleting the record rather than returning it to the server.

## Spring Data JPA - Query DSL