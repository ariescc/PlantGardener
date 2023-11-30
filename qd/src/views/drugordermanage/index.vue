<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        登记订单
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
      <el-table-column label="药品名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.drugname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="购药顾客" align="center">
        <template slot-scope="{row}">
          <span>{{ row.consumername }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单价" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sell_price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数量" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sell_num }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center">
        <template slot-scope="{row}">
          <span>{{ row.total_amount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sell_datetime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button size="mini" type="success" @click="handleDetail(row)">
            详情
          </el-button>
          <el-popconfirm
            title="确认删除此条许可证信息？"
            confirm-button-text="确认"
            cancel-button-text="不删了"
          >
            <el-button size="mini" type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 500px%; margin-left:50px;">
        <el-form-item label="药品名称">
          <el-select v-model="temp.drugname" class="filter-item" placeholder="Please select" style="width: 300px;">
            <el-option v-for="item in drugOptions" :key="item.key" :label="item.display_name" :value="item.display_name" />
          </el-select>
          <el-input v-model="search_keyword" style="margin-left: 10px; width: 150px;" placeholder="查询药品库" />
          <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="searchDrugOptions">search</el-button>
        </el-form-item>
        <el-form-item label="售药日期">
          <el-date-picker v-model="temp.sell_datetime" type="datetime" placeholder="请选择日期" />
        </el-form-item>
        <!-- <el-form-item label="供货商">
          <el-input v-model="temp.suppliername" />
        </el-form-item> -->
        <el-form-item label="数量">
          <el-input v-model="temp.sell_num" />
        </el-form-item>
        <el-form-item label="购药人">
          <el-input v-model="temp.consumername" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          提交
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'DrugPriceManage',
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
      search_keyword: '',
      importanceOptions: [1, 2, 3],
      drugOptions: [],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        drugname: '',
        consumername: '',
        sell_price: undefined,
        sell_num: undefined,
        total_amount: undefined,
        sell_datetime: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '供货商详情',
        create: '订单'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
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
    searchDrugOptions() {
      this.$axios
        .get('/drugorder/fuzzysearch', {
          params: {
            keyword: this.search_keyword
          }
        })
        .then(resp => {
          if (resp && resp.data.code === 200) {
            resp.data.result.forEach(purchaserecord => {
              this.drugOptions.push({
                display_name: purchaserecord.drugname + '|' + purchaserecord.suppliername,
                key: purchaserecord.id
              })
            })
          } else {
            this.$message({
              type: 'error',
              message: '库存库搜索异常，稍后重试!',
              showClose: true
            })
          }
        })
        .catch(failResp => {
          console.log(failResp)
        })
    },
    getList() {
      this.listLoading = true
      this.$axios
        .get('/drugorderlist', {
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
            resp.data.result.items.forEach(order => {
              this.list.push({
                id: order.id,
                drugname: order.drugname,
                consumername: order.consumername,
                sell_price: order.sell_price,
                sell_num: order.sell_num,
                total_amount: order.sell_price * order.sell_num,
                sell_datetime: order.sell_datetime
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
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    // sortByID(order) {
    //   if (order === 'ascending') {
    //     this.listQuery.sort = '+id'
    //   } else {
    //     this.listQuery.sort = '-id'
    //   }
    //   this.handleFilter()
    // },
    resetTemp() {
      this.temp = {
        name: '',
        manufacturer: '',
        dosageform: '',
        specification: '',
        efficacy: '',
        usagemethod: '',
        adversereaction: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      console.log(this.temp.drugname)
      const arr = this.temp.drugname.toString().split('|', 2)
      console.log(arr[0], arr[1])
      this.$axios
        .post('/drugorder/add', {
          drugname: arr[0],
          sell_price: this.temp.sell_price,
          sell_num: this.temp.sell_num,
          consumername: this.temp.consumername,
          total_amount: this.temp.sell_price * this.temp.sell_num,
          sell_datetime: this.temp.sell_datetime
        })
        .then(resp => {
          console.log(resp)
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: '新增成功！',
              showClose: true
            })
            this.dialogFormVisible = false
          }
        })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          // updateArticle(tempData).then(() => {
          //   const index = this.list.findIndex(v => v.id === this.temp.id)
          //   this.list.splice(index, 1, this.temp)
          //   this.dialogFormVisible = false
          //   this.$notify({
          //     title: 'Success',
          //     message: 'Update Successfully',
          //     type: 'success',
          //     duration: 2000
          //   })
          // })
        }
      })
    },
    handleDelete(row) {
      console.log('???')
      this.$axios
        .post('/drugdel', {
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
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>
