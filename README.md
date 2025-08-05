The Backend Server (GitHub Webhooks)
==========================================

The purpose of this project is to serve as backend server for apps included in,
but are not limited to, [AndroidPlayroom](https://github.com/shyamkp-11/AndroidPlayroom). 

Project is developed using Spring framework with java and maven. It serves as REST api server for the android apps.
Features are implemented using spring-web, spring-security, spring-data-jpa and spring-data-rest.  
It uses JWT based token authentication to restrict access to secured API endpoint. Uses MySQL database as its data source. 

> Endpoint `/auth/authenticate` will generate access_token and refresh_token. Currently, project doesn't support refresh_tokens and is a TODO.  

# Build and run
1. Clone this [Repository](https://github.com/shyamkp-11/The-Backend-Server-Github-Webhooks) into IntelliJ or IDE of your choice.
2. Create a file **webapp-secrets.properties** in the root project folder. Add following to the file:
   > ADMIN_PASSWORD=(your admin password)
   ADMIN_USERNAME=(your admin username)
   SECRET_KEY=(a secret key for signing JWT tokens)
   WEBAPP_DATASOURCE_URL=(database url)
   WEBAPP_DATASOURCE_USERNAME=(database username)
   WEBAPP_DATASOURCE_PASSWORD=(database password)
   GITHUB_SERVER_BASE_URL=https://api.github.com
   GITHUB_SERVER_AUTH_TOKEN=(a GitHub api auth token. Not currently used so can be any value)
   GITHUB_SERVER_HMAC_KEY=(hmac key for the GithubApp that will be sending webhooks requests. Can be downloaded from https://github.com/settings/apps/ webhook settings)
3. Download [**github-playroom-firebase-adminsdk.json**](https://firebase.google.com/docs/admin/setup#initialize_the_sdk_in_non-google_environments) from  [Firebase](https://console.firebase.google.com).  
   Save it into [src/main/resources](src/main/resources) directory of the project.
4. Run the project from your IDE or build project use `mvn clean install` and run with `java -jar target/<name of project jar file>`

## Some basic endpoints
1. POST `/auth/authenticate`, will generate a JWT access token to access secured endpoints  
   ```curl
   curl --location 'http://localhost:8090/api/v1/auth/authenticate' \
   --header 'Content-Type: application/json' \
   --data-raw ' {
      "email": "(your admin username)",
      "password": "(your admin password)"
   }'
   ```
2. POST `/githubUsers/githubWebhookDelivery` to be called be GithubApp to deliver webhook data.
3. POST `/auth/registerGithubApiClient` will generate non expiring JWT access token for accessing `/githubUsers/*` endpoints, to be used with mobile/client apps.
   ```curl
   curl --location 'http://localhost:8090/api/v1/auth/registerGithubApiClient' \
   --header 'Authorization: Bearer <admin_token>' \
   --data-raw ' {
        "firstname": "<first_name>",
        "lastname": "<last_name>",
        "role":"USER",
        "password": "<password>",
        "email": "<email>"
    }'
   ```
4. POST called when a user signs in to the client app 
   ```curl
   curl --location 'http://localhost:8090/api/v1/githubUsers/signedInToApp' \
   --header 'Authorization: Bearer <client_api_token>' \
   --header 'Content-Type: application/json' \
   --data ' {
       "fcmToken":"<client app firebase messaging token>",
       "deviceId":"<client app deivce id>",
       "globalId":"<github user global id>",
       "username":"<github username>",
       "firstName":"<user first name>",
       "lastName":"<user last name>"
   }'
   ```
5. POST called when user enables / disables push notifications for repository stars.
   ```curl
   {
    "fcmToken": "<client app firebase messaging token>",
    "deviceId":"<client app deivce id>",
    "globalId":"<github user global id>",
    "fcmEnabled": <boolean: notifications enabled on client app>
   }
   ```