<template>
    <div style="width: 100%">
      <el-row>
        <el-col :span="2">
          <a-button type="primary" ghost @click="add" v-hasPermission="['device:add']">新增</a-button>
        </el-col>
      </el-row>
        <el-table
          :data="tableData"
          style="width: 100%">
          <el-table-column
            prop="deviceMac"
            label="终端mac码"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="deviceType"
            label="终端的类型"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="userId"
            label="用户Id"
            min-width="120">
          </el-table-column>
          <el-table-column
            label="操作">
            <template slot-scope="scope">
              <a-icon v-hasPermission="['device:update']" type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="handleedit(scope.row.deviceMac)" title="修改园区"></a-icon> &nbsp;
              <a-icon v-hasPermission="['device:delete']" type="delete" theme="twoTone" twoToneColor="#4a9ff5" @click="handledelete(scope.row.deviceMac)" title="删除园区"></a-icon>
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
        title="新增园区"
        :visible.sync="deviceaddVisiable"
        :before-close="handleaddClose">
        <el-form label-width="120px" :model="formdeviceAdd">
          <el-form-item label="终端mac码">
            <el-input v-model="formdeviceAdd.deviceMac"></el-input>
          </el-form-item>
          <el-form-item label="终端的类型">
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="item in deviceType"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户Id">
            <el-input v-model="formdeviceAdd.userId"></el-input>
          </el-form-item>
          <el-row style="position: fixed;bottom: 15px;right: -10px">
            <el-col :offset="16" :span="4" style="margin: 0 40px">
              <el-button @click="deviceaddcancelForm">取 消</el-button>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="deviceaddSubmit">确 定</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-drawer>
      <el-drawer
        title="修改基站信息"
        :visible.sync="deviceeditVisible"
        :before-close="handleClose2">
        <el-form label-width="150px" :model="edit">
          <el-form-item label="终端mac码:">
            {{edit.deviceMac}}
          </el-form-item>
          <el-form-item label="终端的类型">
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="item in deviceType"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户Id">
            <el-input v-model="edit.userId"></el-input>
          </el-form-item>
          <el-row style="position: fixed;bottom: 15px;right: -10px">
            <el-col :offset="16" :span="4" style="margin: 0 40px">
              <el-button @click="deviceeditcancelForm">取 消</el-button>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="deviceeditSubmit">确 定</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-drawer>
    </div>
</template>

<script>
export default {
  name: 'device',
  data () {
    return {
      deviceaddVisiable: false,
      deviceeditVisible: false,
      tableData: [],
      formdeviceAdd: {
        deviceMac: '',
        userId: ''
      },
      edit: {
        deviceMac: '',
        userId: ''
      },
      deviceType: [{
        value: 0,
        label: '工牌'
      }, {
        value: 1,
        label: '手环'
      }, {
        value: 2,
        label: '手表'
      }],
      value: null,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10
    }
  },
  mounted () {
    this.getdevice()
  },
  methods: {
    getdevice () {
      this.$get('/devices').then(res => {
        this.total = res.data.total
        this.tableData = res.data.rows
        this.tableData.forEach(x => {
          if (x.deviceType === 0) {
            x.deviceType = '工牌'
          } else if (x.deviceType === 1) {
            x.deviceType = '手环'
          } else if (x.deviceType === 2) {
            x.deviceType = '手表'
          }
        })
      })
    },
    // 新增终端
    add () {
      this.deviceaddVisiable = true
    },
    deviceaddcancelForm () {
      this.deviceaddVisiable = false
    },
    handleaddClose () {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$post('/devices', {
            deviceMac: this.formdeviceAdd.deviceMac,
            deviceType: this.value,
            userId: this.formdeviceAdd.userId
          }).then(res => {
            if (res.status === 200) {
              this.formdeviceAdd.deviceMac = null
              this.value = ''
              this.formdeviceAdd.userId = ''
              this.getdevice()
            }
            this.deviceaddVisiable = false
          }).catch((e) => {
            this.deviceaddVisiable = false
            console.error(e)
          })
        }).catch((e) => {
          this.deviceaddVisiable = false
          console.error(e)
        })
    },
    deviceaddSubmit () {
      this.$post('/devices', {
        deviceMac: this.formdeviceAdd.deviceMac,
        deviceType: this.value,
        userId: this.formdeviceAdd.userId
      }).then(res => {
        if (res.status === 200) {
          this.formdeviceAdd.deviceMac = null
          this.value = ''
          this.formdeviceAdd.userId = ''
          this.getdevice()
        }
        this.deviceaddVisiable = false
      }).catch((e) => {
        this.deviceaddVisiable = false
        console.error(e)
      })
    },
    // 修改终端
    deviceeditcancelForm () {
      this.deviceeditVisible = false
    },
    handleedit (deviceMac) {
      this.edit.deviceMac = deviceMac
      this.deviceeditVisible = true
    },
    handleClose2 () {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$put('/devices', {
            deviceMac: this.edit.deviceMac,
            deviceType: this.value,
            userId: this.edit.userId
          }).then(res => {
            this.value = null
            this.edit.userId = null
            this.getdevice()
          })
          this.deviceeditVisible = false
        }).catch((e) => {
          this.deviceeditVisible = false
          console.error(e)
        })
    },
    deviceeditSubmit () {
      this.$put('/devices', {
        deviceMac: this.edit.deviceMac,
        deviceType: this.value,
        userId: this.edit.userId
      }).then(res => {
        this.value = null
        this.edit.userId = null
        this.getdevice()
        this.deviceeditVisible = false
      }).catch((e) => {
        this.deviceeditVisible = false
        console.error(e)
      })
    },
    // 删除终端
    handledelete (deviceMac) {
      this.$confirm('确定要删除该楼层吗？')
        .then(_ => {
          this.$delete(`/devices/${deviceMac}`, {
            deviceMac: deviceMac
          }).then(_ => {
            this.getdevice()
            this.$message({
              message: '删除成功'
            })
          })
        }).catch((e) => {
          console.error(e)
        })
    },
    // 分页
    handleSizeChange (val) {
      this.currentPage = this.currentPage
      this.pageSize = val
      this.$get(`/devices`, {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.tableData = res.data.rows
        this.tableData.forEach(x => {
          if (x.deviceType === 0) {
            x.deviceType = '工牌'
          } else if (x.deviceType === 1) {
            x.deviceType = '手环'
          } else if (x.deviceType === 2) {
            x.deviceType = '手表'
          }
        })
      })
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.$get(`/devices`, {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.tableData = res.data.rows
        this.tableData.forEach(x => {
          if (x.deviceType === 0) {
            x.deviceType = '工牌'
          } else if (x.deviceType === 1) {
            x.deviceType = '手环'
          } else if (x.deviceType === 2) {
            x.deviceType = '手表'
          }
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
