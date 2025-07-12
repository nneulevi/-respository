
@Component
export struct Home{

  swiperList:string[]=[
    'app.media.lb1',
    'app.media.lb2'
  ]

    build() {
      Column(){
        Row(){
          Text('测盟汇')
            .fontSize(25)
            .fontColor(Color.White)
        }
        .backgroundColor(Color.Blue)
        .width('100%')
        .height(50)
        .justifyContent(FlexAlign.Center)
        .border({radius:20})
        .padding({left:8,right:8})

        Swiper(){
          ForEach(this.swiperList,(item:string)=>{
            Image($r(item))
              .width("100%")
              .height(200)
              .border({radius:10})
              .onClick(()=>{
              })
          })
        }
        .autoPlay(true)
        .margin({top:20,bottom:20})

        Image($r('app.media.introduction'))
          .width("100%")
          .border({
            radius: 20,
          })
          .shadow({ radius: 10, color: '#888888', offsetX: 2, offsetY: 2 })


      }
      .width('100%')
      .height('100%')
      .padding({left:10,right:10,top:5,bottom:5})
    }
}