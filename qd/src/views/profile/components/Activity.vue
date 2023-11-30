<template>
  <div class="components-container" style="height: 500px;">
    <pan-thumb :image="image" />
    <el-button type="primary" icon="el-icon-upload" style="position: absolute; bottom: 15px; margin-left: 40px;" @click="imagecropperShow = true">
      上传头像
    </el-button>

    <image-cropper
      v-show="imagecropperShow"
      :key="imagecropperKey"
      :width="300"
      :height="300"
      url=""
      lang-type="zh"
      @close="close"
      @crop-upload-success="cropSuccess"
    />

  </div>
</template>

<script>
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {
  name: 'AvatorUpload',
  components: { ImageCropper, PanThumb },
  data() {
    return {
      imagecropperShow: false,
      imagecropperKey: 0,
      image: ''
    }
  },
  methods: {
    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.image = resData.files.avatar
    },
    close() {
      this.imagecropperShow = false
    }
  }
}
</script>

<style scoped>
</style>

