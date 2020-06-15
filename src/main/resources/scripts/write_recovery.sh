#!/system/bin/sh 
#
# This writes a recovery image back to the unit
# Writes either:
# a "raw" backup of the recovery partition
# a "normal" normal image taken from the firmware
# a TWRP recovery image

dd if=/storage/sdcard1/recovery.img of=/dev/block/mmcblk0p3
