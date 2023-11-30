import Layout from '@/layout'

const drugPriceManageRouter = {
  path: '/drugPriceManage',
  component: Layout,
  redirect: '/drugpricemanage',
  roles: ['manager'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/drugPriceManage/index'),
      name: '药品成本管理',
      meta: {
        title: '药品成本管理',
        icon: 'table',
        roles: ['manager']
      }
    }
  ]
}

export default drugPriceManageRouter
