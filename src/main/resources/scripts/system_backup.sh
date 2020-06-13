#!/system/bin/sh
#
# Make a "raw" backup of the necessary partitions

rm -rf /storage/sdcard1/backup
mkdir -p /storage/sdcard1/backup

printf "backup partition /boot to boot.raw.img  ...."
dd if=/dev/block/mmcblk0p25 of=/storage/sdcard1/backup/boot.raw.img
printf "backup partition /recovery to recovery.raw.img  ...."
dd if=/dev/block/mmcblk0p3 of=/storage/sdcard1/backup/recovery.raw.img
printf "Now copy the recovery data file from /system/etc/ ...."
cp /system/etc/recovery-resource.dat /storage/sdcard1/backup/
printf "backup partition /uboot to u-boot.raw.img  ...."
dd if=/dev/block/mmcblk0p10 of=/storage/sdcard1/backup/u-boot.raw.img
printf "backup partition /secvm to secvm.raw.img  ...."
dd if=/dev/block/mmcblk0p7 of=/storage/sdcard1/backup/secvm.raw.img
printf "backup partition /mobilevisor to mobilevisor.raw.img  ...."
dd if=/dev/block/mmcblk0p9 of=/storage/sdcard1/backup/mobilevisor.raw.img
printf "backup partition /mvconfig to mvconfig.raw.img  ...."
dd if=/dev/block/mmcblk0p8 of=/storage/sdcard1/backup/mvconfig.raw.img
printf "backup partition /l_ldsp to l_ldsp.raw.img  ...."
dd if=/dev/block/mmcblk0p23 of=/storage/sdcard1/backup/l_ldsp.raw.img
printf "backup partition /l_tgdsp to l_tgdsp.raw.img  ...."
dd if=/dev/block/mmcblk0p22 of=/storage/sdcard1/backup/l_tgdsp.raw.img
printf "backup partition /l_modem to l_modem.raw.img  ...."
dd if=/dev/block/mmcblk0p20 of=/storage/sdcard1/backup/l_modem.raw.img
printf "backup partition /pm_sys to pm_sys.raw.img  ...."
dd if=/dev/block/mmcblk0p24 of=/storage/sdcard1/backup/pm_sys.raw.img
printf "backup partition /wcn_modem to wcn_modem.raw.img  ...."
dd if=/dev/block/mmcblk0p19 of=/storage/sdcard1/backup/wcn_modem.raw.img
printf "backup partition /vendor to vendor.raw.img  ...."
dd if=/dev/block/mmcblk0p28 of=/storage/sdcard1/backup/vendor.raw.img
printf "backup partition /system to system.raw.img  ...."
printf "This step can take to 3-4 minutes! Be patient ...."
dd if=/dev/block/mmcblk0p26 of=/storage/sdcard1/backup/system.raw.img
printf "Done!!"
printf "Wait for the system to tell you to remove the USB-stick/SD-card"


