import Layout from '@/layout'

const recruitmentManageRouter = {
  path: '/recruitment',
  component: Layout,
  redirect: '/recruitment',
  roles: ['admin'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/recruitmentmanage/index'),
      name: '招聘信息',
      meta: {
        title: '招聘信息',
        icon: 'table',
        roles: ['admin']
      }
    }
  ]
}

export default recruitmentManageRouter
