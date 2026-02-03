import { BehaviorSubject } from "rxjs";

export type AuthState = {
  isAuthenticated: boolean;
  accessToken?: string;
};

const initialState: AuthState = {
  isAuthenticated: false
};

class AuthFacade {
  private authSubject = new BehaviorSubject<AuthState>(initialState);
  auth$ = this.authSubject.asObservable();

  login(token: string) {
    this.authSubject.next({ isAuthenticated: true, accessToken: token });
  }

  logout() {
    this.authSubject.next({ isAuthenticated: false });
  }
}

export const authFacade = new AuthFacade();
