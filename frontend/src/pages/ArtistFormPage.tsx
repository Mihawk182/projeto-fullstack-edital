type Props = {
  mode: "create" | "edit";
};

export default function ArtistFormPage({ mode }: Props) {
  return (
    <section className="space-y-6">
      <div>
        <h1 className="text-2xl font-semibold">
          {mode === "create" ? "Novo artista" : "Editar artista"}
        </h1>
        <p className="text-sm text-slate-500">Informe os dados do artista e adicione álbuns.</p>
      </div>

      <form className="grid gap-6 lg:grid-cols-[2fr_1fr]">
        <div className="space-y-4 rounded-2xl bg-white p-6 shadow-soft">
          <div>
            <label className="text-sm font-medium">Nome do artista</label>
            <input className="mt-2 w-full rounded border border-slate-200 px-3 py-2" />
          </div>
          <div>
            <label className="text-sm font-medium">Adicionar álbum</label>
            <input className="mt-2 w-full rounded border border-slate-200 px-3 py-2" placeholder="Título" />
          </div>
          <div>
            <label className="text-sm font-medium">Upload de capa</label>
            <input className="mt-2 w-full rounded border border-slate-200 px-3 py-2" type="file" />
          </div>
          <button className="rounded bg-ember px-4 py-2 text-white font-semibold">Salvar</button>
        </div>

        <div className="rounded-2xl bg-white p-6 shadow-soft">
          <h2 className="text-lg font-semibold">Álbuns cadastrados</h2>
          <p className="mt-2 text-sm text-slate-500">Nenhum álbum listado ainda.</p>
        </div>
      </form>
    </section>
  );
}
