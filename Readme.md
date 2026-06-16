# Student Management Auth Service

This service uses **Keycloak** as the identity provider.

For the Keycloak Docker image, you can use the current image from the provided link. I am currently using **Keycloak 15.0.2**.

User information is stored in a relational database. In this project, I use **PostgreSQL** for Keycloak.

I also use **Kafka** to listen for login and registration events.

## How It Works

When a user logs in, the login event is captured by a custom `CustomEventListenerProvider`.

The listener sends the logged-in user's unique identifier to Kafka. This identifier can be either the username or the email address, depending on which one is used for login.

The core application listens for login events from Kafka.

When a login event is received, the core application checks its own MySQL database. If there is no person found with the given username or email, it retrieves the user's information from the Keycloak REST API and saves it to its own database.

## How to Use

### 1. Build the JAR file

Run the following command to build the project:

```bash
mvn clean install
```

This will generate the JAR file under the `target` directory.

### 2. Deploy the JAR file to Keycloak

The generated JAR file must be deployed to the Keycloak server.

In this project, I use Docker volumes to deploy the JAR file. You can use another deployment method if you prefer.

The following line in the `docker-compose.yml` file mounts the custom event listener JAR into the Keycloak deployments directory:

```yaml
./target/custom-event-listener.jar:/opt/jboss/keycloak/standalone/deployments/custom-event-listener.jar
```

Once the Keycloak server starts, the JAR file is deployed automatically.

### 3. Create the `.env` file

Create a `.env` file and fill in the required environment variables:

```env
KAFKA_OUTSIDE_URL=localhost
KAFKA_OUTSIDE_PORT=9092
KAFKA_INSIDE_URL=kafka
KAFKA_INSIDE_PORT=9093
```

### 4. Start the services

Run the following command:

```bash
docker-compose up --build -d
```

### 5. Open the Keycloak Administration Console

Keycloak Administration Console:

```text
http://localhost:8084
```

Default credentials:

```text
Username: admin
Password: secret
```

### 6. Enable the Custom Event Listener

After Keycloak is up and running:

1. Open the Keycloak Administration Console.
2. Select your realm.
3. Go to **Events**.
4. Open the **Config** tab.
5. In the **Event Listeners** section, select `custom-event-listener`.
6. Click **Save**.

The custom event listener is now enabled and will start publishing login and registration events to Kafka.
