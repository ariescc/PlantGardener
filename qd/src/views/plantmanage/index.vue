<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        添加植物
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 20px;"
    >
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="植物名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="科属" align="center">
        <template slot-scope="{row}">
          <span>{{ row.plantType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生长环境" align="center">
        <template slot-scope="{row}">
          <span>{{ row.environment }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button size="mini" type="success" @click="handleDetail(row)">
            详情
          </el-button>
          <el-popconfirm
            title="确认删除此条植物信息？"
            @onConfirm="handleDelete(row)"
          >
            <el-button slot="reference" size="mini" type="danger">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="plantForm" label-position="left" label-width="100px" style="width: 500px%; margin-left:50px;">
        <el-form-item label="植株美照">
          <img :src="plantForm.picture" class="avatar">
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="plantForm.name" />
        </el-form-item>
        <el-form-item label="科属">
          <el-input v-model="plantForm.plantType" />
        </el-form-item>
        <el-form-item label="生长环境">
          <el-input v-model="plantForm.environment" />
        </el-form-item>
        <el-form-item label="介绍">
          <el-input v-model="plantForm.description" :autosize="{ minRows: 2, maxRows: 6}" type="textarea" placeholder="Please input" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          保存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'PlantManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      statusOptions: ['published', 'draft', 'deleted'],
      plantForm: { // 植物信息表单
        name: '',
        plantType: '',
        environment: '',
        description: '',
        picture: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '植物详情',
        create: '登记植物信息'
      },
      downloadLoading: false,
      page: 1,
      limit: 20,
      sort: 'asc'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() { // 获取所有植物信息
      this.listLoading = true
      this.$axios
        .get('/plant/list', {
          params: {
            page: this.page,
            limit: this.limit,
            sort: 'asc'
          }
        })
        .then(resp => {
          if (resp.data.code === 200) {
            console.log(resp)
            this.list = []
            resp.data.result.items.forEach(plant => {
              this.list.push({
                id: plant.id,
                name: plant.name,
                plantType: plant.plantType,
                environment: plant.environment,
                description: plant.description,
                picture: plant.picture
              })
            })
            this.total = resp.data.result.total
            this.listLoading = false
          } else {
            this.listLoading = false
          }
        })
        .catch(failResp => {
          console.log(failResp)
          this.listLoading = false
        })
    },
    resetPlantForm() {
      this.plantForm = { // 植物信息表单
        name: '',
        plantType: '',
        environment: '',
        description: '',
        picture: ''
      }
    },
    handleCreate() {
      this.resetPlantForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      this.$axios
        .post('/plant/add', {
          name: this.plantForm.name,
          plantType: this.plantForm.plantType,
          environment: this.plantForm.environment,
          description: this.plantForm.description,
          picture: this.plantForm.picture
        })
        .then(resp => {
          console.log(resp)
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: '新增成功！',
              showClose: true
            })
          }
        })
    },
    handleDetail(row) {
      this.$axios.get('/plantInfo/' + row.id).then(resp => {
        console.log(resp)
        if (resp && resp.data.code === 200) {
          this.plantForm.id = resp.data.result.id
          this.plantForm.name = resp.data.result.name
          this.plantForm.plantType = resp.data.result.plantType
          this.plantForm.environment = resp.data.result.environment
          this.plantForm.description = resp.data.result.description
          this.plantForm.picture = resp.data.result.picture
        }
      })
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    updateData() {
    },
    handleDelete(row) {
      this.$axios
        .post('/plant/del', {
          id: row.id
        })
        .then(resp => {
          console.log(resp)
          if (resp && resp.data.code === 200) {
            this.$notify({
              title: 'Success',
              message: `${row.name} 删除成功！`,
              type: 'success',
              duration: 2000
            })
            // 重新请求第一页内容
            this.getList()
          }
        })
        .catch(failResp => {
          console.log(failResp)
        })
    }
  }
}
</script>
