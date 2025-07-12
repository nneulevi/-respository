if (!("finalizeConstruction" in ViewPU.prototype)) {
    Reflect.set(ViewPU.prototype, "finalizeConstruction", () => { });
}
interface Home_Params {
    swiperList?: string[];
    detailList?: detail[];
}
interface detail {
    name: string;
    img: string;
}
export class Home extends ViewPU {
    constructor(parent, params, __localStorage, elmtId = -1, paramsLambda = undefined, extraInfo) {
        super(parent, __localStorage, elmtId, extraInfo);
        if (typeof paramsLambda === "function") {
            this.paramsGenerator_ = paramsLambda;
        }
        this.swiperList = [
            'app.media.lb1',
            'app.media.lb2'
        ];
        this.detailList = [
            { name: '道普信息', img: 'app.media.search' },
            { name: '上海计算机', img: 'app.media.search' },
            { name: '北方实验室', img: 'app.media.search' },
            { name: '湖南佳策', img: 'app.media.search' },
            { name: '上海软评', img: 'app.media.search' },
            { name: '吉林电检院', img: 'app.media.search' },
            { name: '重庆软评', img: 'app.media.search' },
            { name: '西安863', img: 'app.media.search' },
            { name: '江苏软检', img: 'app.media.search' },
            { name: '北京软质检', img: 'app.media.search' },
            { name: '天津软评', img: 'app.media.search' }
        ];
        this.setInitiallyProvidedValue(params);
        this.finalizeConstruction();
    }
    setInitiallyProvidedValue(params: Home_Params) {
        if (params.swiperList !== undefined) {
            this.swiperList = params.swiperList;
        }
        if (params.detailList !== undefined) {
            this.detailList = params.detailList;
        }
    }
    updateStateVars(params: Home_Params) {
    }
    purgeVariableDependenciesOnElmtId(rmElmtId) {
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id__());
        this.aboutToBeDeletedInternal();
    }
    private swiperList: string[];
    initialRender() {
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Column.create();
            Column.debugLine("entry/src/main/ets/pages/Homepage.ets(15:7)", "entry");
            Column.width('100%');
            Column.height('100%');
            Column.padding({ left: 10, right: 10, top: 5, bottom: 5 });
        }, Column);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Row.create();
            Row.debugLine("entry/src/main/ets/pages/Homepage.ets(16:9)", "entry");
            Row.backgroundColor(Color.Blue);
            Row.width('100%');
            Row.height(50);
            Row.justifyContent(FlexAlign.Center);
            Row.border({ radius: 20 });
            Row.padding({ left: 8, right: 8 });
        }, Row);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Text.create('测盟汇');
            Text.debugLine("entry/src/main/ets/pages/Homepage.ets(17:11)", "entry");
            Text.fontSize(25);
            Text.fontColor(Color.White);
        }, Text);
        Text.pop();
        Row.pop();
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Swiper.create();
            Swiper.debugLine("entry/src/main/ets/pages/Homepage.ets(28:9)", "entry");
            Swiper.autoPlay(true);
            Swiper.margin({ top: 20, bottom: 20 });
        }, Swiper);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            ForEach.create();
            const forEachItemGenFunction = _item => {
                const item = _item;
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    Image.create({ "id": -1, "type": -1, params: [item], "bundleName": "com.example.cemenghui", "moduleName": "entry" });
                    Image.debugLine("entry/src/main/ets/pages/Homepage.ets(30:13)", "entry");
                    Image.width("100%");
                    Image.border({ radius: 10 });
                }, Image);
            };
            this.forEachUpdateFunction(elmtId, this.swiperList, forEachItemGenFunction);
        }, ForEach);
        ForEach.pop();
        Swiper.pop();
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Image.create({ "id": 16777233, "type": 20000, params: [], "bundleName": "com.example.cemenghui", "moduleName": "entry" });
            Image.debugLine("entry/src/main/ets/pages/Homepage.ets(38:9)", "entry");
        }, Image);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            //
            Grid.create();
            Grid.debugLine("entry/src/main/ets/pages/Homepage.ets(41:9)", "entry");
            //
            Grid.columnsTemplate('1fr 1fr 1fr 1fr');
            //
            Grid.rowsGap(8);
            //
            Grid.columnsGap(8);
            //
            Grid.padding(12);
        }, Grid);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            ForEach.create();
            const forEachItemGenFunction = _item => {
                const item = _item;
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    Column.create();
                    Column.debugLine("entry/src/main/ets/pages/Homepage.ets(43:13)", "entry");
                    Column.padding(8);
                    Column.onClick(() => {
                        // 点击跳转到详情页，并传递参数
                        router.pushUrl({
                            url: 'pages/Detail',
                            params: { name: item.name }
                        });
                    });
                }, Column);
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    Image.create({ "id": -1, "type": -1, params: [item.img], "bundleName": "com.example.cemenghui", "moduleName": "entry" });
                    Image.debugLine("entry/src/main/ets/pages/Homepage.ets(44:15)", "entry");
                    Image.width(48);
                    Image.height(48);
                    Image.margin({ bottom: 6 });
                }, Image);
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    Text.create(item.name);
                    Text.debugLine("entry/src/main/ets/pages/Homepage.ets(47:15)", "entry");
                    Text.fontSize(12);
                    Text.textAlign(TextAlign.Center);
                }, Text);
                Text.pop();
                Column.pop();
            };
            this.forEachUpdateFunction(elmtId, this.detailList, forEachItemGenFunction);
        }, ForEach);
        ForEach.pop();
        //
        Grid.pop();
        Column.pop();
    }
    private detailList: detail[];
    rerender() {
        this.updateDirtyElements();
    }
}
