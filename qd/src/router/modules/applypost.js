import Layout from '@/layout'

const applyPostRouter = {
  path: '/applypost',
  component: Layout,
  redirect: '/applypost',
  roles: ['salesstaff'],
  children: [
    {
      path: 'index',
      component: () => import('@/views/applypost/index'),
      name: '岗位申请',
      meta: {
        title: '岗位申请',
        icon: 'table',
        roles: ['salesstaff']
      }
    }
  ]
}

export default applyPostRouter
