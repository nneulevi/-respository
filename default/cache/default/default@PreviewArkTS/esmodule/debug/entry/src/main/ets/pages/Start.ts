if (!("finalizeConstruction" in ViewPU.prototype)) {
    Reflect.set(ViewPU.prototype, "finalizeConstruction", () => { });
}
interface Start_Params {
    pathStack?: NavPathStack;
}
export function StarBuilder(parent = null) {
    {
        (parent ? parent : this).observeComponentCreation2((elmtId, isInitialRender) => {
            if (isInitialRender) {
                let componentCall = new Start(parent ? parent : this, {}, undefined, elmtId, () => { }, { page: "entry/src/main/ets/pages/Start.ets", line: 3, col: 3 });
                ViewPU.create(componentCall);
                let paramsLambda = () => {
                    return {};
                };
                componentCall.paramsGenerator_ = paramsLambda;
            }
            else {
                (parent ? parent : this).updateStateVarsOfChildByElmtId(elmtId, {});
            }
        }, { name: "Start" });
    }
}
class Start extends ViewPU {
    constructor(parent, params, __localStorage, elmtId = -1, paramsLambda = undefined, extraInfo) {
        super(parent, __localStorage, elmtId, extraInfo);
        if (typeof paramsLambda === "function") {
            this.paramsGenerator_ = paramsLambda;
        }
        this.pathStack = new NavPathStack();
        this.setInitiallyProvidedValue(params);
        this.finalizeConstruction();
    }
    setInitiallyProvidedValue(params: Start_Params) {
        if (params.pathStack !== undefined) {
            this.pathStack = params.pathStack;
        }
    }
    updateStateVars(params: Start_Params) {
    }
    purgeVariableDependenciesOnElmtId(rmElmtId) {
    }
    aboutToBeDeleted() {
        SubscriberManager.Get().delete(this.id__());
        this.aboutToBeDeletedInternal();
    }
    private pathStack: NavPathStack;
    aboutToAppear(): void {
        setTimeout(() => {
            this.pathStack.replacePathByName("Layout", null, false);
        }, 3000);
    }
    initialRender() {
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            NavDestination.create(() => {
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    Stack.create({ alignContent: Alignment.TopEnd });
                    Stack.debugLine("entry/src/main/ets/pages/Start.ets(18:7)", "entry");
                }, Stack);
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    Image.create({ "id": 16777222, "type": 20000, params: [], "bundleName": "com.example.cemenghui", "moduleName": "entry" });
                    Image.debugLine("entry/src/main/ets/pages/Start.ets(19:9)", "entry");
                    Image.width('100%');
                    Image.height('100%');
                    Image.expandSafeArea([SafeAreaType.SYSTEM], [SafeAreaEdge.TOP, SafeAreaEdge.BOTTOM]);
                }, Image);
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    Button.createWithLabel('跳过');
                    Button.debugLine("entry/src/main/ets/pages/Start.ets(23:9)", "entry");
                    Button.backgroundColor(Color.Gray);
                    Button.margin(15);
                    Button.onClick(() => {
                        this.pathStack.replacePathByName("Layout", null, false);
                    });
                }, Button);
                Button.pop();
                Stack.pop();
            }, { moduleName: "entry", pagePath: "entry/src/main/ets/pages/Start" });
            NavDestination.onReady((context: NavDestinationContext) => {
                this.pathStack = context.pathStack;
            });
            NavDestination.debugLine("entry/src/main/ets/pages/Start.ets(17:5)", "entry");
        }, NavDestination);
        NavDestination.pop();
    }
    rerender() {
        this.updateDirtyElements();
    }
}
(function () {
    if (typeof NavigationBuilderRegister === "function") {
        NavigationBuilderRegister("Start", wrapBuilder(StarBuilder));
    }
})();
