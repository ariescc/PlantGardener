import Layout from '@/layout'

const drugPurchaseRouter = {
  path: '/drugPurchase',
  component: Layout,
  redirect: '/drugPurchase',
  roles: ['stockclerk', 'manager'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/drugPurchase/index'),
      name: '药品采购订单',
      meta: {
        title: '药品采购订单',
        icon: 'table',
        roles: ['stockclerk', 'manager']
      }
    }
  ]
}

export default drugPurchaseRouter
