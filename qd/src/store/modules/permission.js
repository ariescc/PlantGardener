import { asyncRoutes, constantRoutes } from '@/router'
import router from '@/router'

function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 根据角色过滤菜单路由
 * @param routes
 * @param role
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []
  // 遍历动态路由中的每一条路由信息
  routes.forEach(route => {
    const tmp = { ...route } // 针对每一条路由信息生成键值对
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
      console.log(res)
    }
  })
  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    const accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
    console.log(roles)
    commit('SET_ROUTES', accessedRoutes)
    router.addRoutes(accessedRoutes)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
