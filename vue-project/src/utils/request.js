import axios from "axios";
import {ElMessage} from "element-plus";
import {getStoredUser,isTokenExpiringSoon,tryRefreshToken} from "@/utils/authToken.js";


const request = axios.create({
    baseURL: '/api',  // 用代理重定向到后端
    timeout: 30000  // 后台接口超时时间
})



// request 拦截器
// 可以自请求发送前对请求做一些处理
/**TODO:前端在发送请求前：
// 检查本地的 access_token 是否即将过期（比如小于 1 分钟）
// ✅ 如果还没过期：直接发请求
// ❌ 如果即将或已经过期：先调用 /token/refresh 获取新的 token，再继续
**/
request.interceptors.request.use(async config => {

    const user = getStoredUser();

    if (user && user.accessToken) {

        let token = user.accessToken;

        if (isTokenExpiringSoon(token)) {
            const newToken = await tryRefreshToken();
            if (newToken) {
                token = newToken;
            }
        }

        config.headers['Authorization'] = "Bearer " + token;
    }

    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    return config
}, error => Promise.reject(error));

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res //// 如果 res 有值（非空字符串、非 null/undefined）// 尝试解析为 JSON 对象
        }
        return res;
    },
    error => {
        if (error.response.status === 404) {
            ElMessage.error('未找到请求接口')
            location.href = '/login'
        } else if (error.response.status === 500) {
            ElMessage.error('系统异常，请查看后端控制台报错')
        }else if (error.response.status === 401) {
            ElMessage.error('登录异常，请重新登录')
            location.href = '/login'
        } else {
            console.error(error.message)
            location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default request