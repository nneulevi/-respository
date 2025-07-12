import { AppStorageV2 } from "@kit.ArkUI"

@Component
export struct technique{

  pathStack:NavPathStack=AppStorageV2.connect(NavPathStack,'navStack',()=>new NavPathStack())!

  build() {
    Column(){
      Row(){
        Text('技术')
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

      Image($r('app.media.tech1'))
        .width('100%')
        .onClick(()=>{
          this.pathStack.pushPathByName('Play',null,false)
        })
      Image($r('app.media.tech'))
        .width("100%")
        .onClick(()=>{
          this.pathStack.pushPathByName('Play',null,false)
        })

    }
    .width('100%')
    .height('100%')
    .padding({left:10,right:10,top:5,bottom:5})
    .backgroundColor('#F5F8FB')
  }
}