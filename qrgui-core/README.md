
## Task ##

The project is composed by 3 different main modules:

- Rest module with Security (as a Gateway to Services and Multitenant modules)
- Services
- Multi-tenant

Within the Multi-tenant module, we have a data source router. We can add as many tenants as we want in the /resources/tenants/ directory. One tenant correspond to a database connection.
At runtime, we can switch over different databases and the data source router will be able to pick the one corresponding to the tenant of the query (tenant -> X-TenantID header)

## Requirements :

* Git
* Java11
* Maven
* Docker

## Getting Started

* Build the project

```bash
 - mvn clean install
```

## Run application

