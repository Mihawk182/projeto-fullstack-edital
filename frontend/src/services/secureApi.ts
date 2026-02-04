import { authFacade } from "../facades/authFacade";
import { API_URL } from "./api";

async function withAuth(init?: RequestInit) {
  await authFacade.refreshIfNeeded();
  const token = authFacade.getAccessToken();
  const headers = new Headers(init?.headers);
  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }
  return { ...init, headers };
}

export async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  const requestInit = await withAuth(init);
  let res = await fetch(`${API_URL}${path}`, requestInit);

  if (res.status === 401) {
    await authFacade.refreshIfNeeded();
    const retryInit = await withAuth(init);
    res = await fetch(`${API_URL}${path}`, retryInit);
  }

  if (!res.ok) {
    throw new Error(`API error: ${res.status}`);
  }

  if (res.status === 204) {
    return undefined as T;
  }

  return res.json() as Promise<T>;
}
