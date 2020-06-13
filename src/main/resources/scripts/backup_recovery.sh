#!/system/bin/sh 
#
# Make a "raw" backup of the recovery partition

dd if=/dev/block/mmcblk0p3 of=/storage/sdcard1/recovery.img
