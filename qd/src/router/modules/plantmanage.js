import Layout from '@/layout'

const plantManageRouter = {
  path: '/plantmanage',
  component: Layout,
  redirect: '/plantmanage',
  roles: ['admin'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/plantmanage/index'),
      name: '植物信息',
      meta: {
        title: '植物信息',
        icon: 'table',
        roles: ['admin']
      }
    }
  ]
}

export default plantManageRouter
