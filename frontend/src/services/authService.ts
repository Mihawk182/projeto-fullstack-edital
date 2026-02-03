export type Credentials = { email: string; password: string };

export async function login(_credentials: Credentials) {
  return { accessToken: "mock-token" };
}
