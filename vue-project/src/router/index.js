import { createRouter, createWebHistory } from 'vue-router';


const router = createRouter({
    history: createWebHistory(),
    routes:[
        {path :'/' ,redirect:'/home'},
        {path:'/login',name:'login',meta:{title:'欢迎登录'},component:() => import('../components/Login.vue')},
        {path:'/register',name:'register',meta:{title:'欢迎注册'},component:() => import('../components/Register.vue')},
        {path :'/manager',component:() => import('../components/Manager.vue') ,children:[
            { path: 'home', name: 'home',meta:{title:'首页'}, component:() => import('../components/Home.vue') },
            { path: 'test', name: 'test',meta:{title:'测试'},component:() => import('../components/Test.vue') },
            {path:'data',name:'data',meta:{title:'数据展示页面'},component:() => import('../components/Data.vue')},
            {path:'employee',name:'employee',meta:{title:'员工信息'},component:() => import('../components/Employee.vue')},
            {path:'admin',name:'admin',meta:{title:'管理员信息'},component:() => import('../components/Admin.vue')},
            {path:'person',name:'person',meta:{title:'个人信息'},component:() => import('../components/Person.vue')},
            {path:'password',name:'password',meta:{title:'修改密码'},component:() => import('../components/Password.vue')},
            {path:'article',name:'article',meta:{title:'文章信息'},component:() => import('../components/Article.vue')}]
        },
        {path:'/404' ,name:'Not Found', meta:{title:'404'},component:() => import('../components/404.vue')},
        {path:'/:pathMatch(.*)', redirect:'/404'}

    ]
});

router.beforeEach((to,from,next)=> {
    document.title = to.meta.title;
    next();
})

export default router;