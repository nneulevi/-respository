import { collaborate } from "./collaboratepage"
import { Dynamic } from "./dynamicpage"
import { Home } from "./Homepage"
import { technique } from "./techniquepage"

interface TabClass{
  text:string
  icon:ResourceStr
}

@Builder
export function LayoutBuilder(){
  Layout()
}

@ComponentV2
struct Layout{
  @Local currentIndex:number=0

  pathStack: NavPathStack=new NavPathStack()
  tabData:TabClass[]=[
    {text:'首页',icon:$r('app.media.ic1')},
    {text:'动态',icon:$r('app.media.ic2')},
    {text:'技术',icon:$r('app.media.ic3')},
    {text:'合作',icon:$r('app.media.ic4')},
  ]

  @Builder tabBuilder(item:TabClass,index:number){
    Column({space:5}){
      Image(item.icon)
        .width(24)
        .fillColor(this.currentIndex===index?Color.Pink:Color.Black)
      Text(item.text)
        .fontColor(this.currentIndex===index?Color.Blue:Color.Black)
    }
  }

  build() {
    NavDestination(){
      Tabs({barPosition:BarPosition.End}){
        ForEach(this.tabData,(item:TabClass,index:number)=>{
          TabContent(){
            if(this.currentIndex===0){
              Home()
            }else if(this.currentIndex===1){
              Dynamic()
            }else if(this.currentIndex===2){
              technique()
            }else if(this.currentIndex===3){
              collaborate()
            }
          }
          .tabBar(this.tabBuilder(item,index))
        })
      }
      .onChange((index:number)=>{
        this.currentIndex=index
      })
    }
    .onReady((context:NavDestinationContext) =>{
      this.pathStack = context.pathStack
    })
  }
}