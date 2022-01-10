@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  BlipEventsDatamartIntegrationFunction startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and BLIP_EVENTS_DATAMART_INTEGRATION_FUNCTION_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\BlipEventsDatamartIntegrationFunction.jar;%APP_HOME%\lib\log4j-slf4j18-impl-2.13.0.jar;%APP_HOME%\lib\aws-lambda-java-log4j2-1.2.0.jar;%APP_HOME%\lib\log4j-core-2.13.2.jar;%APP_HOME%\lib\log4j-api-2.13.2.jar;%APP_HOME%\lib\aws-lambda-java-core-1.2.1.jar;%APP_HOME%\lib\aws-lambda-java-events-3.10.0.jar;%APP_HOME%\lib\s3-2.17.40.jar;%APP_HOME%\lib\apache-client-2.17.40.jar;%APP_HOME%\lib\jackson-dataformat-csv-2.12.5.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.12.5.jar;%APP_HOME%\lib\jackson-databind-2.12.5.jar;%APP_HOME%\lib\jackson-core-2.12.5.jar;%APP_HOME%\lib\jackson-annotations-2.12.5.jar;%APP_HOME%\lib\mssql-jdbc-9.4.0.jre11.jar;%APP_HOME%\lib\sqlite-jdbc-3.36.0.3.jar;%APP_HOME%\lib\joda-time-2.6.jar;%APP_HOME%\lib\aws-xml-protocol-2.17.40.jar;%APP_HOME%\lib\aws-query-protocol-2.17.40.jar;%APP_HOME%\lib\protocol-core-2.17.40.jar;%APP_HOME%\lib\aws-core-2.17.40.jar;%APP_HOME%\lib\auth-2.17.40.jar;%APP_HOME%\lib\regions-2.17.40.jar;%APP_HOME%\lib\sdk-core-2.17.40.jar;%APP_HOME%\lib\netty-nio-client-2.17.40.jar;%APP_HOME%\lib\http-client-spi-2.17.40.jar;%APP_HOME%\lib\metrics-spi-2.17.40.jar;%APP_HOME%\lib\arns-2.17.40.jar;%APP_HOME%\lib\profiles-2.17.40.jar;%APP_HOME%\lib\json-utils-2.17.40.jar;%APP_HOME%\lib\utils-2.17.40.jar;%APP_HOME%\lib\annotations-2.17.40.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\slf4j-api-1.8.0-alpha2.jar;%APP_HOME%\lib\netty-reactive-streams-http-2.0.5.jar;%APP_HOME%\lib\netty-reactive-streams-2.0.5.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.11.jar;%APP_HOME%\lib\eventstream-1.0.1.jar;%APP_HOME%\lib\netty-codec-http2-4.1.68.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.68.Final.jar;%APP_HOME%\lib\netty-handler-4.1.68.Final.jar;%APP_HOME%\lib\netty-codec-4.1.68.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.68.Final-linux-x86_64.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.68.Final.jar;%APP_HOME%\lib\netty-transport-4.1.68.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.68.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.68.Final.jar;%APP_HOME%\lib\netty-common-4.1.68.Final.jar;%APP_HOME%\lib\third-party-jackson-core-2.17.40.jar

@rem Execute BlipEventsDatamartIntegrationFunction
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %BLIP_EVENTS_DATAMART_INTEGRATION_FUNCTION_OPTS%  -classpath "%CLASSPATH%" br.com.vwco.chatbot.blip.datamart.integration.App %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable BLIP_EVENTS_DATAMART_INTEGRATION_FUNCTION_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%BLIP_EVENTS_DATAMART_INTEGRATION_FUNCTION_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
