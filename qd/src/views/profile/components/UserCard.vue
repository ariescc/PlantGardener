<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <span>我的信息</span>
    </div>

    <div class="user-profile">
      <div class="box-center">
        <el-upload
          class="avatar-upload"
          action="/api/updateAvatar"
          :headers="headers"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="imageUrl" :src="imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
      </div>
      <div class="box-center">
        <div class="user-name text-center">{{ user.name }}</div>
        <div class="user-role text-center text-muted">{{ user.role | roleFilter }}</div>
      </div>
    </div>

    <div class="user-bio">
      <div class="user-education user-bio-section">
        <div class="user-bio-section-header"><svg-icon icon-class="education" /><span>个性签名</span></div>
        <div class="user-bio-section-body">
          <div class="text-muted">
            {{ user.personSignature }}
          </div>
        </div>
      </div>

      <div class="user-skills user-bio-section">
        <div class="user-bio-section-header"><svg-icon icon-class="skill" /><span>用户评价</span></div>
        <div class="user-bio-section-body">
          <div class="progress-item">
            <span>信誉度</span>
            <el-progress :percentage="70" />
          </div>
          <div class="progress-item">
            <span>风险指数</span>
            <el-progress :percentage="18" status="exception" />
          </div>
          <div class="progress-item">
            <span>满意度</span>
            <el-progress :percentage="12" status="success" />
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  filters: {
    // 角色格式化显示
    roleFilter(rolename) {
      const roleMap = {
        salesstaff: '医药销售',
        stockclerk: '库存管理员',
        admin: '系统管理员'
      }
      return roleMap[rolename]
    }
  },
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          email: '',
          role: '',
          avatar: '',
          personSignature: ''
        }
      }
    }
  },
  data() {
    return {
      imageUrl: this.user.avatar,
      headers: undefined
    }
  },
  methods: {
    handleAvatarSuccess(res, file) {
      console.log(res)
      this.imageUrl = res.result
      // 更新用户信息
      // 加载用户信息
      this.$axios.get('/getUserInfo').then(resp => {
        if (resp && resp.status === 200) {
          this.$store.dispatch('user/getInfo', resp.data.result)
        }
      })
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLT2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像只能是 JPG 格式！')
      }
      if (!isLT2M) {
        this.$message.error('上传头像图片大小不能超过2MB！')
      }

      this.headers = {
        Authorization: this.token
      }

      return isJPG && isLT2M
    }
  }
}
</script>

<style lang="scss" scoped>
.box-center {
  margin: 0 auto;
  display: table;
}

.text-muted {
  color: #777;
}

.user-profile {
  .user-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .user-role {
    padding-top: 10px;
    font-weight: 400;
    font-size: 14px;
  }

  .box-social {
    padding-top: 30px;

    .el-table {
      border-top: 1px solid #dfe6ec;
    }
  }

  .user-follow {
    padding-top: 20px;
  }
}

.user-bio {
  margin-top: 20px;
  color: #606266;

  span {
    padding-left: 4px;
  }

  .user-bio-section {
    font-size: 14px;
    padding: 15px 0;

    .user-bio-section-header {
      border-bottom: 1px solid #dfe6ec;
      padding-bottom: 10px;
      margin-bottom: 10px;
      font-weight: bold;
    }
  }
}

.avatar-upload {
    border: 5px solid #F2F2F2;
    border-radius: 50%;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
