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
export function getUserList(currentPage,searchText) {
    return axios.get(`/api/admin/userPageByAdmin?currentPage=${currentPage}&&searchText=${searchText}`)
}
export function disabledUser(id,status) {
    return axios.post(`/api/admin/disabledUser/${id}/${status}`)
}
export function updateUser(user) {
    return axios.post(`/api/admin/update`,user)
}
// 队伍管理
export function teamPageByAdmin(currentPage,searchText) {
    return axios.get(`/api/admin/teamPageByAdmin?currentPage=${currentPage}&&searchText=${searchText}`)
}

export function disabledTeam(id) {
    return axios.post(`/api/admin/disabledTeam/${id}`)
}
export function joinUser(id) {
    return axios.get(`/api/admin/joinUser/${id}`)
}
// 好友管理
export function friendByAdmin(currentPage,searchText) {
    return axios.get(`/api/admin/friendByAdmin?currentPage=${currentPage}&&searchText=${searchText}`)
}
// 博客管理
export function blogByAdmin(currentPage,searchText) {
    return axios.get(`/api/admin/blogByAdmin?currentPage=${currentPage}&&searchText=${searchText}`)
}
// 积分
export function signByAdmin(currentPage,searchText) {
    return axios.get(`/api/admin/signByAdmin?currentPage=${currentPage}&&searchText=${searchText}`)
}
// 聊天
export function chatByAdmin(currentPage,searchText) {
    return axios.get(`/api/admin/chatByAdmin?currentPage=${currentPage}&&searchText=${searchText}`)
}
