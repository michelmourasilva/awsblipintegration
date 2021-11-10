This is a single AWS Lambda project that is responsible for fetching Blip *event-track* data and
placing them in some SQL table, which we named `event`.

Blip API provides an API command to fetch the event details, which expects `category` and
`action` as parameters. Therefore we first fetch the categories, then the actions for each
category, then the events for each category-action pairs.

As of now, on each execution, the destination table is cleared up before inserting the events.

### Requirements
Some JDK (11+).

### Local execution

```bash

# Clone/unpack
$ git clone ...

# Create the `event` table on the preferred DB (tested with SqlLite and MS Sql Server) by using `sql/20211108_075900_create_event_table.sql`

# Copy `BlipEventsDatamartIntegrationFunction/config.properties.example` to `BlipEventsDatamartIntegrationFunction/config.properties` 

# Adjust the database URL in `BlipEventsDatamartIntegrationFunction/config.properties`

# Replace `<...>` with the Blip API key in `BlipEventsDatamartIntegrationFunction/config.properties`

# cd into the lambda source and build it
$ ./gradlew assemble

# Run
$ ./gradlew run --args "<config-file> <start-date> <end-date>"

# Lots of logs are being spilled out and can/should be removed/improved
```

### AWS Lambda

- AWS Tooling must have been installed locally and configured. Those are:
    - AWS CLI
    - SAM CLI

```bash

# Clone/unpack
$ git clone ...

# Create the `event` table on target Datamart DB by using `sql/20211108_075900_create_event_table.sql`

# Copy `BlipEventsDatamartIntegrationFunction/config.properties.example` to `BlipEventsDatamartIntegrationFunction/config.properties` 

# Adjust the database URL in `BlipEventsDatamartIntegrationFunction/config.properties`

# Replace `<...>` with the Blip API key in `BlipEventsDatamartIntegrationFunction/config.properties`

# In the root of the project, build it using `sam`. Replace <> with the correct values
$ sam build --parameter-overrides "ParameterKey=BlipKey,ParameterValue=<blipKey> ParameterKey=BlipUri,ParameterValue=<blipUri> ParameterKey=DbConnectionUrl,ParameterValue=<dbConnectionUrl>

# Deploy the first time
$ sam deploy --guided --parameter-overrides "ParameterKey=BlipKey,ParameterValue=<blipKey> ParameterKey=BlipUri,ParameterValue=<blipUri> ParameterKey=DbConnectionUrl,ParameterValue=<dbConnectionUrl>

# Afterwards `--guided` is omitted

```
