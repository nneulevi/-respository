/// <reference types="vite/client" />
declare module '*.vue' {
    import type { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
}

// 添加模块声明
declare module '@api/*' {
    const value: any
    export default value
}