// 引入axios
import axios from '~/axios'

// 登陆接口
export function login(username, password) {
    return axios.post("/api/admin/login", {
        userAccount: username,
        userPassword: password
    })
}

// 获取菜单信息接口
export function getinfo(token) {
    return axios.post("/api/admin/getInfo",{
        token
    })
}

// 退出登陆接口
export function logout() {
    return axios.post("/api/admin/logout")
}



// 登陆接口
export function updatepassword(username, password) {
    return axios.post("/api/admin/login", {
        username,
        password
    })
}

export function updateuser(username, password) {
    return axios.post("/api/admin/login", {
        username,
        password
    })
}
