import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
    const currentUser = ref(null)
    const isSuperAdmin = ref(false)

    const initUser = () => {
        console.log('初始化用户信息...')
        const userData = localStorage.getItem('userInfo')
        const token = localStorage.getItem('token')

        console.log('从localStorage获取的数据:', { userData, token })

        // 如果没有token，直接清空用户信息
        if (!token) {
            currentUser.value = null
            isSuperAdmin.value = false
            console.log('未找到token，用户状态已重置')
            return
        }

        if (userData) {
            try {
                const parsedData = JSON.parse(userData)
                currentUser.value = parsedData

                // 更健壮的管理员状态判断
                isSuperAdmin.value = parsedData?.status === 1 ||  // 根据status字段判断
                    parsedData?.isSuperAdmin ||  // 或根据isSuperAdmin字段
                    parsedData?.username === 'admin'  // 或根据用户名

                console.log('用户信息初始化完成:', {
                    currentUser: currentUser.value,
                    isSuperAdmin: isSuperAdmin.value
                })
            } catch (error) {
                console.error('解析用户信息失败:', error)
                clearUser()
            }
        } else {
            console.log('有token但未找到用户信息，尝试保持登录状态')
            // 这里可以添加从服务器获取用户信息的逻辑
        }
    }

    const clearUser = () => {
        currentUser.value = null
        isSuperAdmin.value = false
    }

    const logout = () => {
        console.log('执行登出操作')
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        clearUser()
        console.log('用户已登出，状态已重置')
    }

    const isLoggedIn = () => {
        const loggedIn = !!localStorage.getItem('token') && !!currentUser.value
        console.log(`检查登录状态: ${loggedIn}`)
        return loggedIn
    }

    // 新增方法：更新用户信息
    const updateUserInfo = (userInfo) => {
        try {
            currentUser.value = userInfo
            localStorage.setItem('userInfo', JSON.stringify(userInfo))
            initUser() // 重新初始化以确保一致性
            return true
        } catch (error) {
            console.error('更新用户信息失败:', error)
            return false
        }
    }

    return {
        currentUser,
        isSuperAdmin,
        initUser,
        logout,
        isLoggedIn,
        updateUserInfo,
        clearUser
    }
})