#!/system/bin/sh
#

mount -o remount,rw /oem
cp /storage/sdcard1/lsec_updatesh/blink_ring.mp3 /oem/blink_ring.mp3
chmod 0644 /oem/blink_ring.mp3

printf "Done!!"
printf "Wait for the system to tell you to remove the USB-stick/SD-card"

