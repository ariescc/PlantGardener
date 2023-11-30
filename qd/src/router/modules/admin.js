import Layout from '@/layout'

const usermanageRouter = {
  path: '/usermanage',
  component: Layout,
  redirect: '/usermanage',
  roles: ['admin'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/usermanage/index'),
      name: '用户管理',
      meta: {
        title: '用户管理',
        icon: 'table',
        roles: ['admin']
      }
    }
  ]
}

export default usermanageRouter
