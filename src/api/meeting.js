import axios from 'axios'

const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true,
    timeout: 10000
})

export default {
    // 获取已通过会议列表
    getPassedMeetings(params) {
        return apiClient.get('/getPassedMeetings', {
            params: {
                page: params.page || 1,
                size: params.size || 10,
                title: params.title,
                startTime: params.startTime,
                endTime: params.endTime
            }
        }).then(response => ({
            data: response.data.list, // 后端返回的会议列表
            total: response.data.total // 总记录数
        }))
    },

    // 获取未通过会议列表
    getNotPassedMeetings(params) {
        return apiClient.get('/getNotPassedMeetings', {
            params: {
                page: params.page || 1,
                size: params.size || 10,
                title: params.title
            }
        })
    },

    // 获取特定状态会议
    getCertainStatusMeetings(params) {
        return apiClient.get('/getCertainStatusMeetings', {
            params: {
                status: params.status,
                creator: params.creator,
                page: params.page || 1,
                size: params.size || 10
            }
        })
    },

    // 获取会议详情
    getMeetingDetail(id) {
        return apiClient.get('/getMeetingDetail', {
            params: { id }
        })
    },

    // 添加会议
    addMeeting(data) {
        return apiClient.post('/addMeeting', {
            title: data.title,
            coverImage: data.coverImage,
            address: data.address,
            startTime: data.startTime,
            endTime: data.endTime,
            content: data.content,
            creator: data.creator
        }, {
            params: {
                now_username: data.creator
            }
        })
    },

    // 更新会议信息（增加调试日志）
    updateMeeting(id, data) {
        console.log('[API] 更新会议请求:', { id, data });
        return apiClient.post('/updateMeeting', data, {
            params: {
                now_username: data.creator
            }
        }).then(response => {
            console.log('[API] 更新会议响应:', response.data);
            return response;
        }).catch(error => {
            console.error('[API] 更新会议错误:', {
                config: error.config,
                response: error.response?.data
            });
            throw error;
        });
    },

    // 删除会议
    deleteMeeting(id, username) {
        return apiClient.get('/deleteMeeting', {
            params: {
                id: id,
                now_username: username
            }
        })
    },

    // 通过会议
    passMeeting(id) {
        return apiClient.get('/passMeeting', {
            params: { id }
        })
    },

    // 拒绝会议
    refuseMeeting(id) {
        return apiClient.get('/refuseMeeting', {
            params: { id }
        })
    },



    // AI会议总结
    getAISummary(prompt) {
        return apiClient.get('/AIsummary', {
            params: { prompt }
        })
    },
    // 添加议程
    addAgenda(data) {
        return apiClient.post('/addAgenda', {
            meeting_id: data.meeting_id,
            title: data.title,
            speaker: data.speaker,
            startTime: data.startTime,
            duration: data.duration,
            content: data.content
        })
    },

    // 获取按时间排序的议程列表
    getAgendasByTime(params) {
        return apiClient.get('/getAllAgendaByTime', {
            params: {
                meeting_id: params.meeting_id,
                page: params.page || 1,
                size: params.size || 10
            }
        })
    },

    // 获取按时长排序的议程列表
    getAgendasByDuration(params) {
        return apiClient.get('/getAllAgendaByDuration', {
            params: {
                meeting_id: params.meeting_id,
                page: params.page || 1,
                size: params.size || 10
            }
        })
    },

    // 获取议程详情
    getAgendaDetail(id) {
        return apiClient.get('/getAgendaDetail', {
            params: { id }
        })
    },

    // 更新议程信息
    updateAgenda(data) {
        return apiClient.post('/updateAgendaInfo', {
            id: data.id,
            title: data.title,
            speaker: data.speaker,
            duration: data.duration
        })
    },

    // 删除议程
    deleteAgenda(id) {
        return apiClient.get('/deleteAgenda', {
            params: { id }
        })
    }



}