import { useState } from "react";
import { useNavigate } from "react-router";
import { Sparkles, Eye, EyeOff, Loader2, AlertCircle } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";

type Mode = "login" | "register";

export function Login() {
  const navigate = useNavigate();
  const { login, register } = useAuth();

  const [mode, setMode] = useState<Mode>("login");
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const switchMode = (m: Mode) => {
    setMode(m);
    setError("");
    setName("");
    setEmail("");
    setPassword("");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    if (!email.trim() || !password.trim()) {
      setError("Preencha todos os campos.");
      return;
    }
    if (mode === "register" && !name.trim()) {
      setError("Informe seu nome completo.");
      return;
    }
    if (password.length < 6) {
      setError("A senha deve ter pelo menos 6 caracteres.");
      return;
    }

    setLoading(true);
    // Small delay to simulate async (UX feedback)
    await new Promise((r) => setTimeout(r, 500));

    const result =
      mode === "login"
        ? login(email, password)
        : register(name, email, password);

    setLoading(false);

    if (!result.ok) {
      setError(result.error || "Algo deu errado.");
      return;
    }

    navigate("/app", { replace: true });
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-700 via-purple-600 to-pink-500 flex flex-col">
      {/* Top branding area */}
      <div className="flex-1 flex flex-col items-center justify-center px-6 pt-12 pb-6">
        {/* Logo */}
        <div className="bg-white/20 backdrop-blur-sm p-4 rounded-2xl mb-4">
          <Sparkles className="w-10 h-10 text-white" />
        </div>
        <h1 className="text-white text-4xl mb-1" style={{ fontWeight: 800, letterSpacing: "-1px" }}>
          Afirma<span className="text-pink-200">+</span>
        </h1>
        <p className="text-purple-200 text-sm text-center max-w-xs">
          Vagas afirmativas para mulheres e pessoas pretas
        </p>
      </div>

      {/* Card */}
      <div className="bg-white rounded-t-3xl px-6 pt-7 pb-10 shadow-2xl">
        {/* Tab switcher */}
        <div className="flex bg-slate-100 rounded-2xl p-1 mb-6">
          <button
            type="button"
            onClick={() => switchMode("login")}
            className={`flex-1 py-2.5 rounded-xl text-sm transition-all ${
              mode === "login"
                ? "bg-white text-purple-700 shadow-sm"
                : "text-slate-500"
            }`}
            style={{ fontWeight: mode === "login" ? 700 : 400 }}
          >
            Entrar
          </button>
          <button
            type="button"
            onClick={() => switchMode("register")}
            className={`flex-1 py-2.5 rounded-xl text-sm transition-all ${
              mode === "register"
                ? "bg-white text-purple-700 shadow-sm"
                : "text-slate-500"
            }`}
            style={{ fontWeight: mode === "register" ? 700 : 400 }}
          >
            Criar conta
          </button>
        </div>

        <form onSubmit={handleSubmit} noValidate className="space-y-4">
          {/* Name (register only) */}
          {mode === "register" && (
            <div>
              <label
                htmlFor="name"
                className="text-xs text-slate-500 mb-1.5 block"
                style={{ fontWeight: 600 }}
              >
                Nome completo
              </label>
              <input
                id="name"
                type="text"
                autoComplete="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Como você se chama?"
                className="w-full rounded-xl border border-slate-200 px-4 py-3.5 text-sm bg-slate-50 outline-none focus:ring-2 focus:ring-purple-300 focus:border-purple-300 transition-all"
              />
            </div>
          )}

          {/* Email */}
          <div>
            <label
              htmlFor="email"
              className="text-xs text-slate-500 mb-1.5 block"
              style={{ fontWeight: 600 }}
            >
              E-mail
            </label>
            <input
              id="email"
              type="email"
              inputMode="email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="seu@email.com"
              className="w-full rounded-xl border border-slate-200 px-4 py-3.5 text-sm bg-slate-50 outline-none focus:ring-2 focus:ring-purple-300 focus:border-purple-300 transition-all"
            />
          </div>

          {/* Password */}
          <div>
            <label
              htmlFor="password"
              className="text-xs text-slate-500 mb-1.5 block"
              style={{ fontWeight: 600 }}
            >
              Senha {mode === "register" && <span className="text-slate-400">(mín. 6 caracteres)</span>}
            </label>
            <div className="relative">
              <input
                id="password"
                type={showPassword ? "text" : "password"}
                autoComplete={mode === "login" ? "current-password" : "new-password"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="********"
                className="w-full rounded-xl border border-slate-200 px-4 py-3.5 pr-12 text-sm bg-slate-50 outline-none focus:ring-2 focus:ring-purple-300 focus:border-purple-300 transition-all"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 p-1.5 text-slate-400 active:text-slate-600"
                aria-label={showPassword ? "Ocultar senha" : "Mostrar senha"}
              >
                {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
              </button>
            </div>
          </div>

          {/* Error message */}
          {error && (
            <div className="flex items-start gap-2 bg-red-50 border border-red-100 rounded-xl px-3 py-3">
              <AlertCircle className="w-4 h-4 text-red-500 shrink-0 mt-0.5" />
              <p className="text-sm text-red-600">{error}</p>
            </div>
          )}

          {/* Submit */}
          <button
            type="submit"
            disabled={loading}
            className="w-full flex items-center justify-center gap-2 py-4 rounded-2xl bg-gradient-to-r from-purple-600 to-pink-500 text-white text-base mt-2 active:opacity-90 disabled:opacity-60 transition-all"
            style={{ fontWeight: 700 }}
          >
            {loading ? (
              <>
                <Loader2 className="w-5 h-5 animate-spin" />
                {mode === "login" ? "Entrando..." : "Criando conta..."}
              </>
            ) : mode === "login" ? (
              "Entrar"
            ) : (
              "Criar conta"
            )}
          </button>
        </form>

        {/* Footer note */}
        <p className="text-center text-xs text-slate-400 mt-5 leading-relaxed">
          Ao entrar, você concorda em usar o Afirma+ para explorar oportunidades afirmativas de trabalho.
        </p>
      </div>
    </div>
  );
}

