import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import axios from "axios";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [

    {
      path: '/newlogin',
      name: 'newlogin',
      component: () => import('../views/ce/Login.vue'),
      meta: {
        title: '登录 - 测盟会',
        requiresAuth: false
      }
    },
    {
      path: '/newregister',
      name: 'newregister',
      component: () => import('../views/ce/Register.vue'),
      meta: {
        title: '注册 - 测盟会 ',
        requiresAuth: false
      }
    },


      // 认证相关路由
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue'),
      meta: {
        title: '登录 - 青棠小说',
        requiresAuth: false
      }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue'),
      meta: {
        title: '注册 -  青棠小说',
        requiresAuth: false
      }
    },
    {
      path: '/center',
      name: 'center',
      component: () => import('../views/ce/Center.vue'),
      meta: {
        title: '个人中心',
        requiresAuth: false
      }
    },
    {
      path: '/user',
      name: 'register',
      component: () => import('../views/ce/UserList.vue'),
      meta: {
        title: '用户列表',
        requiresAuth: false
      }
    },
    // 主布局路由
    {
      path: '/',
      name: 'home',
      component: () => import('../views/layouts/MainLayout.vue'),
      redirect: '/news',
      meta: { requiresAuth: false },
      children: [
        // 首页仪表盘
        {
          path: '/newlogin',
          name: 'login',
          component: () => import('../views/ce/Login.vue'),
          meta: {
            title: '首页 -  青棠小说',
            icon: 'dashboard'
          }
        },
        {
          path: '/stats',
          name: 'Stats',
          component: () => import('../views/Stats.vue')
        },
        // 书籍管理
        {
          path: '/books',
          name: 'books',
          component: () => import('../views/books/BookList.vue'),
          meta: {
            title: '书籍管理',
            icon: 'book'
          }
        },
        {
          path: '/books/add',
          name: 'book-add',
          component: () => import('../views/books/BookForm.vue'),
          meta: {
            title: '添加书籍',
            hideInMenu: true
          }
        },
        {
          path: '/books/edit/:id',
          name: 'book-edit',
          component: () => import('../views/books/BookForm.vue'),
          meta: {
            title: '编辑书籍',
            hideInMenu: true
          }
        },

        // 作家管理
        {
          path: '/writers',
          name: 'writers',
          component: () => import('../views/writers/WriterList.vue'),
          meta: {
            title: '作家管理',
            icon: 'user'
          }
        },
        {
          path: '/course',
          name: 'course',
          component: () => import('../views/Course/CourseList.vue'),
          meta: {
            title: '作家管理',
            icon: 'user'
          }
        },
        {
          path: '/news',
          name: 'news',
          component: () => import('../views/News/NewsList.vue'),
          meta: {
            title: '作家管理',
            icon: 'user'
          }
        },
        {
          path: '/meeting',
          name: 'meeting',
          component: () => import('../views/Meeting/MeetingList.vue'),
          meta: {
            title: '作家管理',
            icon: 'user'
          }
        },


        {
          path: '/course/add',
          name: 'course-add',
          component: () => import('../views/Course/CourseDialog.vue'),
          meta: {
            title: '添加课程',
            hideInMenu: true
          }
        },
        {
          path: '/news/add',
          name: 'news-add',
          component: () => import('../views/News/NewsDialog.vue'),
          meta: {
            title: '添加作家',
            hideInMenu: true
          }
        },
        {
          path: '/meeting/add',
          name: 'writer-add',
          component: () => import('../views/Meeting/MeetingDialog.vue'),
          meta: {
            title: '添加作家',
            hideInMenu: true
          }
        },

        {
          path: '/writers/edit/:id',
          name: 'writer-edit',
          component: () => import('../views/writers/WriterDialog.vue'),
          meta: {
            title: '编辑作家',
            hideInMenu: true
          }
        },
        {
          path: '/news/edit/:id',
          name: 'news-edit',
          component: () => import('../views/news/NewsDialog.vue'),
          meta: {
            title: '编辑作家',
            hideInMenu: false
          }
        },
        {
          path: '/course/edit/:id',
          name: 'course-edit',
          component: () => import('../views/course/CourseDialog.vue'),
          meta: {
            title: '编辑课程',
            hideInMenu: false
          }
        },

        // 书友管理
        {
          path: '/readers',
          name: 'readers',
          component: () => import('../views/readers/ReaderList.vue'),
          meta: {
            title: '书友管理',
            icon: 'team'
          }
        },


      ]
    },
    {
      path: '/books/:id',
      name: 'BookDetail',
      component: () => import('../views/books/BookDetail.vue'),
      props: true // 将id作为prop传递给组件
    },
    {
      path: '/writers/:id',
      name: 'WriterDetail',
      component: () => import('../views/writers/WriterDetail.vue'),
      props: true
    },
    {
      path: '/courses/:id',
      name: 'CourseDetail',
      component: () => import('../views/Course/CourseDetail.vue'),
      props: true
    },
    {
      path: '/meetings/:id',
      name: 'MeetingDetail',
      component: () => import('../views/Meeting/MeetingDetail.vue'),
      props: true
    },
    {
      path: '/news/:title',  // 改成 title
      name: 'NewsDetail',
      component: () => import('../views/News/NewsDetail.vue'),
      props: true
    },
    // 404页面
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFound.vue'),
      meta: {
        title: '页面不存在'
      }
    },
    // router/index.js
    {
      path: '/search',
      name: 'Search',
      component: () => import('../views/GlobalSearchResults.vue'),
      props: route => ({ query: route.query.q })
    },

    {
      path: '/profile',
      name: 'profile',
      component:() => import('../views/ProfilePage.vue'),
      meta: { requiresAuth: true } // 需要登录才能访问
    }
  ]
})
router.beforeEach(async (to, from, next) => {
  const isAuthRoute = to.name === 'login' || to.name === 'register';
  try {
    // 检查登录状态
    const response = await axios.get('/api/auth/check');
    // 已登录但访问的是登录页，跳首页
    if (isAuthRoute) {
      return next('/');
    }

    // 正常访问
    next();
  } catch (error) {
    // 未登录且访问需要认证的页面
    if (to.meta.requiresAuth) {
      next('/login');
    } else {
      next();
    }
  }
});


export default router