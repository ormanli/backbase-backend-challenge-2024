## Spring Boot Application with MySQL Database

This application utilizes Spring Boot as its framework and MySQL as the underlying database.
The database schema is defined in the `src/main/resources/init.sql` file.

## Swagger UI and JWT Authentication

Swagger UI is accessible at http://localhost:8080/swagger-ui/index.html for API documentation.
JWT (JSON Web Token) is used for authentication and authorization.

## Oscar Winners Data Storage

Upon application startup, the Oscar Winners CSV file is parsed, and Best Picture winners are stored in memory for quick querying.
This approach is suitable for the relatively small dataset (~80 records).
However, if queries need to be executed on the entire dataset; storing the data in a database table would be more efficient.

## Movie Queries

### Oscar Winner Check

To determine if a movie is an Oscar winner, an exact match is performed on the movie title.

### Movie Rating

Users can rate a movie by providing the IMDb ID and a rating between 1 and 10. Database-level constraints ensure that a user can only rate a movie once.

### Top Rated Movies

Average movie ratings are stored in the `movie_rating_average` table, which is updated with each new rating.
The top 10 rated movies are retrieved from the database by ordering the average ratings in descending order and selecting the first 10.
Box office data is then fetched from the OMDB API and sorted in memory.

## OMDB API Caching

To reduce response times for consecutive calls and conserve the daily free limit responses from the OMDB API are cached
in memory.
Since the data from OMDB is relatively static, caching helps avoid unnecessary API calls.