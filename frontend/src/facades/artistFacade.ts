import { BehaviorSubject } from "rxjs";
import { Artist, mockArtists } from "../services/models";

export type ArtistFilters = {
  search?: string;
  sort?: "asc" | "desc";
  page?: number;
};

export type ArtistState = {
  items: Artist[];
  filters: ArtistFilters;
};

const initialState: ArtistState = {
  items: mockArtists,
  filters: { search: "", sort: "asc", page: 1 }
};

class ArtistFacade {
  private subject = new BehaviorSubject<ArtistState>(initialState);
  state$ = this.subject.asObservable();

  updateFilters(filters: Partial<ArtistFilters>) {
    const current = this.subject.value;
    this.subject.next({ ...current, filters: { ...current.filters, ...filters } });
  }
}

export const artistFacade = new ArtistFacade();
