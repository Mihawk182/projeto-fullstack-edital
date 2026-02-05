import { BehaviorSubject } from "rxjs";
import { AlbumDto, fetchAlbumsByArtist, createAlbum, updateAlbum } from "../services/albumService";

export type AlbumState = {
  items: AlbumDto[];
  loading: boolean;
  error?: string;
};

const initialState: AlbumState = {
  items: [],
  loading: false
};

class AlbumFacade {
  private subject = new BehaviorSubject<AlbumState>(initialState);
  state$ = this.subject.asObservable();
  private lastArtistId?: string;

  getState() {
    return this.subject.value;
  }

  async loadByArtist(artistId: string) {
    const current = this.subject.value;
    this.lastArtistId = artistId;
    this.subject.next({ ...current, loading: true, error: undefined });
    try {
      const page = await fetchAlbumsByArtist(artistId);
      this.subject.next({ items: page.content, loading: false });
    } catch {
      this.subject.next({ ...current, loading: false, error: "Falha ao carregar álbuns." });
    }
  }

  async addAlbum(artistId: string, title: string) {
    await createAlbum(artistId, title);
    if (this.lastArtistId) {
      await this.loadByArtist(this.lastArtistId);
    }
  }

  async editAlbum(id: string, title: string) {
    await updateAlbum(id, title);
    if (this.lastArtistId) {
      await this.loadByArtist(this.lastArtistId);
    }
  }
}

export const albumFacade = new AlbumFacade();
