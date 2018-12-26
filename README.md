# Everyping-okay

Ping against a specified endpoint and post a message to slack if the endpoint is failing to respond

## Configuration

[Create a simple slack app](https://api.slack.com/incoming-webhooks) that will post to your slack channel

set the `HEARTBEAT_URL` environment variable to whatever URL you want to ping (e.g. `http://localhost:3000/heartbeat`)

set the `SLACK_URL` environment variable to the slack webhook URL in your slack app

## Running it
`gradle run`

## Running it on AWS Lambda

-   Build a jar `./gradlew shadowJar`
  
-   Upload the jar in the "Function Code" section on lambda, choose "Java 8" as the runtime, 
  and set the handler to "MainKt::handleLambda"
  
-   Set your environment variables
    
-   Set your memory usage (I'm testing with 256 MB)

Recommended: Set the project to run on a periodic basis
-   Add a "CloudWatch Event trigger"

-   Set the rule type to "schedule expression" and set the expression (e.g. `rate(1, minute)` 
  This runs the function once every minute)
