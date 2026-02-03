import { BehaviorSubject } from "rxjs";
import { Album, mockAlbums } from "../services/models";

export type AlbumState = {
  items: Album[];
};

const initialState: AlbumState = {
  items: mockAlbums
};

class AlbumFacade {
  private subject = new BehaviorSubject<AlbumState>(initialState);
  state$ = this.subject.asObservable();

  addAlbum(album: Album) {
    const current = this.subject.value;
    this.subject.next({ items: [album, ...current.items] });
  }
}

export const albumFacade = new AlbumFacade();
