#!/bin/sh
# Simplified Gradle wrapper launcher.
# NOTE: the GitHub Actions workflow regenerates the full official wrapper
# (gradlew + gradle-wrapper.jar) before building, so this file is only a
# fallback for local use when the wrapper jar is already present.
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi
exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
