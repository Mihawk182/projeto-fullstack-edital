import { BehaviorSubject } from "rxjs";
import { fetchArtists, ArtistDto } from "../services/artistService";

export type ArtistFilters = {
  search: string;
  sort: "asc" | "desc";
  page: number;
  size: number;
};

export type ArtistState = {
  items: ArtistDto[];
  filters: ArtistFilters;
  total: number;
  totalPages: number;
  loading: boolean;
  error?: string;
};

const initialState: ArtistState = {
  items: [],
  filters: { search: "", sort: "asc", page: 1, size: 9 },
  total: 0,
  totalPages: 0,
  loading: false
};

class ArtistFacade {
  private subject = new BehaviorSubject<ArtistState>(initialState);
  state$ = this.subject.asObservable();

  getState() {
    return this.subject.value;
  }

  async load() {
    const current = this.subject.value;
    this.subject.next({ ...current, loading: true, error: undefined });
    try {
      const page = await fetchArtists(current.filters);
      this.subject.next({
        ...current,
        items: page.content,
        total: page.totalElements,
        totalPages: page.totalPages,
        loading: false
      });
    } catch {
      this.subject.next({ ...current, loading: false, error: "Falha ao carregar artistas." });
    }
  }

  updateFilters(filters: Partial<ArtistFilters>) {
    const current = this.subject.value;
    const next = { ...current.filters, ...filters };
    this.subject.next({ ...current, filters: next });
    this.load();
  }
}

export const artistFacade = new ArtistFacade();
