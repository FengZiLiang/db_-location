<template>
    <div class='conmonitor' style="width: 100%;heigth: 100%">
        <div class='con-camera'>
          <el-row style="width: 100%;heigth: 100%">
            <el-col :span="12" v-for="item in playerDatas" v-bind:key="item.id">
              <div v-if="item.playerType === 'HLS'">
                <videoHlsFlv :videoid="'vhf' + item.playerId" :src="baseUrl + '/hls/' + item.playerId + '.m3u8'"></videoHlsFlv>
              </div>
              <div v-else-if="item.playerType === 'RTMP'">
                  <VideoRtmp :videoid="'vrf' + item.playerId" :src="'rtmp://192.168.1.112/live/' + item.playerId "></VideoRtmp>
              </div>
              <div v-else>
                <video>
                  <source :src="'' + item.playerId + ''">
                </video>
              </div>
            </el-col>
            <!-- <el-col span="12">
              <videoHlsFlv :videoid="'vhf' + playerDatas[1].playerId " :src="'http://192.168.1.112/hls/' + playerDatas[1].playerId + '.m3u8'"></videoHlsFlv>
            </el-col> -->
          </el-row>
        </div>
    </div>
</template>

<script>
import VideoHlsFlv from '@/components/videotool/VideoHlsFlv.vue'
import VideoRtmp from '@/components/videotool/VideoRtmp.vue'
export default {
  data () {
    return {
      baseUrl: this.$store.state.Url,
      cameraDatas: [],
      playerDatas: [],
      spinning: true
    }
  },
  components: {
    VideoHlsFlv,
    VideoRtmp
  },
  mounted: function () {
    console.log('获取数据')
    // 获取所有列表
    this.$get('decoder/camera').then(r => {
      console.log(r.data)
      let data = r.data
      // let decoderId = data.decoderId
      // 设置他的内容
      this.cameraDatas.push(data)
      console.log(this.cameraDatas)
      // 播放全部
      data.forEach((item, index, arr) => {
        this.$postJSON(
          '/decoder/manager/transcode',
          {
            srcId: item.decoderId,
            // srcId: 1234567890,
            outProtocol: 'HLS',
            outOptions: {
              codec: 'h264',
              fmt: 'flv',
              fps: '25',
              rs: '640x360',
              twoPart: '1'
            }
          }
        ).then(r => {
          let playerId = r.data
          let playerData = {
            playerId: r.data,
            playerType: 'HLS'
          }
          this.playerDatas.push(playerData)
          console.log(this.playerDatas)
        })
      })
    })
  }
}
</script>

<style scoped>
.con-camera {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  width: 100%;
  height: 100%;
}
</style>
