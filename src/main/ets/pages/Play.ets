import router from '@ohos.router';
@Builder
export function dyDetailBuilder(){
  dyDetail()
}

@ComponentV2
struct dyDetail{
  @Local panelHeight:string='0%'
  @Local panelOpacity:number=0
    pathStack:NavPathStack=new NavPathStack();
    isPlaying: boolean = false;
    currentTime: number = 0;
    duration: number = 0;
    progress: number = 0;
  private videoController: VideoController = new VideoController();


  formatTime(seconds: number): string {
    const mins: number = Math.floor(seconds / 60);
    const secs: number = Math.floor(seconds % 60);
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  }

  togglePlay(): void {
    if (this.isPlaying) {
      this.videoController.pause();
      this.isPlaying = false;
    } else {
      this.videoController.start();
      this.isPlaying = true;
    }
  }

  seekTo(value: number): void {
    const seekTime: number = (value / 100) * this.duration;
    this.videoController.setCurrentTime(seekTime);
  }

  build() {
    NavDestination(){
      Column() {
        // 顶部导航栏
        Row() {
          Button('返回')
            .width(60)
            .height(32)
            .backgroundColor('transparent')
            .fontColor('#007AFF')
            .fontSize(16)
            .onClick(() => {
              router.back();
            })
          Text('视频播放')
            .fontSize(18)
            .fontWeight(FontWeight.Medium)
            .margin({ left: 16 })
        }
        .width('100%')
        .height(56)
        .padding({ left: 16, right: 16 })
        .backgroundColor(Color.White)
        .justifyContent(FlexAlign.Start)
        .alignItems(VerticalAlign.Center)

        // 视频播放区域
        Video({
          src: $r('app.media.video1'),
          controller: this.videoController
        })
          .width('100%')
          .height(300)
          .objectFit(ImageFit.Contain)
          .autoPlay(false)
          .controls(false)
          .backgroundColor(Color.Black)
          .onPrepared((e) => {
            // 获取视频实际时长
            if (e && e.duration !== undefined) {
              this.duration = e.duration;
            } else {
              // 如果无法获取时长，设置一个默认值
              this.duration = 10;
            }
            this.progress = 0;
            this.currentTime = 0;
          })
          .onUpdate((e) => {
            if (e && e.time !== undefined) {
              this.currentTime = e.time;
              this.progress = this.duration > 0 ? (this.currentTime / this.duration) * 100 : 0;
            }
          })
          .onFinish(() => {
            // 视频播放完成时的处理
            this.isPlaying = false;
            this.currentTime = 0;
            this.progress = 0;
          })
          .onError(() => {
            console.error('Video playback error');
          })

        // 播放控制区域
        Column() {
          // 进度条
          Row() {
            Text(this.formatTime(this.currentTime))
              .fontSize(12)
              .fontColor('#666666')

            Slider({
              value: this.progress,
              min: 0,
              max: 100,
              step: 1
            })
              .layoutWeight(1)
              .margin({ left: 8, right: 8 })
              .onChange((value: number) => {
                this.seekTo(value);
              })


            Text(this.formatTime(this.duration))
              .fontSize(12)
              .fontColor('#666666')
          }
          .width('100%')
          .padding({ left: 16, right: 16 })
          .margin({ top: 16 })

          // 播放/暂停按钮
          Row() {
            Button(this.isPlaying ? '暂停' : '播放')
              .width(120)
              .height(40)
              .backgroundColor(this.isPlaying ? '#FF6B6B' : '#4CAF50')
              .fontColor(Color.White)
              .borderRadius(20)
              .onClick(() => {
                this.togglePlay();
              })
          }
          .width('100%')
          .justifyContent(FlexAlign.Center)
          .margin({ top: 16 })
        }
        .width('100%')
        .padding({ top: 16, bottom: 16 })
        .backgroundColor(Color.White)
      }
      .width('100%')
      .height('100%')
      .backgroundColor('#F5F5F5')
    }
    .onReady((context:NavDestinationContext)=>{
      this.pathStack=context.pathStack
    })
    .hideTitleBar(true)
  }
}