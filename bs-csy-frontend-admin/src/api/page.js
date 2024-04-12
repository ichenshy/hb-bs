// 引入axios
import axios from '~/axios'

// 表格接口
export function getNoticeList(page) {
    return axios.get(`/getUserList/${page}`)
}

export function createNotice(data) {
    return axios.post("/addUserlist", data)
}

export function updateNotice(id, data) {
    return axios.post("/updateList/" + id, data)
}

export function deleteNotice(id) {
    return axios.post(`/deleteList/${id}`)
}

// 卡片列表接口
export function getcardlists() {
    return axios.get("/getcardlist")
}

// 添加卡片数据
export function addcardList(data) {
    return axios.post("/addcardList", {data})
}

// 更新(编辑)卡片数据
export function upcardList(id, data) {
    return axios.post("/upcardList/" + id, data)
}

// 删除卡片数据
export function delcardlist(id) {
    return axios.post("/delcardlist/" + id)
}

// 兴趣伙伴匹配平台标签统计
export function getStatistics() {
    return axios.get("/api/admin/statistics")
}

// 用户管理
export function getUserList(currentPage) {
    return axios.get("/api/admin/userPageByAdmin?currentPage=" + currentPage)
}
// 用户管理
export function disabledUser(id,status) {
    return axios.post(`/api/admin/disabledUser/${id}/${status}`)
}
// 用户管理
export function updateUser(user) {
    return axios.post(`/api/admin/update`,user)
}
