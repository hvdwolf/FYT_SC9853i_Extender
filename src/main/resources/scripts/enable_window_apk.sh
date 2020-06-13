#!/system/bin/sh 
#

# necessary or not, we simply remount the partitions read-write
mount -o remount,rw /oem

# Ensable window apk
mv /oem/app/190001003_com.syu.window/190001003_com.syu.window.apk.org /oem/app/190001003_com.syu.window/190001003_com.syu.window.apk
chmod 0644 /oem/app/190001003_com.syu.window/190001003_com.syu.window.apk
printf "Done!!"
printf "Wait for the system to tell you to remove the USB-stick/SD-card"
