<template>
  <div ref="w" :id="videoid"></div>
</template>

<script scoped>
import Chimee from 'chimee'
import flv from 'chimee-kernel-flv'
import hls from 'chimee-kernel-hls'
var chimee
export default {
  props: {
    src: {
      type: String,
      required: true
    },
    controls: {
      type: Boolean,
      default: true
    },
    autoplay: {
      type: Boolean,
      default: true
    },
    videoid: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      chimee
    }
  },
  activated: function () {
    this.chimee.play()
  },
  mounted: function () {
    this.$nextTick(() => {
      this.chimee = new Chimee({
        wrapper: this.$refs.w,
        // '#' + this.videoid,
        src: this.src,
        controls: this.controls,
        autoplay: this.autoplay,
        kernels: {
          flv,
          hls
        }
      })
      this.chimee.play()
    }
    )
  }
}
</script>
