<template>
  <body id="paper">
    <el-form
      ref="registerForm"
      v-loading="loading"
      :model="registerForm"
      :rules="rules"
      class="login-container"
      label-position="left"
      label-width="0px"
    >
      <h3 class="login_title">用户注册</h3>
      <el-row>
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            type="text"
            auto-complete="off"
            placeholder="账号"
          />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            show-password
          />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            auto-complete="off"
            placeholder="确认密码"
            show-password
          />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item>
          <el-input
            v-model="registerForm.email"
            type="text"
            auto-complete="off"
            placeholder="邮箱"
          />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item>
          <el-input
            v-model="registerForm.phone"
            type="text"
            auto-complete="off"
            placeholder="电话号码"
          />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item>
          <el-input
            v-model="registerForm.name"
            type="text"
            auto-complete="off"
            placeholder="真实姓名"
          />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item>
          <el-select
            v-model="registerForm.rolename"
            placeholder="请选择用户类型"
            style="width: 100%;"
          >
            <el-option
              v-for="item in userOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-row>
      <el-row>
        <el-button
          type="plain"
          style="width: 100%; border: none"
          @click="register('registerForm')"
        >
          注册
        </el-button>
      </el-row>
    </el-form>
  </body>
</template>
<script>
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.registerForm.password !== '') {
          // 不做处理
        }
        callback()
      }
    }

    var validateConfirmPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }

    return {
      rules: {
        username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
        password: [{ validator: validatePass, trigger: 'blur' }],
        confirmPassword: [{ validator: validateConfirmPass, trigger: 'blur' }]
      },
      checked: true,
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        phone: '',
        rolename: '',
        realname: ''
      },
      loading: false,
      userOptions: [
        {
          label: '系统管理员',
          value: 'admin'
        },
        {
          label: '企业管理',
          value: 'manager'
        },
        {
          label: '库存管理员',
          value: 'stockclerk'
        },
        {
          label: '销售人员',
          value: 'salesstaff'
        }
      ]
    }
  },
  methods: {
    register(formName) {
      var _this = this
      this.$axios
        .post('/register', {
          username: this.registerForm.username,
          password: this.registerForm.password,
          phone: this.registerForm.phone,
          email: this.registerForm.email,
          name: this.registerForm.name,
          rolename: this.registerForm.rolename
        })
        .then(resp => {
          if (resp.data.code === 200) {
            this.$message({
              showClose: true,
              message: '注册成功！',
              type: 'success'
            })
            _this.$router.replace('/login')
          } else {
            this.$message({
              showClose: true,
              message: resp.data.message,
              type: 'error'
            })
          }
        })
        .catch(failResp => {
          console.log(failResp)
        })
    }
  }
}
</script>
<style scoped>
#paper {
  background:url("../../assets/background.jpg") no-repeat;
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}
body{
    margin: -8px -8px;
}
.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 90px auto;
  width: 450px;
  padding: 35px 35px 15px 35px;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #fff;
}
.login_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
}
</style>
