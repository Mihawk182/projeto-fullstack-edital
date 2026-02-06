import { useEffect, useState } from "react";
import { Outlet, Link, useNavigate } from "react-router-dom";
import { authFacade } from "../../facades/authFacade";
import { notificationFacade, NotificationItem } from "../../facades/notificationFacade";
import { albumSocket } from "../../services/albumSocket";

export default function AppLayout() {
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState<NotificationItem[]>([]);

  const onLogout = () => {
    authFacade.logout();
    navigate("/login");
  };

  useEffect(() => {
    const authSub = authFacade.auth$.subscribe((state) => {
      if (state.isAuthenticated) {
        albumSocket.connect();
      } else {
        albumSocket.disconnect();
      }
    });

    const notifySub = notificationFacade.notifications$.subscribe(setNotifications);

    return () => {
      authSub.unsubscribe();
      notifySub.unsubscribe();
      albumSocket.disconnect();
    };
  }, []);

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
      <div className="pointer-events-none fixed right-6 top-20 z-50 flex flex-col gap-2">
        {notifications.map((note) => (
          <div
            key={note.id}
            className="pointer-events-auto w-72 rounded-xl border border-amber-200 bg-white/95 p-3 text-sm shadow-soft"
          >
            {note.message}
          </div>
        ))}
      </div>
      <main className="container-app">
        <Outlet />
      </main>
    </div>
  );
}
