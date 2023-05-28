<template>
  <div class='con monitor' style="width: 100%" >
    <video
      :id="videoid"
      poster='videojs/eguidlogo.png'
      class='video-js
                vjs-default-skin
                vjs-big-play-centered'
      preload='auto'
      width='500'
      height='400'
      autoplay='autoplay'
    >
    </video>
  </div>

</template>

<script>
/*  下载的包
     'video.js': '^5.1.9',
     'videojs-flash': '^2.1.0'
     'videojs-swf': '^5.4.2',
  */
import videojs from 'video.js'
import 'video.js/dist/video-js.css'
import 'vue-video-player/src/custom-theme.css'
// import { videoPlayer } from 'vue-video-player';
import 'videojs-flash'
import SWF_URL from 'videojs-swf/dist/video-js.swf'

videojs.options.flash.swf = SWF_URL // 设置flash路径，Video.js会在不支持html5的浏览中使用flash播放视频文件
export default {
  name: 'videojs',
  props: {
    src: {
      type: String,
      required: true
    },
    videoid: {
      type: String,
      required: true
    }
  },
  // components: {
  //     videoPlayer
  // },
  mounted () {
    this.player1 = videojs(
      'my-player1',
      this.options1,
      function onPlayerReady () {
        videojs.log('播放器已经准备好了!')
        this.on('play', function () {
          console.log('开始/恢复播放')
        })
        this.on('pause', function () {
          console.log('暂停播放')
        })
        this.on('ended', function () {
          console.log('结束播放')
        })
      }
    )
    const vm = this
    vm.player1.play()
    vm.player1.bigPlayButton.show()
    // setTimeout(function() {
    //     vm.player1.player();
    // }, 1000);
  },
  data () {
    return {
      options1: {
        // autoplay: true,
        // loop: true,
        autoplay: true, // 是否自动播放
        muted: false, // 是否静音
        controls: false,
        fluid: true, // 宽高自适应
        bigPlayButton: 'hide', // 隐藏播放按钮
        dataSetup: {
          html5: { 'nativeTextTracks': false }
        },
        sources: [
          {
            src: this.src,
            type: 'rtmp/flv'
          }
        ],
        function () {
          this.play()
        }
      }
    }
  }
}
</script>

<style scoped>
</style>
