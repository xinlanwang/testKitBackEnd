#!/bin/bash
# 设置mysql的登录用户名和密码(根据实际情况填写)
mysql_user="root"
mysql_password="12345678"
mysql_host="testkit-mysql"
mysql_port="3306"
mysql_charset="utf8"
mysql_database=testkit

# 定义工作路径
BASE_DIR=/home/data/testKit/data

# 备份文件存放地址(根据实际情况填写)
backup_location=$BASE_DIR/backup
echo "$(date "+%Y-%m-%d %H:%M:%S") 备份路径：$backup_location"

# 判断路径是否存在
if [ ! -d "$backup_location" ]; then
    mkdir -p $backup_location
fi

# 增加日志路径 #时间格式DATE=`date '+%Y%m%d-%H%M'`
LOGFILE=$backup_location/$mysql_database-backup.log
echo "$(date "+%Y-%m-%d %H:%M:%S") 日志路径：$LOGFILE"

# 是否删除过期数据
expire_backup_delete="ON"
expire_days=30
backup_time=`date +%Y%m%d%H%M`
backup_dir=$backup_location
welcome_msg="Welcome to use MySQL backup tools!"
mysql_database_backup_file=${mysql_database}_backup-${backup_time}.sql

time=$(date "+%Y-%m-%d %H:%M:%S")
echo -e "\r\n\r\n\r\n--------------------------------" >> $LOGFILE


echo "${time} START BACKUP"
echo "${time} START BACKUP ">> $LOGFILE
echo "${time} docker exec -it mysql mysqldump -h${mysql_host} -P${mysql_port} -u${mysql_user} -p${mysql_password} -B ${mysql_database} > ${backup_dir}/${mysql_database_backup_file}"  >> $LOGFILE
# 备份指定数据库中数据(此处假设数据库是mysql_backup_test)
sudo docker exec -it 3eaf mysqldump -h$mysql_host -P$mysql_port -u$mysql_user -p$mysql_password -B testkit> $backup_dir/mysql_backup_test-$backup_time.sql

 
# 删除过期数据
#if [ "$expire_backup_delete" == "ON" -a  "$backup_location" != "" ];then
#        `find $backup_location/ -type f -mtime +$expire_days | xargs rm -rf`
#        echo "Expired backup data delete complete!"
#fi
time=$(date "+%Y-%m-%d %H:%M:%S")
if [ $? -ne 0 ]; then
	echo '${time} FINISH ERROR'
	echo '${time} FINISH ERROR'  >> $LOGFILE
	exit
	EOF
fi
echo "${time} FINISH BACKUP"
echo "${time} FINISH BACKUP" >> $LOGFILE
# 删除过期数据
if [ "$expire_backup_delete" == "ON" -a  "$backup_location" != "" ];then
		#删除7天以上的备份 find /home/ywtg/backup/ -type f -mtime +7 -exec rm {} ;
        `find $backup_location/ -type f -mtime +$expire_days | xargs rm -rf`
        echo ${time}' Expired backup data delete complete!'
fi
