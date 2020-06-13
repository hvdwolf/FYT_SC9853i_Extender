#!/system/bin/sh 
# 

rm -rf /storage/sdcard1/FYTget_info*
mkdir -p /storage/sdcard1/FYTget_info
rm -rf /storage/sdcard1/FYTget_info.tgz

# system data
cat /proc/cpuinfo > /storage/sdcard1/FYTget_info/cpuinfo.txt
cat /proc/meminfo > /storage/sdcard1/FYTget_info/meminfo.txt
uname -a > /storage/sdcard1/FYTget_info/uname.txt

# properties
getprop > /storage/sdcard1/FYTget_info/properties.txt

# file systems and partitions
ls -lR /dev > storage/sdcard1/FYTget_info/dev_listing.txt
ls -l /dev/block/platform/soc/soc:ap-ahb/c0c00000.sdio/by-name > /storage/sdcard1/FYTget_info/mapping_blocks2partitions.txt
mount > /storage/sdcard1/FYTget_info/mounts.txt
cat /proc/partitions > /storage/sdcard1/FYTget_info/partitions.txt

# listings of partitions
ls -lR /system/ > /storage/sdcard1/FYTget_info/system_listing.txt
ls -lR /sys/ > /storage/sdcard1/FYTget_info/sys_listing.txt
ls -lR /oem/ > /storage/sdcard1/FYTget_info/oem_listing.txt
ls -lR /vendor/ > /storage/sdcard1/FYTget_info/vendor_listing.txt
ls -lR /data/ > /storage/sdcard1/FYTget_info/data_listing.txt
# What does storage contain?
ls -l /storage > /storage/sdcard1/FYTget_info/storage_listing.txt

# Create tar compressed file
tar -cvzf /storage/sdcard1/FYTget_info.tgz /storage/sdcard1/FYTget_info
#tar -cvzf /storage/sdcard0/FYTget_info.tgz /storage/sdcard1/FYTget_info
