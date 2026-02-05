import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createArtist, fetchArtist, updateArtist } from "../services/artistService";
import { albumFacade } from "../facades/albumFacade";
import { useObservable } from "../hooks/useObservable";

type Props = {
  mode: "create" | "edit";
};

export default function ArtistFormPage({ mode }: Props) {
  const { id } = useParams();
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [albumTitle, setAlbumTitle] = useState("");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const albumsState = useObservable(albumFacade.state$, albumFacade.getState());

  useEffect(() => {
    if (mode === "edit" && id) {
      fetchArtist(id)
        .then((artist) => setName(artist.name))
        .catch(() => setError("Artista não encontrado."));
      albumFacade.loadByArtist(id);
    }
  }, [mode, id]);

  const onSaveArtist = async (event: React.FormEvent) => {
    event.preventDefault();
    setError(null);

    if (!name.trim()) {
      setError("Informe o nome do artista.");
      return;
    }

    setSaving(true);
    try {
      if (mode === "create") {
        const created = await createArtist(name);
        navigate(`/artists/${created.id}/edit`);
      } else if (id) {
        await updateArtist(id, name);
      }
    } catch {
      setError("Falha ao salvar artista.");
    } finally {
      setSaving(false);
    }
  };

  const onAddAlbum = async () => {
    setError(null);
    if (!albumTitle.trim() || !id) {
      setError("Informe o título do álbum.");
      return;
    }
    try {
      await albumFacade.addAlbum(id, albumTitle);
      setAlbumTitle("");
    } catch {
      setError("Falha ao adicionar álbum.");
    }
  };

  return (
    <section className="space-y-6">
      <div>
        <h1 className="text-2xl font-semibold">
          {mode === "create" ? "Novo artista" : "Editar artista"}
        </h1>
        <p className="text-sm text-slate-500">Informe os dados do artista e adicione álbuns.</p>
      </div>

      {error && (
        <div className="rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-600">
          {error}
        </div>
      )}

      <form className="grid gap-6 lg:grid-cols-[2fr_1fr]" onSubmit={onSaveArtist}>
        <div className="space-y-4 rounded-2xl bg-white p-6 shadow-soft">
          <div>
            <label className="text-sm font-medium">Nome do artista</label>
            <input
              className="mt-2 w-full rounded border border-slate-200 px-3 py-2"
              value={name}
              onChange={(event) => setName(event.target.value)}
            />
          </div>

          {mode === "edit" && (
            <>
              <div>
                <label className="text-sm font-medium">Adicionar álbum</label>
                <div className="mt-2 flex gap-2">
                  <input
                    className="w-full rounded border border-slate-200 px-3 py-2"
                    placeholder="Título"
                    value={albumTitle}
                    onChange={(event) => setAlbumTitle(event.target.value)}
                  />
                  <button
                    type="button"
                    className="rounded bg-lake px-4 py-2 text-sm font-semibold text-white"
                    onClick={onAddAlbum}
                  >
                    Adicionar
                  </button>
                </div>
              </div>

              <div>
                <label className="text-sm font-medium">Upload de capa</label>
                <input className="mt-2 w-full rounded border border-slate-200 px-3 py-2" type="file" disabled />
                <p className="mt-1 text-xs text-slate-400">Será ativado no item de MinIO.</p>
              </div>
            </>
          )}

          <button
            className="rounded bg-ember px-4 py-2 text-white font-semibold disabled:opacity-70"
            disabled={saving}
            type="submit"
          >
            {saving ? "Salvando..." : "Salvar"}
          </button>
        </div>

        <div className="rounded-2xl bg-white p-6 shadow-soft">
          <h2 className="text-lg font-semibold">Álbuns cadastrados</h2>
          {mode !== "edit" && (
            <p className="mt-2 text-sm text-slate-500">Crie o artista para adicionar álbuns.</p>
          )}
          {mode === "edit" && albumsState.loading && (
            <p className="mt-2 text-sm text-slate-500">Carregando álbuns...</p>
          )}
          {mode === "edit" && !albumsState.loading && albumsState.items.length === 0 && (
            <p className="mt-2 text-sm text-slate-500">Nenhum álbum listado ainda.</p>
          )}
          <div className="mt-3 space-y-2">
            {albumsState.items.map((album) => (
              <div key={album.id} className="rounded border border-slate-100 px-3 py-2 text-sm">
                {album.title}
              </div>
            ))}
          </div>
        </div>
      </form>
    </section>
  );
}
