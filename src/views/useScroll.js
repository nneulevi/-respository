import { onMounted } from 'vue'

export function useScroll() {
    const initScroll = (element) => {
        if (!element) return

        let isDown = false
        let startX
        let scrollLeft

        const handleMouseDown = (e) => {
            isDown = true
            startX = e.pageX - element.offsetLeft
            scrollLeft = element.scrollLeft
            element.style.cursor = 'grabbing'
        }

        const handleMouseLeave = () => {
            isDown = false
            element.style.cursor = 'grab'
        }

        const handleMouseUp = () => {
            isDown = false
            element.style.cursor = 'grab'
        }

        const handleMouseMove = (e) => {
            if (!isDown) return
            e.preventDefault()
            const x = e.pageX - element.offsetLeft
            const walk = (x - startX) * 2
            element.scrollLeft = scrollLeft - walk
        }

        element.addEventListener('mousedown', handleMouseDown)
        element.addEventListener('mouseleave', handleMouseLeave)
        element.addEventListener('mouseup', handleMouseUp)
        element.addEventListener('mousemove', handleMouseMove)

        // 设置初始光标样式
        element.style.cursor = 'grab'

        // 返回清理函数
        return () => {
            element.removeEventListener('mousedown', handleMouseDown)
            element.removeEventListener('mouseleave', handleMouseLeave)
            element.removeEventListener('mouseup', handleMouseUp)
            element.removeEventListener('mousemove', handleMouseMove)
        }
    }

    return { initScroll }
}