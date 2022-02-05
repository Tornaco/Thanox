SKIPUNZIP=1

# Extract verify.sh
ui_print "- Extracting verify.sh"
unzip -o "$ZIPFILE" 'verify.sh' -d "$TMPDIR" >&2
if [ ! -f "$TMPDIR/verify.sh" ]; then
  ui_print    "*********************************************************"
  ui_print    "! Unable to extract verify.sh!"
  ui_print    "! This zip may be corrupted, please try downloading again"
  abort "*********************************************************"
fi
. $TMPDIR/verify.sh

# Check architecture
if [ "$ARCH" != "arm" ] && [ "$ARCH" != "arm64" ] && [ "$ARCH" != "x86" ] && [ "$ARCH" != "x64" ]; then
  abort "! Unsupported platform: $ARCH"
else
  ui_print "- Device platform: $ARCH"
fi

# Extract libs
ui_print "- Extracting module files"

extract "$ZIPFILE" 'module.prop' "$MODPATH"
extract "$ZIPFILE" 'post-fs-data.sh' "$MODPATH"
extract "$ZIPFILE" 'uninstall.sh' "$MODPATH"

# Riru v24+ load files from the "riru" folder in the Magisk module folder
# This "riru" folder is also used to determine if a Magisk module is a Riru module

mkdir "$MODPATH/zygisk"

ui_print "- Mod path $MODPATH"
ui_print "- Extracting framework jars"
extract "$ZIPFILE" "system/framework/thanox-bridge.jar" $MODPATH

if [ "$ARCH" = "arm" ] || [ "$ARCH" = "arm64" ]; then
  ui_print "- Extracting arm libraries"
  extract "$ZIPFILE" "libzygisk/armeabi-v7a.so" "$MODPATH/zygisk" true

  if [ "$IS64BIT" = true ]; then
    ui_print "- Extracting arm64 libraries"
    extract "$ZIPFILE" "libzygisk/arm64-v8a.so" "$MODPATH/zygisk" true
  fi
fi

if [ "$ARCH" = "x86" ] || [ "$ARCH" = "x64" ]; then
  ui_print "- Extracting x86 libraries"
  extract "$ZIPFILE" "libzygisk/x86.so" "$MODPATH/zygisk" true

  if [ "$IS64BIT" = true ]; then
    ui_print "- Extracting x64 libraries"
    extract "$ZIPFILE" "libzygisk/x86_64.so" "$MODPATH/zygisk" true
  fi
fi

set_perm_recursive "$MODPATH" 0 0 0755 0644
