rm -rf out/
./gradlew clean
./gradlew -no-daemon :android_framework:patch-magisk:bridge:build
./gradlew -no-daemon :android_framework:patch-magisk:module:zipRelease
