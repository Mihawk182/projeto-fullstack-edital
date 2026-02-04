import { apiFetch } from "./api";

export type Credentials = { email: string; password: string };

export type AuthResponse = {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
};

export async function login(credentials: Credentials) {
  return apiFetch<AuthResponse>("/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(credentials)
  });
}
