import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/controller';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },

});
export default api;


//const response = await api.post("/controller/login", { nomUsuario, contrasena });