#!/system/bin/sh 
#
# This writes "a" boot image back to the unit
# Writes either:
# a "raw" backup of the boot partition
# a "normal" boot image taken from the firmware
# a rooted boot image

dd if=/storage/sdcard1/boot.img of=/dev/block/mmcblk0p25
