import { AppStorageV2 } from "@kit.ArkUI"

interface dyDetail{
  name:string
  id:string
}

@Component
export struct Dynamic {

  recommend:dyDetail[]=[
    { name:"app.media.dy7",id:'dyDetail2' },
    { name:"app.media.dy1",id:'dyDetail1' },
    { name:"app.media.dy2",id:'dyDetail2' },
    { name:"app.media.dy3",id:'dyDetail1' },
    { name:"app.media.dy4",id:'dyDetail1' },
    { name:"app.media.dy5",id:'dyDetail1' },
    { name:"app.media.dy6",id:'dyDetail1' },
  ]
  pathStack:NavPathStack=AppStorageV2.connect(NavPathStack,'navStack',()=>new NavPathStack())!

  build() {
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
            .onClick(()=>{
              this.pathStack.pushPathByName('search',null,false)
            })
        }
        .width('100%')
        .border({radius:20})
        .padding({left:8,right:8})

        List(){
          ForEach(this.recommend,(item:dyDetail)=>{
            ListItem(){
              Column(){
              Image($r(item.name))
                .width('100%')
                .onClick(()=>{
                  this.pathStack.pushPathByName(item.id,null,false)
                })
              }
              .width('100%')
              .height(110)
              .backgroundColor(Color.Blue)
              .border({radius:10})
              .margin({top:15,bottom:15})
              .shadow({ radius: 10, color: '#888888', offsetX: 2, offsetY: 2 })
            }
          })
        }

      }
      .width('100%')
      .height('100%')
      .padding({left:10,right:10,top:5,bottom:5})
    }
}