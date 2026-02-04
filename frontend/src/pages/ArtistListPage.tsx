import { Link } from "react-router-dom";
import { useObservable } from "../hooks/useObservable";
import { artistFacade } from "../facades/artistFacade";

export default function ArtistListPage() {
  const { items, filters } = useObservable(artistFacade.state$);

  return (
    <section className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold">Artistas</h1>
          <p className="text-sm text-slate-500">Gerencie artistas e seus álbuns.</p>
        </div>
        <Link className="rounded bg-ember px-4 py-2 text-sm font-semibold text-white" to="/artists/new">
          Novo artista
        </Link>
      </div>

      <div className="flex flex-wrap items-center gap-3">
        <input
          className="w-full md:w-64 rounded border border-slate-200 px-3 py-2"
          placeholder="Buscar por nome"
          value={filters.search}
          onChange={(event) => artistFacade.updateFilters({ search: event.target.value })}
        />
        <button
          className="rounded border border-slate-200 px-3 py-2 text-sm"
          onClick={() => artistFacade.updateFilters({ sort: filters.sort === "asc" ? "desc" : "asc" })}
        >
          Ordenação: {filters.sort}
        </button>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {items.map((artist) => (
          <Link
            key={artist.id}
            to={`/artists/${artist.id}`}
            className="rounded-2xl bg-white p-5 shadow-soft transition hover:-translate-y-1"
          >
            <div className="text-lg font-semibold">{artist.name}</div>
            <div className="mt-2 text-sm text-slate-500">{artist.albumsCount} álbuns</div>
          </Link>
        ))}
      </div>
    </section>
  );
}
