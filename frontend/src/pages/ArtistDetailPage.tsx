import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useObservable } from "../hooks/useObservable";
import { albumFacade } from "../facades/albumFacade";
import { fetchArtist, ArtistDto } from "../services/artistService";
import { getCover } from "../services/albumService";

export default function ArtistDetailPage() {
  const { id } = useParams();
  const [artist, setArtist] = useState<ArtistDto | null>(null);
  const [artistError, setArtistError] = useState<string | null>(null);
  const [coverUrls, setCoverUrls] = useState<Record<string, string>>({});
  const { items, loading, error } = useObservable(albumFacade.state$, albumFacade.getState());

  useEffect(() => {
    if (!id) return;
    fetchArtist(id)
      .then(setArtist)
      .catch(() => setArtistError("Artista não encontrado."));
    albumFacade.loadByArtist(id);
  }, [id]);

  useEffect(() => {
    const fetchCovers = async () => {
      const entries = await Promise.all(
        items.map(async (album) => {
          if (!album.coverObjectKey) return [album.id, ""] as const;
          try {
            const res = await getCover(album.id);
            return [album.id, res.coverUrl] as const;
          } catch {
            return [album.id, ""] as const;
          }
        })
      );
      setCoverUrls(Object.fromEntries(entries));
    };

    if (items.length > 0) {
      fetchCovers();
    }
  }, [items]);

  return (
    <section className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold">{artist?.name ?? "Artista"}</h1>
          <p className="text-sm text-slate-500">Detalhamento do artista e álbuns associados.</p>
          {artistError && <div className="mt-2 text-sm text-red-600">{artistError}</div>}
        </div>
        <Link className="rounded border border-slate-200 px-4 py-2 text-sm" to={`/artists/${id}/edit`}>
          Editar
        </Link>
      </div>

      {loading && <div className="text-sm text-slate-500">Carregando álbuns...</div>}
      {error && <div className="text-sm text-red-600">{error}</div>}

      {!loading && items.length === 0 && (
        <div className="rounded-2xl bg-white p-6 text-sm text-slate-500 shadow-soft">
          Nenhum álbum cadastrado.
        </div>
      )}

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {items.map((album) => (
          <div key={album.id} className="rounded-2xl bg-white p-5 shadow-soft">
            <div className="text-lg font-semibold">{album.title}</div>
            {coverUrls[album.id] ? (
              <img className="mt-3 h-40 w-full rounded-lg object-cover" src={coverUrls[album.id]} alt={album.title} />
            ) : (
              <div className="mt-2 text-sm text-slate-500">Capa: pendente</div>
            )}
          </div>
        ))}
      </div>
    </section>
  );
}
