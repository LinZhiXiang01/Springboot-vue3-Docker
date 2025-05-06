#!/bin/bash
#暂时用不上，等待部署上云可以使用该脚本，主要用于启动项目时注入环境变量
#source .env

echo "🔧 正在启动服务..."
docker-compose up -d --build

echo "✅ 所有容器已启动！"
docker ps