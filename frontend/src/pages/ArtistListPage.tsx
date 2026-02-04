import { Link } from "react-router-dom";
import { useEffect } from "react";
import { useObservable } from "../hooks/useObservable";
import { artistFacade } from "../facades/artistFacade";

export default function ArtistListPage() {
  const state = useObservable(artistFacade.state$, artistFacade.getState());
  const { items, filters, totalPages, loading, error } = state;

  useEffect(() => {
    if (items.length === 0) {
      artistFacade.load();
    }
  }, []);

  return (
    <section className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
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
          onChange={(event) => artistFacade.updateFilters({ search: event.target.value, page: 1 })}
        />
        <button
          className="rounded border border-slate-200 px-3 py-2 text-sm"
          onClick={() => artistFacade.updateFilters({ sort: filters.sort === "asc" ? "desc" : "asc" })}
        >
          Ordenação: {filters.sort === "asc" ? "A-Z" : "Z-A"}
        </button>
      </div>

      {loading && <div className="text-sm text-slate-500">Carregando artistas...</div>}
      {error && <div className="text-sm text-red-600">{error}</div>}

      {!loading && items.length === 0 && (
        <div className="rounded-2xl bg-white p-6 text-sm text-slate-500 shadow-soft">
          Nenhum artista encontrado.
        </div>
      )}

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

      <div className="flex items-center justify-between">
        <button
          className="rounded border border-slate-200 px-3 py-2 text-sm disabled:opacity-50"
          disabled={filters.page <= 1}
          onClick={() => artistFacade.updateFilters({ page: filters.page - 1 })}
        >
          Anterior
        </button>
        <div className="text-sm text-slate-500">
          Página {filters.page} de {Math.max(1, totalPages)}
        </div>
        <button
          className="rounded border border-slate-200 px-3 py-2 text-sm disabled:opacity-50"
          disabled={filters.page >= totalPages}
          onClick={() => artistFacade.updateFilters({ page: filters.page + 1 })}
        >
          Próxima
        </button>
      </div>
    </section>
  );
}
