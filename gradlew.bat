@echo off
rem Simplified Gradle wrapper launcher (Windows fallback).
rem NOTE: the GitHub Actions workflow regenerates the full official wrapper
rem before building, so this file is only a fallback for local use.
set APP_HOME=%~dp0
set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if not "%JAVA_HOME%"=="" set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if "%JAVA_EXE%"=="" set JAVA_EXE=java.exe
"%JAVA_EXE%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
