export type AlbumPayload = { title: string; artistId: string };

export async function createAlbum(_payload: AlbumPayload) {
  return { id: "new" };
}
