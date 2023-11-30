<template>
  <el-row>
    <el-col :span="12" :xs="24">
      <h3>精细化生产管理</h3>
      <p style="line-height: 25px;">
        实现精细化的生产过程管控与生产成本管理，具体包括：
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        批生产指令管理、配方管理、批生产过程管理、物料管控、批生产记录管理等
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        实际成本核算、标准成本核算、成本还原、成本分析等
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
      tab1_img: require('@/assets/intro-tabs/tab2.jpg')
    }
  }
}
</script>
<style>
.icon-color {
  color: red;
}
</style>
