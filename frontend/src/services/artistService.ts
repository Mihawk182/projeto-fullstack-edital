import { apiFetch } from "./secureApi";

export type ArtistDto = {
  id: string;
  name: string;
  albumsCount: number;
};

export type ArtistQuery = {
  search?: string;
  sort?: "asc" | "desc";
  page?: number;
  size?: number;
};

export type PageResponse<T> = {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export async function fetchArtists(query: ArtistQuery) {
  const params = new URLSearchParams();
  if (query.search) params.set("search", query.search);
  const page = Math.max(0, (query.page ?? 1) - 1);
  params.set("page", String(page));
  params.set("size", String(query.size ?? 9));
  params.set("sort", `name,${query.sort ?? "asc"}`);

  return apiFetch<PageResponse<ArtistDto>>(`/artists?${params.toString()}`);
}
