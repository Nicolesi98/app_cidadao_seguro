import { useState, useEffect } from "react";
import { JobCard } from "../components/JobCard";
import { Search, SlidersHorizontal, Loader2, X } from "lucide-react";
import { fetchAllJobs, type Job } from "../utils/api";

const scheduleLabel: Record<string, string> = {
  "full-time": "Tempo integral",
  "part-time": "Meio período",
};

export function Home() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [scheduleFilter, setScheduleFilter] = useState("all");
  const [cityFilter, setCityFilter] = useState("all");
  const [showFilters, setShowFilters] = useState(false);

  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      setLoading(true);
      const jobsData = await fetchAllJobs();
      setJobs(jobsData);
    } catch (error) {
      console.error("Erro ao carregar vagas:", error);
      setJobs([]);
    } finally {
      setLoading(false);
    }
  };

  const filteredJobs = jobs.filter((job) => {
    const matchesSearch =
      job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      job.company.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesSchedule =
      scheduleFilter === "all" || job.schedule === scheduleFilter;
    const matchesCity = cityFilter === "all" || job.city === cityFilter;
    return matchesSearch && matchesSchedule && matchesCity;
  });

  const cities = Array.from(new Set(jobs.map((job) => job.city))).sort();
  const schedules = Array.from(new Set(jobs.map((job) => job.schedule)));
  const hasFilters = scheduleFilter !== "all" || cityFilter !== "all";

  const clearFilters = () => {
    setScheduleFilter("all");
    setCityFilter("all");
  };

  return (
    <div className="px-4 pt-5 pb-4">
      {/* Hero Banner */}
      <div className="bg-gradient-to-r from-purple-600 to-pink-500 rounded-2xl p-5 mb-5 text-white">
        <p className="text-xs text-purple-200 mb-1" style={{ fontWeight: 500 }}>
          🎯 Vagas afirmativas
        </p>
        <h2 className="text-xl mb-1" style={{ fontWeight: 700 }}>
          Sua próxima oportunidade está aqui
        </h2>
        <p className="text-sm text-purple-100">
          Para mulheres e pessoas pretas que buscam crescimento
        </p>
        {!loading && (
          <div className="mt-3 bg-white/15 rounded-lg px-3 py-1.5 inline-block">
            <span className="text-sm" style={{ fontWeight: 600 }}>
              {jobs.length} vagas disponíveis
            </span>
          </div>
        )}
      </div>

      {/* Search + Filter row */}
      <div className="flex gap-2 mb-3">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
          <input
            type="search"
            placeholder="Buscar vaga ou empresa..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-9 pr-3 py-3 bg-white rounded-xl border border-slate-200 text-sm outline-none focus:ring-2 focus:ring-purple-300"
          />
        </div>
        <button
          onClick={() => setShowFilters(!showFilters)}
          className={`flex items-center gap-1.5 px-4 py-3 rounded-xl text-sm transition-colors relative ${
            hasFilters
              ? "bg-purple-600 text-white"
              : "bg-white text-slate-600 border border-slate-200"
          }`}
          style={{ fontWeight: 500 }}
        >
          <SlidersHorizontal className="w-4 h-4" />
          Filtros
          {hasFilters && (
            <span className="absolute -top-1.5 -right-1.5 w-4 h-4 bg-pink-500 rounded-full text-[10px] flex items-center justify-center text-white">
              {(scheduleFilter !== "all" ? 1 : 0) + (cityFilter !== "all" ? 1 : 0)}
            </span>
          )}
        </button>
      </div>

      {/* Expandable filters */}
      {showFilters && (
        <div className="bg-white rounded-2xl p-4 mb-3 shadow-sm border border-slate-100 space-y-3">
          <div>
            <label className="text-xs text-slate-500 mb-1 block" style={{ fontWeight: 600 }}>
              Tipo de contrato
            </label>
            <div className="flex flex-wrap gap-2">
              <button
                onClick={() => setScheduleFilter("all")}
                className={`px-3 py-1.5 rounded-full text-sm transition-colors ${
                  scheduleFilter === "all"
                    ? "bg-purple-600 text-white"
                    : "bg-slate-100 text-slate-600"
                }`}
              >
                Todos
              </button>
              {schedules.map((s) => (
                <button
                  key={s}
                  onClick={() => setScheduleFilter(s)}
                  className={`px-3 py-1.5 rounded-full text-sm transition-colors ${
                    scheduleFilter === s
                      ? "bg-purple-600 text-white"
                      : "bg-slate-100 text-slate-600"
                  }`}
                >
                  {scheduleLabel[s] ?? s}
                </button>
              ))}
            </div>
          </div>

          <div>
            <label className="text-xs text-slate-500 mb-1 block" style={{ fontWeight: 600 }}>
              Cidade
            </label>
            <select
              value={cityFilter}
              onChange={(e) => setCityFilter(e.target.value)}
              className="w-full rounded-xl border border-slate-200 px-3 py-2.5 text-sm bg-slate-50 outline-none focus:ring-2 focus:ring-purple-300"
            >
              <option value="all">Todas as cidades</option>
              {cities.map((city) => (
                <option key={city} value={city}>
                  {city}
                </option>
              ))}
            </select>
          </div>

          {hasFilters && (
            <button
              onClick={clearFilters}
              className="flex items-center gap-1.5 text-sm text-red-500"
            >
              <X className="w-4 h-4" /> Limpar filtros
            </button>
          )}
        </div>
      )}

      {/* Results count */}
      {!loading && (
        <p className="text-xs text-slate-400 mb-3" style={{ fontWeight: 500 }}>
          {filteredJobs.length} vaga{filteredJobs.length !== 1 ? "s" : ""} encontrada{filteredJobs.length !== 1 ? "s" : ""}
        </p>
      )}

      {/* Jobs List */}
      {loading ? (
        <div className="flex flex-col items-center justify-center py-20 gap-3">
          <Loader2 className="w-8 h-8 animate-spin text-purple-500" />
          <p className="text-sm text-slate-400">Carregando vagas...</p>
        </div>
      ) : filteredJobs.length === 0 ? (
        <div className="text-center py-16">
          <p className="text-4xl mb-3">🔍</p>
          <p className="text-slate-500 mb-1">Nenhuma vaga encontrada</p>
          <p className="text-sm text-slate-400">Tente outros filtros ou termos de busca</p>
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {filteredJobs.map((job) => (
            <JobCard key={job.id} job={job} />
          ))}
        </div>
      )}
    </div>
  );
}
