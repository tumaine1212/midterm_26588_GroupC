Movie Management System
Project Information

Student Name: Tumayine Desire
Student Id:26588

University: Adventist University of Central Africa

Course: Web Technology and Internet

Project: Database & Spring Boot Application Development

Project Name: Movie Management System

Date: March 10, 2026

Project Description

## The Movie Management System is a web application built using Spring Boot that manages information related to movies, actors, genres, and cinema locations.

The system demonstrates database relationships and Spring Data JPA functionalities, including:

One-to-One relationship

One-to-Many relationship

Many-to-Many relationship

Sorting

Pagination

Existence checking using existsBy()

Retrieving movies based on location (province)

## The goal of the project is to demonstrate how relational databases work with Spring Boot applications.

Technologies Used

Java

Spring Boot

Spring Data JPA

MySQL

Maven

Visual Studio Code

Postman for API testing

Entity Relationship Diagram (ERD)

## The system contains five main tables:

Movie

Actor

Genre

Cinema

Province

Relationships

Province → Cinema → One-to-Many

Cinema → Movie → One-to-Many

Movie ↔ Actor → Many-to-Many

Movie → Genre → Many-to-One

Movie ↔ MovieDetails → One-to-One

These relationships help organize movie data and ensure efficient database operations.

Implemented Functionalities
1. Saving Cinema Location

Cinema locations are stored in the database and linked to a Province.

Example logic:

A cinema is created.

It belongs to a specific province.

Movies are shown in a cinema.

This structure helps track where movies are being shown.

2. Sorting Implementation

Sorting is implemented using Sort from Spring Data JPA.

Example:

Sort sort = Sort.by("title").ascending();
movieRepository.findAll(sort);

Movies can be sorted by:

Title

Release Date

Rating

3. Pagination Implementation

Pagination is implemented using Pageable.

Example:

Pageable pageable = PageRequest.of(pageNumber, pageSize);
Page<Movie> movies = movieRepository.findAll(pageable);

Pagination improves performance by returning only a limited number of records per request.

4. Many-to-Many Relationship

A Many-to-Many relationship exists between:

Movie ↔ Actor

One movie can have many actors and one actor can appear in many movies.

Example mapping:

@ManyToMany
@JoinTable(
 name = "movie_actor",
 joinColumns = @JoinColumn(name = "movie_id"),
 inverseJoinColumns = @JoinColumn(name = "actor_id")
)

This relationship uses a join table called movie_actor.

5. One-to-Many Relationship

Example:

Cinema → Movie

One cinema can show many movies.

Example mapping:

@OneToMany(mappedBy = "cinema")
private List<Movie> movies;

The movie table contains a foreign key referencing cinema.

6. One-to-One Relationship

Example:

Movie ↔ MovieDetails

Each movie has one detailed record containing:

Description

Duration

Director

Example mapping:

@OneToOne
@JoinColumn(name = "movie_details_id")
private MovieDetails movieDetails;
7. existsBy() Method

The existsBy() method checks whether a movie already exists in the database.

Example:
boolean exists = movieRepository.existsByTitle(title);

This prevents duplicate movie entries.

8. Retrieve Movies by Province

Movies can be retrieved based on the province where the cinema is located.

Example repository method:

List<Movie> findByCinema_Province_Name(String provinceName);

Query logic:

Movie → Cinema → Province

This allows the system to list movies available in a specific province.

## Project Structure

- Movie
- Actor
- Director
- UserProfile
## How to Run the Project
1. Clone the repository
git clone https://github.com/your-username/movie-management-system.git
2. Open the project in

Visual Studio Code

3. Run the application
mvn spring-boot:run
4. Test the APIs using

Postman.

## Conclusion

The Movie Management System demonstrates how to build a Spring Boot application integrated with a relational database.

The project shows practical use of:

Entity relationships

Spring Data JPA repositories

Sorting and pagination

Query methods

## This implementation helps understand how modern web applications manage structured data efficiently.