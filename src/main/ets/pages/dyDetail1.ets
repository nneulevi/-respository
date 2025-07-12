
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
        .padding({left:8,right:8})

        Image($r('app.media.dD1'))
          .width('100%')
      }

    }
    .onReady((context:NavDestinationContext)=>{
      this.pathStack=context.pathStack
    })
    .hideTitleBar(true)
  }
}