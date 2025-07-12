# -*- coding: utf-8 -*-
from loguru import logger
from webrunnercore import wr #wr模块是webrunner内置的脚本增强sdk
from webrunnercore import *



class 测试负载(WebLoadMachine):
    '''
    用户自定义负载信息
    '''
    负载名称 = '默认负载'

    测试机 = [
        {
            "ip地址": '192.168.231.55',
            "端口": 50000,
            "节点数": 1,
            "主节点": True
        },
    ]

class 测试场景(WebScenario):
    '''
    用户自定义场景信息
    '''
    场景名称 = '默认场景'

    模式 = '梯形负载'
    参数 = {
        '用户数': 50,
        '创建速率': 10,
        '运行时长': 300
    }



class Transaction_Yonghuguanli(SerialTransaction):
    '''
    事务定义, 一个事务由多个task构成, 每个task只包含一个请求
    '''

    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    @property
    def transaction(self):
        # 事务名称
        return "yonghuguanli"

    def on_start(self):
        # 事务启动函数
        super().on_start()
    
    @task
    def task_0(self):
        url = "http://26.234.8.159:5173/"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Upgrade-Insecure-Requests': '1'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/html,res.text

    
    @task
    def task_1(self):
        url = "http://26.234.8.159:5173/@vite/client"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_2(self):
        url = "http://26.234.8.159:5173/src/main.ts"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_3(self):
        url = "http://26.234.8.159:5173/node_modules/vite/dist/client/env.mjs"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/@vite/client'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_4(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/vue.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_5(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/pinia.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_6(self):
        url = "http://26.234.8.159:5173/node_modules/element-plus/dist/index.css"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_7(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/@element-plus_icons-vue.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_8(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/vue-echarts.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_9(self):
        url = "http://26.234.8.159:5173/src/router/index.ts"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_10(self):
        url = "http://26.234.8.159:5173/src/App.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_11(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/axios.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_12(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/element-plus.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_13(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/echarts.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_14(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-UQWBJQZ5.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/vue.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_15(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-G3PMV62Z.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/vue.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_16(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-HYZ2CRGS.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/pinia.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_17(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-YFT6OQ5R.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/pinia.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_18(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-TN6XA7V6.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/@element-plus_icons-vue.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_19(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/vue-router.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_20(self):
        url = "http://26.234.8.159:5173/src/views/HomeView.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_21(self):
        url = "http://26.234.8.159:5173/src/App.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/App.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_22(self):
        url = "http://26.234.8.159:5173/@id/__x00__plugin-vue:export-helper"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/App.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_23(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-66Z6VWCW.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/vue-echarts.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_24(self):
        url = "http://26.234.8.159:5173/src/views/books/BookCard.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/HomeView.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_25(self):
        url = "http://26.234.8.159:5173/src/views/writers/WriterDetail.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/HomeView.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_26(self):
        url = "http://26.234.8.159:5173/src/views/HomeView.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/HomeView.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='b4e148ca'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_27(self):
        url = "http://26.234.8.159:5173/src/views/books/BookCard.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/books/BookCard.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='313c41a0'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_28(self):
        url = "http://26.234.8.159:5173/src/views/writers/WriterDetail.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/writers/WriterDetail.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='271c0f73'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_29(self):
        url = "http://26.234.8.159:5173/src/views/layouts/MainLayout.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_30(self):
        url = "http://26.234.8.159:5173/src/views/News/NewsList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_31(self):
        url = "http://26.234.8.159:5173/src/stores/user.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/layouts/MainLayout.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_32(self):
        url = "http://26.234.8.159:5173/src/assets/logo.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/layouts/MainLayout.vue'}
        params = {}
        params['import']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_33(self):
        url = "http://26.234.8.159:5173/src/views/layouts/MainLayout.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/layouts/MainLayout.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='3a17d510'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_34(self):
        url = "http://26.234.8.159:5173/src/views/News/NewsDialog.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsList.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_35(self):
        url = "http://26.234.8.159:5173/src/api/news.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsList.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_36(self):
        url = "http://26.234.8.159:5173/src/views/News/NewsList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsList.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='0e7d6f31'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_37(self):
        url = "http://26.234.8.159:5173/src/components/RichTextEditor.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsDialog.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_38(self):
        url = "http://26.234.8.159:5173/src/views/News/NewsDialog.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsDialog.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='fc5a3289'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_39(self):
        url = "http://26.234.8.159:5173/src/components/RichTextEditor.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/components/RichTextEditor.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='8f9639ab'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_40(self):
        url = "http://26.234.8.159:5173/src/assets/logo.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_41(self):
        url = "http://localhost:8080/getNews"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['pageNum']='1'
        params['pageSize']='12'
        params['tags']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_42(self):
        url = "http://26.234.8.159:5173/src/assets/videos/b46440be-1394-4c11-80d6-bb76b66aad9d.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_43(self):
        url = "http://26.234.8.159:5173/src/assets/videos/f782db07-f59c-4fc5-a4c9-a2c7a9514bae.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_44(self):
        url = "http://26.234.8.159:5173/src/assets/videos/6640c742-abe3-4e64-8bfe-98c7c44eae48.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/png,res.text

    
    @task
    def task_45(self):
        url = "http://26.234.8.159:5173/src/assets/videos/797df73d-a81a-4690-b9ad-fff31866a7d5.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/png,res.text

    
    @task
    def task_46(self):
        url = "http://26.234.8.159:5173/src/assets/videos/977ec8e2-c1f5-4959-850f-14486a10acf3.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/png,res.text

    
    @task
    def task_47(self):
        url = "http://26.234.8.159:5173/src/assets/videos/6779e111-5172-43ef-893f-0a08666121e1.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_48(self):
        url = "http://26.234.8.159:5173/src/assets/videos/14c6a446-f9b3-43ae-ab1f-a4385bfdbd54.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_49(self):
        url = "http://26.234.8.159:5173/src/assets/videos/37619f2c-6185-4f25-8206-91177c6d1718.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/png,res.text

    
    @task
    def task_50(self):
        url = "http://26.234.8.159:5173/src/assets/videos/8eb9278d-901c-42a9-823f-7ed854eb3b7d.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_51(self):
        url = "http://26.234.8.159:5173/src/assets/videos/300d06fc-aa60-48f5-9919-36060bac3158.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/png,res.text

    
    @task
    def task_52(self):
        url = "http://26.234.8.159:5173/src/assets/videos/f5727fdf-3da4-45ec-ab13-b0ee6f837b60.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_53(self):
        url = "http://26.234.8.159:5173/src/views/ce/Center.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_54(self):
        url = "http://26.234.8.159:5173/src/views/ce/Center.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/ce/Center.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='f02b0dd1'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_55(self):
        url = "http://26.234.8.159:5173/src/views/ce/Login.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_56(self):
        url = "http://26.234.8.159:5173/src/views/ce/Login.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/ce/Login.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='02282ef0'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_57(self):
        url = "http://26.234.8.159:5173/newlogin"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Upgrade-Insecure-Requests': '1'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/html,res.text

    
    @task
    def task_58(self):
        url = "http://26.234.8.159:5173/@vite/client"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/newlogin'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_59(self):
        url = "http://26.234.8.159:5173/src/main.ts"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/newlogin'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_60(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/vue.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_61(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/pinia.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_62(self):
        url = "http://26.234.8.159:5173/node_modules/element-plus/dist/index.css"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_63(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/@element-plus_icons-vue.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_64(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/vue-echarts.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_65(self):
        url = "http://26.234.8.159:5173/src/App.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_66(self):
        url = "http://26.234.8.159:5173/src/router/index.ts"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_67(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/element-plus.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_68(self):
        url = "http://26.234.8.159:5173/node_modules/vite/dist/client/env.mjs"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/@vite/client'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_69(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/axios.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_70(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/echarts.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_71(self):
        url = "http://26.234.8.159:5173/src/App.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/App.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_72(self):
        url = "http://26.234.8.159:5173/@id/__x00__plugin-vue:export-helper"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/App.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_73(self):
        url = "http://26.234.8.159:5173/src/views/HomeView.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_74(self):
        url = "http://26.234.8.159:5173/src/views/books/BookCard.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/HomeView.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_75(self):
        url = "http://26.234.8.159:5173/src/views/writers/WriterDetail.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/HomeView.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_76(self):
        url = "http://26.234.8.159:5173/src/views/HomeView.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/HomeView.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='b4e148ca'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_77(self):
        url = "http://26.234.8.159:5173/src/views/books/BookCard.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/books/BookCard.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='313c41a0'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_78(self):
        url = "http://26.234.8.159:5173/src/views/writers/WriterDetail.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/writers/WriterDetail.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='271c0f73'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_79(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-UQWBJQZ5.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/vue.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_80(self):
        url = "http://26.234.8.159:5173/src/views/ce/Login.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_81(self):
        url = "http://26.234.8.159:5173/src/stores/user.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/ce/Login.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_82(self):
        url = "http://26.234.8.159:5173/src/views/ce/Login.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/ce/Login.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='02282ef0'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_83(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-G3PMV62Z.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/vue.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_84(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-TN6XA7V6.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/@element-plus_icons-vue.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_85(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-66Z6VWCW.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/vue-echarts.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_86(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-HYZ2CRGS.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/pinia.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_87(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/chunk-YFT6OQ5R.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/node_modules/.vite/deps/pinia.js?v=72a24e1e'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_88(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/vue-router.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_89(self):
        url = "http://localhost:8080/login"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Content-Type': 'application/json', 'Content-Length': '55', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        # application/json (dict)
        data = {}
        data['username'] = 'adminww'
        data['password'] = '123asdfg'
        data['status'] = 1

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()

    
    @task
    def task_90(self):
        url = "http://26.234.8.159:5173/src/views/layouts/MainLayout.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_91(self):
        url = "http://26.234.8.159:5173/src/views/News/NewsList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_92(self):
        url = "http://26.234.8.159:5173/src/assets/logo.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/layouts/MainLayout.vue'}
        params = {}
        params['import']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_93(self):
        url = "http://26.234.8.159:5173/src/views/layouts/MainLayout.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/layouts/MainLayout.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='3a17d510'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_94(self):
        url = "http://26.234.8.159:5173/src/views/News/NewsDialog.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsList.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_95(self):
        url = "http://26.234.8.159:5173/src/api/news.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsList.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_96(self):
        url = "http://26.234.8.159:5173/src/views/News/NewsList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsList.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='0e7d6f31'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_97(self):
        url = "http://26.234.8.159:5173/src/components/RichTextEditor.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsDialog.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_98(self):
        url = "http://26.234.8.159:5173/src/views/News/NewsDialog.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/News/NewsDialog.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='fc5a3289'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_99(self):
        url = "http://26.234.8.159:5173/src/components/RichTextEditor.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/components/RichTextEditor.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='8f9639ab'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_100(self):
        url = "http://26.234.8.159:5173/src/assets/logo.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_101(self):
        url = "http://localhost:8080/getNews"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['pageNum']='1'
        params['pageSize']='12'
        params['tags']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_102(self):
        url = "http://26.234.8.159:5173/src/assets/videos/b46440be-1394-4c11-80d6-bb76b66aad9d.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_103(self):
        url = "http://26.234.8.159:5173/src/assets/videos/f782db07-f59c-4fc5-a4c9-a2c7a9514bae.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_104(self):
        url = "http://26.234.8.159:5173/src/assets/videos/6640c742-abe3-4e64-8bfe-98c7c44eae48.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_105(self):
        url = "http://26.234.8.159:5173/src/assets/videos/797df73d-a81a-4690-b9ad-fff31866a7d5.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_106(self):
        url = "http://26.234.8.159:5173/src/assets/videos/977ec8e2-c1f5-4959-850f-14486a10acf3.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_107(self):
        url = "http://26.234.8.159:5173/src/assets/videos/6779e111-5172-43ef-893f-0a08666121e1.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_108(self):
        url = "http://26.234.8.159:5173/src/assets/videos/14c6a446-f9b3-43ae-ab1f-a4385bfdbd54.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_109(self):
        url = "http://26.234.8.159:5173/src/assets/videos/37619f2c-6185-4f25-8206-91177c6d1718.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_110(self):
        url = "http://26.234.8.159:5173/src/assets/videos/f5727fdf-3da4-45ec-ab13-b0ee6f837b60.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_111(self):
        url = "http://26.234.8.159:5173/src/assets/videos/300d06fc-aa60-48f5-9919-36060bac3158.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_112(self):
        url = "http://26.234.8.159:5173/src/assets/videos/8eb9278d-901c-42a9-823f-7ed854eb3b7d.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_113(self):
        url = "http://26.234.8.159:5173/src/views/ce/UserList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_114(self):
        url = "http://26.234.8.159:5173/src/views/ce/UserList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/ce/UserList.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='f331c57a'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_115(self):
        url = "http://26.234.8.159:5173/src/views/ce/UserList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/ce/UserList.vue'}
        params = {}
        params['type']='style'
        params['index']='1'
        params['scoped']='f331c57a'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_116(self):
        url = "http://26.234.8.159:5173/src/views/ce/UserList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/ce/UserList.vue'}
        params = {}
        params['type']='style'
        params['index']='2'
        params['scoped']='f331c57a'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_117(self):
        url = "http://localhost:8080/getUserlist"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['pageNum']='1'
        params['pageSize']='10'
        params['username']=''
        params['phone']=''
        params['email']=''
        params['gender']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_118(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/11.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_119(self):
        url = "http://26.234.8.159:5173/src/assets/videos/7c85cacb-c5e3-4166-9ac0-cef58427a458.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_120(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/6.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_121(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/9.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_122(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/10.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_123(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/3.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_124(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/4.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_125(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/7.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_126(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/8.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_127(self):
        url = "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
        headers = {'Host': 'cube.elemecdn.com', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'image', 'Sec-Fetch-Mode': 'no-cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_128(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/11.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_129(self):
        url = "http://26.234.8.159:5173/src/assets/videos/7c85cacb-c5e3-4166-9ac0-cef58427a458.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_130(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/6.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_131(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/9.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_132(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/10.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_133(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/3.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_134(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/4.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_135(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/7.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_136(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/8.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # x-unknown,res.text

    
    @task
    def task_137(self):
        url = "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
        headers = {'Host': 'cube.elemecdn.com', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'image', 'Sec-Fetch-Mode': 'no-cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/png,res.text

    
    @task
    def task_138(self):
        url = "http://localhost:8080/changeUserinfo"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Content-Type': 'application/json', 'Content-Length': '12', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['now_username']='adminww'
        params['username']=''
        # application/json (dict)
        data = {}
        data['status'] = 0

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()

    
    @task
    def task_139(self):
        url = "http://localhost:8080/changeUserinfo"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Content-Type': 'application/json', 'Content-Length': '175', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['now_username']='adminww'
        params['username']='adminww'
        # application/json (dict)
        data = {}
        data['username'] = 'adminww'
        data['phone'] = '13800138011'
        data['email'] = 'admin@kingbase.com'
        data['gender'] = 'male'
        data['status'] = 1
        data['avatar_url'] = '/src/assets/videos/7c85cacb-c5e3-4166-9ac0-cef58427a458.jpg'

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()

    
    @task
    def task_140(self):
        url = "http://localhost:8080/getUserlist"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['pageNum']='1'
        params['pageSize']='10'
        params['username']=''
        params['phone']=''
        params['email']=''
        params['gender']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_141(self):
        url = "http://localhost:8080/changeUserinfo"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Content-Type': 'application/json', 'Content-Length': '12', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['now_username']='adminww'
        params['username']=''
        # application/json (dict)
        data = {}
        data['status'] = 1

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()

    
    @task
    def task_142(self):
        url = "http://localhost:8080/changeUserinfo"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Content-Type': 'application/json', 'Content-Length': '12', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['now_username']='adminww'
        params['username']='admin_kevin'
        # application/json (dict)
        data = {}
        data['status'] = 1

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()

    
    @task
    def task_143(self):
        url = "http://localhost:8080/changeUserinfo"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Content-Type': 'application/json', 'Content-Length': '138', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['now_username']='adminww'
        params['username']='peng'
        # application/json (dict)
        data = {}
        data['username'] = 'peng'
        data['phone'] = '18833334444'
        data['email'] = 'gg@qq.com'
        data['gender'] = 'female'
        data['status'] = 0
        data['avatar_url'] = '/src/assets/images/avatars/4.jpg'

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()

    
    @task
    def task_144(self):
        url = "http://localhost:8080/getUserlist"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['pageNum']='1'
        params['pageSize']='10'
        params['username']=''
        params['phone']=''
        params['email']=''
        params['gender']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_145(self):
        url = "http://localhost:8080/getUserlist"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['pageNum']='2'
        params['pageSize']='10'
        params['username']=''
        params['phone']=''
        params['email']=''
        params['gender']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_146(self):
        url = "http://26.234.8.159:5173/src/assets/videos/b34116e2-25f9-4270-b22f-8361509169c2.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_147(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/2.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_148(self):
        url = "http://26.234.8.159:5173/src/assets/images/avatars/5.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/user'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text


    def on_stop(self):
        # 事务结束函数
        super().on_stop()


class WebrunnerAction(BrowserAction):
    '''
    事务集合, 一个Action包含多个Transaction事务, @task装饰器参数表示事务混合比，
    初始化事务 和 结束事务只执行一次， 执行事务按照混合比执行多次
    '''
    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    def on_start(self):
        super().on_start()

    @task(1)
    @transaction("yonghuguanli")
    def task_yonghuguanli(self):
        # 执行事务
        Transaction_Yonghuguanli(self).run()

    def on_stop(self):
        super().on_stop()


class WebrunnerUser(CFastHttpUser):
    '''
    虚拟用户, 一个用户循环执行一个Action
    '''
    host = ""
    tasks = [WebrunnerAction]

    def __init__(self, *args, **kwargs) -> None:
        super().__init__(*args, **kwargs)

    def on_start(self):
        # 所有虚拟用户创建完成后开始执行，主要用于定义参数化和检查点的策略

        super().on_start()