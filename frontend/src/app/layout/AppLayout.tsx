import { Outlet, Link, useNavigate } from "react-router-dom";
import { authFacade } from "../../facades/authFacade";

export default function AppLayout() {
  const navigate = useNavigate();

  const onLogout = () => {
    authFacade.logout();
    navigate("/login");
  };

  return (
    <div className="min-h-screen">
      <header className="bg-white shadow-soft">
        <div className="container-app flex items-center justify-between">
          <div>
            <div className="text-xl font-semibold">Fullstack Edital</div>
            <div className="text-sm text-slate-500">Artistas e álbuns</div>
          </div>
          <nav className="flex items-center gap-4">
            <Link className="text-sm font-medium hover:text-ember" to="/artists">Artistas</Link>
            <Link className="text-sm font-medium hover:text-ember" to="/artists/new">Novo</Link>
            <button className="rounded bg-ember px-3 py-2 text-sm font-semibold text-white" onClick={onLogout}>
              Sair
            </button>
          </nav>
        </div>
      </header>
      <main className="container-app">
        <Outlet />
      </main>
    </div>
  );
}
