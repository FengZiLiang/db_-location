import Vue from 'vue'
import Vuex from 'vuex'
import account from './modules/account'
import setting from './modules/setting'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    account,
    setting
  },
  state: {
    // request and Login验证码
    // URL: 'http://121.37.142.234:9527',
    URL: 'http://192.168.43.75:9527',
    // URL: 'http://192.168.10.104:9527',
    // track and camera
    Url: 'http://192.168.43.75',
    // Url: 'http://192.168.10.104',
    // track中的ws
    url: '192.168.43.75:9527'
    // url: '192.168.10.104:9527'
  }
})
