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

### View topic

See: http://localhost:8092/ui/clusters/local/all-topics/sales-tran/messages


### View database (Simply)

See: http://localhost:3000

**How to show data**

1. Expand [postgres] > [sales_statistics] in Left upper pane
2. Expand [Tables] in Left lower pane
3. Right click to [sales_statistics] > [Open data] in [Tables] tree

**How to query data**

1. Click to [+] at Right upper in display
2. Copy & Paste query as follows to editor, and Press `Ctrl` + `Enter` keys

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

**How to show diagram**

1. Expand [postgres] > [sales_statistics] in Left upper pane
2. Expand [Tables] in Left lower pane
3. Right click to [sales_statistics] > [Show diagram] in [Tables] tree


### View database (More detailed)

See: http://localhost:5050

Login as Email=`admin@example.com`, Password=`admin`.
Expand [Servers] in Left pane.
Input db password as `postgres`, and Click [OK] button.

**How to show data**

1. Expand [Servers] > [Postgres] > [Databases] > [sales_statistics] > [Schemas] > [public] > [Tables] in Left pane
2. Right click to [sales_statistics] > [View/Edit Data] > [All Rows] in [Tables] tree

**How to query data**

1. Expand [Servers] > [Postgres] > [Databases] > [sales_statistics] > [Schemas] > [public] > [Tables] in Left pane
2. Right click to [sales_statistics] > [Query Tool] in [Tables] tree
3. Copy & Paste query as follows to editor, and Press `F5` key

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

**How to show diagram**

1. Expand [Servers] > [Postgres] > [Databases] > [sales_statistics] > [Schemas] > [public] > [Tables] in Left pane
2. Right click to [sales_statistics] > [ERD For Table] in [Tables] tree


(Optional) AWS MSK IAM Auth support
--------------------------------------------------------------------------------

```bash
./mvnw clean package -Paws-msk-iam-auth
```

### Config examples

See: 
[compose.iam-auth.yml:L11-14](https://github.com/yo1000/spring-kafka-template/blob/master/compose.iam-auth.yml#L11-L14),
[compose.iam-auth.yml:L30-33](https://github.com/yo1000/spring-kafka-template/blob/master/compose.iam-auth.yml#L30-L33)
