import Layout from '@/layout'

const drugmanageRouter = {
  path: '/drugmanage',
  component: Layout,
  redirect: '/drugmanage',
  roles: ['manager'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/drugmanage/index'),
      name: '药品管理',
      meta: {
        title: '药品管理',
        icon: 'table',
        roles: ['manager']
      }
    }
  ]
}

export default drugmanageRouter
