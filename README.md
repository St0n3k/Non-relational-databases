# Non-relational databases - Guesthouse

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

### :construction_worker: Contributors
|Name |Github|
|-|-|
|Rafał|[rstrzalkowski](https://github.com/rstrzalkowski)|
|Kamil|[St0n3k](https://github.com/St0n3k)|

<br/>

Technologies used:
- Java 17
- Maven 3.8.3

In this project, we learnt about various databases, including:
- PostgreSQL ORM (as a introduction and comparison to NoSQL databases)
- MongoDB (replica-set)
- Redis (as a cache)
- Apache Cassandra
- Apache Kafka (as a message broker)

Guesthouse is an application, where `clients` can `rent` `rooms` for given period of time. Model includes abstract class `ClientType`, which had to be properly mapped into records in used databases. We had to make sure, that room can't be rented by two clients in the same time (concurrent safe), and that data stays consistent in case of distributed database.

Every project with different database contains tests, which verify that application functions properly and the data is consistent.
