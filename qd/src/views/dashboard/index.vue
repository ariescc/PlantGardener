<template>
  <div class="dashboard-container">
    <el-card style="margin: 20px 59px 20px 59px;">
      <h1 class="fnt_32">药企运营监控典型痛点</h1>
      <div class="EBC-intro-list">
        <ul style="width: 90%; margin-top: 50px;">
          <li>
            <div class="icon"><img src="https://kingdeecms-1252177366.cos.ap-guangzhou.myqcloud.com/u/cms/www/202307/182133518yt5.png" alt="盈利的压力"></div>
            <div class="txt">
              <h3 class="fnt_20"><b>盈利的压力</b></h3>
              <p class="manage-p">随着药品集中带量采购工作常态化制度化开展，国家层面已开展多批药品和高值耗材的集中采购，集采品类由化学药品扩展至生物制药、中成药、高值耗材等，医药企业的盈利压力很大</p>
            </div>
          </li>
          <li>
            <div class="icon"><img src="https://kingdeecms-1252177366.cos.ap-guangzhou.myqcloud.com/u/cms/www/202307/182133286518.png" alt="创新的挑战"></div>
            <div class="txt">
              <h3 class="fnt_20"><b>创新的挑战</b></h3>
              <p class="manage-p">无论从政策层面还是市场竞争层面来看，产品技术创新已经成为医药行业高质量发展的关键举措，当前医药企业面临着产品创新能力不足带来的挑战</p>
            </div>
          </li>
          <li>
            <div class="icon"><img src="https://kingdeecms-1252177366.cos.ap-guangzhou.myqcloud.com/u/cms/www/202307/18213259ouqh.png" alt="运营的挑战"></div>
            <div class="txt">
              <h3 class="fnt_20"><b>运营的挑战</b></h3>
              <p class="manage-p">带量采购给医药企业带来的盈利能力的压力，倒逼医药企业更加重视内部的管理，通过运营优化来降低成本和提高业务效率</p>
            </div>
          </li>
        </ul>
      </div>
    </el-card>
    <div class="carousel-back">
      <el-carousel :interval="2000" type="card" height="50vh" width="100vh">
        <el-carousel-item v-for="item in carouselImages" :key="item">
          <img :src="item+carouselPrefix" style="height: 100%; width: 100%;">
        </el-carousel-item>
      </el-carousel>
    </div>
    <el-card style="margin: 20px 59px 20px 59px;">
      <h2 class="fnt_32">医药企业加速数字化创新</h2>
      <el-tabs
        v-model="activeTab"
        class="intro-tabs"
      >
        <el-tab-pane class="tab-pane" label="产销协同" name="tab1">
          <tab1 />
        </el-tab-pane>
        <el-tab-pane class="tab-pane" label="精细化管理" name="tab2">
          <tab2 />
        </el-tab-pane>
        <el-tab-pane class="tab-pane" label="渠道营销数字化" name="tab3">
          <tab3 />
        </el-tab-pane>
        <el-tab-pane class="tab-pane" label="合规管控" name="tab4">
          <tab4 />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import tab1 from './components/tab1'
import tab2 from './components/tab2'
import tab3 from './components/tab3'
import tab4 from './components/tab4'

const carouselPrefix = '?imageView2/2/h/440'

export default {
  name: 'Dashboard',
  components: { tab1, tab2, tab3, tab4 },
  data() {
    return {
      carouselImages: [
        require('@/assets/carousel/VCG41N904570528.jpg'),
        require('@/assets/carousel/VCG41N1140141407.jpg'),
        require('@/assets/carousel/VCG41N1251485252.jpg'),
        require('@/assets/carousel/VCG41N1251485255.jpg'),
        require('@/assets/carousel/VCG211183060293.jpg')
      ],
      carouselPrefix,
      activeTab: 'tab1'
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.fnt_32 {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 30px;
}

ul {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}

li {
  margin-right: 50px;
}

.EBC-intro-list {
  display: flex;
  justify-content: center;
  align-items: center;
}

.manage {
  background-color: white;
}

.manage-p {
  color: #9DA2A0;
  line-height: 30px;
}

</style>
