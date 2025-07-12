if (!("finalizeConstruction" in ViewPU.prototype)) {
    Reflect.set(ViewPU.prototype, "finalizeConstruction", () => { });
}
interface collaborate_Params {
}
export class collaborate extends ViewPU {
    constructor(parent, params, __localStorage, elmtId = -1, paramsLambda = undefined, extraInfo) {
        super(parent, __localStorage, elmtId, extraInfo);
        if (typeof paramsLambda === "function") {
            this.paramsGenerator_ = paramsLambda;
        }
        this.setInitiallyProvidedValue(params);
        this.finalizeConstruction();
    }
    setInitiallyProvidedValue(params: collaborate_Params) {
    }
    updateStateVars(params: collaborate_Params) {
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
            Column.debugLine("entry/src/main/ets/pages/collaboratepage.ets(4:5)", "entry");
            Column.width('100%');
            Column.height('100%');
            Column.padding({ left: 10, right: 10, top: 5, bottom: 5 });
        }, Column);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Row.create();
            Row.debugLine("entry/src/main/ets/pages/collaboratepage.ets(5:7)", "entry");
            Row.backgroundColor(Color.Blue);
            Row.width('100%');
            Row.height(50);
            Row.justifyContent(FlexAlign.Center);
            Row.border({ radius: 20 });
        }, Row);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Text.create('合作');
            Text.debugLine("entry/src/main/ets/pages/collaboratepage.ets(6:9)", "entry");
            Text.fontSize(25);
            Text.fontColor(Color.White);
        }, Text);
        Text.pop();
        Row.pop();
        Column.pop();
    }
    rerender() {
        this.updateDirtyElements();
    }
}
