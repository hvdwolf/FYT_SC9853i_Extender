#!/system/bin/sh 
#

# necessary or not, we simply remount the partitions read-write
mount -o remount,rw /oem

# Disable window apk
mv /oem/app/190001003_com.syu.window/190001003_com.syu.window.apk /oem/app/190001003_com.syu.window/190001003_com.syu.window.apk.org
rm -f /data/dalvik-cache/x86_64/oem@app@190001003_com.syu.window*
rm -rf /data/data/com.syu.window
rm -rf /data/system/package_cache/1/com.syu.window*
printf "Done!!"
printf "Wait for the system to tell you to remove the USB-stick/SD-card"
