import Layout from '@/layout'

const companymanageRouter = {
  path: '/companymanage',
  component: Layout,
  redirect: '/companymanage',
  roles: ['manager'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/companymanage/index'),
      name: '供货商管理',
      meta: {
        title: '供货商管理',
        icon: 'table',
        roles: ['manager']
      }
    }
  ]
}

export default companymanageRouter
