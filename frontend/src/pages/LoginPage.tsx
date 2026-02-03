import { useNavigate } from "react-router-dom";
import { authFacade } from "../facades/authFacade";

export default function LoginPage() {
  const navigate = useNavigate();

  const onSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    authFacade.login("mock-token");
    navigate("/artists");
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-white to-fog">
      <form className="w-full max-w-md rounded-2xl bg-white p-8 shadow-soft" onSubmit={onSubmit}>
        <h1 className="text-2xl font-semibold">Acesso</h1>
        <p className="mt-2 text-sm text-slate-500">Entre para gerenciar artistas e álbuns.</p>
        <div className="mt-6 space-y-4">
          <input className="w-full rounded border border-slate-200 px-3 py-2" placeholder="E-mail" />
          <input className="w-full rounded border border-slate-200 px-3 py-2" placeholder="Senha" type="password" />
          <button className="w-full rounded bg-ember py-2 text-white font-semibold">Entrar</button>
        </div>
      </form>
    </div>
  );
}
