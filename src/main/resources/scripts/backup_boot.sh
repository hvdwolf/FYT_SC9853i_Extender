#!/system/bin/sh 
#
# Make a "raw" backup of the boot partition

dd if=/dev/block/mmcblk0p25 of=/storage/sdcard1/boot.img
