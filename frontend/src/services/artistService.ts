export type ArtistPayload = { name: string };

export async function fetchArtists() {
  return [];
}

export async function createArtist(_payload: ArtistPayload) {
  return { id: "new" };
}
