<template>
    <div style="width: 100%">
      <el-row>
        <el-col :span="2">
          <a-button type="primary" ghost @click="add" v-hasPermission="['floor:add']">新增</a-button>
        </el-col>
      </el-row>
        <el-table
          :data="tableData"
          style="width: 100%">
          <el-table-column
            prop="floorId"
            label="楼层Id"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="floorName"
            label="房间名称"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="floorInfo"
            label="几号楼"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="buildId"
            label="建筑Id"
            min-width="180">
          </el-table-column>
          <el-table-column
            label="操作">
            <template slot-scope="scope">
              <a-icon v-hasPermission="['floor:update']" type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="handleedit(scope.row.floorId)" title="修改楼层"></a-icon> &nbsp;
              <a-icon v-hasPermission="['floor:delete']" type="delete" theme="twoTone" twoToneColor="#4a9ff5" @click="handledelete(scope.row.floorId)" title="删除楼层"></a-icon>
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
        title="新增地图"
        :visible.sync="flooraddVisiable"
        :before-close="handleaddClose">
        <el-form label-width="100px" :model="formfloorAdd">
          <el-form-item label="楼层Id">
            <el-input v-model="formfloorAdd.floorId"></el-input>
          </el-form-item>
          <el-form-item label="房间名称">
            <el-input v-model="formfloorAdd.floorName"></el-input>
          </el-form-item>
          <el-form-item label="几号楼">
            <el-input v-model="formfloorAdd.floorInfo"></el-input>
          </el-form-item>
          <el-form-item label="建筑Id">
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="item in builds"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-row style="position: fixed;bottom: 15px;right: -10px">
            <el-col :offset="16" :span="4" style="margin: 0 40px">
              <el-button @click="flooraddcancelForm">取 消</el-button>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="flooraddSubmit">确 定</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-drawer>
      <el-drawer
        title="修改基站信息"
        :visible.sync="flooreditVisiable"
        :before-close="handleClose2">
        <el-form label-width="100px" :model="edit">
          <el-form-item label="楼层Id">
            {{edit.floorId}}
          </el-form-item>
          <el-form-item label="房间名称">
            <el-input v-model="edit.floorName"></el-input>
          </el-form-item>
          <el-form-item label="几号楼">
            <el-input v-model="edit.floorInfo"></el-input>
          </el-form-item>
          <el-form-item label="建筑Id">
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="item in builds"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-row style="position: fixed;bottom: 15px;right: -10px">
            <el-col :offset="16" :span="4" style="margin: 0 40px">
              <el-button @click="flooreditcancelForm">取 消</el-button>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="flooreditSubmit">确 定</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-drawer>
    </div>
</template>

<script>
export default {
  name: 'floor',
  data () {
    return {
      flooraddVisiable: false,
      flooreditVisiable: false,
      tableData: [],
      builds: [],
      floorId: [],
      formfloorAdd: {
        floorId: null,
        floorName: '',
        floorInfo: ''
      },
      edit: {
        floorId: null,
        floorName: '',
        floorInfo: ''
      },
      value: null,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10
    }
  },
  mounted () {
    this.getfloor()
    this.getbuildId()
  },
  methods: {
    getfloor () {
      this.$get('/floors').then(res => {
        this.total = res.data.total
        this.tableData = res.data.rows
      })
    },
    getbuildId () {
      this.$get('/builds/ids').then(res => {
        this.builds = res.data
      })
    },
    // 新增楼层
    add () {
      this.flooraddVisiable = true
    },
    handleaddClose (done) {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$post('/floors', {
            floorId: this.formfloorAdd.floorId,
            floorName: this.formfloorAdd.floorName,
            floorInfo: this.formfloorAdd.floorInfo,
            buildId: this.value
          }).then(res => {
            if (res.status === 200) {
              this.formfloorAdd.floorId = null
              this.formfloorAdd.floorName = ''
              this.formfloorAdd.floorInfo = null
              this.value = null
              this.getfloor()
            }
            this.flooraddVisiable = false
          }).catch((e) => {
            this.flooraddVisiable = false
            console.error(e)
          })
        }).catch((e) => {
          this.flooraddVisiable = false
          console.error(e)
        })
    },
    flooraddcancelForm () {
      this.flooraddVisiable = false
    },
    flooraddSubmit () {
      this.$post('/floors', {
        floorId: this.formfloorAdd.floorId,
        floorName: this.formfloorAdd.floorName,
        floorInfo: this.formfloorAdd.floorInfo,
        buildId: this.value
      }).then(res => {
        if (res.status === 200) {
          this.formfloorAdd.floorId = null
          this.formfloorAdd.floorName = ''
          this.formfloorAdd.floorInfo = null
          this.value = null
          this.getfloor()
        }
        this.flooraddVisiable = false
      }).catch((e) => {
        this.flooraddVisiable = false
        console.error(e)
      })
    },
    // 修改楼层
    flooreditcancelForm () {
      this.flooreditVisiable = false
    },
    handleedit (floorId) {
      this.edit.floorId = floorId
      this.flooreditVisiable = true
    },
    handleClose2 () {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$put('/floors', {
            floorId: this.edit.floorId,
            floorName: this.edit.floorName,
            floorInfo: this.edit.floorInfo,
            buildId: this.value
          }).then(res => {
            this.edit.floorName = null
            this.edit.floorInfo = null
            this.value = null
            this.getfloor()
          })
          this.flooreditVisiable = false
        }).catch((e) => {
          this.flooreditVisiable = false
          console.error(e)
        })
    },
    flooreditSubmit () {
      this.$put('/floors', {
        floorId: this.edit.floorId,
        floorName: this.edit.floorName,
        floorInfo: this.edit.floorInfo,
        buildId: this.value
      }).then(res => {
        this.edit.floorName = null
        this.edit.floorInfo = null
        this.value = null
        this.getfloor()
        this.flooreditVisiable = false
      }).catch(e => {
        this.flooreditVisiable = false
        console.log(e)
      })
    },
    // 删除楼层
    handledelete (floorId) {
      this.$confirm('确定要删除该楼层吗？')
        .then(_ => {
          this.$delete(`/floors/${floorId}`, {
            floorId: floorId
          }).then(_ => {
            this.getfloor()
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
      this.$get(`/floors`, {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.tableData = res.data.rows
      })
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.$get(`/floors`, {
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
