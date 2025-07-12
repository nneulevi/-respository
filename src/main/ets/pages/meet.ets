
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
      Scroll() {
        Column() {

          Row(){
            Text('会议')
              .fontSize(25)
              .fontColor(Color.White)
          }
          .backgroundColor(Color.Blue)
          .width('100%')
          .height(50)
          .justifyContent(FlexAlign.Center)
          .border({radius:20})
          .padding({left:8,right:8})

          Image($r('app.media.lb1'))
            .width('100%')
            .height('auto')
            .objectFit(ImageFit.Contain)

          Text('公司游戏开发大会')
            .fontSize(24)
            .fontWeight(FontWeight.Bold)
            .margin(10)

          // 时间地点
          Text('2024/3/18 09:00 - 3/22 18:00')
            .fontSize(14)
            .margin(5)
          Text('公司一号楼')
            .fontSize(14)
            .margin(5)

          // 行业趋势列表
          Column() {
            Text('行业趋势：')
              .fontSize(16)
              .margin({top:10,bottom:8})

            Image($r('app.media.meet1'))
              .width('100%')

            Text('- 元宇宙游戏引擎')
            Text('- AIGC内容生产')
            Text('- 云游戏优化')
            Text('- VR触觉反馈')

            Row() {
              Text('会议议程')
                .fontSize(20)
                .fontWeight(FontWeight.Bold)

            }
            .width('100%')
            .padding(10)

            Column() {
              Text('09:00-09:30 (30分钟)')
                .fontColor('#666666')

              Row() {
                Text('项目启动介绍')
                  .fontSize(16)

              }

              Text('参会者：adminww')
              Text('发言时长：30分钟')
              Text('游戏前景，技术创新等项目介绍')
                .margin({top: 5})
            }
            .width('100%')
            .padding(10)
            .border({width: 1, color: '#EEEEEE'})
            .margin({top: 10})

            Column() {
              Text('09:00-09:00 (0分钟)')
                .fontColor('#666666')

              Row() {
                Text('暂无标题')
                  .fontSize(16)

              }

              Text('参会者：adminww')
              Text('发言时长：0分钟')
              Text('暂无详细内容')
                .margin({top: 5})
            }
            .width('100%')
            .padding(10)
            .border({width: 1, color: '#EEEEEE'})
            .margin({top: 10})

            Image($r('app.media.meet2'))

          }
          .padding(10)
          .backgroundColor(Color.White)
          .borderRadius(8)
          .margin(10)

        }
        .width('100%')
        .padding(20)



      }
      .width('100%')
      .height('100%')
      .scrollBar(BarState.On) // 显示滚动条
      .scrollable(ScrollDirection.Vertical) // 限制垂直滚动

    }
    .onReady((context:NavDestinationContext)=>{
      this.pathStack=context.pathStack
    })
    .hideTitleBar(true)
  }
}