import { createStore } from 'vuex'
import { EventSourcePolyfill } from 'event-source-polyfill'
// 创建一个新的 store 实例
const store = createStore({
  state () {
    return {
      sse:null,
      baseUrl:"http://localhost:8080"
      // baseUrl:"http://175.178.74.112:8080"
    }
  },
  mutations: {
    seeConnector(state){
      state.sse=new EventSourcePolyfill(`${state.baseUrl}/user/sse`,{headers:{"token":localStorage.getItem('token')}, heartbeatTimeout: 60*60*1000 });
    }
  }
})
export default store;