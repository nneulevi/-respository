if (!("finalizeConstruction" in ViewPU.prototype)) {
    Reflect.set(ViewPU.prototype, "finalizeConstruction", () => { });
}
import { collaborate } from "@normalized:N&&&entry/src/main/ets/pages/collaboratepage&";
import { Dynamic } from "@normalized:N&&&entry/src/main/ets/pages/dynamicpage&";
import { Home } from "@normalized:N&&&entry/src/main/ets/pages/Homepage&";
import { technique } from "@normalized:N&&&entry/src/main/ets/pages/techniquepage&";
interface TabClass {
    text: string;
    icon: ResourceStr;
}
export function LayoutBuilder(parent = null) {
    {
        (parent ? parent : this).observeComponentCreation2((elmtId, isInitialRender) => {
            if (isInitialRender) {
                let componentCall = new Layout(parent ? parent : this, {}, undefined, elmtId, () => { }, { page: "entry/src/main/ets/pages/Layout.ets", line: 13, col: 3 });
                ViewV2.create(componentCall);
                let paramsLambda = () => {
                    return {};
                };
                componentCall.paramsGenerator_ = paramsLambda;
            }
            else {
                (parent ? parent : this).updateStateVarsOfChildByElmtId(elmtId, {});
            }
        }, { name: "Layout" });
    }
}
class Layout extends ViewV2 {
    constructor(parent, params, __localStorage, elmtId = -1, paramsLambda, extraInfo) {
        super(parent, elmtId, extraInfo);
        this.currentIndex = 0;
        this.pathStack = new NavPathStack();
        this.tabData = [
            { text: '首页', icon: { "id": 16777236, "type": 20000, params: [], "bundleName": "com.example.cemenghui", "moduleName": "entry" } },
            { text: '动态', icon: { "id": 16777234, "type": 20000, params: [], "bundleName": "com.example.cemenghui", "moduleName": "entry" } },
            { text: '技术', icon: { "id": 16777235, "type": 20000, params: [], "bundleName": "com.example.cemenghui", "moduleName": "entry" } },
            { text: '合作', icon: { "id": 16777225, "type": 20000, params: [], "bundleName": "com.example.cemenghui", "moduleName": "entry" } },
        ];
        this.finalizeConstruction();
    }
    public resetStateVarsOnReuse(params: Object): void {
        this.currentIndex = 0;
    }
    @Local
    currentIndex: number;
    pathStack: NavPathStack;
    tabData: TabClass[];
    tabBuilder(item: TabClass, index: number, parent = null) {
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Column.create({ space: 5 });
            Column.debugLine("entry/src/main/ets/pages/Layout.ets(29:5)", "entry");
        }, Column);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Image.create(item.icon);
            Image.debugLine("entry/src/main/ets/pages/Layout.ets(30:7)", "entry");
            Image.width(24);
            Image.fillColor(this.currentIndex === index ? Color.Pink : Color.Black);
        }, Image);
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            Text.create(item.text);
            Text.debugLine("entry/src/main/ets/pages/Layout.ets(33:7)", "entry");
            Text.fontColor(this.currentIndex === index ? Color.Blue : Color.Black);
        }, Text);
        Text.pop();
        Column.pop();
    }
    initialRender() {
        this.observeComponentCreation2((elmtId, isInitialRender) => {
            NavDestination.create(() => {
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    Tabs.create({ barPosition: BarPosition.End });
                    Tabs.debugLine("entry/src/main/ets/pages/Layout.ets(40:7)", "entry");
                    Tabs.onChange((index: number) => {
                        this.currentIndex = index;
                    });
                }, Tabs);
                this.observeComponentCreation2((elmtId, isInitialRender) => {
                    ForEach.create();
                    const forEachItemGenFunction = (_item, index: number) => {
                        const item = _item;
                        this.observeComponentCreation2((elmtId, isInitialRender) => {
                            TabContent.create(() => {
                                this.observeComponentCreation2((elmtId, isInitialRender) => {
                                    If.create();
                                    if (this.currentIndex === 0) {
                                        this.ifElseBranchUpdateFunction(0, () => {
                                            {
                                                this.observeComponentCreation2((elmtId, isInitialRender) => {
                                                    if (isInitialRender) {
                                                        let componentCall = new Home(this, {}, undefined, elmtId, () => { }, { page: "entry/src/main/ets/pages/Layout.ets", line: 44, col: 15 });
                                                        ViewPU.create(componentCall);
                                                        let paramsLambda = () => {
                                                            return {};
                                                        };
                                                        componentCall.paramsGenerator_ = paramsLambda;
                                                    }
                                                    else {
                                                        this.updateStateVarsOfChildByElmtId(elmtId, {});
                                                    }
                                                }, { name: "Home" });
                                            }
                                        });
                                    }
                                    else if (this.currentIndex === 1) {
                                        this.ifElseBranchUpdateFunction(1, () => {
                                            {
                                                this.observeComponentCreation2((elmtId, isInitialRender) => {
                                                    if (isInitialRender) {
                                                        let componentCall = new Dynamic(this, {}, undefined, elmtId, () => { }, { page: "entry/src/main/ets/pages/Layout.ets", line: 46, col: 15 });
                                                        ViewPU.create(componentCall);
                                                        let paramsLambda = () => {
                                                            return {};
                                                        };
                                                        componentCall.paramsGenerator_ = paramsLambda;
                                                    }
                                                    else {
                                                        this.updateStateVarsOfChildByElmtId(elmtId, {});
                                                    }
                                                }, { name: "Dynamic" });
                                            }
                                        });
                                    }
                                    else if (this.currentIndex === 2) {
                                        this.ifElseBranchUpdateFunction(2, () => {
                                            {
                                                this.observeComponentCreation2((elmtId, isInitialRender) => {
                                                    if (isInitialRender) {
                                                        let componentCall = new technique(this, {}, undefined, elmtId, () => { }, { page: "entry/src/main/ets/pages/Layout.ets", line: 48, col: 15 });
                                                        ViewPU.create(componentCall);
                                                        let paramsLambda = () => {
                                                            return {};
                                                        };
                                                        componentCall.paramsGenerator_ = paramsLambda;
                                                    }
                                                    else {
                                                        this.updateStateVarsOfChildByElmtId(elmtId, {});
                                                    }
                                                }, { name: "technique" });
                                            }
                                        });
                                    }
                                    else if (this.currentIndex === 3) {
                                        this.ifElseBranchUpdateFunction(3, () => {
                                            {
                                                this.observeComponentCreation2((elmtId, isInitialRender) => {
                                                    if (isInitialRender) {
                                                        let componentCall = new collaborate(this, {}, undefined, elmtId, () => { }, { page: "entry/src/main/ets/pages/Layout.ets", line: 50, col: 15 });
                                                        ViewPU.create(componentCall);
                                                        let paramsLambda = () => {
                                                            return {};
                                                        };
                                                        componentCall.paramsGenerator_ = paramsLambda;
                                                    }
                                                    else {
                                                        this.updateStateVarsOfChildByElmtId(elmtId, {});
                                                    }
                                                }, { name: "collaborate" });
                                            }
                                        });
                                    }
                                    else {
                                        this.ifElseBranchUpdateFunction(4, () => {
                                        });
                                    }
                                }, If);
                                If.pop();
                            });
                            TabContent.tabBar({ builder: () => {
                                    this.tabBuilder.call(this, item, index);
                                } });
                            TabContent.debugLine("entry/src/main/ets/pages/Layout.ets(42:11)", "entry");
                        }, TabContent);
                        TabContent.pop();
                    };
                    this.forEachUpdateFunction(elmtId, this.tabData, forEachItemGenFunction, undefined, true, false);
                }, ForEach);
                ForEach.pop();
                Tabs.pop();
            }, { moduleName: "entry", pagePath: "entry/src/main/ets/pages/Layout" });
            NavDestination.onReady((context: NavDestinationContext) => {
                this.pathStack = context.pathStack;
            });
            NavDestination.debugLine("entry/src/main/ets/pages/Layout.ets(39:5)", "entry");
        }, NavDestination);
        NavDestination.pop();
    }
    rerender() {
        this.updateDirtyElements();
    }
}
(function () {
    if (typeof NavigationBuilderRegister === "function") {
        NavigationBuilderRegister("Layout", wrapBuilder(LayoutBuilder));
    }
})();
