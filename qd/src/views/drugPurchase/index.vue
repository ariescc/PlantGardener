<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="selectDrug" class="filter-item" placeholder="查询药品库后选择..." style="width: 300px;">
        <el-option
          v-for="item in drugOptions"
          :key="item.key"
          :label="item.display_name"
          :value="item.value"
        />
      </el-select>
      <el-input
        v-model="search_keyword"
        style="margin-left: 10px; width: 180px;"
        placeholder="输入查询药品名称"
        prefix-icon="el-icon-search"
      />
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-s-promotion"
        :disabled="search_keyword == ''"
        :loading="drugFuzzySearchLoading"
        @click.native="searchDrugOptions"
      >
        查询药品库
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        :disabled="Object.keys(selectDrug) == 0"
        @click="handleCreate"
      >
        新建订单
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
          <span>{{ row.drugName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="供货商" align="center">
        <template slot-scope="{row}">
          <span>{{ row.manufacturer }}</span>
        </template>
      </el-table-column>
      <el-table-column label="采购价格（元）" align="center">
        <template slot-scope="{row}">
          <span>￥{{ row.incomePrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数量（盒）" align="center">
        <template slot-scope="{row}">
          <span>{{ row.quantity }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总金额（元）" align="center">
        <template slot-scope="{row}">
          <span>￥{{ row.totalAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生产日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.productionDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="保质期（天）" align="center">
        <template slot-scope="{row}">
          <span>{{ row.shelfLife }}</span>
        </template>
      </el-table-column>
      <el-table-column label="采购责任人" align="center">
        <template slot-scope="{row}">
          <span>{{ row.personInCharge }}</span>
        </template>
      </el-table-column>
      <el-table-column label="采购日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.purchaseTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="采购状态" align="center">
        <template slot-scope="{row}">
          <span>
            <el-tag :type="statusOptions[row.approvelStatus].type">
              {{ statusOptions[row.approvelStatus].key }}
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-popconfirm
            title="确认批准此采购订单？"
            @onConfirm="confirmPurchseOrder"
          >
            <el-button slot="reference" style="margin-right: 5px;" size="mini" type="primary">
              审核
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            title="确认删除此条采购信息？"
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
      <el-form ref="dataForm" :model="orderForm" label-position="left" label-width="100px" style="width: 500px%; margin-left:50px;">
        <el-form-item label="药品图片">
          <img :src="orderForm.picture" class="avatar">
        </el-form-item>
        <el-form-item label="药品名称">
          <el-input v-model="orderForm.drugName" disabled />
        </el-form-item>
        <el-form-item label="供货商">
          <el-input v-model="orderForm.manufacturer" disabled />
          <!-- <el-date-picker v-model="orderForm.manufacturer" type="datetime" placeholder="请选择日期" /> -->
        </el-form-item>
        <el-form-item label="生产日期">
          <el-date-picker v-model="orderForm.productionDate" type="datetime" placeholder="请选择日期" />
        </el-form-item>
        <el-form-item label="保质期（天）">
          <el-input v-model="orderForm.shelfLife" />
        </el-form-item>
        <el-form-item label="采购价格（元）">
          <el-input v-model="orderForm.incomePrice" disabled />
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="orderForm.quantity" />
        </el-form-item>
        <el-form-item label="采购日期">
          <el-date-picker v-model="orderForm.purchaseTime" type="datetime" placeholder="请选择日期" />
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
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      importanceOptions: [1, 2, 3],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: [
        {
          key: '待审核',
          type: 'warning'
        },
        {
          key: '审核通过',
          type: 'success'
        },
        {
          key: '未通过',
          type: 'danger'
        }
      ],
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
      orderForm: {
        drugId: undefined,
        drugName: undefined,
        quantity: undefined,
        productionDate: undefined,
        shelfLife: undefined,
        manufacturer: undefined,
        incomePrice: undefined,
        totalAmount: undefined,
        purchaseTime: undefined,
        personInCharge: undefined,
        approvelStatus: undefined,
        picture: undefined
      } // 订单表单
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
        .get('/drugData/fuzzySearch', {
          params: {
            keyword: this.search_keyword
          }
        })
        .then(resp => {
          if (resp && resp.data.code === 200) {
            resp.data.result.forEach(drug => {
              this.drugOptions.push({
                display_name: drug.drugName + '|' + drug.manufacturer,
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
        .get('/purchaseOrder/list', {
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
                drugId: order.drugId,
                drugName: order.drugName,
                quantity: order.quantity,
                productionDate: order.productionDate,
                shelfLife: order.shelfLife,
                manufacturer: order.manufacturer,
                incomePrice: order.incomePrice,
                totalAmount: order.totalAmount,
                purchaseTime: order.purchaseTime,
                personInCharge: order.personInCharge,
                approvelStatus: order.approvelStatus
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
      this.orderForm = {
        drugId: undefined,
        drugName: undefined,
        quantity: undefined,
        productionDate: undefined,
        shelfLife: undefined,
        manufacturer: undefined,
        incomePrice: undefined,
        totalAmount: undefined,
        purchaseTime: undefined,
        personInCharge: undefined,
        approvelStatus: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.orderForm = {
        drugId: this.selectDrug.drugId,
        drugName: this.selectDrug.drugName,
        quantity: undefined,
        productionDate: undefined,
        shelfLife: undefined,
        manufacturer: this.selectDrug.manufacturer,
        incomePrice: this.selectDrug.incomePrice,
        totalAmount: undefined,
        purchaseTime: undefined,
        personInCharge: undefined,
        approvelStatus: undefined,
        picture: this.selectDrug.picture
      }
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
          totalAmount: this.orderForm.incomePrice * this.orderForm.quantity,
          purchaseTime: this.orderForm.purchaseTime,
          personInCharge: this.orderForm.personInCharge,
          approvelStatus: this.approvelStatus
        })
        .then(resp => {
          console.log(resp)
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: resp.data.message,
              showClose: true
            })
          } else {
            if (resp && resp.data.code === 400) {
              this.$message({
                type: 'error',
                message: resp.data.message,
                showClose: true
              })
            }
          }
        })
      this.dialogFormVisible = false
      this.getList()
    },
    handleDetail(row) {
      this.$axios.get('/druginfo/' + row.id).then(resp => {
        console.log(resp)
        if (resp && resp.data.code === 200) {
          this.temp.id = resp.data.result.id
          this.temp.manufacturer = resp.data.result.manufacturer
          this.temp.adversereaction = resp.data.result.adversereaction
          this.temp.dosageform = resp.data.result.dosageform
          this.temp.efficacy = resp.data.result.efficacy
          this.temp.name = resp.data.result.name
          this.temp.specification = resp.data.result.specification
          this.temp.usagemethod = resp.data.result.usagemethod
        }
      })
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
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
