<template>
  <div class="app-container">
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
      <el-table-column label="岗位名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.postName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="照顾植物类别" align="center">
        <template slot-scope="{row}">
          <span>{{ row.plantName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期望性别" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sex }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工作年限" align="center">
        <template slot-scope="{row}">
          <span>{{ row.workYears }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工作地点" align="center">
        <template slot-scope="{row}">
          <span>{{ row.workAddress }}</span>
        </template>
      </el-table-column>
      <el-table-column label="薪酬" align="center">
        <template slot-scope="{row}">
          <span>{{ row.reward }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button size="mini" type="success" @click="handleCreate(row)">
            申请
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="recruitmentForm" label-position="left" label-width="100px" style="width: 500px%; margin-left:50px;">
        <el-form-item label="岗位名称">
          <el-input v-model="recruitmentForm.postName" />
        </el-form-item>
        <el-form-item label="照顾植物">
          <el-input v-model="recruitmentForm.plantName" />
        </el-form-item>
        <el-form-item label="性别要求">
          <el-input v-model="recruitmentForm.sex" />
        </el-form-item>
        <el-form-item label="工作年限">
          <el-input v-model="recruitmentForm.workYears" />
        </el-form-item>
        <el-form-item label="工作地点">
          <el-input v-model="recruitmentForm.workAddress" />
        </el-form-item>
        <el-form-item label="薪酬">
          <el-input v-model="recruitmentForm.reward" />
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
  name: 'ApplyPost', // 申请岗位
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
      recruitmentForm: { // 招聘信息表单
        postName: '',
        plantName: '',
        sex: '',
        workYears: '',
        reward: '',
        workAddress: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '岗位详情',
        create: '发布招聘信息'
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
        .get('/recruitment/list', {
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
            resp.data.result.items.forEach(recruitment => {
              this.list.push({
                id: recruitment.id,
                postName: recruitment.postName,
                plantName: recruitment.plantName,
                sex: recruitment.sex,
                workYears: recruitment.workYears,
                reward: recruitment.reward,
                workAddress: recruitment.workAddress
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
    resetRecruitmentForm() {
      this.recruitmentForm = {
        postName: '',
        plantName: '',
        sex: '',
        workYears: '',
        reward: '',
        workAddress: ''
      }
    },
    handleCreate(row) { // 申请岗位
      this.$axios
        .post('/applyStatus/add', {
          id: row.id
        })
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: '申请成功！',
              showClose: true
            })
          }
        })
    },
    createData() {
      this.$axios
        .post('/recruitment/add', {
          postName: this.recruitment.postName,
          plantName: this.recruitment.plantName,
          sex: this.recruitment.sex,
          workYears: this.recruitment.workYears,
          reward: this.recruitment.reward,
          workAddress: this.recruitment.workAddress
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
      this.$axios.get('/recruitment/' + row.id).then(resp => {
        console.log(resp)
        if (resp && resp.data.code === 200) {
          this.recruitment.id = resp.data.result.id
          this.recruitment.postName = resp.data.result.postName
          this.recruitment.plantName = resp.data.result.plantName
          this.recruitment.sex = resp.data.result.sex
          this.recruitment.workYears = resp.data.result.workYears
          this.recruitment.reward = resp.data.result.reward
          this.recruitment.workAddress = resp.data.result.workAddress
        }
      })
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    updateData() {
    },
    handleDelete(row) {
      this.$axios
        .post('/recruitment/del', {
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
