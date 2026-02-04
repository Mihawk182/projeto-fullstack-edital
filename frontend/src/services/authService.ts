import { apiFetchPublic } from "./api";

export type Credentials = { email: string; password: string };

export type AuthResponse = {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
};

export async function login(credentials: Credentials) {
  return apiFetchPublic<AuthResponse>("/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(credentials)
  });
}

export async function refresh(refreshToken: string) {
  return apiFetchPublic<AuthResponse>("/auth/refresh", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ refreshToken })
  });
}
