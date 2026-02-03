import { Routes, Route, Navigate } from "react-router-dom";
import AppLayout from "./layout/AppLayout";
import LoginPage from "../pages/LoginPage";
import ArtistListPage from "../pages/ArtistListPage";
import ArtistDetailPage from "../pages/ArtistDetailPage";
import ArtistFormPage from "../pages/ArtistFormPage";
import RequireAuth from "../components/RequireAuth";

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<RequireAuth />}>
        <Route element={<AppLayout />}>
          <Route path="/" element={<Navigate to="/artists" replace />} />
          <Route path="/artists" element={<ArtistListPage />} />
          <Route path="/artists/new" element={<ArtistFormPage mode="create" />} />
          <Route path="/artists/:id" element={<ArtistDetailPage />} />
          <Route path="/artists/:id/edit" element={<ArtistFormPage mode="edit" />} />
        </Route>
      </Route>
    </Routes>
  );
}
