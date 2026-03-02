@REM Maven Wrapper startup script for Windows
@echo off
@setlocal

set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

set MAVEN_CMD_LINE_ARGS=%*

@REM Find the project base dir
set MAVEN_PROJECTBASEDIR=%~dp0
IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

:endDetectBaseDir

IF NOT EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" goto downloadWrapper

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

%JAVA_HOME%\bin\java.exe %MAVEN_OPTS% -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %MAVEN_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:downloadWrapper
echo Downloading Maven Wrapper...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar'}"
goto runMaven

:runMaven
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
%JAVA_HOME%\bin\java.exe %MAVEN_OPTS% -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %MAVEN_CMD_LINE_ARGS%

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%
exit /B %ERROR_CODE%
