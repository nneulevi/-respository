if (!("finalizeConstruction" in ViewPU.prototype)) {
    Reflect.set(ViewPU.prototype, "finalizeConstruction", () => { });
}
interface Dynamic_Params {
}
export class Dynamic extends ViewPU {
    constructor(parent, params, __localStorage, elmtId = -1, paramsLambda = undefined, extraInfo) {
        super(parent, __localStorage, elmtId, extraInfo);
        if (typeof paramsLambda === "function") {
            this.paramsGenerator_ = paramsLambda;
        }
        this.setInitiallyProvidedValue(params);
        this.finalizeConstruction();
    }
    setInitiallyProvidedValue(params: Dynamic_Params) {
    }
    updateStateVars(params: Dynamic_Params) {
    }
    purgeVariableDependenciesOnElmtId(rmElmtId) {
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id__());
        this.aboutToBeDeletedInternal();
    }
    initialRender() {
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Column.create();
            Column.debugLine("entry/src/main/ets/pages/dynamicpage.ets(4:7)", "entry");
            Column.width('100%');
            Column.height('100%');
            Column.padding({ left: 10, right: 10, top: 5, bottom: 5 });
        }, Column);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Row.create();
            Row.debugLine("entry/src/main/ets/pages/dynamicpage.ets(5:9)", "entry");
            Row.backgroundColor(Color.Blue);
            Row.width('100%');
            Row.height(50);
            Row.justifyContent(FlexAlign.Center);
            Row.border({ radius: 20 });
        }, Row);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Text.create('行业动态');
            Text.debugLine("entry/src/main/ets/pages/dynamicpage.ets(6:11)", "entry");
            Text.fontSize(25);
            Text.fontColor(Color.White);
        }, Text);
        Text.pop();
        Row.pop();
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Row.create();
            Row.debugLine("entry/src/main/ets/pages/dynamicpage.ets(16:9)", "entry");
            Row.width('100%');
            Row.border({ radius: 20 });
            Row.padding({ left: 8, right: 8 });
        }, Row);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Image.create({ "id": 16777223, "type": 20000, params: [], "bundleName": "com.example.cemenghui", "moduleName": "entry" });
            Image.debugLine("entry/src/main/ets/pages/dynamicpage.ets(17:11)", "entry");
            Image.width(22);
            Image.fillColor(Color.Gray);
        }, Image);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            TextInput.create({ placeholder: '请输入搜索关键词' });
            TextInput.debugLine("entry/src/main/ets/pages/dynamicpage.ets(20:11)", "entry");
            TextInput.layoutWeight(1);
        }, TextInput);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Button.createWithLabel('搜索');
            Button.debugLine("entry/src/main/ets/pages/dynamicpage.ets(22:11)", "entry");
            Button.backgroundColor('#F5F5F5');
            Button.fontColor('#5A5A5A');
        }, Button);
        Button.pop();
        Row.pop();
        Column.pop();
    }
    rerender() {
        this.updateDirtyElements();
    }
}
