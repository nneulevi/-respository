
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
            Text('公益活动')
              .fontSize(25)
              .fontColor(Color.White)
          }
          .backgroundColor(Color.Blue)
          .width('100%')
          .height(50)
          .justifyContent(FlexAlign.Center)
          .border({radius:20})
          .padding({left:8,right:8})

          Image($r('app.media.public'))
            .width('100%')
            .height('auto')
            .objectFit(ImageFit.Contain)

          Text('上滑查看完整证书内容')
            .fontSize(12)
            .margin({ top: 10 })
        }
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