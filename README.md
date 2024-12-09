# Back-end Technical Challenge: Movies API

## Overview

The goal of the technical test is to create a **Java web application** that fulfills the following requirements:
- Determines whether a movie won a “Best Picture” Oscar based on its title.
- Allows users to give ratings to movies.
- Provides a list of the **top 10 rated movies**, ordered by their box office value.

The challenge uses:
- [OMDB Movies API](http://www.omdbapi.com/)
- A [CSV file of Oscar winners from 1927 to 2010](src/main/resources/academy_awards.csv).

## Solution Requirements

Your code and deliverables should be **production-ready**, adhering to the [Backbase Stack](https://stackshare.io/backbase/backbase)

The deliverables must include:
1. `solution.md`: A brief description of your solution and key design decisions.
2. `how_to_run.md`: Instructions on how to run the solution.
3. `how_to_test.md`: Steps to test the service.
4. `to_do.md`: A to-do list for pending features or improvements.
5. `assumptions.md`: Key assumptions you made while solving the challenge.
6. `scale.md`: Discussion on how the solution would scale from 100 users/day to 10,000,000 users/day.

Optional (but recommended):
- Diagrams to explain your architecture, such as:
    - **System context diagram**
    - **Entity diagram**
    - **Component diagram**
    - **Class diagram**
    - **Data-flow diagram**
    - **Sequence diagram**


## Time to Complete

There are no strict time limits, but it’s recommended to keep the effort within a couple of days.


## Suggestions

- **Keep it simple**: Avoid over-engineering the solution.
- Ensure the API expects an **API token** for authentication.
- The solution should use a **real database** for persistence.
