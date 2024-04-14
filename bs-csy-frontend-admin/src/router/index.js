// 引入vue-router官方路由
import {createRouter, createWebHashHistory} from "vue-router";
// 引入各类页面
// 后台框架
import Admin from "~/layouts/admin.vue";
// 首页
import Index from "~/pages/index.vue";
// 登陆页
import Login from "~/pages/login.vue";
// 404页面
import NotFound from "~/pages/404.vue";
// 个人中心
import User from "~/pages/user/user.vue";
import UserList from "~/pages/user/userList.vue";
import TeamList from "~/pages/user/teamList.vue";
import Statistics from "~/pages/user/statistics.vue";
import FriendList from "~/pages/user/friendList.vue";
import Blog from "~/pages/user/blog.vue";
import Sign from "~/pages/user/sign.vue";
import Chat from "~/pages/user/chat.vue";


//默认路由,所有用户共享
const routes = [
    // 后台框架
    {
        path: "/home",
        name: "admin",
        component: Admin,
    },
    {
        path: "/",
        component: Login,
        meta: {
            title: "登录页",
        },
    },
    {
        path: "/login",
        component: Login,
        meta: {
            title: "登录页",
        },
    },
    // 404匹配规则
    {
        path: "/:pathMatch(.*)*",
        name: "NotFound",
        component: NotFound,
    },
];

//动态匹配添加路由
const asyncRoutes = [
    {
        path: "/home",
        component: Index,
        meta: {
            title: "首页"
        },
    },
    {
        path: "/user",
        component: User,
        meta: {
            title: "个人信息"
        },
    },
    {
        path: "/user/list",
        component: UserList,
        meta: {
            title: "用户信息管理",
        },
    },
    {
        path: "/team",
        component: TeamList,
        meta: {
            title: "队伍信息管理",
        },
    },
    {
        path: "/statistics",
        component: Statistics,
        meta: {
            title: "用户标签统计（图表）",
        },
    },
    {
        path: "/friend",
        component: FriendList,
        meta: {
            title: "好友列表管理",
        },
    },
    {
        path: "/blog",
        component: Blog,
        meta: {
            title: "博客信息列表",
        },
    },
    {
        path: "/sign",
        component: Sign,
        meta: {
            title: "签到记录列表",
        },
    },
    {
        path: "/chat",
        component: Chat,
        meta: {
            title: "聊天室消息列表",
        },
    },

];

// 创建实例化路由
export const router = createRouter({
    history: createWebHashHistory(),
    routes,
});


//动态添加路由方法
export function addRoutes(menus) {
    //是否有新的路由
    let hasNewRoutes = false;
    // 创建方法
    const findAndAddRoutesByMenus = (arr) => {
        // 遍历得到每个菜单项
        arr.forEach(e => {
            // 遍历动态路由列表
            let item = asyncRoutes.find((o) => o.path === e.path);
            // 判断路由是否存在
            if (item && !router.hasRoute(item.path)) {
                // 设置标题为获取到的name值
                item.meta.title = e.title
                // 指定父级路由
                router.addRoute('admin', item);
                // 设置标题
                hasNewRoutes = true;
            }
            //子路由,并且长度大于0,即添加子路由
            if (e.secondary && e.secondary.length > 0) {
                // 执行方法添加子路由
                findAndAddRoutesByMenus(e.secondary);
            }
        });
    };
    // 执行方法
    findAndAddRoutesByMenus(menus)
    return hasNewRoutes;
}


// export function addRoutes(menus) {
//     let hasNewRoutes = false;
//     const findAndAddRoutesByMenus = (arr) => {
//         arr.forEach(e => {
//             let item = asyncRoutes.find((o) => o.path === e.path);
//             item.meta.title = e.title
//             router.addRoute("admin", item);
//             hasNewRoutes = true;
//             //子路由,并且长度大于0,即添加子路由
//             if (e.secMenus && e.secMenus.length > 0) {
//                 // 执行方法添加子路由
//                 findAndAddRoutesByMenus(e.secMenus);
//             }
//         });
//     };
//     // 执行方法
//     findAndAddRoutesByMenus(menus)
//     return hasNewRoutes;
// }
