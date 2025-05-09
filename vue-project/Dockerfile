FROM node:lts-alpine AS build

WORKDIR /build

# 先拷贝 package.json 和 package-lock.json（如果有），这样可以利用缓存
COPY package*.json ./

# 云服务器上 设置淘宝 npm 镜像，加快下载速度
RUN npm config set registry https://registry.npmmirror.com
RUN npm install

COPY . .
RUN npm run build

FROM nginx:stable-alpine

COPY --from=build /build/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80