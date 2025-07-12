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



class Transaction_Huiyi(SerialTransaction):
    '''
    事务定义, 一个事务由多个task构成, 每个task只包含一个请求
    '''

    def __init__(self, parent: "User") -> None:
        super().__init__(parent)

    @property
    def transaction(self):
        # 事务名称
        return "huiyi"

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
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/vue.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_4(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/pinia.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_5(self):
        url = "http://26.234.8.159:5173/node_modules/element-plus/dist/index.css"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_6(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/@element-plus_icons-vue.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_7(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/vue-echarts.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_8(self):
        url = "http://26.234.8.159:5173/src/router/index.ts"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_9(self):
        url = "http://26.234.8.159:5173/node_modules/.vite/deps/axios.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/main.ts'}
        params = {}
        params['v']='72a24e1e'
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
        url = "http://26.234.8.159:5173/node_modules/vite/dist/client/env.mjs"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/@vite/client'}
        params = {}
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
        url = "http://26.234.8.159:5173/src/assets/videos/6779e111-5172-43ef-893f-0a08666121e1.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_47(self):
        url = "http://26.234.8.159:5173/src/assets/videos/977ec8e2-c1f5-4959-850f-14486a10acf3.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/png,res.text

    
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
        url = "http://26.234.8.159:5173/src/assets/videos/300d06fc-aa60-48f5-9919-36060bac3158.png"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/png,res.text

    
    @task
    def task_51(self):
        url = "http://26.234.8.159:5173/src/assets/videos/8eb9278d-901c-42a9-823f-7ed854eb3b7d.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/news'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
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
        url = "http://26.234.8.159:5173/src/views/Meeting/MeetingList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_54(self):
        url = "http://26.234.8.159:5173/src/views/Meeting/MeetingDialog.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/Meeting/MeetingList.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_55(self):
        url = "http://26.234.8.159:5173/src/api/meeting.js"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/Meeting/MeetingList.vue'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_56(self):
        url = "http://26.234.8.159:5173/src/views/Meeting/MeetingList.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/Meeting/MeetingList.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='cd5d3098'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_57(self):
        url = "http://26.234.8.159:5173/src/views/Meeting/MeetingDialog.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/Meeting/MeetingDialog.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='15319fd7'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_58(self):
        url = "http://localhost:8080/getCertainStatusMeetings"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['status']='2'
        params['page']='1'
        params['size']='12'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_59(self):
        url = "http://26.234.8.159:5173/src/assets/videos/eac488af-922f-4b0e-bc03-4bd560aafa3d.jpg"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'image/avif,image/webp,*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/meeting'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # image/jpeg,res.text

    
    @task
    def task_60(self):
        url = "http://26.234.8.159:5173/src/views/Meeting/MeetingDetail.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/router/index.ts'}
        params = {}
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_61(self):
        url = "http://26.234.8.159:5173/src/views/Meeting/MeetingDetail.vue"
        headers = {'Host': '26.234.8.159:5173', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/src/views/Meeting/MeetingDetail.vue'}
        params = {}
        params['type']='style'
        params['index']='0'
        params['scoped']='d620684f'
        params['vue']=''
        params['lang.css']=''
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # text/javascript,res.text

    
    @task
    def task_62(self):
        url = "http://localhost:8080/getMeetingDetail"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['id']='8'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_63(self):
        url = "http://localhost:8080/getAllAgendaByTime"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['meeting_id']='8'
        params['page']='1'
        params['size']='100'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_64(self):
        url = "http://localhost:8080/getCertainStatusMeetings"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['status']='2'
        params['page']='1'
        params['size']='12'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()

    
    @task
    def task_65(self):
        url = "http://localhost:8080/addMeeting"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Content-Type': 'application/json', 'Content-Length': '116', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['now_username']=''
        # application/json (dict)
        data = {}
        data['title'] = '10000000'
        data['coverImage'] = ''
        data['address'] = '11111111'
        data['startTime'] = ''
        data['endTime'] = ''
        data['content'] = '11111'
        data['creator'] = ''

        res = self.post(url, headers=headers, params=params, json=data)
        # application/json,res.json()

    
    @task
    def task_66(self):
        url = "http://localhost:8080/getCertainStatusMeetings"
        headers = {'Host': 'localhost:8080', 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0', 'Accept': 'application/json, text/plain, */*', 'Accept-Language': 'en-US,en;q=0.5', 'Origin': 'http://26.234.8.159:5173', 'Connection': 'keep-alive', 'Referer': 'http://26.234.8.159:5173/', 'Sec-Fetch-Dest': 'empty', 'Sec-Fetch-Mode': 'cors', 'Sec-Fetch-Site': 'cross-site'}
        params = {}
        params['status']='2'
        params['page']='1'
        params['size']='12'
        # null
        data = None
        res = self.get(url, headers=headers, params=params, data=data)
        # application/json,res.json()


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
    @transaction("huiyi")
    def task_huiyi(self):
        # 执行事务
        Transaction_Huiyi(self).run()

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