import axios from "axios";
import {ElMessage} from "element-plus";

const request = axios.create({
    baseURL: '/api',  // 用代理重定向到后端
    timeout: 30000  // 后台接口超时时间
})



// request 拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {

    try {
        const userData = window.localStorage.getItem("xm-pro-user");
        if (userData) {
            const user = JSON.parse(userData);
            if (user && user.token) {
                config.headers['Token'] = user.token;
                // request.defaults.headers.common['Token'] = user.token;
            }
        }
    } catch (error) {
        console.error("Failed to parse user data from localStorage:", error);
    }

    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    return config
}, error => {
    return Promise.reject(error)
});

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
        } else if (error.response.status === 500) {
            ElMessage.error('系统异常，请查看后端控制台报错')
        }else if (error.response.status === 401) {
            ElMessage.error('登录异常，请重新登录')
            location.href = '/login'
        } else {
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default request