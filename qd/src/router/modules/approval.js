import Layout from '@/layout'

const approvalManageRouter = {
  path: '/approvalManage',
  component: Layout,
  redirect: '/approvalManage',
  roles: ['manager'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/approvalmanage/index'),
      name: '药品许可证管理',
      meta: {
        title: '药品许可证管理',
        icon: 'table',
        roles: ['manager']
      }
    }
  ]
}

export default approvalManageRouter
