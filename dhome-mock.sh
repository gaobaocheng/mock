#!/bin/bash

#cd `dirname $0`
 
CUR_SHELL_DIR="/opt/mock"
CUR_SHELL_NAME=`basename ${BASH_SOURCE}`
 
JAR_NAME="demo-mocker-0.1.0.jar"
JAR_PATH=$CUR_SHELL_DIR/$JAR_NAME
 
#JAVA_MEM_OPTS=" -server -Xms1024m -Xmx1024m -XX:PermSize=128m"
JAVA_MEM_OPTS=" -server -Xms2048m -Xmx2048m -XX:MetaspaceSize=512m"
 
#SPRING_PROFILES_ACTIV="-Dspring.profiles.active=prod"
#SPRING_PROFILES_ACTIV=""
 
#LOG_DIR=$CUR_SHELL_DIR/logs
#LOG_PATH=$LOG_DIR/${JAR_NAME%.*}.log
 
echo_help()
{
    echo -e "syntax: sh $CUR_SHELL_NAME start|stop"
}
 
if [ -z $1 ];then
    echo_help
    exit 1
fi
 
#if [ ! -d "$LOG_DIR" ];then
#    mkdir "$LOG_DIR"
#fi
 
#if [ ! -f "$LOG_PATH" ];then
#    touch "$LOG_DIR"
#fi
 
if [ "$1" == "start" ];then
 
    # check server
    PIDS=`ps --no-heading -C java -f --width 1000 | grep $JAR_NAME | awk '{print $2}'`
    if [ -n "$PIDS" ]; then
        echo -e "ERROR: The $JAR_NAME already started and the PID is ${PIDS}."
        exit 1
    fi
 
    echo "Starting the $JAR_NAME..."
 
    # start
    #nohup java $JAVA_MEM_OPTS -jar $SPRING_PROFILES_ACTIV $JAR_PATH >> $LOG_PATH 2>&1
	nohup java $JAVA_MEM_OPTS -jar $JAR_PATH --spring.config.location=file:/opt/config/mock/application.yml 2>&1 &
 
    COUNT=0
    while [ $COUNT -lt 1 ]; do
        sleep 1
        COUNT=`ps  --no-heading -C java -f --width 1000 | grep "$JAR_NAME" | awk '{print $2}' | wc -l`
        if [ $COUNT -gt 0 ]; then
            break
        fi
    done
    PIDS=`ps  --no-heading -C java -f --width 1000 | grep "$JAR_NAME" | awk '{print $2}'`
    echo "${JAR_NAME} Started and the PID is ${PIDS}."
    #echo "You can check the log file in ${LOG_PATH} for details."
 
elif [ "$1" == "stop" ];then
 
    PIDS=`ps --no-heading -C java -f --width 1000 | grep $JAR_NAME | awk '{print $2}'`
    if [ -z "$PIDS" ]; then
        echo "ERROR:The $JAR_NAME does not started!"
        exit 1
    fi
 
    echo -e "Stopping the $JAR_NAME..."
 
    for PID in $PIDS; do
        kill $PID > /dev/null 2>&1
    done
 
    COUNT=0
    while [ $COUNT -lt 1 ]; do
        sleep 1
        COUNT=1
        for PID in $PIDS ; do
            PID_EXIST=`ps --no-heading -p $PID`
            if [ -n "$PID_EXIST" ]; then
                COUNT=0
                break
            fi
        done
    done
 
    echo -e "${JAR_NAME} Stopped and the PID is ${PIDS}."
else
    echo_help
    exit 1
fi