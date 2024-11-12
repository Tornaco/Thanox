./gradlew clean

 ./gradlew :android_framework:patch-magisk:bridge-dex-app:extractBridgeJar
./gradlew :android_framework:patch-magisk:module:zipRelease

 ./gradlew app:assembleRowRelease
