import { AppStorageV2 } from "@kit.ArkUI"

@Component
export struct collaborate{

  pathStack:NavPathStack=AppStorageV2.connect(NavPathStack,'navStack',()=>new NavPathStack())!


  build() {
    Column(){
      Row(){
        Text('合作')
          .fontSize(25)
          .fontColor(Color.White)
      }
      .backgroundColor(Color.Blue)
      .width('100%')
      .height(50)
      .justifyContent(FlexAlign.Center)
      .border({radius:20})

      Row(){
        Image($r('app.media.c1'))
          .width('45%')
          .height('150')
          .margin({left:10,right:10})
          .border({radius:20})
          .onClick(()=>{
            this.pathStack.pushPathByName('meet',null,false)
          })

        Image($r('app.media.c2'))
          .width('45%')
          .height('150')
          .margin({left:10,right:10})
          .border({radius:20})
          .onClick(()=>{
            this.pathStack.pushPathByName('stand',null,false)
          })
      }
      .width('100%')
      .margin({top:20,bottom:20})

      Row(){
        Image($r('app.media.c3'))
          .width('45%')
          .height('150')
          .margin({left:10,right:10})
          .border({radius:20})

        Image($r('app.media.c4'))
          .width('45%')
          .height('150')
          .margin({left:10,right:10})
          .border({radius:20})
          .onClick(()=>{
            this.pathStack.pushPathByName('tool',null,false)
          })
      }
      .width('100%')
      .margin({top:20,bottom:20})

      Row(){
        Image($r('app.media.c5'))
          .width('45%')
          .height('150')
          .margin({left:10,right:10})
          .border({radius:20})
          .onClick(()=>{
            this.pathStack.pushPathByName('public',null,false)
          })
      }
      .width('100%')
      .margin({top:20,bottom:20})

    }
    .width('100%')
    .height('100%')
    .padding({left:10,right:10,top:5,bottom:5})
  }
  }