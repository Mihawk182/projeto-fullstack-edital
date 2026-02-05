import { apiFetch } from "./secureApi";

export type AlbumDto = {
  id: string;
  artistId: string;
  title: string;
  coverObjectKey?: string;
};

export type PageResponse<T> = {
  content: T[];
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export async function fetchAlbumsByArtist(artistId: string, page = 1, size = 12) {
  const params = new URLSearchParams();
  params.set("artistId", artistId);
  params.set("page", String(Math.max(0, page - 1)));
  params.set("size", String(size));
  params.set("sort", "title,asc");

  return apiFetch<PageResponse<AlbumDto>>(`/albums?${params.toString()}`);
}

export async function createAlbum(artistId: string, title: string) {
  return apiFetch<AlbumDto>(`/albums`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ artistId, title })
  });
}

export async function updateAlbum(id: string, title: string) {
  return apiFetch<AlbumDto>(`/albums/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ title })
  });
}
