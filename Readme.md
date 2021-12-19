# STUDENT MANAGEMENT AUTH SERVICE

I used keycloak as an identity provider.

For Keyclok image  you can use the current image in this [link](https://hub.docker.com/r/jboss/keycloak/****). I am currently using the latest version 15.0.2.

I store the user information in a relational database.
I used postgres for this.

I also use kafka to listen to login and register events.

There is some logic here. When the user logs in, I catch this login event with CustomEventListenerProvider. I'm sending the unique username or email of the logged in user, whichever one is using, to the kafka.

Core application listens for login event. Core checks its own mysql database and if there is no person in the db with this username, it retrieves this user's information with Keycloak REST API and saves it to its own database.

### HOW TO USE

- **For extract jar file**

`mvn clean install`

We need to deploy this jar file to keycloak server.

For this I use docker volumes. If you want to use another way to deploy jar file feel free to use.

Inside docker-compose file this line deploy jar file to keycloak.

```yaml
./target/custom-event-listener.jar://opt/jboss/keycloak/standalone/deployments/custom-event-listener.jar 
```

- **Create .env file and fill environment variable like below**

`KAFKA_OUTSIDE_URL=locahost
KAFKA_OUTSIDE_PORT=9092
KAFKA_INSIDE_URL=kafka
KAFKA_INSIDE_PORT=9093`

- **docker-compose up --build -d**

keycloak administration console --> [http://localhost:8084](http://localhoist:8084/)

<aside>
📌 username: admin

</aside>

<aside>
📌 password: secret

</aside>

Jar file deploy automatically When  keycloak tomcat server up and running.

- **Select Custom Event Listener**

Open administration console select custom even listener in this case(email) and click saved.

![console](https://imgur.com/a/i9I2dSy)