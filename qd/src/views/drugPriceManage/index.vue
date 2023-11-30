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
        药品定价
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
      <el-table-column label="采购单价（元）" align="center">
        <template slot-scope="{row}">
          <span>{{ row.incomePrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="销售单价（元）" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sellPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="折扣" align="center">
        <template slot-scope="{row}">
          <span>{{ row.discount | discountFilter(row) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最大库存" align="center">
        <template slot-scope="{row}">
          <span>{{ row.maxInventoryQuantity }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新日期" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button size="mini" type="success" @click="handleDetail(row)">
            详情
          </el-button>
          <el-popconfirm
            title="确认删除此条定价信息？"
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
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="100px" style="width: 500px%; margin-left:50px;">
        <el-form-item label="药品">
          <img :src="selectDrug.picture">
        </el-form-item>
        <el-form-item label="药品名称">
          <el-input v-model="drugPriceForm.drugName" disabled />
        </el-form-item>
        <el-form-item label="供货商">
          <el-input v-model="drugPriceForm.manufacturer" disabled />
        </el-form-item>
        <el-form-item label="采购单价（元）">
          <el-input v-model="drugPriceForm.incomePrice" />
        </el-form-item>
        <el-form-item label="销售单价（元）">
          <el-input v-model="drugPriceForm.sellPrice" />
        </el-form-item>
        <el-form-item label="药品折扣">
          <el-select v-model="drugPriceForm.discount" class="filter-item" placeholder="Please select" style="width: 300px;">
            <el-option v-for="item in discountOptions" :key="item.key" :label="item.value" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大库存">
          <el-input v-model="drugPriceForm.maxInventoryQuantity" />
        </el-form-item>
        <el-form-item label="更新日期">
          <el-date-picker v-model="drugPriceForm.updateTime" type="datetime" placeholder="请选择日期" />
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
// import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'DrugPriceManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    discountFilter(row) {
      return (parseFloat(row.discount) * 10) + '折'
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      importanceOptions: [1, 2, 3],
      discountOptions: [
        {
          key: 1,
          value: 0.1
        },
        {
          key: 2,
          value: 0.2
        },
        {
          key: 3,
          value: 0.3
        },
        {
          key: 4,
          value: 0.4
        },
        {
          key: 5,
          value: 0.5
        },
        {
          key: 6,
          value: 0.6
        },
        {
          key: 7,
          value: 0.7
        },
        {
          key: 8,
          value: 0.8
        },
        {
          key: 9,
          value: 0.9
        },
        {
          key: 10,
          value: 1
        }
      ],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '定价详情',
        create: '药品定价'
      },
      page: 1,
      limit: 20,
      sort: 'asc',
      approvalDrugName: '', // 选中的待登记许可证药品
      search_keyword: '', // 药品库查询 -- 药品名称关键字
      drugOptions: [], // 药品库查询结果
      drugFuzzySearchLoading: false, // 药品库搜索的加载状态
      selectDrug: {}, // 药品库中选中的药品
      drugPriceForm: { // 药品价格表单
        drugId: undefined,
        drugName: '',
        manufacturer: '',
        incomePrice: undefined,
        sellPrice: undefined,
        discount: undefined,
        updateTime: undefined,
        maxInventoryQuantity: undefined
      },
      uploadApprovalLoading: false // 上传许可证loading
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
      this.selectDrug = {}
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
        .get('/drugPrice/list', {
          params: {
            page: this.page,
            limit: this.limit,
            sort: 'asc'
          }
        })
        .then(resp => {
          if (resp.data.code === 200) {
            this.list = []
            resp.data.result.items.forEach(drugPrice => {
              this.list.push({
                id: drugPrice.id,
                drugId: drugPrice.drugId,
                drugName: drugPrice.drugName,
                incomePrice: drugPrice.incomePrice,
                sellPrice: drugPrice.sellPrice,
                discount: drugPrice.discount,
                manufacturer: drugPrice.manufacturer,
                updateTime: drugPrice.updateTime,
                maxInventoryQuantity: drugPrice.maxInventoryQuantity
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
    resetDrugPriceForm() {
      this.drugPriceForm = { // 药品价格表单
        drugId: undefined,
        drugName: '',
        manufacturer: '',
        incomePrice: undefined,
        sellPrice: undefined,
        discount: undefined,
        updateTime: undefined,
        maxInventoryQuantity: undefined
      }
    },
    handleCreate() {
      this.resetDrugPriceForm()
      console.log(this.selectDrug)
      this.drugPriceForm = {
        drugId: this.selectDrug.id,
        drugName: this.selectDrug.name,
        manufacturer: this.selectDrug.manufacturer,
        incomePrice: this.selectDrug.referencePrice,
        sellPrice: undefined,
        discount: undefined,
        updateTime: undefined,
        maxInventoryQuantity: undefined
      }
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      this.$axios
        .post('/drugPrice/add', {
          drugId: this.drugPriceForm.drugId,
          drugName: this.drugPriceForm.drugName,
          manufacturer: this.drugPriceForm.manufacturer,
          incomePrice: parseFloat(this.drugPriceForm.incomePrice),
          sellPrice: parseFloat(this.drugPriceForm.sellPrice),
          discount: this.drugPriceForm.discount,
          updateTime: this.drugPriceForm.updateTime,
          maxInventoryQuantity: this.drugPriceForm.maxInventoryQuantity
        })
        .then(resp => {
          console.log(this.drugPriceForm)
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: '新增成功！',
              showClose: true
            })
            this.dialogFormVisible = false
          }
        })
      this.getList()
    },
    handleDetail(row) {
      // this.$axios.get('/druginfo/' + row.id).then(resp => {
      //   console.log(resp)
      //   if (resp && resp.data.code === 200) {
      //     this.temp.id = resp.data.result.id,
      //     this.temp.manufacturer = resp.data.result.manufacturer,
      //     this.temp.adversereaction = resp.data.result.adversereaction,
      //     this.temp.dosageform = resp.data.result.dosageform,
      //     this.temp.efficacy = resp.data.result.efficacy,
      //     this.temp.name = resp.data.result.name,
      //     this.temp.specification = resp.data.result.specification,
      //     this.temp.usagemethod = resp.data.result.usagemethod
      //   }
      // })
      // this.dialogStatus = 'update'
      // this.dialogFormVisible = true
      // this.$nextTick(() => {
      //   this.$refs['dataForm'].clearValidate()
      // })
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
        .post('/drugPrice/delete', {
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
