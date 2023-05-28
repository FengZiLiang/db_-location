<template>
    <div style="width: 100%">
      <el-row>
        <el-col :span="2">
          <a-button type="primary" ghost @click="add" v-hasPermission="['build:add']">新增</a-button>
        </el-col>
      </el-row>
        <el-table
          :data="tableData"
          style="width: 100%">
          <el-table-column
            prop="parkId"
            label="园区Id"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="parkName"
            label="园区名字"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="parkProvince"
            label="园区所在省份"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="parkCity"
            label="园区所在城市"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="parkAddress"
            label="园区详细地址"
            min-width="270">
          </el-table-column>
          <el-table-column
            prop="parkInfo"
            label="园区信息"
            min-width="120">
          </el-table-column>
          <el-table-column
            label="操作">
            <template slot-scope="scope">
              <a-icon v-hasPermission="['build:update']" type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="handleedit(scope.row.parkId)" title="修改园区"></a-icon> &nbsp;
              <a-icon v-hasPermission="['build:delete']" type="delete" theme="twoTone" twoToneColor="#4a9ff5" @click="handledelete(scope.row.parkId)" title="删除园区"></a-icon>
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
        :visible.sync="parkaddVisiable"
        :before-close="handleaddClose">
        <el-form label-width="120px" :model="formparkAdd">
          <el-form-item label="园区Id">
            <el-input v-model="formparkAdd.parkId"></el-input>
          </el-form-item>
          <el-form-item label="园区名字">
            <el-input v-model="formparkAdd.parkName"></el-input>
          </el-form-item>
          <el-form-item label="园区所在省份">
            <el-input v-model="formparkAdd.parkProvince"></el-input>
          </el-form-item>
          <el-form-item label="园区所在城市">
            <el-input v-model="formparkAdd.parkCity"></el-input>
          </el-form-item>
          <el-form-item label="园区详细地址">
            <el-input v-model="formparkAdd.parkAddress"></el-input>
          </el-form-item>
          <el-form-item label="园区信息">
            <el-input v-model="formparkAdd.parkInfo"></el-input>
          </el-form-item>
          <el-row style="position: fixed;bottom: 15px;right: -10px">
            <el-col :offset="16" :span="4" style="margin: 0 40px">
              <el-button @click="parkaddcancelForm">取 消</el-button>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="parkaddSubmit">确 定</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-drawer>
      <el-drawer
        title="修改园区信息"
        :visible.sync="parkeditVisiable"
        :before-close="handleClose2">
        <el-form label-width="120px" :model="edit">
          <el-form-item label="园区Id">
            {{edit.parkId}}
          </el-form-item>
          <el-form-item label="园区名字">
            <el-input v-model="edit.parkName"></el-input>
          </el-form-item>
          <el-form-item label="园区所在省份">
            <el-input v-model="edit.parkProvince"></el-input>
          </el-form-item>
          <el-form-item label="园区所在城市">
            <el-input v-model="edit.parkCity"></el-input>
          </el-form-item>
          <el-form-item label="园区详细地址">
            <el-input v-model="edit.parkAddress"></el-input>
          </el-form-item>
          <el-form-item label="园区信息">
            <el-input v-model="edit.parkInfo"></el-input>
          </el-form-item>
          <el-row style="position: fixed;bottom: 15px;right: -10px">
            <el-col :offset="16" :span="4" style="margin: 0 40px">
              <el-button @click="parkeditcancelForm">取 消</el-button>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="parkeditSubmit">确 定</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-drawer>
    </div>
</template>

<script>
export default {
  name: 'park',
  data () {
    return {
      parkaddVisiable: false,
      parkeditVisiable: false,
      tableData: [],
      formparkAdd: {
        parkId: null,
        parkName: '',
        parkProvince: '',
        parkCity: '',
        parkAddress: '',
        parkInfo: ''
      },
      edit: {
        parkId: null,
        parkName: '',
        parkProvince: '',
        parkCity: '',
        parkAddress: '',
        parkInfo: ''
      },
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10
    }
  },
  mounted () {
    this.getpark()
  },
  methods: {
    getpark () {
      this.$get('/parks').then(res => {
        this.total = res.data.total
        this.tableData = res.data.rows
      })
    },
    // 新增园区
    add () {
      this.parkaddVisiable = true
    },
    parkaddcancelForm () {
      this.parkaddVisiable = false
    },
    handleaddClose () {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$post('/parks', {
            parkId: this.formparkAdd.parkId,
            parkName: this.formparkAdd.parkName,
            parkProvince: this.formparkAdd.parkProvince,
            parkCity: this.formparkAdd.parkCity,
            parkAddress: this.formparkAdd.parkAddress,
            parkInfo: this.formparkAdd.parkInfo
          }).then(res => {
            if (res.status === 200) {
              this.formparkAdd.parkId = null
              this.formparkAdd.parkName = ''
              this.formparkAdd.parkProvince = ''
              this.formparkAdd.parkCity = ''
              this.formparkAdd.parkAddress = ''
              this.formparkAdd.parkInfo = ''
              this.getpark()
            }
            this.parkaddVisiable = false
          }).catch((e) => {
            this.parkaddVisiable = false
            console.error(e)
          })
        }).catch((e) => {
          this.parkaddVisiable = false
          console.error(e)
        })
    },
    parkaddSubmit () {
      this.$post('/parks', {
        parkId: this.formparkAdd.parkId,
        parkName: this.formparkAdd.parkName,
        parkProvince: this.formparkAdd.parkProvince,
        parkCity: this.formparkAdd.parkCity,
        parkAddress: this.formparkAdd.parkAddress,
        parkInfo: this.formparkAdd.parkInfo
      }).then(res => {
        if (res.status === 200) {
          this.formparkAdd.parkId = null
          this.formparkAdd.parkName = ''
          this.formparkAdd.parkProvince = ''
          this.formparkAdd.parkCity = ''
          this.formparkAdd.parkAddress = ''
          this.formparkAdd.parkInfo = ''
          this.getpark()
        }
        this.parkaddVisiable = false
      }).catch((e) => {
        this.parkaddVisiable = false
        console.error(e)
      })
    },
    // 修改园区
    parkeditcancelForm () {
      this.parkeditVisiable = false
    },
    handleedit (parkId) {
      this.edit.parkId = parkId
      this.parkeditVisiable = true
    },
    handleClose2 () {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$put('/parks', {
            parkId: this.edit.parkId,
            parkName: this.edit.parkName,
            parkProvince: this.edit.parkProvince,
            parkCity: this.edit.parkCity,
            parkAddress: this.edit.parkAddress,
            parkInfo: this.edit.parkInfo
          }).then(res => {
            if (res.status === 200) {
              this.edit.parkName = ''
              this.edit.parkProvince = ''
              this.edit.parkCity = ''
              this.edit.parkAddress = ''
              this.edit.parkInfo = ''
              this.getpark()
            }
          })
          this.parkeditVisiable = false
        }).catch((e) => {
          this.parkeditVisiable = false
          console.error(e)
        })
    },
    parkeditSubmit () {
      this.$put('/parks', {
        parkId: this.edit.parkId,
        parkName: this.edit.parkName,
        parkProvince: this.edit.parkProvince,
        parkCity: this.edit.parkCity,
        parkAddress: this.edit.parkAddress,
        parkInfo: this.edit.parkInfo
      }).then(res => {
        if (res.status === 200) {
          this.edit.parkName = ''
          this.edit.parkProvince = ''
          this.edit.parkCity = ''
          this.edit.parkAddress = ''
          this.edit.parkInfo = ''
          this.getpark()
        }
        this.parkeditVisiable = false
      }).catch((e) => {
        this.parkeditVisiable = false
        console.error(e)
      })
    },
    // 删除园区
    handledelete (parkId) {
      this.$confirm('确定要删除该楼层吗？')
        .then(_ => {
          this.$delete(`/parks/${parkId}`, {
            parkId: parkId
          }).then(_ => {
            this.getpark()
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
      this.$get(`/parks`, {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.tableData = res.data.rows
      })
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.$get(`/parks`, {
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
