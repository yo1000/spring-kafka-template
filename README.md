Spring Kafka Template
================================================================================

This is a template for a Kafka application using Spring Boot.
It includes a simple producer and consumer example.


Requirements
--------------------------------------------------------------------------------

- Java 21
- Docker


Quickstart
--------------------------------------------------------------------------------

```bash
./mvnw clean package

docker compose down && \
docker compose up --build
```

### View database

See: http://localhost:8032

Navigate to [Query], and query the following SQL:

```sql
SELECT
    age_group,
    sum(quantity)               AS qty,
    sum(amount)                 AS amt,
    sum(amount) / sum(quantity) AS avg_amt
FROM
    sales_statistics
GROUP BY
    age_group
ORDER BY
    age_group
``` 

### View topic

See: http://localhost:8092/ui/clusters/local/all-topics/sales-tran/messages


(Optional) AWS MSK IAM Auth support
--------------------------------------------------------------------------------

```bash
./mvnw clean package -Paws-msk-iam-auth
```

### Config example

See: 
[compose.iam-auth.yml:L11-14](https://github.com/yo1000/spring-kafka-template/blob/master/compose.iam-auth.yml#L11-L14),
[compose.iam-auth.yml:L30-33](https://github.com/yo1000/spring-kafka-template/blob/master/compose.iam-auth.yml#L30-L33)
