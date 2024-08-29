In the project there is a folder docker that contains folder app with docker-compose.app.yml for connection application and database and folder db with separate folders for three data bases - dev (development), prod (production), test. All three of them contain similar files, in particular docker-compose.db-<dev, prod, test>.yml and schema-<dev, prod, test> sql.

I also added Dockerfile to build application in Docker.


To start database containers you need first in application.properties specify what db you would like to use for example spring.profiles.active=dev (prod, test)

Open Terminal in the directory of the project.
Write the following commands:

cd docker/db/dev 
docker-compose -f docker-compose.db-dev.yml up -d 

or

cd docker/db/prod 
docker-compose -f docker-compose.db-prod.yml up -d

or

cd docker/db/test 
docker-compose -f docker-compose.db-test.yml up -d


Then start the application in IntelliJ IDEA
After you will have database in docker container


____________________________________________________________________

To start application and database in docker (by default database is prod) 
Open Terminal in the directory of the project.
Then write the following commands:

cd docker/app
docker-compose -f docker-compose.app.yml up -d 

In that case you do not need to start the application in IntelliJ IDEA because your application has been already started in Docker.

If you would like to start application again in IntelliJ IDEA you need to stop in docker and vice versa.
