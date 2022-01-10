This is a single AWS Lambda project that is responsible for fetching Blip *event-track* data and
placing them in some SQL table, which we named `event`.

Blip API provides an API command to fetch the event details, which expects `category` and
`action` as parameters. Therefore we first fetch the categories, then the actions for each
category, then the events for each category-action pairs.

As of now, on each execution, the destination table is cleared up before inserting the events.

![alt text](/images/solution.jpg "")

# Requirements 

## JDK 11


Check if there is already a Java installation in the environment
> java --version

For compatibility, JDK version 11 must be installed.

If there is already a Java installation in the environment above or below version 11, first the installation of version 11 of the JDK must be carried out, following the steps described in the [documentation](https://www.oracle.com/java/technologies/downloads/#javasejdk).


Depending on the operating system, it may happen that there are several versions installed, for this it will be necessary to carry out the change so that the operating system uses JDK version 11. 

### Example using Linux

List installed java versions: 
> update-java-alternatives --list

If there is more than one java version, set to version 11
> sudo update-java-alternatives --set /usr/lib/jvm/java-1.11.0-openjdk-amd64


## AWS CLI

Follow steps described in the [documentation](https://docs.aws.amazon.com/pt_br/cli/latest/userguide/install-cliv2.html).

Example of installation in [Linux environment](https://docs.aws.amazon.com/pt_br/cli/latest/userguide/install-cliv2-linux.html):
> curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
> 
> unzip awscliv2.zip
> 
> sudo ./aws/install

## SAM CLI

Follow steps described in the [documentation](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html).


## Visual Code (for developing and debug)


![alt text](/images/workspace.png "Workspace")

Visual Studio Code is a source-code editor made by Microsoft for Windows, Linux and macOS.[9] Features include support for debugging, syntax highlighting, intelligent code completion, snippets, code refactoring, and embedded Git. Users can change the theme, keyboard shortcuts, preferences, and install extensions that add additional functionality.

Visual code was used for the development and debugging of the Lambda Function, however it is not mandatory to use it to perform the build and deploy as this can be done by the SAM CLI.

Follow installation steps described in the [documentation](https://code.visualstudio.com/download).

Install an AWS Toolkit plugin (amazon web services.aws-toolkit-vscode) in order to run, debug, build and deploy the Lambda function

## AWS Account

To perform the deployment of Lambda Function, it will be necessary to inform the login user with the necessary permissions.

This configuration can be done through the command:
> aws configure

It will be necessary to inform the access key ID and secret acess key.

### Example of how to create access data directly from the AWS console

- Access resource Identity and Acess Management (IAM)
- Click on the Add users button

![alt text](/images/Step1.png "Click on the Add users button")

1- Inform the username;

2- Check the option "Access key - Programmatic access";

3- Click on the "Next: Permissions" button.

![alt text](/images/Step2.png "Inform the username")

1- Add a user to a group

2- Click on the "Next: Tags" button.

![alt text](/images/Step3.png "Add a user to a group")

- Create a user

![alt text](/images/Step4.png "Create a user")

- Retrieve "Access Key ID" and "Secret access key" to be used in the "aws configure" command

![alt text](/images/Step5.png "Create a user")

- Run the command "aws configure" in the terminal, and inform the requested data.

![alt text](/images/Step6.png "aws configure")


## Blip

The main goal of Blip Docs is to provide technical development knowledge on the Blip platform and present various code samples. These are the minimum necessary concepts for who wants to explore all power of Blip.

For any questions about the tool, consult the [documentation](https://docs.blip.ai/).


The Lambda function needs to integrate with the Blip service. This access is done through the tool's APIs. Therefore, some data will be needed to perform this access. These data can be found in the chatbot configuration.


Example of chatbot [settings](https://docs.blip.ai/#using-http)

![alt text](/images/http-token.png "")


## Database in AWS RDS

To perform the injection tests it will be necessary to create a database instance. For all tests an instance of Microsoft SQL Server was created.

1- Retrieve endpoint to be used in environment variables

2- For the tests, it is necessary to leave public access to the database

3- Also for local tests, it will be necessary to assign specific Inbound rules for the local environment.
  
![alt text](/images/Step7.png "")


4 - Connect to the database to execute the script [20211108_075900_create_event_table.sql](Database/sql/20211108_075900_create_event_table.sql). This script will create the table in a specific database. For the tests a fictitious database called "optest" was created.

![alt text](/images/Step8.png "")

>  CREATE TABLE event ( \
>    storageDate       DATETIME2, \
>    category          NVARCHAR(100), \
>    action            NVARCHAR(100), \
>    stateName         NVARCHAR(100), \
>    stateId           NVARCHAR(100), \
>    messageId         UNIQUEIDENTIFIER, \
>    previousStateId   NVARCHAR(100), \
>    previousStateName NVARCHAR(100) \
);


# Enviroments

Using Visual Code we will be able to debug the code using some parameters that can be changed in the template_dev.yaml file.

For the deployment in the production environment, the template.yaml file will be used. The environment variables will be set at runtime for the deployment of the Lambda function

These environment variables are:

| Variable name | Description | Origin | Example
|---|---|---|---|
| BLIP_KEY   | Authorization token of your bot  | Configurations > Conection information > HTTP Enpoints > Header authentication (Authorization)  |Key dndjb3YyOjR0SFZKdlRjR3FqbE1hbDd6RlVR|
| BLIP_URI   | Url to send commands | Configurations > Conection information > HTTP Enpoints  > Url to send commands |https://http.msging.net/commands|
| DB_CONNECTION_URL   | Database instance connection string| Amazon RDS > Database painel > Connectivity & security > Endpoint & port  |jdbc:sqlserver://database.cy4ibmqxejb7.us-east-1.rds.amazonaws.com:1433;databaseName=optest;user=admin;password=pass123|



# Build and Deploy

## Through visual Code

- Open the Command Palette (Ctrl+Shift+P)
- select AWS: Deploy SAM application
- Select template file (template.yaml)
- If the .aws/templates.json file has not been created, the tool itself will ask you to set the environment variables.Select Configure in list (The template contains parameters without default values. In order to deploy, you must provide values for these parameters). The file will be created and the environment variables can be changed according to the information described in the [Environment](#enviroments) sector
- Select AWS Region to deploy. 
- Select AWS S3 Bucket to deploy code.
- Define a name to use for the deployed stack


## Through the terminal

### Build

- Run the command in the application's root folder:
> sam build --parameter-overrides "ParameterKey=BlipKey,ParameterValue=<blipKey> ParameterKey=BlipUri,ParameterValue=<blipUri> ParameterKey=DbConnectionUrl,ParameterValue=<dbConnectionUrl>

- Example:
> sam build --parameter-overrides "ParameterKey=BlipKey,ParameterValue=Key dndjb3YyOjR0SFZKdlRjR3FqbE1hbDd6RlVR ParameterKey=BlipUri,ParameterValue=https://http.msging.net/commands ParameterKey=DbConnectionUrl,ParameterValue=jdbc:sqlserver://database.cy4ibmqxejb7.us-east-1.rds.amazonaws.com:1433;databaseName=optest;user=admin;password=pass123"

![alt text](/images/Terminal_build.png "")

- Run the command in the application's root folder and enter the requested data

> sam deploy --guided --parameter-overrides "ParameterKey=BlipKey,ParameterValue=<blipKey> ParameterKey=BlipUri,ParameterValue=<blipUri> ParameterKey=DbConnectionUrl,ParameterValue=<dbConnectionUrl>

- Exemple:
> sam deploy --guided --parameter-overrides "ParameterKey=BlipKey,ParameterValue=Key dndjb3YyOjR0SFZKdlRjR3FqbE1hbDd6RlVR ParameterKey=BlipUri,ParameterValue=https://http.msging.net/commands ParameterKey=DbConnectionUrl,ParameterValue=jdbc:sqlserver://database.cy4ibmqxejb7.us-east-1.rds.amazonaws.com:1433;databaseName=optest;user=admin;password=pass123"

![alt text](/images/Terminal_deploy.png "")


# Tests

## Local

Use the IDE itself (Visual Code). In case it is necessary to update the environment variables set in the template_dev.yaml file.

As the function is executed through a Post method, the request body example is informed in the .vscode/launch.json file. 

![alt text](/images/launch.png "")

- In the debug console will be presented several logs and a return with status 200.

![alt text](/images/LogResult.png "")

- And the target table will be loaded with chatbot events

![alt text](/images/DatabaseResult.png "")


## After deploy to AWS

### Postman

It is recommended to use Postman to perform the tests in a production/AWS environment. Postman is an API platform for building and using APIs. Postman simplifies each step of the API lifecycle and streamlines collaboration so you can create better APIs—faster. To install Postman follow the steps described in the [documentation](https://www.postman.com/downloads/).

- Access the application

![alt text](/images/AfterDeploy_1.png "Log")

- Copy the API endpoint

![alt text](/images/AfterDeploy_2.png "Log")


1- Select POST method

2- Paste endpoint address including /Prod/execute in URL

3- Under authorization, select AWS Signature

4- Inform the access key and the secret key. This information is the user's keys with a specific permission that enables the API to run. As a reference to retrieve this information, just use the form already described in this [manual](#example-of-how-to-create-access-data-directly-from-the-aws-console).


![alt text](/images/Postman1.png "")

- Submit the start and end dates that will be stored.

![alt text](/images/Postman2.png "")

-  Expected return. Situation 200 will be returned and the database will be loaded with period data as shown above.

![alt text](/images/Postman_Return.png "")

### Curl

However, we can use the Curl tool for testing. You will need to change the credentials. With that, just run the command:

> curl --location --request POST 'https://3ogzepr0yc.execute-api.us-east-1.amazonaws.com/Prod/execute' \
--header 'X-Amz-Content-Sha256: beaead3198f7da1e70d03ab969765e0821b24fc913697e929e726aeaebf0eba3' \
--header 'X-Amz-Date: 20220108T145323Z' \
--header 'Authorization: AWS4-HMAC-SHA256 Credential=AKIAX5IRDW6PA7ISJIIU/20220108/us-east-1/execute-api/aws4_request, SignedHeaders=host;x-amz-content-sha256;x-amz-date, Signature=6229e11c0b6151ed337d777363fb057eb4dd1992c6ec312b41ad5a5cfd6f5e63' \
--header 'Content-Type: application/json' \
--data-raw '{"startDate": "2021-01-01",  "endDate": "2021-12-30"}'
