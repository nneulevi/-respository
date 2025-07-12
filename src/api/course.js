import axios from 'axios'
// 在api.js顶部定义分类映射
const CATEGORY_MAPPING = {
    1: "编程开发",
    2: "产品设计",
    3: "数据分析",
    4: "人工智能",
    5: "网络安全",
    6: "云计算",
    7: "职场技能"
}
const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true,
    timeout: 10000
})

export default {
    // 1. 获取筛选后的课程列表
    getCoursesByFilter(params) {
        // 转换前端duration选项为秒数范围
        let durationMin = null;
        let durationMax = null;

        if (params.duration === "short") {
            durationMin = 0;
            durationMax = 1800; // 30分钟=1800秒
        } else if (params.duration === "medium") {
            durationMin = 1800;
            durationMax = 3600; // 60分钟=3600秒
        } else if (params.duration === "long") {
            durationMin = 3600;
            durationMax = null;
        }

        const categoryName1 = params.category ? CATEGORY_MAPPING[params.category] : null;

        return apiClient.get('/getCoursesByFilter', {
            params: {
                page: params.page || 1,
                size: params.size || 10,
                title: params.title,
                categoryName: categoryName1,
                durationMin: durationMin,
                durationMax: durationMax,
                sortBy: params.sortBy // 例如：'likes', 'duration'等
            }
        }).then(response => ({
            data: response.data.list,
            total: response.data.total
        }))
    },

    // 2. 获取未通过审核的课程
    getUnpassedCourses(params) {
        // 同样处理duration参数
        let durationMin = null;
        let durationMax = null;

        if (params.duration === "short") {
            durationMin = 0;
            durationMax = 1800;
        } else if (params.duration === "medium") {
            durationMin = 1800;
            durationMax = 3600;
        } else if (params.duration === "long") {
            durationMin = 3600;
            durationMax = null;
        }

        return apiClient.get('/unpassed_courses', {
            params: {
                page: params.page || 1,
                size: params.size || 10,
                title: params.title,
                category: params.category,
                durationMin: durationMin,
                durationMax: durationMax
            }
        })
    },

    // 3. 获取个人课程
    getPersonalCourses(params) {
        // 处理duration参数
        let durationMin = null;
        let durationMax = null;

        if (params.duration === "short") {
            durationMin = 0;
            durationMax = 1800;
        } else if (params.duration === "medium") {
            durationMin = 1800;
            durationMax = 3600;
        } else if (params.duration === "long") {
            durationMin = 3600;
            durationMax = null;
        }

        return apiClient.get('/personalCourse', {
            params: {
                status: params.status,
                author: params.author,
                durationMin: durationMin,
                durationMax: durationMax,
                page: params.page || 1,
                size: params.size || 10
            }
        })
    },

    // 4-12. 其他方法保持不变...
    getCourseDetail(id) {
        return apiClient.get('/getCourse', {
            params: { id }
        })
    },

    submitCourse(data) {
        return apiClient.post('/submit_course', {
            title: data.title,
            videoUrl: data.videoUrl,
            coverImage: data.coverImage,
            duration: data.duration,
            author: data.author,
            summary: data.summary,
            content: data.content,
            categories: data.categories.map(id => ({ id }))
        })
    },

    // 修改后的 commitCourse 方法
    commitCourse(data) {
        return apiClient.post('/commit_course', {
            title: data.title,
            videoUrl: data.videoUrl,
            coverImage: data.coverImage,
            duration: data.duration,
            author: data.author,
            summary: data.summary,
            content: data.content,
            categories: data.categories.map(id => ({ id })) // 将ID数组转换为对象数组
        })
    },

    passCourse(id) {
        return apiClient.get('/passCourse', {
            params: { id }
        })
    },

    updateCourse(data) {
        console.group('=== API 请求 ===')
        // 安全处理分类数据
        const categories = (data.categories || []).map(id => ({ id }))

        const requestData = {
            id: data.id,
            title: data.title,
            videoUrl: data.videoUrl,
            coverImage: data.coverImage,
            duration: data.duration,
            author: data.author,
            summary: data.summary,
            content: data.content,
            status: data.status,
            sortOrder: data.sortOrder,
            categories: categories // 转换后的分类数据
        }

        console.log('📨 最终API请求数据:', JSON.parse(JSON.stringify(requestData)))
        console.groupEnd()

        return apiClient.post('/updateCourse', requestData, {
            params: {
                now_username: data.now_username
            }
        })
    },

    updateLikes(id) {
        return apiClient.get('/updateLikes', {
            params: { id }
        })
    },

    refuseCourse(id) {
        return apiClient.get('/refuseCourse', {
            params: { id }
        })
    },

    rollbackCourse(id) {
        return apiClient.get('/rollbackCourse', {
            params: { id }
        })
    },

    askAIQuestion(prompt) {
        return apiClient.get('/askProblem', {
            params: { prompt }
        })
    }
}