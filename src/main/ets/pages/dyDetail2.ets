
interface dyDetailType{
  name:string
  id:string
}

@Builder
export function dyDetailBuilder(){
  dyDetail()
}

@ComponentV2
struct dyDetail{
  @Local panelHeight:string='0%'
  @Local panelOpacity:number=0
  pathStack:NavPathStack=new NavPathStack();


  build() {
    NavDestination(){
      Column() {
        // 标题部分
        Text('国际电联启动6G标准制定，中国提交35%关键技术提案')
          .fontSize(24)
          .fontWeight(FontWeight.Bold)
          .margin({ top: 20, bottom: 10 })
          .width('90%')

        // 作者和时间信息
        Row() {
          Text('adminww')
            .fontSize(12)
            .fontColor('#666666')
          Text('2024/6/18 09:15:00')
            .fontSize(12)
            .fontColor('#666666')
            .margin({ left: 10 })
          Text('5467浏览')
            .fontSize(12)
            .fontColor('#666666')
            .margin({ left: 10 })
        }
        .width('90%')
        .margin({ bottom: 20 })

        // 正文内容
        Column() {
          Text('ITU-R会议确定：')
            .fontSize(16)
            .fontWeight(FontWeight.Bold)
            .margin({ bottom: 10 })

          Text('● 太赫兹通信为核心方向')
            .fontSize(14)
            .margin({ bottom: 5 })
          Text('● AI原生网络架构')
            .fontSize(14)
            .margin({ bottom: 5 })
          Text('● 华为、爱立信等成立联合创新组')
            .fontSize(14)
            .margin({ bottom: 15 })

          Text('我国已在北京、深圳建设6G试验网。')
            .fontSize(14)
        }
        .width('90%')
        .padding(10)
        .backgroundColor('#f5f5f5')
        .borderRadius(8)
        .margin({ bottom: 20 })

        // 6G图片部分
        Stack() {
          // 蓝色背景
          Column() {
            // 留空作为蓝色背景
          }
          .width('100%')
          .height(200)
          .backgroundColor('#1976d2')
          .borderRadius(8)

          Row() {
            Image($r('app.media.dyy2'))
          }
          .alignItems(VerticalAlign.Center)
          .justifyContent(FlexAlign.Center)
          .width('100%')
          .height(200)

        }
        .width('90%')
        .height(200)
        .margin({ bottom: 20 })
      }
      .width('100%')
      .alignItems(HorizontalAlign.Center)

    }
    .onReady((context:NavDestinationContext)=>{
      this.pathStack=context.pathStack
    })
    .hideTitleBar(true)
  }
}