import Vue from 'vue'
import Febs from './FEBS'
import router from './router'
import Antd from 'ant-design-vue'
import store from './store'
import request from 'utils/request'
import db from 'utils/localstorage'
import ElementUI from 'element-ui'
import axios from 'axios'
import heatmap from 'vue-heatmapjs'
import Ionic from '@ionic/vue'
import '@ionic/core/css/ionic.bundle.css'
import 'element-ui/lib/theme-chalk/index.css'
import VueApexCharts from 'vue-apexcharts'
import 'ant-design-vue/dist/antd.css'

import 'utils/install'
import VideoPlayer from 'vue-video-player'
import 'video.js/dist/video-js.css'
import 'videojs-flash'
require('vue-video-player/src/custom-theme.css')

Vue.config.productionTip = false
Vue.use(Antd)
Vue.use(ElementUI)
Vue.use(db)
Vue.use(VueApexCharts)
Vue.use(heatmap)
Vue.use(Ionic)
Vue.use(VideoPlayer)

Vue.component('apexchart', VueApexCharts)

Vue.use({
  install (Vue) {
    Vue.prototype.$db = db
  }
})

Vue.prototype.$post = request.post
Vue.prototype.$postJSON = request.postJSON
Vue.prototype.$get = request.get
Vue.prototype.$put = request.put
Vue.prototype.$delete = request.delete
Vue.prototype.$export = request.export
Vue.prototype.$download = request.download
Vue.prototype.$upload = request.upload
axios.defaults.withCredentials = false
/* eslint-disable no-new */
new Vue({
  router,
  store,
  render: h => h(Febs)
}).$mount('#febs')
