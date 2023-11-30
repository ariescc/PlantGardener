import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: '',
    phone: '',
    personSignature: '',
    roles: [],
    email: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_PHONE: (state, phone) => {
    state.phone = phone
  },
  SET_PERSONSIGNATURE: (state, personSignature) => {
    state.personSignature = personSignature
  },
  SET_EMAIL: (state, email) => {
    state.email = email
  }
}

const actions = {
  // user login
  login({ commit }, data) {
    commit('SET_TOKEN', data.token)
    setToken(data.token)
  },

  // get user info
  getInfo({ commit }, data) {
    const name = data.name
    const avatar = data.avatar
    const roles = [data.rolename]
    const phone = data.phone
    const personSignature = data.personSignature
    const email = data.email
    console.log(name, avatar)
    commit('SET_NAME', name)
    commit('SET_AVATAR', avatar)
    commit('SET_ROLES', roles)
    commit('SET_PHONE', phone)
    commit('SET_PERSONSIGNATURE', personSignature)
    commit('SET_EMAIL', email)
  },

  // user logout
  logout({ commit }) {
    removeToken()
    resetRouter()
    commit('RESET_STATE')
    commit('SET_ROLES', [])
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

