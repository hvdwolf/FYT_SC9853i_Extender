#!/system/bin/sh 
#
# Write "raw" backup of the boot partition vack to the unit

dd if=/storage/sdcard1/boot.img of=/dev/block/mmcblk0p25