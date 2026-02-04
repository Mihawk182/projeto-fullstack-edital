import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { authFacade } from "../facades/authFacade";

export default function LoginPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const onSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);

    if (!email.trim() || !password.trim()) {
      setError("Informe e-mail e senha.");
      return;
    }

    setLoading(true);
    try {
      await authFacade.loginWithCredentials({ email, password });
      navigate("/artists");
    } catch {
      setError("Credenciais inválidas.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-white to-fog">
      <form className="w-full max-w-md rounded-2xl bg-white p-8 shadow-soft" onSubmit={onSubmit}>
        <h1 className="text-2xl font-semibold">Acesso</h1>
        <p className="mt-2 text-sm text-slate-500">Entre para gerenciar artistas e álbuns.</p>

        <div className="mt-6 space-y-4">
          <div>
            <label className="text-sm font-medium">E-mail</label>
            <input
              className="mt-2 w-full rounded border border-slate-200 px-3 py-2"
              placeholder="email@exemplo.com"
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
            />
          </div>
          <div>
            <label className="text-sm font-medium">Senha</label>
            <input
              className="mt-2 w-full rounded border border-slate-200 px-3 py-2"
              placeholder="••••••••"
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />
          </div>

          {error && (
            <div className="rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-600">
              {error}
            </div>
          )}

          <button
            className="w-full rounded bg-ember py-2 text-white font-semibold disabled:opacity-70"
            type="submit"
            disabled={loading}
          >
            {loading ? "Entrando..." : "Entrar"}
          </button>
        </div>
      </form>
    </div>
  );
}
