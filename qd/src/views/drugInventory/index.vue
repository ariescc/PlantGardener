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
      <el-table-column label="药品名称" align="center">
        <template slot-scope="{row}">
          <span>{{ row.drugName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="供货商" align="center">
        <template slot-scope="{row}">
          <span>{{ row.manufacturer }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生产日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.productionDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="保质期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.shelfLife }}</span>
        </template>
      </el-table-column>
      <el-table-column label="库存余量" align="center">
        <template slot-scope="{row}">
          <span>{{ row.inventoryQuantity }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最大库存量" align="center">
        <template slot-scope="{row}">
          <span>{{ row.maxInventoryQuantity }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button size="mini" type="success" @click="handleDetail(row)">
            详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="inventoryForm" label-position="left" label-width="100px" style="width: 500px%; margin-left:50px;">
        <el-form-item label="药品名称">
          <el-input v-model="inventoryForm.drugName" disabled />
        </el-form-item>
        <el-form-item label="供货商">
          <el-input v-model="inventoryForm.manufacturer" disabled />
        </el-form-item>
        <el-form-item label="生产日期">
          <el-input v-model="inventoryForm.productionDate" disabled />
        </el-form-item>
        <el-form-item label="保质期（天）">
          <el-input v-model="inventoryForm.shelfLife" disabled />
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input v-model="inventoryForm.inventoryQuantity" disabled />
        </el-form-item>
        <el-form-item label="最大库存" required>
          <el-input v-model="inventoryForm.maxInventoryQuantity" />
        </el-form-item>
        <el-form-item label="最新出库时间">
          <el-input v-model="inventoryForm.updateTime" disabled />
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
// import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'SupplierManage', // 供货商管理
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
      importanceOptions: [1, 2, 3],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '订单详情',
        create: '采购申请'
      },
      dialogPvVisible: false,
      pvData: [],
      downloadLoading: false,
      page: 1,
      limit: 20,
      sort: 'asc',
      approvalDrugName: '', // 选中的待登记许可证药品
      search_keyword: '', // 药品库查询 -- 药品名称关键字
      drugOptions: [], // 药品库查询结果
      drugFuzzySearchLoading: false, // 药品库搜索的加载状态
      selectDrug: {},
      uploadApprovalLoading: false, // 上传许可证loading
      inventoryForm: {
        drugName: undefined,
        drugId: undefined,
        manufacturer: undefined,
        productionDate: undefined,
        shelfLife: undefined,
        inventoryQuantity: undefined,
        maxInventoryQuantity: undefined,
        updateTime: undefined
      } // 库存信息表单
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 根据药品名称关键词模糊查询药品列表
    searchDrugOptions() {
      this.drugFuzzySearchLoading = true
      this.drugOptions = []
      this.approvalDrug = {}
      this.$axios
        .get('/approval/drugSearch', {
          params: {
            keyword: this.search_keyword
          }
        })
        .then(resp => {
          if (resp && resp.data.code === 200) {
            resp.data.result.forEach(drug => {
              this.drugOptions.push({
                display_name: drug.name + '|' + drug.manufacturer,
                key: drug.id,
                value: drug
              })
            })
          } else {
            this.$message({
              type: 'error',
              message: '药品库搜索异常，稍后重试!',
              showClose: true
            })
          }
        })
        .catch(failResp => {
          console.log(failResp)
        })
      this.drugFuzzySearchLoading = false
    },
    getList() {
      this.listLoading = true
      this.$axios
        .get('/drugInventory/list', {
          params: {
            page: this.page,
            limit: this.limit,
            sort: 'asc'
          }
        })
        .then(resp => {
          console.log(resp)
          if (resp.data.code === 200) {
            this.list = []
            resp.data.result.items.forEach(inventory => {
              this.list.push({
                id: inventory.id,
                drugName: inventory.drugName,
                drugId: inventory.drugId,
                manufacturer: inventory.manufacturer,
                productionDate: inventory.productionDate,
                shelfLife: inventory.shelfLife,
                inventoryQuantity: inventory.inventoryQuantity,
                maxInventoryQuantity: inventory.maxInventoryQuantity,
                updateTime: inventory.updateTime
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
      // fetchList(this.listQuery).then(response => {
      //   this.list = response.data.items
      //   this.total = response.data.total

      //   // Just to simulate the time of the request
      //   setTimeout(() => {
      //     this.listLoading = false
      //   }, 1.5 * 1000)
      // })
    },
    // handleFilter() {
    //   this.listQuery.page = 1
    //   this.getList()
    // },
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
      this.inventoryForm = {
        drugName: undefined,
        drugId: undefined,
        manufacturer: undefined,
        productionDate: undefined,
        shelfLife: undefined,
        inventoryQuantity: undefined,
        maxInventoryQuantity: undefined,
        updateTime: undefined
      } // 库存信息表单
    },
    handleCreate() {
      this.resetTemp()
      this.inventoryForm = {
        drugName: undefined,
        drugId: undefined,
        manufacturer: undefined,
        productionDate: undefined,
        shelfLife: undefined,
        inventoryQuantity: undefined,
        maxInventoryQuantity: undefined,
        updateTime: undefined
      } // 库存信息表单
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      this.$axios
        .post('/purchaseOrder/add', {
          drugName: this.orderForm.drugName,
          drugId: this.orderForm.drugId,
          quantity: this.orderForm.quantity,
          productionDate: this.orderForm.productionDate,
          shelfLife: this.orderForm.shelfLife,
          manufacturer: this.orderForm.manufacturer,
          incomePrice: this.orderForm.incomePrice,
          totalAmount: this.orderForm.totalAmount,
          purchaseTime: this.orderForm.purchaseTime,
          personInCharge: this.orderForm.personInCharge,
          approveStatus: this.approveStatus
        })
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: '新增成功！',
              showClose: true
            })
            this.dialogFormVisible = false
            // 刷新表格数据
            this.getList()
          }
        })
    },
    handleDetail(row) {
      this.$axios.get('/drugInventoryInfo/' + row.id).then(resp => {
        if (resp && resp.data.code === 200) {
          this.inventoryForm.id = resp.data.result.id
          this.inventoryForm.drugId = resp.data.result.drugId
          this.inventoryForm.drugName = resp.data.result.drugName
          this.inventoryForm.manufacturer = resp.data.result.manufacturer
          this.inventoryForm.productionDate = resp.data.result.productionDate
          this.inventoryForm.shelfLife = resp.data.result.shelfLife
          this.inventoryForm.inventoryQuantity = resp.data.result.inventoryQuantity
          this.inventoryForm.maxInventoryQuantity = resp.data.result.maxInventoryQuantity
          this.inventoryForm.updateTime = resp.data.result.updateTime
        }
      })
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    updateData() {
      this.$axios
        .post('/updateInventory', {
          id: this.inventoryForm.id,
          drugId: this.inventoryForm.drugId,
          drugName: this.inventoryForm.drugName,
          manufacturer: this.inventoryForm.manufacturer,
          productionDate: this.inventoryForm.productionDate,
          shelfLife: this.inventoryForm.shelfLife,
          inventoryQuantity: this.inventoryForm.inventoryQuantity,
          maxInventoryQuantity: this.inventoryForm.maxInventoryQuantity,
          updateTime: this.inventoryForm.updateTime
        })
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: '更新成功！',
              showClose: true
            })
          }
        })
        .catch(failResp => {
          console.log(failResp)
        })
      this.dialogFormVisible = false
      // 更新数据
      this.getList()
    },
    handleDelete(row) {
      this.$axios
        .post('/purchaseOrder/delete', {
          id: row.id
        })
        .then(resp => {
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
    handleFetchPv(pv) {
      // fetchPv(pv).then(response => {
      //   this.pvData = response.data.pvData
      //   this.dialogPvVisible = true
      // })
    },
    handleDownload() {
      // this.downloadLoading = true
      // // import('@/vendor/Export2Excel').then(excel => {
      // //   const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
      // //   const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
      // //   const data = this.formatJson(filterVal)
      // //   excel.export_json_to_excel({
      // //     header: tHeader,
      // //     data,
      // //     filename: 'table-list'
      // //   })
      //   this.downloadLoading = false
      // })
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
    // getSortClass: function(key) {
    //   const sort = this.listQuery.sort
    //   return sort === `+${key}` ? 'ascending' : 'descending'
    // }
  }
}
</script>
