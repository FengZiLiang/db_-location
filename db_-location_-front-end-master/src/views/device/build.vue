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
          prop="buildId"
          label="建筑Id"
          min-width="120">
        </el-table-column>
        <el-table-column
          prop="buildName"
          label="建筑名字"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="buildInfo"
          label="建筑信息"
          min-width="180">
        </el-table-column>
        <el-table-column
          prop="parkId"
          label="建筑所属园区ID"
          min-width="180">
        </el-table-column>
        <el-table-column
          label="操作">
          <template slot-scope="scope">
            <a-icon v-hasPermission="['build:update']" type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="handleedit(scope.row.buildId)" title="修改建筑"></a-icon> &nbsp;
            <a-icon v-hasPermission="['build:delete']" type="delete" theme="twoTone" twoToneColor="#4a9ff5" @click="handledelete(scope.row.buildId)" title="删除建筑"></a-icon>
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
      title="新增建筑"
      :visible.sync="buildaddVisiable"
      :before-close="handleaddClose">
      <el-form label-width="120px" :model="formbuildAdd">
        <el-form-item label="建筑Id">
          <el-input v-model="formbuildAdd.buildId"></el-input>
        </el-form-item>
        <el-form-item label="建筑名字">
          <el-input v-model="formbuildAdd.buildName"></el-input>
        </el-form-item>
        <el-form-item label="建筑信息">
          <el-input v-model="formbuildAdd.buildInfo"></el-input>
        </el-form-item>
        <el-form-item label="建筑所属园区ID">
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in parks"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-row style="position: fixed;bottom: 15px;right: -10px">
          <el-col :offset="16" :span="4" style="margin: 0 40px">
            <el-button @click="buildaddcancelForm">取 消</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="buildaddSubmit">确 定</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-drawer>
    <el-drawer
      title="修改基站信息"
      :visible.sync="buildeditVisiable"
      :before-close="handleClose2">
      <el-form label-width="120px" :model="edit">
        <el-form-item label="建筑Id">
          {{edit.buildId}}
        </el-form-item>
        <el-form-item label="建筑名字">
          <el-input v-model="edit.buildName"></el-input>
        </el-form-item>
        <el-form-item label="建筑信息">
          <el-input v-model="edit.buildInfo"></el-input>
        </el-form-item>
        <el-form-item label="建筑所属园区ID">
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in parks"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-row style="position: fixed;bottom: 15px;right: -10px">
          <el-col :offset="16" :span="4" style="margin: 0 40px">
            <el-button @click="buildeditcancelForm">取 消</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="buildeditSubmit">确 定</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-drawer>
  </div>
</template>

<script>
export default {
  name: 'build',
  data () {
    return {
      buildaddVisiable: false,
      buildeditVisiable: false,
      tableData: [],
      parks: [],
      formbuildAdd: {
        buildId: null,
        buildName: '',
        buildInfo: ''
      },
      edit: {
        buildId: null,
        buildName: '',
        buildInfo: ''
      },
      value: null,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10
    }
  },
  mounted () {
    this.getbuild()
    this.getparkId()
  },
  methods: {
    getbuild () {
      this.$get('/builds').then(res => {
        this.total = res.data.total
        this.tableData = res.data.rows
      })
    },
    getparkId () {
      this.$get('/parks/ids').then(res => {
        this.parks = res.data
      })
    },
    //  新增建筑
    add () {
      this.buildaddVisiable = true
    },
    buildaddcancelForm () {
      this.buildaddVisiable = false
    },
    handleaddClose () {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$post('/builds', {
            buildId: this.formbuildAdd.buildId,
            buildName: this.formbuildAdd.buildName,
            buildInfo: this.formbuildAdd.buildInfo,
            parkId: this.value
          }).then(res => {
            if (res.status === 200) {
              this.formbuildAdd.buildId = null
              this.formbuildAdd.buildName = ''
              this.formbuildAdd.buildInfo = null
              this.value = null
              this.getbuild()
            }
            this.buildaddVisiable = false
          }).catch((e) => {
            this.buildaddVisiable = false
            console.error(e)
          })
        }).catch((e) => {
          this.buildaddVisiable = false
          console.error(e)
        })
    },
    buildaddSubmit () {
      this.$post('/builds', {
        buildId: this.formbuildAdd.buildId,
        buildName: this.formbuildAdd.buildName,
        buildInfo: this.formbuildAdd.buildInfo,
        parkId: this.value
      }).then(res => {
        if (res.status === 200) {
          this.formbuildAdd.buildId = null
          this.formbuildAdd.buildName = ''
          this.formbuildAdd.buildInfo = null
          this.value = null
          this.getbuild()
        }
        this.buildaddVisiable = false
      }).catch((e) => {
        this.buildaddVisiable = false
        console.error(e)
      })
    },
    // 修改建筑
    buildeditcancelForm () {
      this.buildeditVisiable = false
    },
    handleedit (buildId) {
      this.edit.buildId = buildId
      this.buildeditVisiable = true
    },
    handleClose2 () {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$put('/builds', {
            buildId: this.edit.buildId,
            buildName: this.edit.buildName,
            buildInfo: this.edit.buildInfo,
            parkId: this.value
          }).then(res => {
            this.edit.buildName = null
            this.edit.buildInfo = null
            this.value = null
            this.getbuild()
          })
          this.buildeditVisiable = false
        }).catch((e) => {
          this.buildeditVisiable = false
          console.error(e)
        })
    },
    buildeditSubmit () {
      this.$put('/builds', {
        buildId: this.edit.buildId,
        buildName: this.edit.buildName,
        buildInfo: this.edit.buildInfo,
        parkId: this.value
      }).then(res => {
        this.edit.buildName = null
        this.edit.buildInfo = null
        this.value = null
        this.getbuild()
        this.buildeditVisiable = false
      }).catch((e) => {
        this.buildeditVisiable = false
        console.error(e)
      })
    },
    // 删除建筑
    handledelete (buildId) {
      this.$confirm('确定要删除该楼层吗？')
        .then(_ => {
          this.$delete(`/builds/${buildId}`, {
            buildId: buildId
          }).then(_ => {
            this.getbuild()
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
      this.$get(`/builds`, {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.tableData = res.data.rows
      })
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.$get(`/builds`, {
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
