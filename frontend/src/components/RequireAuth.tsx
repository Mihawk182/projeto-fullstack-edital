import { Navigate, Outlet } from "react-router-dom";
import { useObservable } from "../hooks/useObservable";
import { authFacade } from "../facades/authFacade";

export default function RequireAuth() {
  const auth = useObservable(authFacade.auth$);

  if (!auth.isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
}
