#!/system/bin/sh
# Please don't hardcode /magisk/modname/... ; instead, please use $MODDIR/...
# This will make your scripts compatible even if Magisk change its mount point in the future
MODDIR=${0%/*}


magiskpolicy --live "allow permissioncontroller_app appops_service:service_manager { find }"
echo "HAHAHA"