./gradlew clean

./gradlew -no-daemon :android_framework:patch-magisk:bridge-dex-app:extractBridgeJar
./gradlew -no-daemon :android_framework:patch-magisk:module:zipRelease

./gradlew -no-daemon app:assemblePrcDebug
