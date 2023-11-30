import Layout from '@/layout'

const drugInventoryManage = {
  path: '/drugInventory',
  component: Layout,
  redirect: '/drugInventory',
  roles: ['manager', 'stockclerk'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/drugInventory/index'),
      name: '库存管理',
      meta: {
        title: '库存管理',
        icon: 'table',
        roles: ['manager', 'stockclerk']
      }
    }
  ]
}

export default drugInventoryManage
