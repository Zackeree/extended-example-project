---
title: "Spring Data JPA"
---

## Entities and Repositories
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

## Query DSL
Spring's Query DSL (Domain-Specific Language) is a key feature behind the design choice for the larger Spring 
Framework as a whole. It allows us to 
build out custom repository methods really easily.The way Spring Data JPA is designed allows Spring Repositories 
to be "aware" of the contents of the Entity
object they are tied to.

This means we can build out really powerful queries as long as we follow the Query DSL syntax.
All methods built using the 
Query DSL syntax must start with either "findBy", "findAllBy", "findFirstBy", "countBy", or "existsBy".

Beyond that, since the Query DSL knows about the fields contained in the Entity object they are tied to,
we can build a method to find all people by 
first name just by instantiating the method prototype "findAllByFirstName". Spring will deal with the rest.

## Query Annotation
So I may have ever so slightly fibbed when I said we would NEVER have to write something that resembles a 
SQL statement. As it turns out, 
there are some queries that we can't build out using Spring Data JPA's. What Spring allows us to do in this
scenario is use their built-in 
@Query annotation to build something that is more or less a psuedo-SQL statement. 

You can see examples of this in the example project repositories. In the example application, the majority
of the repository methods were written 
using the Query annotation, and while it is not necessary, due to an issue with how Kotlin as a language works,
Kotlin has trouble creating those
repository methods correctly sometimes once it is compiled to Java.

As it turns out, the best way to ameliorate this issue is by using the Query annotation. It allows Spring to compile
the repository method correctly because
rather than trying to build the method using the Query DSL, it will use the Query annotation, which works regardless 
of the method name it is tied to.

Note that queries built following the Query DSL conventions should work when Kotlin is compiled to Java, but the
Query annotation acts
as an extra failsafe.

## Paging And Sorting
One final thing to note about Spring Data JPA is the PagingAndSortingRepository interface that all of our repositories
will extend from.
The PagingAndSortingRepository itself extends the basic CrudRepository that Spring has and just adds a few extra methods
that allow for us to
integrate pagination into our list queries for our Entity objects. This will more or less just allow us to return a slice
of an entire set so 
we can cut down on network overhead

