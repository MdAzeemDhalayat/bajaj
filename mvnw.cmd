@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM Apache Maven Wrapper startup batch script, compatible with
@REM the official Maven Wrapper v3.2.0 that auto-downloads wrapper JAR.
@REM
@REM ----------------------------------------------------------------------------

@IF "%DEBUG%"=="" ECHO OFF
@SETLOCAL

SET ERROR_CODE=0

SET MAVEN_PROJECTBASEDIR=%~dp0
IF NOT DEFINED MAVEN_PROJECTBASEDIR SET MAVEN_PROJECTBASEDIR=%CD%
SET WRAPPER_DIR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper
SET WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar
SET WRAPPER_PROPERTIES=%WRAPPER_DIR%\maven-wrapper.properties

SET DOWNLOAD_URL=
FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%WRAPPER_PROPERTIES%") DO (
  IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)
IF "%DOWNLOAD_URL%"=="" SET DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO - Downloading Maven Wrapper JAR from %DOWNLOAD_URL%
  powershell -Command "try { $ProgressPreference='SilentlyContinue'; (New-Object System.Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%') } catch { Write-Error $_; exit 1 }"
  IF ERRORLEVEL 1 (
    ECHO Failed to download Maven Wrapper JAR.
    EXIT /B 1
  )
)

REM Resolve Java
SET JAVA_EXE=java.exe
IF DEFINED JAVA_HOME (
  IF EXIST "%JAVA_HOME%\bin\java.exe" (
    SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
  ) ELSE (
    ECHO.
    ECHO Warning: JAVA_HOME is set but "%JAVA_HOME%\bin\java.exe" was not found. Falling back to java on PATH.
    ECHO.
    SET JAVA_EXE=java.exe
  )
)

SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

"%JAVA_EXE%" -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %*
SET ERROR_CODE=%ERRORLEVEL%
IF %ERROR_CODE% NEQ 0 GOTO error
GOTO end

:error
ECHO.
ECHO Maven wrapper execution failed with exit code %ERROR_CODE%.
ECHO.

:end
@ENDLOCAL & EXIT /B %ERROR_CODE%


