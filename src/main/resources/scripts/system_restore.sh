#!/system/bin/sh
#
# write the "raw" backup of the necessary partitions.



printf "write to boot.raw.img /boot partition ...."
dd if=/storage/sdcard1/boot.raw.img of=/dev/block/mmcblk0p25

printf "write recovery.raw.img to partition /recovery ...."
dd if=/storage/sdcard1/recovery.raw.img of=/dev/block/mmcblk0p3

printf "Now copy the recovery data file from /system/etc/ ...."
cp /storage/sdcard1/recovery-resource.dat /system/etc/

printf "write u-boot.raw.img to partition /uboot ...."
dd if=/storage/sdcard1/u-boot.raw.img of=/dev/block/mmcblk0p10

printf "write secvm.raw.img to partition /secvm ...."
dd if=/dev/block/mmcblk0p7 of=/storage/sdcard1/secvm.raw.img

printf "write mobilevisor.raw.img to  partition /mobilevisor ...."
dd if=/storage/sdcard1/mobilevisor.raw.img of=/dev/block/mmcblk0p9

printf "write mvconfig.raw.img to partition /mvconfig ...."
dd if=/storage/sdcard1/mvconfig.raw.img of=/dev/block/mmcblk0p8

printf "write l_ldsp.raw.img to partition /l_ldsp ...."
dd if=/storage/sdcard1/l_ldsp.raw.img of=/dev/block/mmcblk0p23

printf "write l_tgdsp.raw.img to partition /l_tgdsp ...."
dd if=/storage/sdcard1/l_tgdsp.raw.img of=/dev/block/mmcblk0p22

printf "write l_modem.raw.img to  partition /l_modem  ...."
dd if=/storage/sdcard1/l_modem.raw.img of=/dev/block/mmcblk0p20

printf "write pm_sys.raw.img to partition /pm_sys ...."
dd if=/storage/sdcard1/pm_sys.raw.img of=/dev/block/mmcblk0p24

printf "write wcn_modem.raw.img to partition /wcn_modem ...."
dd if=/storage/sdcard1/wcn_modem.raw.img of=/dev/block/mmcblk0p19

printf "write vendor.raw.img to partition /vendor ...."
dd if=/storage/sdcard1/vendor.raw.img of=/dev/block/mmcblk0p28

printf "write system.raw.img to partition /system ...."
printf "This step can take to 3-4 minutes! Be patient ...."
dd if=/storage/sdcard1/system.raw.img of=/dev/block/mmcblk0p26

printf "Done!!"
printf "Wait for the system to tell you to remove the USB-stick/SD-card"


