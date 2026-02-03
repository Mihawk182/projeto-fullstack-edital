import { Link, useParams } from "react-router-dom";
import { useObservable } from "../hooks/useObservable";
import { albumFacade } from "../facades/albumFacade";

export default function ArtistDetailPage() {
  const { id } = useParams();
  const { items } = useObservable(albumFacade.state$);

  return (
    <section className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold">Artista {id}</h1>
          <p className="text-sm text-slate-500">Detalhamento do artista e álbuns associados.</p>
        </div>
        <Link className="rounded border border-slate-200 px-4 py-2 text-sm" to={`/artists/${id}/edit`}>
          Editar
        </Link>
      </div>

      {items.length === 0 ? (
        <div className="rounded-2xl bg-white p-6 text-sm text-slate-500 shadow-soft">
          Nenhum álbum cadastrado.
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {items.map((album) => (
            <div key={album.id} className="rounded-2xl bg-white p-5 shadow-soft">
              <div className="text-lg font-semibold">{album.title}</div>
              <div className="mt-2 text-sm text-slate-500">Capa: {album.coverUrl ?? "pendente"}</div>
            </div>
          ))}
        </div>
      )}
    </section>
  );
}
