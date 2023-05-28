<template>
    <div style="width: 100%">
      <el-row>
        <el-col :span="2">
          <a-button type="primary" ghost @click="add" v-hasPermission="['map:add']">新增</a-button>
        </el-col>
      </el-row>
      <el-row>
        <el-table
          :data="tableData"
          style="width: 100%">
          <el-table-column
            prop="mapId"
            label="地图Id"
            min-width="120">
          </el-table-column>
          <el-table-column
            prop="mapName"
            label="地图名称"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="mapX"
            label="地图所在X轴位置"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="mapY"
            label="地图所在Y轴位置"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="mapZ"
            label="地图所在Z轴位置"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="mapFile"
            label="地图所用的json文件"
            min-width="180">
          </el-table-column>
          <el-table-column
            prop="floorId"
            label="所在的楼层"
            min-width="180">
          </el-table-column>
          <el-table-column
            label="操作">
            <template slot-scope="scope">
              <a-icon v-hasPermission="['map:update']" type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="handleedit(scope.row.mapId)" title="修改地图"></a-icon> &nbsp;
              <a-icon v-hasPermission="['map:delete']" type="delete" theme="twoTone" twoToneColor="#4a9ff5" @click="handledelete(scope.row.mapId)" title="删除地图"></a-icon>
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
      </el-row>
      <el-drawer
        title="新增地图"
        :visible.sync="mapaddVisiable"
        :before-close="handleaddClose">
        <el-form label-width="150px" :model="formMapAdd">
          <el-form-item label="地图Id">
            <el-input v-model="formMapAdd.mapId"></el-input>
          </el-form-item>
          <el-form-item label="地图名称">
            <el-input v-model="formMapAdd.mapName"></el-input>
          </el-form-item>
          <el-form-item label="地图所在X轴位置">
            <el-input v-model="formMapAdd.mapX"></el-input>
          </el-form-item>
          <el-form-item label="地图所在Y轴位置">
            <el-input v-model="formMapAdd.mapY"></el-input>
          </el-form-item>
          <el-form-item label="地图所在Z轴位置">
            <el-input v-model="formMapAdd.mapZ"></el-input>
          </el-form-item>
          <el-form-item label="地图所属的json文件">
            <el-input v-model="formMapAdd.mapFile"></el-input>
          </el-form-item>
          <el-form-item label="地图所属楼层">
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="item in floorId"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-row style="position: fixed;bottom: 15px;right: -10px">
            <el-col :offset="16" :span="4" style="margin: 0 40px">
              <el-button @click="mapaddcancelForm">取 消</el-button>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="mapaddSubmit">确 定</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-drawer>
      <el-drawer
        title="修改基站信息"
        :visible.sync="mapeditVisiable"
        :before-close="handleClose2">
        <el-form label-width="150px" :model="edit">
          <el-form-item label="地图Id">
            {{edit.mapId}}
          </el-form-item>
          <el-form-item label="地图名称">
            <el-input v-model="edit.mapName"></el-input>
          </el-form-item>
          <el-form-item label="地图所在X轴位置">
            <el-input v-model="edit.mapX"></el-input>
          </el-form-item>
          <el-form-item label="地图所在Y轴位置">
            <el-input v-model="edit.mapY"></el-input>
          </el-form-item>
          <el-form-item label="地图所在Z轴位置">
            <el-input v-model="edit.mapZ"></el-input>
          </el-form-item>
          <el-form-item label="地图所属的json文件">
            <el-input v-model="edit.mapFile"></el-input>
          </el-form-item>
          <el-form-item label="地图所属楼层">
            <el-select v-model="value" placeholder="请选择">
              <el-option
                v-for="item in floorId"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-row style="position: fixed;bottom: 15px;right: -10px">
            <el-col :offset="16" :span="4" style="margin: 0 40px">
              <el-button @click="mapeditcancelForm">取 消</el-button>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="mapeditSubmit">确 定</el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-drawer>
    </div>
</template>

<script>
export default {
  name: 'map',
  data () {
    return {
      tableData: [],
      mapaddVisiable: false,
      mapeditVisiable: false,
      formMapAdd: {
        mapId: null,
        mapName: '',
        mapX: null,
        mapY: null,
        mapZ: null,
        mapFile: ''
      },
      edit: {
        mapId: null,
        mapName: '',
        mapX: null,
        mapY: null,
        mapZ: null,
        mapFile: ''
      },
      floorId: [],
      value: null,
      currentPage: 1, // 当前页码
      total: 20, // 总条数
      pageSize: 10
    }
  },
  mounted () {
    this.getMap()
    this.getfloorId()
  },
  methods: {
    getMap () {
      this.$get('/map').then(res => {
        this.total = res.data.total
        this.tableData = res.data.rows
      })
    },
    getfloorId () {
      this.$get('/floors/ids').then(res => {
        this.floorId = res.data
      })
    },
    add () {
      this.mapaddVisiable = true
    },
    // 新增地图
    handleaddClose (done) {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$post('/map', {
            mapId: this.formMapAdd.mapId,
            mapName: this.formMapAdd.mapName,
            mapX: this.formMapAdd.mapX,
            mapY: this.formMapAdd.mapY,
            mapZ: this.formMapAdd.mapZ,
            mapFile: this.formMapAdd.mapFile,
            floorId: this.value
          }).then(res => {
            if (res.status === 200) {
              this.formMapAdd.mapId = null
              this.formMapAdd.mapName = ''
              this.formMapAdd.mapX = null
              this.formMapAdd.mapY = null
              this.formMapAdd.mapZ = null
              this.formMapAdd.mapFile = ''
              this.value = null
              this.getMap()
            }
            this.mapaddVisiable = false
          }).catch((e) => {
            this.mapaddVisiable = false
            console.error(e)
          })
        }).catch((e) => {
          this.mapaddVisiable = false
          console.error(e)
        })
    },
    mapaddcancelForm () {
      this.mapaddVisiable = false
    },
    mapaddSubmit () {
      this.$post('/map', {
        mapId: this.formMapAdd.mapId,
        mapName: this.formMapAdd.mapName,
        mapX: this.formMapAdd.mapX,
        mapY: this.formMapAdd.mapY,
        mapZ: this.formMapAdd.mapZ,
        mapFile: this.formMapAdd.mapFile,
        floorId: this.value
      }).then(res => {
        if (res.status === 200) {
          this.formMapAdd.mapId = null
          this.formMapAdd.mapName = ''
          this.formMapAdd.mapX = null
          this.formMapAdd.mapY = null
          this.formMapAdd.mapZ = null
          this.formMapAdd.mapFile = ''
          this.value = null
          this.getMap()
        }
        this.mapaddVisiable = false
      }).catch((e) => {
        this.mapaddVisiable = false
        console.error(e)
      })
    },
    // 修改地图信息
    mapeditcancelForm () {
      this.mapeditVisiable = false
    },
    handleedit (mapId) {
      this.edit.mapId = mapId
      this.mapeditVisiable = true
    },
    handleClose2 () {
      this.$confirm('确定要提交表单吗？')
        .then(_ => {
          this.$put('/map', {
            mapId: this.edit.mapId,
            mapName: this.edit.mapName,
            mapX: this.edit.mapX,
            mapY: this.edit.mapY,
            mapZ: this.edit.mapZ,
            mapFile: this.edit.mapFile,
            floorId: this.value
          }).then(res => {
            this.edit.mapId = null
            this.edit.mapName = ''
            this.edit.mapX = null
            this.edit.mapY = null
            this.edit.mapZ = null
            this.edit.mapFile = ''
            this.value = null
            this.getMap()
          })
          this.mapeditVisiable = false
        }).catch((e) => {
          this.mapeditVisiable = false
          console.error(e)
        })
    },
    mapeditSubmit () {
      this.$put('/map', {
        mapId: this.edit.mapId,
        mapName: this.edit.mapName,
        mapX: this.edit.mapX,
        mapY: this.edit.mapY,
        mapZ: this.edit.mapZ,
        mapFile: this.edit.mapFile,
        floorId: this.value
      }).then(res => {
        this.edit.mapId = null
        this.edit.mapName = ''
        this.edit.mapX = null
        this.edit.mapY = null
        this.edit.mapZ = null
        this.edit.mapFile = ''
        this.value = null
        this.getMap()
        this.mapeditVisiable = false
      }).catch(e => {
        this.mapeditVisiable = false
        console.error(e)
      })
    },
    // 删除地图
    handledelete (mapId) {
      this.$confirm('确定要删除该地图吗？')
        .then(_ => {
          this.$delete(`/map/${mapId}`, {
            mapId: mapId
          }).then(_ => {
            this.getMap()
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
      this.$get(`/map`, {
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.tableData = res.data.rows
      })
    },
    handleCurrentChange (val) {
      this.currentPage = val
      this.$get(`/map`, {
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
