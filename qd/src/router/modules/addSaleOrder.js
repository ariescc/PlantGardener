import Layout from '@/layout'

const addSaleOrder = {
  path: '/mySaleOrder',
  component: Layout,
  redirect: '/mySaleOrder',
  roles: ['salesstaff'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/saleOrder/addSaleOrder/index'),
      name: '我的订单',
      meta: {
        title: '我的订单',
        icon: 'table',
        roles: ['salesstaff']
      }
    }
  ]
}

export default addSaleOrder
