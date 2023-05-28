<template>
  <div class="login">
    <a-form @submit.prevent="doLogin" :form="form">
      <a-tabs size="large" :tabBarStyle="{textAlign: 'center'}" style="padding: 0 2px;" :activeKey="activeKey"
              @change="handleTabsChange">
        <a-tab-pane tab="账户密码登录" key="1">
          <a-alert type="error" :closable="true" v-show="error" :message="error" showIcon
                   style="margin-bottom: 24px;"></a-alert>
          <a-form-item>
            <a-input size="large"  v-decorator="['name',{rules: [{ required: true, message: '请输入账户名', whitespace: true}]}]">
              <a-icon slot="prefix" type="user"></a-icon>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-input size="large" type="password" v-decorator="['password',{rules: [{ required: true, message: '请输入密码', whitespace: true}]}]">
              <a-icon slot="prefix" type="lock"></a-icon>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-row :gutter="8" style="margin: 0 -4px">
              <a-col :span="16">
                <a-input size="large"  v-decorator="['code',{rules: [{ required: true, message: '请输入验证码', whitespace: true}]}]">
                  <a-icon slot="prefix" type="mail"></a-icon>
                </a-input>
              </a-col>
              <a-col :span="8">
                    <!--<i class="el-icon-picture-outline"></i>-->
                <img :src="vscode" @click="getVerifyCode"/>
              </a-col>
            </a-row>
          </a-form-item>
        </a-tab-pane>
        <a-tab-pane tab="手机号登录" key="2">
          <a-form-item>
            <a-input size="large">
              <a-icon slot="prefix" type="mobile"></a-icon>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-row :gutter="8" style="margin: 0 -4px">
              <a-col :span="16">
                <a-input size="large">
                  <a-icon slot="prefix" type="mail"></a-icon>
                </a-input>
              </a-col>
              <a-col :span="8" style="padding-left: 4px">
                <a-button style="width: 100%" class="captcha-button" size="large" @click="getCaptcha">获取验证码</a-button>
              </a-col>
            </a-row>
          </a-form-item>
        </a-tab-pane>
      </a-tabs>
      <a-form-item>
        <a-button :loading="loading" style="width: 100%; margin-top: 4px" size="large" htmlType="submit" type="primary">
          登录
        </a-button>
      </a-form-item>
      <div>
        <a style="float: right" @click="regist">注册账户</a>
      </div>
    </a-form>
  </div>
</template>

<script>
import {mapMutations} from 'vuex'
import { JSEncrypt } from 'jsencrypt'

export default {
  beforeCreate () {
    this.form = this.$form.createForm(this)
  },
  name: 'Login',
  data () {
    return {
      vscode: '',
      // baseUrl: 'http://localhost:9527',
      // baseUrl: 'http://192.168.1.112:9527',
      baseUrl: this.$store.state.URL,
      loading: false,
      error: '',
      activeKey: '1',
      timestamp: new Date().getTime()
    }
  },
  computed: {
    systemName () {
      return this.$store.state.setting.systemName
    },
    copyright () {
      return this.$store.state.setting.copyright
    }
  },
  created () {
    this.$db.clear()
    this.$router.options.routes = []
    this.getVerifyCode()
  },
  methods: {
    doLogin () {
      if (this.activeKey === '1') {
        // 用户名密码登录
        this.form.validateFields(['name', 'password', 'code'], (errors, values) => {
          if (!errors) {
            this.loading = true
            let name = this.form.getFieldValue('name')
            let password = this.form.getFieldValue('password')
            let code = this.form.getFieldValue('code')
            password = this.passwordEncryption(code + ',' + password)
            /* password = 'R9rGcrqsMf4tBrhkDAHOyAvYAsX89jsGE90f1SR28CI/aYfYVr++BbNF2xfuIRB7hv9UmNLiZ75l\n' +
              'l22LXgG2tquVUDQvWKojKZKodYchdrS8qrXpbY0Kc0HbOEGhpKbOpk/hxt97KyQKlukqYrnMzpMw\n' +
              'oDK8yGSwbW7lc2wy0UTiae63eO21WMEewpVL7bX7GC6RXI1wN8el9Ldk3WIIUAWNw/Jq6071U/sB\n' +
              'rSJzYF2LvqGjr8+k1eU7DbXZCMT4TruFuwKVR3/DWENMSwJhp7IP04k6uzazdL423q85rzcnv+Ls\n' +
              'yYYX/a3i2Vvan/Kl0tzRDSVTQT4xx1IokRhbJA==' */
            this.$post('login', {
              username: name,
              password: password,
              verifyCode: code,
              timestamp: this.timestamp
            }).then((r) => {
              let data = r.data.data
              this.saveLoginData(data)
              setTimeout(() => {
                this.loading = false
              }, 500)
              this.$router.push('/')
            }).catch((e) => {
              this.getVerifyCode()
              console.error(e)
              setTimeout(() => {
                this.loading = false
              }, 500)
            })
          }
        })
      }
      if (this.activeKey === '2') {
        // 手机验证码登录
        this.$message.warning('暂未开发')
      }
    },
    getVerifyCode () {
      let url = this.baseUrl + '/images/captcha/' + this.timestamp + '#' + new Date().getTime()
      this.$get(url).then(data => {
        this.vscode = this.baseUrl + '/images/captcha/' + this.timestamp + '#' + new Date().getTime()
      })
    },
    regist () {
      this.$emit('regist', 'Regist')
    },
    getCaptcha () {
      this.$message.warning('暂未开发')
    },
    handleTabsChange (val) {
      this.activeKey = val
    },
    passwordEncryption (passwordUser) {
      // 公钥
      let publicKey = '-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqf21NybzZuALEIT5KMQq/r14+rx4sOvsPIh62G5t09abGxt0YDaLVvnSX0nvfptL+6n01nomPofpyATYD223YfLk0wNO2F7N1SquSkX8iwtcgSmLxR8506uhe5MZoWoyKo/hf/SQMPhwhNanlvJKnC6idwMWRNRBzddWRWE+zB0Qp0ndnJtA7H1eNJJ0r2uwT5LF9DWT7zTdxpNRiZruc4NzKYCZRmLyIxKYIqOju7+X71humyoQkmcSgfhv6KKjCel40zLNnwEW9fGz2tBggIKhmRuxVBY0qJLhdcVgKjPRJxzeTdy4yc956jL6sZ9iMAw2TOfl+mWLV5/it/QcDwIDAQAB-----END PUBLIC KEY-----'
      // Base64解码
      // publicKey = window.atob(publicKey)
      let encryptor = new JSEncrypt()
      encryptor.setPublicKey(publicKey)
      let passwordEncryp = encryptor.encrypt(passwordUser)
      return passwordEncryp
    },
    ...mapMutations({
      setToken: 'account/setToken',
      setExpireTime: 'account/setExpireTime',
      setPermissions: 'account/setPermissions',
      setRoles: 'account/setRoles',
      setUser: 'account/setUser',
      setTheme: 'setting/setTheme',
      setLayout: 'setting/setLayout',
      setMultipage: 'setting/setMultipage',
      fixSiderbar: 'setting/fixSiderbar',
      fixHeader: 'setting/fixHeader',
      setColor: 'setting/setColor'
    }),
    saveLoginData (data) {
      this.setToken(data.token)
      this.setExpireTime(data.exipreTime)
      this.setUser(data.user)
      this.setPermissions(data.permissions)
      this.setRoles(data.roles)
      this.setTheme(data.config.theme)
      this.setLayout(data.config.layout)
      this.setMultipage(data.config.multiPage === '1')
      this.fixSiderbar(data.config.fixSiderbar === '1')
      this.fixHeader(data.config.fixHeader === '1')
      this.setColor(data.config.color)
    }
  }
}
</script>

<style lang="less" scoped>
  .login {
    .icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }
  }
</style>
