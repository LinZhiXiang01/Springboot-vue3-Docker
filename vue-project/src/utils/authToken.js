import axios from 'axios';
import dayjs from 'dayjs';

const STORAGE_KEY = 'xm-pro-user';

/**
 * 获取本地用户数据
 */
export function getStoredUser() {
    const userData = window.localStorage.getItem(STORAGE_KEY);
    if (!userData) return null;
    try {
        return JSON.parse(userData);
    } catch (e) {
        console.error('Failed to parse user data:', e);
        return null;
    }
}

/**
 * 存储用户数据
 */
export function setStoredUser(user) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
}

export function updateStoredUser(updatedFields) {
    // 获取当前存储的用户数据
    const storedUser = JSON.parse(localStorage.getItem(STORAGE_KEY));

    // 如果没有用户数据，则直接返回
    if (!storedUser) return;

    // 更新需要更新的字段
    const updatedUser = {
        ...storedUser,   // 保留原有数据
        ...updatedFields // 更新传入的字段
    };

    // 将更新后的数据重新存储
    localStorage.setItem("xm-pro-user", JSON.stringify(updatedUser));
}


/**
 * 清除用户数据
 */
export function clearStoredUser() {
    localStorage.removeItem(STORAGE_KEY);
}

/**
 * 解码 JWT 获取 payload（注意安全，只能读取公有字段如 exp）
 */
export function parseJwt(token) {
    try {
        const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

/**
 * 判断 accessToken 是否即将过期（提前1分钟刷新）
 */
export function isTokenExpiringSoon(token) {
    const payload = parseJwt(token);
    if (!payload || !payload.exp) return true; // 无法解析则视为即将过期
    const exp = dayjs.unix(payload.exp);
    //TODO,测试过期时间是否计算准确
    return dayjs().add(1, 'minute').isAfter(exp);
}

/**
 * 刷新 token
 * @returns {Promise<string|null>} 返回新的 accessToken，失败则返回 null
 */
export async function tryRefreshToken() {
    try {
        const user = JSON.parse(localStorage.getItem("xm-pro-user"));
        if (!user) return null;

        const res = await axios.post(
            '/api/refreshToken',
            //FIXME:前端发了空数据
            //DONE
            { userId: user.authId || user.profile?.authId },
            {
                headers: {
                    'Authorization': 'Bearer ' + user.refreshToken,
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }
        );

        console.log(res.data.accessToken)
        const newAccessToken = res.data.data.accessToken;
        const newRefreshToken = res.data.data.refreshToken;

        if (newAccessToken) {
            updateStoredUser({
                accessToken: newAccessToken,
                refreshToken: newRefreshToken
            });

            return newAccessToken;
        }
    } catch (e) {
        console.error('Token refresh failed:', e);
        return null;
    }
}
