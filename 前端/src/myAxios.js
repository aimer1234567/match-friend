import axios from "axios";
const myAxios = axios.create({
    // baseURL: 'http://175.178.74.112:8080',
    baseURL: 'http://localhost:8080',
    timeout: 30000,
  });
//
myAxios.interceptors.request.use(function (config) {
  if(config.url!=='/login'){
      let token=localStorage.getItem('token') 
      config.headers['token'] = token;
  }
  return config
}, function (error) {
  return Promise.reject(error);
});

// 添加响应拦截器
myAxios.interceptors.response.use(function (response) {
  return response.data;
}, function (error) {
  if (error.response.status === 401) {
    localStorage.removeItem('token')
    this.$router.push('/login')
  }
  return Promise.reject(error);
});
export default myAxios;