<template>
  <div style="width: 100%">
    <el-row>
      <el-col :span="2">
        <a-button type="primary" ghost @click="add" v-hasPermission="['station:add']">新增</a-button>
      </el-col>
    </el-row>
      <el-table
        :data="tableData"
        style="width: 100%">
        <el-table-column
          prop="mapId"
          label="基站所属地图Id"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="baseMac"
          label="基站Mac设备码"
          min-width="270">
        </el-table-column>
        <el-table-column
          prop="baseX"
          label="基站的所在X轴位置"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="baseY"
          label="基站的所在Y轴位置"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="baseZ"
          label="基站的所在Z轴位置"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="isMaster"
          label="主从基站"
          min-width="180">
        </el-table-column>
        <el-table-column
          label="操作"
          min-width="90">
          <template slot-scope="scope">
            <a-icon v-hasPermission="['station:update']" type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="handleedit(scope.row.baseMac)" title="修改基站"></a-icon> &nbsp;
            <a-icon v-hasPermission="['station:delete']" type="delete" theme="twoTone" twoToneColor="#4a9ff5" @click="handledelete(scope.row.baseMac)" title="删除基站"></a-icon>
          </template>
        </el-table-column>
      </el-table>
    <el-row style="background: white">
      <el-pagination align='center'
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="currentPage"
                     :page-sizes="[1,5,10,20]"
                     :page-size="pageSize"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="total">
      </el-pagination>
    </el-row>
    <el-drawer
      title="新增基站"
      :visible.sync="stationaddVisiable"
      :before-close="handleClose">
      <el-form label-width="150px" :model="formStationAdd">
        <el-form-item label="基站Mac设备码">
          <el-input v-model="formStationAdd.baseMac"></el-input>
        </el-form-item>
        <el-form-item label="基站的所在X轴位置">
          <el-input v-model="formStationAdd.baseX"></el-input>
        </el-form-item>
        <el-form-item label="基站的所在Y轴位置">
          <el-input v-model="formStationAdd.baseY"></el-input>
        </el-form-item>
        <el-form-item label="基站的所在Z轴位置">
          <el-input v-model="formStationAdd.baseZ"></el-input>
        </el-form-item>
        <el-form-item label="基站所属地图名称">
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in mapId"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="主从基站">
          <el-select v-model="typevalue" placeholder="请选择">
            <el-option
              v-for="item in stationtype"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-row style="position: fixed;bottom: 15px;right: -10px">
          <el-col :offset="16" :span="4" style="margin: 0 40px">
            <el-button @click="cancelForm">取 消</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="Submit">确 定</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-drawer>
    <el-drawer
      title="修改基站信息"
      :visible.sync="stationeditVisible"
      :before-close="handleClose3">
      <el-form label-width="150px" :model="edit">
        <el-form-item label="基站Mac设备码:">
          {{edit.Mac}}
        </el-form-item>
        <el-form-item label="基站的所在X轴位置">
          <el-input v-model="edit.baseX"></el-input>
        </el-form-item>
        <el-form-item label="基站的所在Y轴位置">
          <el-input v-model="edit.baseY"></el-input>
        </el-form-item>
        <el-form-item label="基站的所在Z轴位置">
          <el-input v-model="edit.baseZ"></el-input>
        </el-form-item>
        <el-form-item label="基站所属地图名称">
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in mapId"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="主从基站">
          <el-select v-model="typevalue" placeholder="请选择">
            <el-option
              v-for="item in stationtype"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-row style="position: fixed;bottom: 15px;right: -10px">
          <el-col :offset="16" :span="4" style="margin: 0 40px">
            <el-button @click="editcancelForm">取 消</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="Submit2">确 定</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-drawer>
  </div>
</template>

<script>
export default {
  name: 'station',
  data () {
    return {
      tableData: [],
      stationaddVisiable: false,
      stationeditVisible: false,
      formStationAdd: {
        baseMac: '',
        baseX: null,
        baseY: null,
        baseZ: null,
        isMaster: null
      },
      edit: {
        Mac: '',
        baseX: null,
        baseY: null,
        baseZ: null
      },
      stationtype: [{
        value: 1,
        label: '主基站'
      }, {
        value: 0,
        label: '从基站'
      }],
      typevalue: '',
      mapId: [],
      value: null,
      value2: null,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10
    }
  },
  mounted () {
    this.getStation()
    this.getMapId()
  },
  methods: {
    getStation () {
      this.$get('/map/stations').then(res => {
        this.total = res.data.total
        this.tableData = res.data.rows
        this.tableData.forEach(x => {
          if (x.isMaster === true) {
            x.isMaster = '主基站'
          } else if (x.isMaster === false) {
            x.isMaster = '从基站'
          }
        })
      })
    },
    getMapId () {
      this.$get('/map/ids').then(res => {
        this.mapId = res.data
      })
    },
    add () {
      this.stationaddVisiable = true
    },
    handledelete (baseMac) {
      this.$confirm('确定要删除该基站吗？')
        .then(_ => {
          this.$delete(`/map/stations/mac/${baseMac}`, {
            baseMac: baseMac
          }).then(_ => {
            this.getStation()
            this.$message({
              message: '删除成功'
            })
          })
        }).catch((e) => {
          console.error(e)
        })
    },
    handleedit (baseMac) {
      this.edit.Mac = baseMac
      this.stationeditVisible = true
    },
    // 新增基站
    handleClose (done) {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$post('/map/stations', {
            baseMac: this.formStationAdd.bmaseMac,
            baseX: this.formStationAdd.baseX,
            baseY: this.formStationAdd.baseY,
            baseZ: this.formStationAdd.baseZ,
            mapId: this.value,
            isMaster: this.typevalue
          }).then(res => {
            if (res.status === 200) {
              this.formStationAdd.baseMac = ''
              this.formStationAdd.baseX = null
              this.formStationAdd.baseY = null
              this.formStationAdd.baseZ = null
              this.typevalue = null
              this.getStation()
            }
            this.stationaddVisiable = false
          }).catch((e) => {
            this.stationaddVisiable = false
            console.error(e)
          })
        }).catch((e) => {
          this.stationaddVisiable = false
          console.error(e)
        })
    },
    // 修改基站
    handleClose3 (done) {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$put('map/stations', {
            baseMac: this.edit.Mac,
            baseX: this.edit.baseX,
            baseY: this.edit.baseY,
            baseZ: this.edit.baseZ,
            mapId: this.value,
            isMaster: this.typevalue
          }).then(res => {
            this.getStation()
          })
          this.stationeditVisible = false
        }).catch((e) => {
          this.stationeditVisible = false
          console.error(e)
        })
    },
    cancelForm () {
      this.stationaddVisiable = false
    },
    editcancelForm () {
      this.stationeditVisible = false
    },
    Submit () {
      this.$post('/map/stations', {
        baseMac: this.formStationAdd.baseMac,
        baseX: this.formStationAdd.baseX,
        baseY: this.formStationAdd.baseY,
        baseZ: this.formStationAdd.baseZ,
        mapId: this.value,
        isMaster: this.typevalue
      }).then(res => {
        if (res.status === 200) {
          this.formStationAdd.baseMac = ''
          this.formStationAdd.baseX = null
          this.formStationAdd.baseY = null
          this.formStationAdd.baseZ = null
          this.typevalue = null
          this.getStation()
        }
        this.stationaddVisiable = false
      }).catch((e) => {
        this.stationaddVisiable = false
        console.error(e)
      })
    },
    Submit2 () {
      this.$put('map/stations', {
        baseMac: this.edit.Mac,
        baseX: this.edit.baseX,
        baseY: this.edit.baseY,
        baseZ: this.edit.baseZ,
        mapId: this.value,
        isMaster: this.typevalue
      }).then(res => {
        this.edit.baseX = null
        this.edit.baseY = null
        this.edit.baseZ = null
        this.typevalue = null
        this.getStation()
        this.stationeditVisible = false
      }).catch((e) => {
        this.stationeditVisible = false
        console.error(e)
      })
    },
    handleSizeChange (val) {
      this.currentPage = this.currentPage
      this.pageSize = val
      this.$get(`/map/stations`, {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.tableData = res.data.rows
      })
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.$get(`/map/stations`, {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.tableData = res.data.rows
      })
    }
  }
}
</script>

<style scoped>

</style>
