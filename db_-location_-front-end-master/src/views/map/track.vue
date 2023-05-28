<template>
  <div id="body" data-keyboard=false>
    <div id="label"></div>
    <!--<div id="Stats-output"></div>-->
    <!-- 重置镜头 -->
    <div id="reset">
      <el-button id="button" type="primary" :title="Reset" icon="el-icon-view" circle></el-button>
    </div>
    <!-- 全屏 -->
    <div id="fullscreen" >
      <!--<button id="changescreen" @click="changescreen" >{{escreen}}</button>-->
      <el-button id="changescreen" title="全屏/退出" @click="handleFullScreen" type="primary" icon="el-icon-full-screen" circle></el-button>
    </div>
    <div id="heatmapcontainer" style="width: 467.5px; height: 465px;"></div>
    <!-- <div id="heatmapcontainer" style="width: 1024px; height: 1024px;;"></div> -->
    <!-- 热力图 -->
    <div id="heatmapbutton-div">
      <el-button id="heatmap-button" :title="this.switchHeat" type="primary" icon="el-icon-sunny" circle></el-button>
    </div>
    <!-- 电子围栏按钮 -->
    <div id="fence">
      <el-button @click="changefence" id="fenceshow" title="电子围栏" type="primary" icon="el-icon-wind-power" circle></el-button>
      <el-button id="fenceshow2" v-show="fencedisplay" @click="showfence" title="显示围栏" type="primary" icon="el-icon-view" circle></el-button>
      <el-button v-show="fencedisplay" @click="showfence2" title="围栏特性" type="primary" icon="el-icon-setting" circle></el-button>
    </div>
    <!-- 电子围栏信息 -->
    <div id="fence-info" v-show="fenceinfodiplay">
      <div style="background: #5a6feb;padding: 2px 5px">电子围栏</div>
      <el-card class="box-card">
        <el-row v-if="fencedata.length === 0">暂无数据</el-row>
        <div v-for="item in fencedata" :key="item.id" style="background: #ececeb;border-radius: 5px;margin: 5px 0px">
          <el-row>{{item.enclosureName}}</el-row>
          <el-row>创建人:{{item.userId}}</el-row>
          <el-row>创建时间:{{item.createTime}}</el-row>
        </div>
      </el-card>
      <el-row>
        <el-col :span="12"><el-button @click="addfenceinfo" type="success" style="width: 100%" size="small">新建</el-button></el-col>
        <el-col :span="12"><el-button @click="deletefenceinfodiplay = !deletefenceinfodiplay" type="info" style="width: 100%" size="small">删除</el-button></el-col>
      </el-row>
    </div>
    <!-- 新增电子围栏信息 -->
    <div id="addfence-info" v-show="addfenceinfodiplay">
      <div style="background: #5a6feb;padding: 2px 5px">新增电子围栏</div>
      <el-card class="box-card-add">
        <el-button id="fence-button3" title="双击选取区域顶点" size="mini" icon="el-icon-plus"></el-button>
        <el-row style="font-size: 10px">请点击'+'号，然后在地图区域双击绘制区域</el-row>
        <el-row>
          <el-col :span="12"><el-button style="width: 100%" id="fence-button2" size="small"  type="success">{{fencename2}}</el-button></el-col>
          <el-col :span="12"><el-button style="width: 100%" id="delect" ref="deletefence" size="small" type="info">删除电子围栏</el-button></el-col>
        </el-row>
        <el-row align="middle" type="flex" style="margin-bottom: 10px">
          <el-col :span="8">围栏名称:</el-col>
          <el-col :span="16"><el-input v-model="addfenceinfodata.enclosureName" placeholder="请输入围栏名称"></el-input></el-col>
        </el-row>
        <el-row align="middle" type="flex" style="margin-bottom: 10px">
          <el-col :span="6" :offset="2">管理员:</el-col>
          <el-col :span="16">
            <el-select v-model="addfenceinfodata.userId" :popper-append-to-body="false" placeholder="请选择">
              <el-option
                v-for="item in UserID"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-row>
        <el-row align="middle" type="flex" style="margin-bottom: 20px">
          <el-col :span="5" :offset="3">备注:</el-col>
          <el-col :span="16"><el-input v-model="addfenceinfodata.remarks" placeholder="请输入备注"></el-input></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><el-button @click="addfenceinfo2" style="width: 100%" size="small" type="success">保存</el-button></el-col>
          <el-col :span="12"><el-button @click="addfenceinfodiplay = false, fenceList = []" style="width: 100%" size="small" type="info">取消</el-button></el-col>
        </el-row>
      </el-card>
    </div>
    <!-- 删除电子围栏 -->
    <el-dialog
      title="删除电子围栏"
      :append-to-body="false"
      :visible.sync="deletefenceinfodiplay"
      width="30%"
      :before-close="handleClose">
      <el-row>
        <el-col :span="6">电子围栏名称:</el-col>
        <el-col :span="18">
          <el-select v-model="fenceIDvalue" :popper-append-to-body="false" placeholder="请选择">
            <el-option
              v-for="item in fenceID"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
    <el-button @click="deletefenceinfodiplay = false">取 消</el-button>
    <el-button type="primary" @click="deletefenceinfo">确 定</el-button>
  </span>
    </el-dialog>
    <!-- 电子围栏特性 -->
    <div id="fence-characteristics" v-show="fencecharacteristics">
      <div style="background: #5a6feb;padding: 2px 5px">围栏特性</div>
      <el-card class="box-card">
        <el-row v-if="fencedata2.length === 0">暂无数据</el-row>
        <div v-for="item in fencedata2" :key="item.enclosureId" style="background: #ececeb;border-radius: 5px;margin: 5px 0px">
          <el-row>{{item.enclosureName}}</el-row>
          <el-row>特性:
            <el-tag
              :key="i.id"
              v-for="i in item.features[0]"
              closable
              :disable-transitions="false"
              size="mini"
              @close="deletefencecharacteristics(i.id)">
              {{i.characterName}}
            </el-tag>
            <button @click="addfencecharacteristicsdiplay = true,enclosureId = item.enclosureId" size="mini" style="color:#3e3e3d;border:thin solid #3e3e3d;margin:5px;">新增</button>
          </el-row>
        </div>
      </el-card>
    </div>
    <el-dialog
      title="添加特性"
      :visible.sync="addfencecharacteristicsdiplay"
      width="30%"
      :before-close="handleClose">
      <el-row align="middle" type="flex">
        <el-col :span="6">标识类型:</el-col>
        <el-col :span="18">
          <el-select v-model="characterType" :popper-append-to-body="false" placeholder="请选择">
            <el-option
              v-for="item in identificationdata"
              :key="item.characterType"
              :label="item.characterName"
              :value="item.characterType">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
                  <el-button @click="addfencecharacteristicsdiplay = false">取 消</el-button>
                  <el-button type="primary" @click="addfencecharacteristics(enclosureId)">确 定</el-button>
                </span>
    </el-dialog>
    <!-- 历史轨迹 -->
    <div id="date">
      <el-button @click="changetrack"  title="历史轨迹" type="primary" icon="el-icon-location-information" circle></el-button>
      <el-date-picker
        v-show="trackdisplay"
        :append-to-body="false"
        title="请获取2020-8-4号9点之后的数据"
        align="right"
        size="small"
        v-model="valuedate"
        style="width: 350px"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期">
      </el-date-picker>
      <el-button id="track" size="small" v-show="valuedate.length > 0">{{trackname}}</el-button>
    </div>
    <!-- 基站重置 -->
    <div id="station-div">
      <el-button @click="stations" title="基站" type="primary" icon="el-icon-mobile-phone" circle></el-button>
      <el-button id="stationsshow" @click="stationsshowVisiable = !stationsshowVisiable" v-show="stationsVisiable" title="显示基站位置" type="primary" icon="el-icon-view" circle></el-button>
      <el-button @click="changestations" v-show="stationsVisiable" title="修改基站位置" type="primary" icon="el-icon-setting" circle></el-button>
      <el-dialog
        title="提示"
        :visible.sync="stationVisiable"
        width="30%"
        :before-close="handleClose">
        <el-row align="middle" type="flex">
          <el-col :span="4" :offset="2">基站MAC</el-col>
          <el-col :span="18">
            <el-select v-model="stationvalue" clearable placeholder="请选择">
              <el-option
                v-for="item in stationMac"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-row>
        <el-row align="middle" type="flex">
          <el-col :span="4" :offset="2">基站X轴</el-col>
          <el-col :span="18"><el-input v-model="stationX" placeholder="请输入X轴"></el-input></el-col>
        </el-row>
        <el-row align="middle" type="flex">
          <el-col :span="4" :offset="2">基站Y轴</el-col>
          <el-col :span="18"><el-input v-model="stationY" placeholder="请输入Y轴"></el-input></el-col>
        </el-row>
        <el-row align="middle" type="flex">
          <el-col :span="4" :offset="2">基站Z轴</el-col>
          <el-col :span="18"><el-input v-model="stationZ" placeholder="请输入Z轴"></el-input></el-col>
        </el-row>
        <el-row align="middle" type="flex">
          <el-col :span="4" :offset="2">主从基站</el-col>
          <el-col :span="18">
            <el-select v-model="typevalue" :popper-append-to-body="false" clearable placeholder="请选择">
              <el-option
                v-for="item in stationtype"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-col>
        </el-row>
        <span slot="footer" class="dialog-footer">
    <el-button @click="stationVisiable = false">取 消</el-button>
    <el-button type="primary" @click="stationedit">确 定</el-button>
  </span>
      </el-dialog>
    </div>
    <!-- 视频联动 -->
    <div id="video-div">
      <el-button id="videosshow" title="显示视频位置" @click="videosshowVisiable = !videosshowVisiable, playerId = 0" type="primary" icon="el-icon-video-camera-solid" circle></el-button>
    </div>
    <div id="cameras">
      <div v-if="playerId > 0" :key="playerId">
        <videoHlsFlv :videoid="'vhf' + playerId" :src="this.$store.state.Url + '/hls/' + playerId + '.m3u8'"></videoHlsFlv>
      </div>
      <!--<div v-else-if="playerId > 0">
        <VideoRtmp :videoid="'vrf' + playerId" :src="'rtmp://192.168.1.112/live/' + playerId "></VideoRtmp>
      </div>-->
    </div>
    <!-- 地图选择按钮 -->
    <div id="mapselection">
      <el-button @click="changemap" title="选择地图(暂未完成待定中)" type="primary" icon="el-icon-loading" disabled circle></el-button>
    </div>
    <!-- 下拉选择器  -->
    <div id="select">
      <el-select v-show="mapdisplay" v-model="value" title="选择地图位置" style="width: 100px;" size="mini" :popper-append-to-body="false" placeholder="请选择">
        <el-option
          v-for="item in cities"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-select v-if="this.value === 'outdoor'" style="width: 180px" size="mini" :popper-append-to-body="false" v-model="value2" placeholder="请选择">
        <el-option
          v-for="item in outdoor"
          :key="item.value"
          :label="item.label"
          :value="item.value">
          <span style="float: left">{{ item.label }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
        </el-option>
      </el-select>
      <el-select v-if="this.value === 'indoor'" style="width: 180px" size="mini" :popper-append-to-body="false" v-model="value3" placeholder="请选择">
        <el-option
          v-for="item in indoor"
          :key="item.value"
          :label="item.label"
          :value="item.value">
          <span style="float: left">{{ item.label }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
        </el-option>
      </el-select>
    </div>
  </div>
</template>

<script>
import $ from 'jquery'
// import dat from '../../../static/js/dat.gui'
import * as THREE from 'three'
// import Stats from '../../../static/js/stats'
import screenfull from 'screenfull'
import h337 from '../../../static/js/heatmap'
import {OrbitControls} from 'three/examples/jsm/controls/OrbitControls'
import VideoHlsFlv from '@/components/videotool/VideoHlsFlv.vue'
import VideoRtmp from '@/components/videotool/VideoRtmp.vue'
var camera
var scene
var renderer
// var element = document.documentElement
var socket
var positiontx = 1.5
var positionty = 1.5
var _this
var points = []
var timeout
var fenceList = []
export default {
  name: 'map',
  data () {
    return {
      // 显示视频位置
      videosshowVisiable: false,
      // 显示删除电子围栏弹框
      deletefenceinfodiplay: false,
      // 显示电子围栏弹框
      fenceinfodiplay: false,
      // 显示电子围栏特性弹框
      fencecharacteristics: false,
      // 显示电子围栏特征新增弹框
      addfencecharacteristicsdiplay: false,
      // 显示新增电子围栏弹框
      addfenceinfodiplay: false,
      // 电子围栏按钮
      fencedisplay: false,
      // 地图选择按钮
      mapdisplay: false,
      // 历史轨迹按钮
      trackdisplay: false,
      // 基站按钮
      stationsVisiable: false,
      // 显示基站位置
      stationsshowVisiable: false,
      // 修改基站信息弹框
      stationVisiable: false,
      fencename: '创建电子围栏',
      fencename2: '确认创建',
      trackname: '显示轨迹',
      trackVisiable: true,
      // 历史轨迹信息
      location: [],
      // 电子围栏信息
      fencedata: [],
      // 电子围栏特性
      fencedata2: [],
      // 标识类型
      identificationdata: [],
      // 标识类型
      characterType: '',
      // 电子围栏ID  用于新增电子围栏特性
      enclosureId: '',
      secenes: [],
      // 电子围栏坐标
      objLc: [],
      // 电子围栏ID与围栏名称
      fenceID: [],
      // 用户ID
      UserID: [],
      // 基站位置
      stationList: [],
      // 基站小球存储
      stationsSum: [],
      // 视频信息
      videodata: [],
      // 视频ID
      playerId: 0,
      // 视频位置
      videosList: [],
      // 视频小球存储
      videosSum: [],
      // 用于存储已经改过颜色的小球
      changevideocolor: null,
      // 实时信息
      websocketdata: [],
      // 实时小球存储
      realtime: [],
      Reset: '重置镜头',
      switchHeat: '开关热力图',
      isFull: true,
      // 电子围栏添加输入框
      addfenceinfodata: {
        enclosureName: '',
        userId: '',
        remarks: ''
      },
      cities: [{
        value: 'Please select',
        label: '请选择'
      }, {
        value: 'indoor',
        label: '室内'
      }, {
        value: 'outdoor',
        label: '室外'
      }],
      indoor: [{
        value: 'indoor1',
        label: '室内1'
      }, {
        value: 'indoor2',
        label: '室内2'
      }],
      outdoor: [{
        value: 'outdoor1',
        label: '室外1'
      }, {
        value: 'outdoor2',
        label: '室外2'
      }],
      stationMac: [],
      stationtype: [{
        value: 1,
        label: '主基站'
      }, {
        value: 0,
        label: '从基站'
      }],
      typevalue: '',
      stationvalue: '',
      fenceIDvalue: '',
      stationX: '',
      stationY: '',
      stationZ: '',
      value: '',
      value2: '',
      value3: '',
      valuedate: ''
    }
  },
  components: {
    VideoHlsFlv,
    VideoRtmp
  },
  created () {
    _this = this
  },
  mounted () {
    this.getUserID()
    this.getfenceinfo()
    this.getfencecharacteristic()
    this.getidentification()
    this.getstationMac()
    this.getstation()
    this.getvideocoordinate()
    this.websocket()
    // this.init()
    this.callInit()
    window.addEventListener('resize', this.onResize)
  },
  watch: {
    valuedate (newVal, oldVal) {
      var d1 = _this.valuedate[0]
      var d2 = _this.valuedate[1]
      var startdate = d1.getFullYear() + '-' + (d1.getMonth() + 1) + '-' + d1.getDate() +
        ' ' + d1.getHours() + ':' + d1.getMinutes() + ':' + d1.getSeconds()
      var enddate = d2.getFullYear() + '-' + (d2.getMonth() + 1) + '-' + d2.getDate() +
        ' ' + d2.getHours() + ':' + d2.getMinutes() + ':' + d2.getSeconds()
      this.$get(`/location/playBack/1?userId=1&beginTime=${startdate}&endTime=${enddate}`).then(res => {
        this.location = res.data
      })
    },
    stationvalue (newVal, oldVal) {
      this.stationX = ''
      this.stationY = ''
      this.station = ''
      this.typevalue = ''
    }
  },
  methods: {
    callInit () {
      setTimeout(this.init, 2000)
    },
    // 按钮显示
    // 历史轨迹
    changetrack () {
      this.trackdisplay = !this.trackdisplay
      this.valuedate = ''
      this.mapdisplay = false
      this.value = ''
      this.fencedisplay = false
      this.fenceinfodiplay = false
      this.fencecharacteristics = false
    },
    // 地图选择
    changemap () {
      this.mapdisplay = !this.mapdisplay
      this.value = ''
      this.trackdisplay = false
      this.valuedate = ''
      this.fencedisplay = false
      this.fenceinfodiplay = false
    },
    // 电子围栏
    changefence () {
      this.getfenceinfo()
      this.fencedisplay = !this.fencedisplay
      this.fenceinfodiplay = false
      this.addfenceinfodiplay = false
      this.fencecharacteristics = false
      this.trackdisplay = false
      this.valuedate = ''
      this.mapdisplay = false
      this.value = ''
    },
    // 显示电子围栏
    showfence () {
      this.fenceinfodiplay = !this.fenceinfodiplay
      this.fencecharacteristics = false
      this.playerId = 0
    },
    // 围栏特性
    showfence2 () {
      this.fencecharacteristics = !this.fencecharacteristics
      this.fenceinfodiplay = false
      this.playerId = 0
    },
    // 获取用户ID
    getUserID () {
      this.$get('/user/ids?isAdmin=true').then(res => {
        this.UserID = res.data
      })
    },
    // 获取电子围栏信息
    getfenceinfo () {
      this.$get('/enclosure').then(res => {
        this.fencedata = res.data.rows
        this.fenceID = []
        this.objLc = []
        res.data.rows.forEach(loc => {
          this.objLc.push(JSON.parse(loc.enclosureLocation))
          var IDandName = {}
          IDandName.value = loc.id
          IDandName.label = loc.enclosureName
          this.fenceID.push(IDandName)
        })
        this.fencedata.forEach(row => {
          this.UserID.forEach(x => {
            if (x.value === row.userId) {
              row.userId = x.label
            }
          })
        })
      })
    },
    // 获取电子围栏特征
    getfencecharacteristic () {
      this.$get('/enclosure/character/1').then(res => {
        this.fencedata2 = res.data
        console.log(this.fencedata2)
      })
    },
    // 获取电子围栏标识
    getidentification () {
      this.$get('/enclosure/character/type').then(res => {
        this.identificationdata = res.data.rows
      })
    },
    addfenceinfo () {
      this.fenceinfodiplay = false
      this.addfenceinfodiplay = true
      fenceList = []
      this.getfenceinfo()
    },
    deletefenceinfo () {
      this.deletefenceinfodiplay = false
      this.$delete(`/enclosure/${this.fenceIDvalue}`).then(res => {
        this.fenceIDvalue = ''
        this.$message('成功删除电子围栏')
        this.getfenceinfo()
      })
    },
    addfenceinfo2 () {
      this.$post('/enclosure', {
        enclosureName: this.addfenceinfodata.enclosureName,
        enclosureLocation: JSON.stringify(fenceList),
        mapId: 1,
        userId: this.addfenceinfodata.userId,
        remarks: this.addfenceinfodata.remarks
      }).then(res => {
        this.addfenceinfodata.enclosureName = ''
        this.addfenceinfodata.userId = ''
        this.addfenceinfodata.remarks = ''
        this.$refs.deletefence.$el.click()
        this.$message('成功加载电子围栏')
        fenceList = []
        this.addfenceinfodiplay = false
      })
    },
    // 新增电子围栏特性
    addfencecharacteristics (enclosureId) {
      this.$post('/enclosure/character', {
        enclosureId: enclosureId,
        characterType: this.characterType,
        isForever: false
      }).then(res => {
        this.getfencecharacteristic()
        this.addfencecharacteristicsdiplay = false
      })
    },
    // 删除电子围栏特性
    deletefencecharacteristics (id) {
      this.$delete(`/enclosure/character/${id}`).then(res => {
        this.getfencecharacteristic()
      })
    },
    // 获取基站Mac数据
    getstationMac () {
      this.$get('/map/stations/macs').then(res => {
        this.stationMac = res.data
      })
    },
    // 获取基站坐标位置
    getstation () {
      this.$get('/map/stations').then(res => {
        var List = res.data.rows
        List.forEach(r => {
          var XYZ = {}
          XYZ.x = r.baseX
          XYZ.y = r.baseY
          XYZ.z = r.baseZ
          this.stationList.push(XYZ)
        })
      })
    },
    // 显示基站
    stations () {
      this.stationsVisiable = !this.stationsVisiable
    },
    // 显示基站修改弹框
    changestations () {
      this.stationVisiable = true
    },
    // 修改基站信息
    stationedit () {
      this.$put('map/stations', {
        baseMac: this.stationvalue,
        baseX: this.stationX,
        baseY: this.stationY,
        baseZ: this.stationZ,
        mapId: 1,
        isMaster: this.typevalue
      }).then(res => {
        this.stationvalue = ''
        this.stationX = ''
        this.stationY = ''
        this.stationZ = ''
        this.typevalue = ''
      })
      this.stationVisiable = false
    },
    handleClose (done) {
      this.stationVisiable = false
      this.deletefenceinfodiplay = false
      this.addfencecharacteristicsdiplay = false
      /* this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {}) */
    },
    // 获取视频坐标
    getvideocoordinate () {
      this.$get('/decoder/camera/1').then(res => {
        this.videodata = res.data
        var cameraInfos = []
        for (var i = 0; i < this.videodata.length; i++) {
          cameraInfos.push(this.videodata[i].cameraInfo)
        }
        cameraInfos.forEach(res => {
          var List = {}
          List.x = res.indexX
          List.y = res.indexY
          List.z = res.indexZ
          this.videosList.push(List)
        })
      })
    },
    websocket () {
      if (typeof (WebSocket) === 'undefined') {
        console.log('您的浏览器不支持WebSocket')
      } else {
        console.log('您的浏览器支持WebSocket')
        // 实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
        socket = new WebSocket(`ws://${this.$store.state.url}/websocket/location/1`)
        // socket = new WebSocket(`ws://192.168.1.112:9527/websocket/location/1`)
        // 打开事件
        socket.onopen = function () {
          console.log('Socket 已打开')
          socket.send('get')
        }
        // 获得消息事件
        socket.onmessage = function (msg) {
          var location = JSON.parse(msg.data.toString())
          _this.websocketdata = []
          location.forEach(res => {
            _this.websocketdata.push(JSON.parse(res))
          })
          var lc = JSON.parse(location[0])
          positiontx = lc.locationX
          positionty = lc.locationY
        }
        // 关闭事件
        socket.onclose = function () {
          console.log('Socket已关闭')
        }
        // 发生了错误事件
        socket.onerror = function () {
          alert('Socket发生了错误')
        }
      }
    },
    handleFullScreen () {
      let element = document.getElementById('body')
      // let element = document.body
      // 退出全屏
      if (!this.isFull) {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
        // 进入全屏
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen()
        }
      }
      this.isFull = !this.isFull
    },
    onResize () {
      camera.aspect = window.innerWidth / window.innerHeight
      camera.updateProjectionMatrix()
      if (screenfull.isFullscreen) {
        renderer.setSize(window.innerWidth, window.innerHeight)
      } else {
        renderer.setSize(window.innerWidth * 0.86, window.innerHeight * 0.75)
        // renderer.setSize(window.innerWidth, window.innerHeight)
      }
    },
    init () {
      // var stats = initStats()
      /* const test = await _this.websocket().then(result => {
        console.log(result)
      }) */
      console.log(_this.websocketdata.length)
      scene = new THREE.Scene()
      camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 1000)
      renderer = new THREE.WebGLRenderer()
      renderer.setSize(window.innerWidth * 0.86, window.innerHeight * 0.75)
      // renderer.setSize(window.innerWidth, window.innerHeight)
      document.getElementById('body').appendChild(renderer.domElement)
      renderer.setClearColor(0xffffff)
      // 创建控制界面(控制动画参数)
      var controls = new function () {
        this.rotationSpeed = 0.02
        this.bouncingSpeed = 0.01
        this.xzhi = 6.4
        this.yzhi = 0
      }()
      // 右上角的x,y
      // 创建几何体
      var sphereGeometry = new THREE.SphereGeometry(0.2, 20, 20)
      var sphereMaterial1 = new THREE.MeshBasicMaterial({color: 0xADD8E6})
      // var sphere1 = new THREE.Mesh(sphereGeometry, sphereMaterial1)
      if (_this.websocketdata.length > 0) {
        for (var i = 0; i < _this.websocketdata.length; i++) {
          var sphere = new THREE.Mesh(sphereGeometry, sphereMaterial1)
          scene.add(sphere)
          _this.realtime.push(sphere)
        }
      }
      // 根据获取的基站位置绘制小球
      $('#stationsshow').on('click', function () {
        if (_this.stationsshowVisiable) {
          var i = 0
          for (i; i < _this.stationList.length; i++) {
            var sphereGeometry = new THREE.SphereGeometry(0.2, 20, 20)
            var sphereMaterial = new THREE.MeshBasicMaterial({color: 0xADD8E6})
            var s = new THREE.Mesh(sphereGeometry, sphereMaterial)
            s.position.x = _this.stationList[i].x
            s.position.y = _this.stationList[i].y
            s.position.z = _this.stationList[i].z
            _this.stationsSum.push(s)
            scene.add(s)
          }
        } else {
          _this.stationsSum.forEach(sc => {
            scene.remove(sc)
          })
        }
      })
      // 根据获取的视频位置绘制小球
      $('#videosshow').on('click', function () {
        if (_this.videosshowVisiable) {
          var i = 0
          for (i; i < _this.videosList.length; i++) {
            var sphereGeometry = new THREE.SphereGeometry(0.2, 20, 20)
            var sphereMaterial = new THREE.MeshBasicMaterial({color: 0x0000FF})
            var v = new THREE.Mesh(sphereGeometry, sphereMaterial)
            v.position.x = _this.videosList[i].x
            v.position.y = _this.videosList[i].y
            v.position.z = _this.videosList[i].z
            _this.videosSum.push(v)
            scene.add(v)
          }
          initThreeClickEvent()
        } else {
          _this.videosSum.forEach(sc => {
            scene.remove(sc)
          })
          _this.videosSum = []
          _this.changevideocolor = null
        }
      })
      // 根据坐标获取视频ID
      function getLatestCamera (x, y, z, cameras) {
        let minId = 0
        let minDistance = 0
        cameras.forEach((item, index, arr) => {
          let cameraInfo = item.cameraInfo
          let distance = getDistance(x, y, z,
            cameraInfo.indexX,
            cameraInfo.indexY,
            cameraInfo.indexZ)
          // 获取距离最小距离的ID
          if (minDistance <= distance) {
            minDistance = distance
            minId = item.decoderId
          }
        })
        return minId
      }
      // 获取距离
      function getDistance (ax, ay, az, bx, by, bz) {
        return Math.pow((ax - bx), 2) + Math.pow((ay - by), 2) + Math.pow((az - bz), 2)
      }
      // 点击事件
      function initThreeClickEvent () {
        var raycaster = new THREE.Raycaster()
        var mouse = new THREE.Vector2()
        function onMouseClick (event) {
          // 通过鼠标点击的位置计算出raycaster所需要的点的位置，以屏幕中心为原点，值的范围为-1到1.
          if (_this.isFull) {
            mouse.x = (event.offsetX / (window.innerWidth * 0.86)) * 2 - 1
            mouse.y = -(event.offsetY / (window.innerHeight * 0.75)) * 2 + 1
          } else {
            mouse.x = (event.offsetX / window.innerWidth) * 2 - 1
            mouse.y = -(event.offsetY / window.innerHeight) * 2 + 1
          }
          // 通过鼠标点的位置和当前相机的矩阵计算出raycaster
          raycaster.setFromCamera(mouse, camera)
          // 获取raycaster直线和所有模型相交的数组集合
          var intersects = raycaster.intersectObjects(_this.videosSum)
          // 将所有的相交的模型的颜色设置为红色，如果只需要将第一个触发事件，那就数组的第一个模型改变颜色即可
          if (intersects.length > 0) {
            // 在这里填写点击代码
            if (_this.changevideocolor != null) {
              _this.changevideocolor.object.material.color.set(0x0000FF)
            }
            _this.changevideocolor = intersects[0]
            _this.playerId = getLatestCamera(intersects[0].point.x, intersects[0].point.y, 1, _this.videodata)
            console.log(_this.playerId)
            intersects[0].object.material.color.set(0xff0000)
          }
        }
        window.addEventListener('click', onMouseClick, false)
      }
      // 线
      var materials = new THREE.LineBasicMaterial({color: 0xCD0000})
      var line
      $('#track').on('click', function () {
        if (_this.trackVisiable) {
          var geometry = new THREE.Geometry()
          _this.location.forEach(x => {
            geometry.vertices.push(new THREE.Vector3(x.locationX, x.locationY, 1.5))
          })
          line = new THREE.Line(geometry, materials)
          scene.add(line)
          _this.trackVisiable = false
          _this.trackname = '删除轨迹'
        } else if (!_this.trackVisiable) {
          scene.remove(line)
          _this.trackVisiable = true
          _this.trackname = '显示轨迹'
        }
      })
      // 创建坐标轴
      var axes = new THREE.AxesHelper(10)
      scene.add(axes)
      // 创建光源
      var spotLight1 = new THREE.SpotLight(0xffffff)
      spotLight1.position.set(-50, -50, 50)
      scene.add(spotLight1)
      var spotLight2 = new THREE.SpotLight(0xffffff)
      spotLight2.position.set(100, 100, 100)
      scene.add(spotLight2)
      // 将内容加入组中
      var group = new THREE.Group()
      // group.add(sphere1)
      group.add(_this.realtime[0])
      scene.add(group)
      // 鼠标悬停事件
      var selectObject
      var raycaster = new THREE.Raycaster()
      function onMouseMove (event) {
        var intersects = getIntersect(event)
        // 原本!= 改成!==
        if (intersects.length !== 0 && intersects[0].object instanceof THREE.Mesh) {
          selectObject = intersects[0].object
          $('#label').show()
        } else {
          $('#label').hide()
        }
      }
      // 使用射线来选择目标物体
      function getIntersect (event) {
        event.preventDefault()
        var mouse = new THREE.Vector2()
        if (_this.isFull) {
          mouse.x = (event.offsetX / (window.innerWidth * 0.86)) * 2 - 1
          mouse.y = -(event.offsetY / (window.innerHeight * 0.75)) * 2 + 1
        } else {
          mouse.x = (event.offsetX / window.innerWidth) * 2 - 1
          mouse.y = -(event.offsetY / window.innerHeight) * 2 + 1
        }
        raycaster.setFromCamera(mouse, camera)
        var intersects = raycaster.intersectObjects(group.children)
        return intersects
      }
      window.addEventListener('mousemove', onMouseMove, false)
      // 创建悬浮标签（label标签）
      function renderDiv (object) {
        var halfWidth, halfHeight
        if (_this.isFull) {
          halfWidth = (window.innerWidth * 0.86) / 2
          halfHeight = (window.innerHeight * 0.75) / 2
        } else {
          halfWidth = window.innerWidth / 2
          halfHeight = window.innerHeight / 2
        }
        var vector = object.position.clone().project(camera)
        $('#label').css({
          left: vector.x * halfWidth + halfWidth,
          top: -vector.y * halfHeight + halfHeight - object.position.y
        })
        $('#label').text('name: ' + object.name + '\n' + 'position: ' + object.position.x + ' , ' + object.position.y)
      }
      // 重置镜头的按钮
      $('#button').on('click', function () {
        // trackballControls.target = new THREE.Vector3(4.65, 4.65, 0)
        camera.position.set(4.65, 4.65, 15)
        camera.up.x = 0
        camera.up.y = 1
        camera.up.z = 0
      })
      // 创建平面（地坂
      var planeGeometry = new THREE.PlaneGeometry(60, 30)
      var planeMaterial = new THREE.MeshLambertMaterial({color: 0x708090})
      var plane = new THREE.Mesh(planeGeometry, planeMaterial)
      plane.position.set(0, 0, 0)
      scene.add(plane)
      plane.material.side = THREE.DoubleSide
      camera.position.set(4.65, 4.65, 15)
      var orbitControls = new OrbitControls(camera, renderer.domElement)
      orbitControls.autoRotate = true
      orbitControls.target = new THREE.Vector3(4.65, 4.65, 0)
      orbitControls.minPolarAngle = 0
      orbitControls.maxPolarAngele = 0
      orbitControls.minAzimuthAngle = 0
      orbitControls.maxAzimuthAngle = Math.PI
      // 模型加载器
      var loader = new THREE.ObjectLoader()
      // var loader = new THREE.JSONLoader()
      // loader.load('/static/model/changzhoudao.json', function (obj) {
      loader.load('/static/model/0822f.json', function (obj) {
        obj.scale.x = 1
        obj.scale.y = 1
        obj.scale.z = 1
        scene.add(obj)
        obj.position.set(0, 0, 0)
        obj.rotation.x = 1.5 * Math.PI
      })
      // }
      // 绘制方法
      $('#fenceshow').on('click', function () {
        var i = 0
        if (_this.fencedisplay) {
          for (i; i < _this.objLc.length; i++) {
            var fenceshape = new THREE.Shape()
            fenceshape.setFromPoints(_this.objLc[i])
            var fence = new THREE.ShapeGeometry(fenceshape)
            var planeMateriala = new THREE.MeshBasicMaterial({color: 0xFFFFCC, transparent: true, opacity: 0.5})
            var a = new THREE.Mesh(fence, planeMateriala)
            a.position.z = 1.5
            _this.secenes.push(a)
          }
        } else {
          _this.secenes.forEach(sc => {
            scene.remove(sc)
          })
          _this.secenes = []
        }
        $('#fenceshow2').on('click', function () {
          if (_this.fenceinfodiplay) {
            _this.secenes.forEach(sc => {
              scene.add(sc)
            })
          } else {
            _this.secenes.forEach(sc => {
              scene.remove(sc)
            })
          }
        })
      })
      function Drawing () {
        var vector = new THREE.Vector3()
        var pos = new THREE.Vector3()
        if (_this.isFull) {
          vector.set((event.offsetX / (window.innerWidth * 0.86)) * 2 - 1, -(event.offsetY / (window.innerHeight * 0.75)) * 2 + 1, 0.5)
        } else {
          vector.set((event.offsetX / window.innerWidth) * 2 - 1, -(event.offsetY / window.innerHeight) * 2 + 1, 0.5)
        }
        vector.unproject(camera)
        vector.sub(camera.position).normalize()
        var distance = -camera.position.z / vector.z
        pos.copy(camera.position).add(vector.multiplyScalar(distance))

        var v2 = new THREE.Vector2()
        v2.set(pos.x, pos.y)
        console.log(v2)
        points.push(v2)
        v2.z = 1.5
        fenceList.push(v2)
        return points
      }
      function onDrawed () {
        let points = []
        timeout = setTimeout(function () {
          addEventListener('mousemove', Drawing, false)
        }, 50)
      }
      function onDrawed2 () {
        clearTimeout(timeout)
        removeEventListener('mousemove', Drawing, false)
        console.log(points)
      }
      $('#fence-button3').on('click', function fencebutton (fence) {
        camera.position.set(4.65, 4.65, 15)
        camera.up.x = 0
        camera.up.y = 1
        camera.up.z = 0
        orbitControls.enabled = false
        points = []
        addEventListener('dblclick', Drawing, false)
      })
      $('#fence-button2').on('click', function () {
        orbitControls.enabled = true
        var temp = []
        temp = points
        points = []
        removeEventListener('mousedown', onDrawed, false)
        removeEventListener('mouseup', onDrawed2, false)
        let fencebutton = function () { return (false) }
        console.log(temp)
        console.log(temp[0])

        function drawfence () {
          var fenceshape = new THREE.Shape()
          fenceshape.fromPoints(temp)
          return fenceshape
        }
        var fence = new THREE.ShapeGeometry(drawfence())
        var planeMateriala = new THREE.MeshBasicMaterial({color: 0xFFFFCC, transparent: true, opacity: 0.5})
        var aaa = new THREE.Mesh(fence, planeMateriala)
        aaa.position.z = 1.5
        scene.add(aaa)
        // 判断点是否在面内的方法   pt:点    poly:多边形数组
        // 删除电子围栏
        $('#delect').on('click', function Delect () {
          scene.remove(aaa)
          fenceList = []
        })
        function PointInPoly (pt, poly) {
          for (var c = false, i = -1, l = poly.length, j = l - 1; ++i < l; j = i) {
            ((poly[i].y <= pt.y && pt.y < poly[j].y) || (poly[j].y <= pt.y && pt.y < poly[i].y)) && (pt.x < (poly[j].x - poly[i].x) * (pt.y - poly[i].y) / (poly[j].y - poly[i].y) + poly[i].x) && (c = !c)
            return c
          }
        }
        var pt = new THREE.Vector2()
        pt.x = _this.realtime[0].position.x
        pt.y = _this.realtime[0].position.y

        var poly = temp
        var answer = PointInPoly(pt, poly)
        console.log(answer)
      })
      // 热力图
      // 创建热力图,初始化热力图参数
      var heatmapInstance = h337.create({
        container: document.querySelector('#heatmapcontainer'),
        radius: 40
      })
      var max = 0
      var points = []
      var len = 300
      while (len--) {
        var val = Math.floor(Math.random() * 100)
        max = Math.max(max, val)
        var point = {
          x: _this.realtime[0].position.x * 100,
          y: 930 - controls.yzhi * 100,
          value: val
        }
        points.push(point)
      }
      // 将生成的热力图画布作为纹理创建一个平面
      var heatMapGeometry = new THREE.PlaneGeometry(9.35, 9.3)
      // var heatMapGeometry = new THREE.PlaneGeometry(10.24, 10.24)
      var heatMapTexture = new THREE.Texture(heatmapInstance._renderer.canvas)
      var heatMapMaterial = new THREE.MeshBasicMaterial({
        map: heatMapTexture,
        transparent: true
      })
      var heatMapPlane = new THREE.Mesh(heatMapGeometry, heatMapMaterial)
      heatMapPlane.position.set(4.675, 4.65, 1)
      scene.add(heatMapPlane)
      $('#heatmapcontainer').hide()
      // 开关热力图的按钮
      heatMapPlane.visible = false
      $('#heatmap-button').on('click', function () {
        // 原先==改成===
        if (heatMapPlane.visible === true) {
          heatMapPlane.visible = false
          _this.switchHeat = '打开热力图'
          _this.$message('热力图已关闭')
        } else {
          heatMapPlane.visible = true
          _this.switchHeat = '关闭热力图'
          _this.$message('热力图已打开')
        }
      })
      // 刷新渲染控件主体(动画等的核心所在)
      // var step = 0
      var clock = new THREE.Clock()
      function renderScene () {
        requestAnimationFrame(renderScene)
        // stats.update()
        // sphere1.position.x = positiontx
        // sphere1.position.y = positionty
        // sphere1.position.x = 1
        // sphere1.position.y = 1
        // sphere1.position.z = 1.5
        if (_this.realtime.length > 0) {
          for (var i = 0; i < _this.websocketdata.length; i++) {
            _this.realtime[i].position.x = _this.websocketdata[i].locationX
            _this.realtime[i].position.y = _this.websocketdata[i].locationY
            _this.realtime[i].position.z = 1.5
          }
        }
        // 用于轨迹球控制器刷新屏幕
        var delta = clock.getDelta()
        // trackballControls.update(delta)
        orbitControls.update(delta)
        // 射线选择物体相关   !=改成!==
        if (selectObject !== undefined && selectObject != null) {
          renderDiv(selectObject)
        }
        // 热力图实时更新坐标
        heatMapMaterial.map.needsUpdate = true
        heatmapInstance.addData({
          x: _this.realtime[0].position.x * 50,
          y: 465 - _this.realtime[0].position.y * 50,
          value: 1
        })
        renderer.render(scene, camera)
      }
      renderScene()
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../../../static/less/Common";
  #body{
    position: relative;
    margin:0;
    width:100%;
    height: cala(100vh);
  }
  canvas{display:block;}
  #label{
    padding: 10px;
    background: rgba(255, 255, 255, 0.6);
    line-height: 1;
    border-radius: 5px;
    white-space: pre;
  }
  // 重置镜头按钮
  #reset{
    position: absolute;
    right: 10px;
    margin-top: 0px;
   /* padding: 5px;
    background: rgba(255, 255, 255, 0.6);
    line-height: 1;
    border-radius: 5px;*/
    /* white-space: pre; */
  }
  // 全屏按钮
  #fullscreen{
    position: absolute;
    right: 10px;
    margin-top: 50px;
   /* padding: 5px;
    background: rgba(255, 255, 255, 0.6);
    line-height: 1;
    border-radius: 5px;*/
    /* white-space: pre; */
  }
  // 热力图按钮
  #heatmapbutton-div{
    position: absolute;
    margin-left: 10px;
    margin-top: 150px;
   /* padding: 5px;
    background: rgba(255, 255, 255, 0.6);
    line-height: 1;
    border-radius: 5px;*/
    /* white-space: pre; */
  }
  // 日期时间选择器
  #date{
    position: absolute;
    margin-left: 10px;
    margin-top: 0px;
  }
  // 地图选择按钮
  #mapselection{
    position: absolute;
    margin-left: 10px;
    margin-top: 100px;
  }
  // 地图选择下拉框
  #select{
    position: absolute;
    margin-top: 10px;
    margin-left: 80px;
  }
  // 电子围栏按钮
  #fence{
    position: absolute;
    margin-left: 10px;
    margin-top: 200px;
  }
  // 电子围栏信息
  #fence-info{
    position: absolute;
    right: 80px;
    margin-top: 30px;
    z-index: 3;
  }
  // 电子围栏特性
  #fence-characteristics{
    position: absolute;
    right: 80px;
    margin-top: 30px;
    z-index: 4;
  }
  .box-card {
    width: 250px;
    max-height: 300px;
    overflow: auto;
  }
  // 新增电子围栏信息
  #addfence-info{
    position: absolute;
    right: 80px;
    margin-top: 30px;
    z-index: 2;
  }
  .box-card-add{
    width: 250px;
    overflow: auto;
  }
  #station-div{
    position: absolute;
    margin-left: 10px;
    margin-top: 50px;
  }
  #video-div{
    position: absolute;
    margin-left: 10px;
    margin-top: 250px;
  }
  #cameras{
    position: absolute;
    right: 80px;
    margin-top: 20px;
    width: 300px;
    height: 200px;
    z-index: 1;
  }
</style>
