// 引入vite
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// WindiCSS样式模块
import WindiCSS from 'vite-plugin-windicss'
// 引入路径
import path from "path"


// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    // src目录别名
    alias: {
      "~": path.resolve(__dirname, "src"),
    }
  },

  // 配置前端服务地址和端口
  server: {
    // host: '0.0.0.0',
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8105',
        changeOrigin: true,
        rewrite: (path) => path
      },
    }
  },

  plugins: [vue(), WindiCSS()]
})
