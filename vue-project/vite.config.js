import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import {fileURLToPath, URL} from 'node:url'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import ElementPlus from 'unplugin-element-plus/vite'

// https://vite.dev/config/
// 提取 ElementPlusResolver 配置为常量
const elementPlusResolverConfig = { importStyle: 'sass' };

export default defineConfig({
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  plugins: [
    vue(),
    // 按需定制主题配置
    ElementPlus({
      useSource: true,
    }),
    AutoImport({
      resolvers: [ElementPlusResolver(elementPlusResolverConfig)],
    }),
    Components({
      resolvers: [ElementPlusResolver(elementPlusResolverConfig)],
    }),
  ],

  css: {
    preprocessorOptions: {
      scss: {
        // additionalData: `@use "@/assets/index.scss" as *;`,
      }
    }
  },
  // 添加代理配置
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }

});

