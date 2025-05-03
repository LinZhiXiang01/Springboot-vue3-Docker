#!/bin/bash
#暂时用不上，等待部署上云可以使用该脚本，主要用于启动项目时注入环境变量
source .env
java -jar myapp.jar


#export $(grep -v '^#' .env | xargs)
#java -jar myapp.jar
