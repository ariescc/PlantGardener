<template>
  <el-row>
    <el-col :span="12" :xs="24">
      <h3>渠道营销数字化</h3>
      <p style="line-height: 25px;">
        通过提升渠道的药品流通效率、商务效率和财务效率，整体提升企业的药品营销效率。具体包括以下核心的数字化能力：
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        客户资产管理
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        渠道订单管理
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        药品流向管理
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        营销团队与行为管理
      </p>
      <p>
        <i class="el-icon-check icon-color" />
        应收管理和营销费用管理
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
      tab1_img: require('@/assets/intro-tabs/tab3.jpg')
    }
  }
}
</script>
<style>
.icon-color {
  color: red;
}
</style>
