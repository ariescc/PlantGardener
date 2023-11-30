<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="approvalDrug" class="filter-item" placeholder="查询药品库后选择..." style="width: 300px;">
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
        :disabled="Object.keys(approvalDrug) == 0"
        @click="handleCreate"
      >
        登记许可证
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
      <el-table-column label="生产厂商" align="center">
        <template slot-scope="{row}">
          <span>{{ row.manufacturer }}</span>
        </template>
      </el-table-column>
      <el-table-column label="批准文号" align="center">
        <template slot-scope="{row}">
          <span>{{ row.approvalNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="许可证文件" align="center">
        <template slot-scope="{row}">
          <span><a class="approvalfile" :href="row.fileUrl" @click.native="download">{{ row.approvalFileName }}</a></span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button size="mini" type="success" @click="handleDetail(row)">
            详情
          </el-button>
          <el-popconfirm
            title="确认删除此条许可证信息？"
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
        <el-form-item label="药品图片">
          <img :src="approvalDrug.picture" class="avatar">
        </el-form-item>
        <el-form-item label="药品名称">
          <el-input v-model="approvalDrug.name" disabled />
        </el-form-item>
        <el-form-item label="生产厂商">
          <el-input v-model="approvalDrug.manufacturer" disabled />
        </el-form-item>
        <el-form-item label="批准文号" required>
          <el-input v-model="approvalDrug.approvalNumber" />
        </el-form-item>
        <el-form-item label="许可证文件" required>
          <el-upload
            ref="upload"
            class="upload-demo"
            action="/api/approval/upload"
            :data="getFileParams()"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :before-remove="beforeRemove"
            :before-upload="beforeUpload"
            multiple
            :auto-upload="false"
            :limit="1"
            :on-exceed="handleExceed"
            :file-list="fileList"
          >
            <el-button
              slot="trigger"
              size="small"
              type="primary"
              icon="el-icon-folder-opened"
            >
              选择文件
            </el-button>
            <el-button
              style="margin-left: 10px;"
              size="small"
              type="success"
              icon="el-icon-upload"
              @click="submitUpload"
            >
              上传
            </el-button>
            <div slot="tip" class="el-upload__tip">
              访问
              <a class="approvalfile" href="https://www.nmpa.gov.cn/datasearch/home-index.html">国家药监局药品库</a>
              获取许可证查验
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()" @change="tableKey=tableKey+1">
          保存
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
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'ApprovalManage',
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
      showReviewer: false,
      temp: {
        name: '',
        manufacturer: '',
        dosageForm: '',
        specification: '',
        efficacy: '',
        usageMethod: '',
        adverseReaction: '',
        picture: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '药品许可证',
        create: '登记许可证'
      },
      dialogPvVisible: false,
      pvData: [],
      downloadLoading: false,
      page: 1,
      limit: 20,
      sort: 'asc',
      fileList: [], // 上传文件列表
      approvalDrugName: '', // 选中的待登记许可证药品
      search_keyword: '', // 药品库查询 -- 药品名称关键字
      drugOptions: [], // 药品库查询结果
      drugFuzzySearchLoading: false, // 药品库搜索的加载状态
      approvalDrug: {},
      uploadApprovalLoading: false // 上传许可证loading
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      this.$axios
        .get('/approval/list', {
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
            resp.data.result.items.forEach(approval => {
              this.list.push({
                id: approval.id,
                drugName: approval.drugName,
                drugId: approval.drugId,
                manufacturer: approval.manufacturer,
                approvalNumber: approval.approvalNumber,
                fileUrl: approval.fileUrl,
                approvalFileName: approval.approvalFileName
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
    submitUpload() {
      this.uploadApprovalLoading = true
      this.$refs.upload.submit()
      this.uploadApprovalLoading = false
    },
    // 移除文件
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    // 点击文件名称
    handlePreview(file) {
      console.log('preview')
    },
    handleExceed(files, fileList) {
      this.$message({
        type: 'warning',
        message: '仅限制上传一个许可证文件！',
        showClose: true
      })
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除${file.name}？`)
    },
    beforeUpload() {
    },
    getFileParams() {
      const approvalFileParams = {
        drugId: this.approvalDrug.id,
        drugName: this.approvalDrug.name,
        manufacturer: this.approvalDrug.manufacturer,
        approvalNumber: this.approvalDrug.approvalNumber
      }
      return approvalFileParams
    },
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
        dosageForm: '',
        specification: '',
        efficacy: '',
        usageMethod: '',
        adverseReaction: '',
        picture: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    createData() {
      // this.$axios
      //   .post('/addApproval', {
      //     drugName: this.approvalDrug.drugName,
      //     drugId: this.approvalDrug.drugId,
      //     manufacturer: this.approvalDrug.manufacturer,
      //     approvalNumber: this.approvalDrug.approvalNumber
      //   })
      //   .then(resp => {
      //     console.log('drugid', this.approvalDrug.drugId)
      //     if (resp && resp.data.code === 200) {
      //       this.$message({
      //         type: 'success',
      //         message: resp.data.result,
      //         showClose: true
      //       })
      //     }
      //   })
      this.dialogFormVisible = false
      this.getList()
    },
    updateData() {
      this.dialogFormVisible = false
      this.getList()
    },
    handleDetail(row) {
      this.$axios.get('/approvalInfo/' + row.id).then(resp => {
        if (resp && resp.data.code === 200) {
          console.log(resp)
          this.approvalDrug = {
            picture: resp.data.result.picture,
            name: resp.data.result.drugName,
            manufacturer: resp.data.result.manufacturer,
            approvalNumber: resp.data.result.approvalNumber,
            fileName: resp.data.result.fileName,
            fileUrl: resp.data.result.fileUrl
          }
        }
      })
        .catch(failResp => {
          console.log(`detail: ${failResp}`)
        })
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
    handleDelete(row) {
      this.$axios
        .post('/approval/delete', {
          id: row.id
        }).then(resp => {
          console.log(resp)
          if (resp && resp.data.code === 200) {
            this.$message({
              message: resp.data.result,
              type: 'success',
              showClose: true
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
    }
  }
}
</script>

<style>
.approvalfile:hover {
  color: blue;
}
</style>
