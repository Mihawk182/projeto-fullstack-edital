import { BehaviorSubject } from "rxjs";
import { login, Credentials, AuthResponse } from "../services/authService";

export type AuthState = {
  isAuthenticated: boolean;
  accessToken?: string;
  refreshToken?: string;
  expiresAt?: number;
};

const STORAGE_KEY = "auth_state";

function loadState(): AuthState {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return { isAuthenticated: false };
    }
    const parsed = JSON.parse(raw) as AuthState;
    if (!parsed.accessToken) {
      return { isAuthenticated: false };
    }
    return { ...parsed, isAuthenticated: true };
  } catch {
    return { isAuthenticated: false };
  }
}

function persistState(state: AuthState) {
  try {
    if (!state.isAuthenticated) {
      localStorage.removeItem(STORAGE_KEY);
      return;
    }
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
  } catch {
    // ignore storage errors
  }
}

class AuthFacade {
  private authSubject = new BehaviorSubject<AuthState>(loadState());
  auth$ = this.authSubject.asObservable();

  login(token: string) {
    const state: AuthState = { isAuthenticated: true, accessToken: token };
    this.authSubject.next(state);
    persistState(state);
  }

  loginWithCredentials(credentials: Credentials): Promise<AuthResponse> {
    return login(credentials).then((response) => {
      const expiresAt = Date.now() + response.expiresIn * 1000;
      const state: AuthState = {
        isAuthenticated: true,
        accessToken: response.accessToken,
        refreshToken: response.refreshToken,
        expiresAt
      };
      this.authSubject.next(state);
      persistState(state);
      return response;
    });
  }

  logout() {
    const state: AuthState = { isAuthenticated: false };
    this.authSubject.next(state);
    persistState(state);
  }
}

export const authFacade = new AuthFacade();
