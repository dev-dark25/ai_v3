import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  base: process.env.NODE_ENV === 'production' ? '/deepseek' : './',
  // 代理，开发环境生效
  server: {
    proxy: {
      '/deepseek': {
        target: 'http://localhost:8180',
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, '')
      },
      // '/api': {
      //   target: 'http://localhost:8180/deepseek',
      //   changeOrigin: true,
      //   // rewrite: (path) => path.replace(/^\/api/, '')
      // },
      // '/api1': {
      //   target: 'http://localhost:8081',
      //   changeOrigin: true,
      //   rewrite: (path) => path.replace(/^\/api/, '')
      // }
    }
  },
  plugins: [
    vue(),
    // vueDevTools(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
