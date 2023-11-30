<template>
  <el-form>
    <el-form-item label="名称">
      <el-input v-model.trim="user.name" />
    </el-form-item>
    <el-form-item label="Email">
      <el-input v-model.trim="user.email" />
    </el-form-item>
    <el-form-item label="联系方式">
      <el-input v-model.trim="user.phone" />
    </el-form-item>
    <el-form-item label="个性签名">
      <el-input v-model.trim="user.personSignature" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          email: '',
          phone: '',
          personSignature: ''
        }
      }
    }
  },
  methods: {
    submit() {
      this.$axios
        .post('/updateProfile', {
          phone: this.user.phone,
          email: this.user.email,
          sex: this.user.sex,
          birthday: this.user.birthday,
          occupation: this.user.occupation,
          personSignature: this.user.personSignature
        })
        .then(resp => {
          if (resp && resp.data.code === 200) {
            this.$message({
              type: 'success',
              message: resp.data.result,
              showClose: false
            })
          }
        })
        .catch(failResp => {
          console.log(failResp)
        })
      // 重改后重新获取用户信息，更新全局用户信息
      this.$axios.get('/getUserInfo').then(resp => {
        if (resp && resp.status === 200) {
          this.$store.dispatch('user/getInfo', resp.data.result)
        }
      })
    }
  }
}
</script>
