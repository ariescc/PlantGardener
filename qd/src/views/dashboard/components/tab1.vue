<template>
  <el-row>
    <el-col :span="12" :xs="24">
      <h3>产销协同</h3>
      <p style="line-height: 25px;">
        强化医药企业的产供销协同体系，提升生产计划的预见性和可执行性，实现从销售预测到产销协同、生产计划、采购和生产执行的全过程闭环管控系统。具体包括：
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        药品销售预测管理、销售计划管理
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        需求管理：需求抵消、需求平衡等
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        生产计划管理，计划层面拉通销售、生产和采购
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
      tab1_img: require('@/assets/intro-tabs/tab1.jpg')
    }
  }
}
</script>
<style>
.icon-color {
  color: red;
}
</style>
