export type Artist = {
  id: string;
  name: string;
  albumsCount: number;
};

export type Album = {
  id: string;
  title: string;
  coverUrl?: string;
};

export const mockArtists: Artist[] = [
  { id: "1", name: "Serj Tankian", albumsCount: 3 },
  { id: "2", name: "Mike Shinoda", albumsCount: 4 },
  { id: "3", name: "Michel Teló", albumsCount: 3 },
  { id: "4", name: "Guns N’ Roses", albumsCount: 3 }
];

export const mockAlbums: Album[] = [
  { id: "1", title: "Harakiri" },
  { id: "2", title: "Black Blooms" },
  { id: "3", title: "The Rough Dog" }
];
