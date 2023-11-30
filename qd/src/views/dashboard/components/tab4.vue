<template>
  <el-row>
    <el-col :span="12" :xs="24">
      <h3>全过程GMP合规管控</h3>
      <p style="line-height: 25px;">
        将合规管控融进全业务过程，提升合规管控的效率，降低合规运营风险，具体包括：
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        供应商的准入与管理
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        物料全过程的合规管控
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        生产执行全过程的合规管控
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        生产执行全过程的合规管控
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        药品追溯管理
      </p>
    </el-col>
    <el-col :span="12" :xs="24">
      <img :src="tab1_img">
    </el-col>
  </el-row>
</template>
<script>
export default {
  data() {
    return {
      tab1_img: require('@/assets/intro-tabs/tab4.jpg')
    }
  }
}
</script>
<style>
.icon-color {
  color: red;
}
</style>
