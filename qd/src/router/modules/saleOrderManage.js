import Layout from '@/layout'

const saleOrderManage = {
  path: '/saleOrder',
  component: Layout,
  redirect: '/saleOrder',
  roles: ['manager'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/saleOrder/saleOrderManage/index'),
      name: '销售订单管理',
      meta: {
        title: '销售订单管理',
        icon: 'table',
        roles: ['manager']
      }
    }
  ]
}

export default saleOrderManage
