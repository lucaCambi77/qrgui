
## Task ##

Sometimes companies use scripts to extract data from tables to monitor or have info about some flow.
This application provides a simple user interface to store query with parameters we frequently use.
We can also group queries in so-called routines.

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

The project is composed by 3 different main modules:

 - Rest module with Security (as a Gateway to Services and Multitenant modules)
 - Services 
 - Multi-tenant

Within the Multi-tenant module, we have a data source router. We can add as many tenants as we want in the /resources/tenants/ directory. One tenant correspond to a database connection.
At runtime, we can switch over different databases and the data source router will be able to pick the one corresponding to the tenant of the query (tenant -> X-TenantID header)
