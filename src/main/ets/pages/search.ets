
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
      Column(){
        Row(){
          Text('行业动态')
            .fontSize(25)
            .fontColor(Color.White)
        }
        .backgroundColor(Color.Blue)
        .width('100%')
        .height(50)
        .justifyContent(FlexAlign.Center)
        .border({radius:20})

        Row(){
          Image($r('app.media.search'))
            .width(22)
            .fillColor(Color.Gray)
          TextInput({placeholder:'请输入搜索关键词'})
            .layoutWeight(1)
          Button('搜索')
            .backgroundColor('#F5F5F5')
            .fontColor('#5A5A5A')
        }
        .width('100%')
        .border({radius:20})
        .padding({left:8,right:8})

        Image($r('app.media.dy1'))
          .width('100%')

      }

    }
    .onReady((context:NavDestinationContext)=>{
      this.pathStack=context.pathStack
    })
    .hideTitleBar(true)
  }
}