---
title: "Kotlin Style Guide" 
---

## General Notes
Kotlin has no `new` operator; to create an instance of a class, you simply call the constructor as 
a normal function, e.g.
```kotlin
val userFactory = BaseUserFactory(userRepo)
```
Kotlin has a useful class definition keyword, the `data` keyword. Classes defined with the `data`
keywork implicitly have useful helper methods like `equals()`,
`hashCode()`, and `toString()` of 
the form (for example) `"User(username=cspath1, email=cspath1@ycp.edu, password=pass1234)"`, and `copy()`
The `copy()` function provides the ability to very easily create a copy of a data class
while optionally changing the value for any of the properties in the copy. Consider the 
following example:
```kotlin
data class User(val username: String, val email: String, val password: String)
val johnSmith = User(username = "jsmith", email = "jsmith@gmail.com", password="password")
val johnSmithButSafePassword - johnSmith.copy(password = "Iw24~dkE@4+")
val johnSmithsTwinSteve = johnSmith.copy(username = "ssmith", email = "ssmith@gmail.com")
```

## Code Style
### 1. Colons
There is a space before colon where colon separates type and supertype or interface and there's no 
space where colon separates instance and type:
```kotlin
interface Foo<out T : Any> : Bar {
    fun foo(a: Int): T
}
```

### 2. Lambdas
In lambda expressions, spaces should be used around the curly braces, as well as around the arrow 
which separates the parameters from the body. Whenever possible, a lambda should be passed outside 
of parentheses.
```kotlin
list.filter { it > 10 }.map { element -> element * 2 }
```
In lambdas which are short and not nested, it's recommended to use the it convention instead of
declaring the parameter explicitly. In nested lambdas with parameters, parameters should be always 
declared explicitly.

### 3. Class Header Formatting
Classes with a few arguments can be written in a single line:
```kotlin
class Person(firstName: String, lastName: String)
```
Classes with longer headers should be formatted so that each primary constructor argument is in a 
separate line with indentation. Also, the closing parenthesis should be on a new line. If we use 
inheritance, then the superclass constructor call or list of implemented interfaces should be located 
on the same line as the parenthesis:
```kotlin
class Person(
    id: Int,
    firstName: String,
    lastName: String,
    user: User
) : Human(id, name) {
    // ...
}
```

### 4. Unit Return Type
Unit  is essentially Kotlin's version of `void`. If a method or function returns `Unit`, the return 
type can be omitted.
```kotlin
fun foo() { // ": Unit" is omitted here
    ...
}
```

### 5. Functional Programming
When working with iterables, you should prefer functional programming paradigms over traditional 
loops or iterators; Kotlin provides methods like `map{}, each{}, find{}, findAll{}, every{}, collect{}, 
inject{}`, and more on any class which implements the `Iterable` interface.

### 6. JavaDoc
All public API methods should be fully documented with JavaDoc comments; these will be turned into a 
static HTML API documentation website by a Gradle task during build in order to provide an up-to-date 
API reference. JavaDoc comments for private methods is recommended in most cases, but optional 
(since it will not be included in the API doc; its would only be for developer benefit).

### 7. Access Modifiers
In Kotlin, everything is `public` by default; an explicit access modifier should only be used when 
it is something other than `public`. You should use the most restrictive access modifier that 
makes sense (i.e. things that _can_ be `private` or `protected` _should_ be).

### 8. Data Classes
By convention, `data` classes should be used ONLY for data storage/passing, etc. That is to say, 
in general, `data` classes should not have any additional methods defined or implemented. All 
manipulation of the data classes in the Episo environment will be performed by elements of an 
Episo Contract element (i.e. `UserWrapper`, `Factory`, `Command`, etc.).