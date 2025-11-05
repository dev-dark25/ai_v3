<template>
  <header>
    <h2>DeepSeek</h2>
    <div>
      <el-radio-group v-model="model">
        <el-radio value="local">local</el-radio>
        <el-radio value="deepseek-reasoner">deepseek r1</el-radio>
        <el-radio value="deepseek-chat">deepseek v3</el-radio>
      </el-radio-group>
    </div>
    <div class="wrapper" style="margin-top: 1rem">
      <el-input v-model="input" style="width: 500px" placeholder="Please input" @change="inputChange()" />
    </div>

    <div ref="container" style="width: 500px; height: 500px; margin-top: 1rem; overflow: auto;"></div>

    <!-- <div class="loader"></div> -->
  </header>
</template>

<script>
import { call } from '@/axios/api'

export default {
  data() {
    return {
      input: 'input',
      model: 'deepseek-reasoner',
      output: 'output'
    }
  },
  created() {
    this.input = ''
  },
  mounted() {
    this.input = ''
  },
  methods: {
    async inputChange() {
      const css1 = 'color: #409eff;text-align: right;';
      const css2 = 'color: #67c23a;text-align: left;';
      const newElement = document.createElement('div');
      newElement.setAttribute('style', css1);
      newElement.textContent = this.input;
      this.$refs.container.appendChild(newElement);

      // css加载中
      const css3 = 'border: 5px solid #f3f3f3;border-top: 5px solid #3498db;border-radius: 50%;width: 25px;height: 25px;animation: spin 2s linear infinite;';
      const newElement1 = document.createElement('div');
      newElement1.setAttribute('style', css3);
      this.$refs.container.appendChild(newElement1);

      const newElement2 = document.createElement('div');
      newElement2.setAttribute('style', css2);

      const req = {
        input: this.input,
        model: this.model
      }
      const res = await call(req);
      if (res.code === '000') {
        newElement2.textContent = res.output;
      } else {
        newElement2.textContent = 'error';
        // alert(res.output);
      }
      this.$refs.container.removeChild(newElement1);
      this.$refs.container.appendChild(newElement2);
      this.$refs.container.scrollTop = this.$refs.container.scrollHeight;
      this.input = '';
    }
  }
}
</script>

<style>
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

/* .loader {
  border: 8px solid #f3f3f3;
  border-top: 8px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 2s linear infinite;
} */

header {
  display: block !important;
  line-height: 1.5;
  max-height: 100vh;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
