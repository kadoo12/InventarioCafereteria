import { useState } from "react";
import { replace, useNavigate } from "react-router-dom";
import { User, Lock, LogIn } from "lucide-react";
import api from "@/services/api";

const Login = () => {
  const [nomUsuario, setUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!nomUsuario.trim() || !contrasena.trim()) {
      alert("Por favor completa todos los campos");
      return;
    }
    try {
      const response = await api.post("/login", { 
        nomUsuario: nomUsuario, 
        contrasena: contrasena });

        console.log("Login response:", response);

      if (response.status === 200 && response.data.token) {
        console.log("Token received:", response.data.token);
        
        localStorage.setItem("token", response.data.token);

        api.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;

        const userObject = { nomUsuario };
        localStorage.setItem("user", JSON.stringify(userObject));
        
        console.log("Entrando a inventario con token:", api.defaults.headers.common['Authorization']);

      navigate("/inventario", { replace: true });
      } else {
        alert("Credenciales incorrectas");
      }
    } catch (err: any) {
      if(err.response && err.response.status === 401) {
        alert("Credenciales incorrectas");
      } else {
        alert("Credenciales incorrectas. Vuelva a Intentarlo.");
      }
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-primary relative overflow-hidden">
      {/* Background decoration */}
      <div className="absolute inset-0 opacity-10">
        <div className="absolute top-0 left-0 w-96 h-96 bg-secondary rounded-full -translate-x-1/2 -translate-y-1/2" />
        <div className="absolute bottom-0 right-0 w-[30rem] h-[30rem] bg-accent rounded-full translate-x-1/3 translate-y-1/3" />
      </div>

      <div className="relative z-10 w-full max-w-md px-4">
        {/* Logo / Header */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-20 h-20 rounded-2xl bg-secondary mb-4 shadow-lg shadow-secondary/30">
            <span className="text-3xl font-heading font-extrabold text-secondary-foreground">U</span>
          </div>
          <h1 className="text-3xl font-heading font-bold text-primary-foreground">
            Unimonserrate
          </h1>
          <p className="text-primary-foreground/60 mt-1 text-sm">Sistema de Inventario</p>
        </div>

        {/* Login Card */}
        <div className="bg-card rounded-2xl shadow-2xl p-8">
          <h2 className="text-xl font-heading font-semibold text-card-foreground mb-6 text-center">
            Iniciar Sesión
          </h2>

          <form onSubmit={handleLogin} className="space-y-5">
            <div>
               {/* Se usa htmlFor para conectar con el id del input */}
              <label 
              htmlFor="nomUsuario"
              className="block text-sm font-medium text-muted-foreground mb-1.5">
                Usuario
              </label>
              <div className="relative">
                <User className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <input
                  id="nomUsuario"
                  type="text"
                  name="username"
                  autoComplete="username"
                  value={nomUsuario}
                  onChange={(e) => { setUsuario(e.target.value); setError(""); }}
                  placeholder="Ingresa tu usuario"
                  className="w-full pl-10 pr-4 py-3 rounded-xl border border-input bg-background text-foreground placeholder:text-muted-foreground/50 focus:outline-none focus:ring-2 focus:ring-ring focus:border-transparent transition-all"
                />
              </div>
            </div>

            <div>
              <label 
              htmlFor="password"
              className="block text-sm font-medium text-muted-foreground mb-1.5">
                Contraseña
              </label>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <input
                  id="password"
                  type="password"
                  name="password"
                  autoComplete="current-password"
                  value={contrasena}
                  onChange={(e) => { setContrasena(e.target.value); setError(""); }}
                  placeholder="Ingresa tu contraseña"
                  className="w-full pl-10 pr-4 py-3 rounded-xl border border-input bg-background text-foreground placeholder:text-muted-foreground/50 focus:outline-none focus:ring-2 focus:ring-ring focus:border-transparent transition-all"
                />
              </div>
            </div>

            {error && (
              <p className="text-sm text-destructive text-center">{error}</p>
            )}

            <button
              type="submit"
              className="w-full py-3 rounded-xl bg-secondary text-secondary-foreground font-heading font-semibold text-base flex items-center justify-center gap-2 hover:brightness-110 active:scale-[0.98] transition-all shadow-lg shadow-secondary/25"
            >
              <LogIn className="h-5 w-5" />
              Entrar
            </button>
          </form>
        </div>

        <p className="text-center text-primary-foreground/40 text-xs mt-6">
          © 2026 Fundación Universitaria Monserrate
        </p>
      </div>
    </div>
  );
};

export default Login;
