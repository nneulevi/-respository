import axios from 'axios'

const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true,
    timeout: 10000
})

export default {
    // 获取新闻列表
    getNewsList(params) {
        return apiClient.get('/getNews', {
            params: {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 12,
                title: params.title,
                author: params.author,
                tags: params.tags
            }
        }).then(response => ({
            data: response.data.list, // 后端返回的新闻列表
            total: response.data.total // 总记录数
        }))
    },
// 获取待审核新闻
    getPendingNews(params) {
        return apiClient.get('/pending', {
            params: {
                pageNum: params.pageNum || 1,
                pageSize: params.pageSize || 12,
                title: params.title,
                author: params.author,
                tags: params.tags
            }
        }).then(response => ({
            data: response.data.list,
            total: response.data.total
        }))
    },

    // 提交新闻(普通用户)
    submitNews(data) {
        return apiClient.post('/submit', {
            title: data.title,
            summary: data.summary,
            content: data.content,
            coverImage: data.coverImage,
            author: data.author,
            tags: data.tags
        })
    },

    // 添加新闻(管理员)
    addNews(data) {
        return apiClient.post('/addNews', {
            title: data.title,
            summary: data.summary,
            content: data.content,
            coverImage: data.coverImage,
            author: data.author,
            tags: data.tags,
            viewCount: 0, // 默认值
            publishTime: new Date().toISOString() // 当前时间
        })
    },

    // 获取新闻详情
    getNewsDetail(title) {
        return apiClient.get('/get_newsdetail', {
            params: { title }
        })
    },

    // 更新新闻
    updateNews(id, data) {
        return apiClient.post('/changeNewsInfo', data, {
            params: {
                news_id: id,
                now_username: data.author // 当前用户名
            }
        })
    },

    // 删除新闻
    deleteNews(id) {
        return apiClient.delete('/deleteNews', { params: { id } });
    },
    deletepengdingNews(id) {
        return apiClient.delete('/deletependingNews', { params: { id } });
    },


    // 增加浏览量
    incrementViewCount(title) {
        return apiClient.get('/modify_view', {
            params: { title }
        })
    },

    // 添加评论（增加调试日志）
    addComment(commentData) {
        console.log('[API] 提交评论数据:', JSON.stringify(commentData, null, 2));
        return apiClient.post('/api/comments', commentData)
            .then(response => {
                console.log('[API] 评论提交成功:', response.data);
                return response;
            })
            .catch(error => {
                console.error('[API] 评论提交失败:', error);
                throw error;
            });
    },

    // 获取评论（修正路径和参数处理）
    getComments(newsId, params = {}) {
        const sortType = params.sortBy === 'likes' ? 'likes' : 'time';
        console.log('[API] 获取评论请求:', { newsId, sortType, params });

        return apiClient.get(`/api/comments/news/${newsId}/${sortType}`, {
            params: {
                page: params.page || 1,
                size: params.size || 10
            }
        }).then(response => {
            console.log('[API] 获取评论成功:', response.data);
            return {
                data: response.data.data || response.data, // 适配不同后端结构
                total: response.data.total || 0
            };
        }).catch(error => {
            console.error('[API] 获取评论失败:', {
                url: error.config.url,
                status: error.response?.status,
                data: error.response?.data
            });
            throw error; // 继续抛出错误以便组件处理
        });
    },
    // 点赞评论（增加调试日志）
    likeComment(commentId) {
        console.log('[API] 点赞评论ID:', commentId);
        return apiClient.post(`/api/comments/${commentId}/like`)
            .then(response => {
                console.log('[API] 点赞成功:', response.data);
                return response;
            })
            .catch(error => {
                console.error('[API] 点赞失败:', error);
                throw error;
            });
    },

    // 删除评论（增加调试日志）
    deleteComment(commentId) {
        console.log('[API] 删除评论ID:', commentId);
        return apiClient.delete(`/api/comments/${commentId}`)
            .then(response => {
                console.log('[API] 删除成功:', response.data);
                return response;
            })
            .catch(error => {
                console.error('[API] 删除失败:', error);
                throw error;
            });
    },
    // 点赞评论

    // 获取评论数量
    getCommentCount(newsId) {
        return apiClient.get(`/api/comments/news/${newsId}/count`)
    }




}