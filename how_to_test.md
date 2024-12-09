### Registering with Username and Password

Before making API calls, you need to register with a username and password.

Here's how to register using curl:

```
curl -X 'POST' \
  'http://localhost:8080/auth/singup' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "asd@asd.com",
  "password": "1234"
}'
```

This command will send a POST request to the `/auth/signup` endpoint with your email and password in the request body. A
successful response will return a JSON object containing a token.
After signup, `/auth/signin` endpoint can be used to generate tokens.

**The returned token**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2RAYXNkLmNvbSIsImlhdCI6MTcyOTEwNzIzNywiZXhwIjoxNzI5MTkzNjM3fQ.2Ki50r4PSVu9sJ41q_ABy0P8A8A-XOb9L7j92miNhc4"
}
```

### Making API Calls

After registration, you can use the returned token to authorize yourself for other API calls. Here's an example of
fetching the top 10 box office movies using curl:

```
curl -X 'GET' \
  'http://localhost:8080/movies/top10/boxOffice' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2RAYXNkLmNvbSIsImlhdCI6MTcyOTEwNzIzNywiZXhwIjoxNzI5MTkzNjM3fQ.2Ki50r4PSVu9sJ41q_ABy0P8A8A-XOb9L7j92miNhc4'
```

### Using Swagger UI

Alternatively, you can use the provided Swagger UI for a more user-friendly experience:

1. Access the Swagger UI at http://localhost:8080/swagger-ui/index.html.
2. Use the signup endpoint in the UI to register.
3. Click the green Authorize button in the upper right corner.
4. Paste your obtained token in the provided field.

With the token injected, you can freely explore and interact with the available API endpoints through the Swagger UI.