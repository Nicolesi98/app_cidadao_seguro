import { useState, useEffect, useCallback } from "react";
import { Link, useLocation } from "react-router";
import {
  Heart,
  Briefcase,
  Calendar,
  ArrowRight,
  Trash2,
  Loader2,
  MapPin,
  DollarSign,
  Clock,
} from "lucide-react";
import { fetchAllJobs, type Job } from "../utils/api";
import { useAuth } from "../contexts/AuthContext";
import {
  getFavorites,
  getApplications,
  removeFavorite,
  removeApplication,
  type Application,
} from "../utils/userStorage";

type Tab = "favorites" | "applications";

const scheduleLabel: Record<string, string> = {
  "full-time": "Tempo integral",
  "part-time": "Meio período",
};

export function Applications() {
  const { user } = useAuth();
  const email = user?.email ?? "";
  const location = useLocation();

  const [tab, setTab] = useState<Tab>("favorites");
  const [allJobs, setAllJobs] = useState<Job[]>([]);
  const [favoriteJobs, setFavoriteJobs] = useState<Job[]>([]);
  const [applications, setApplications] = useState<Application[]>([]);
  const [loading, setLoading] = useState(true);

  const refreshData = useCallback(
    (jobs: Job[]) => {
      const favIds = getFavorites(email);
      setFavoriteJobs(jobs.filter((j) => favIds.includes(j.id)));
      setApplications(getApplications(email));
    },
    [email]
  );

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        const jobs = await fetchAllJobs();
        setAllJobs(jobs);
        refreshData(jobs);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [email]);

  // Refresh when navigating back to this tab
  useEffect(() => {
    if (allJobs.length > 0) refreshData(allJobs);
  }, [location.pathname]);

  const handleRemoveFavorite = (jobId: number) => {
    removeFavorite(email, jobId);
    setFavoriteJobs((prev) => prev.filter((j) => j.id !== jobId));
  };

  const handleRemoveApplication = (jobId: number) => {
    removeApplication(email, jobId);
    setApplications((prev) => prev.filter((a) => a.jobId !== jobId));
  };

  return (
    <div className="px-4 pt-5">
      {/* Header */}
      <div className="mb-5">
        <h1 className="text-slate-900" style={{ fontWeight: 700, fontSize: "20px" }}>
          Minhas Oportunidades
        </h1>
        <p className="text-sm text-slate-400">
          Dados vinculados a <span className="text-purple-600" style={{ fontWeight: 500 }}>{email}</span>
        </p>
      </div>

      {/* Summary cards */}
      <div className="grid grid-cols-2 gap-3 mb-5">
        <div className="bg-gradient-to-br from-pink-500 to-rose-500 rounded-2xl p-4 text-white">
          <Heart className="w-5 h-5 mb-2 opacity-80" fill="currentColor" />
          <p className="text-2xl" style={{ fontWeight: 700 }}>{favoriteJobs.length}</p>
          <p className="text-xs text-pink-100 mt-0.5">Vagas favoritas</p>
        </div>
        <div className="bg-gradient-to-br from-purple-600 to-violet-600 rounded-2xl p-4 text-white">
          <Briefcase className="w-5 h-5 mb-2 opacity-80" />
          <p className="text-2xl" style={{ fontWeight: 700 }}>{applications.length}</p>
          <p className="text-xs text-purple-200 mt-0.5">Candidaturas</p>
        </div>
      </div>

      {/* Tab selector */}
      <div className="flex bg-slate-100 rounded-2xl p-1 mb-5">
        <button
          onClick={() => setTab("favorites")}
          className={`flex-1 flex items-center justify-center gap-2 py-2.5 rounded-xl text-sm transition-all ${
            tab === "favorites" ? "bg-white text-purple-700 shadow-sm" : "text-slate-500"
          }`}
          style={{ fontWeight: tab === "favorites" ? 600 : 400 }}
        >
          <Heart className="w-4 h-4" fill={tab === "favorites" ? "currentColor" : "none"} />
          Favoritas ({favoriteJobs.length})
        </button>
        <button
          onClick={() => setTab("applications")}
          className={`flex-1 flex items-center justify-center gap-2 py-2.5 rounded-xl text-sm transition-all ${
            tab === "applications" ? "bg-white text-purple-700 shadow-sm" : "text-slate-500"
          }`}
          style={{ fontWeight: tab === "applications" ? 600 : 400 }}
        >
          <Briefcase className="w-4 h-4" />
          Candidaturas ({applications.length})
        </button>
      </div>

      {loading ? (
        <div className="flex flex-col items-center justify-center py-16 gap-3">
          <Loader2 className="w-7 h-7 animate-spin text-purple-500" />
          <p className="text-sm text-slate-400">Carregando...</p>
        </div>
      ) : tab === "favorites" ? (
        favoriteJobs.length === 0 ? (
          <div className="text-center py-16">
            <p className="text-5xl mb-4">❤</p>
            <p className="text-slate-700 mb-1" style={{ fontWeight: 600 }}>
              Nenhuma vaga favorita ainda
            </p>
            <p className="text-sm text-slate-400 mb-6">
              Toque no ❤️ de uma vaga para salvar aqui
            </p>
            <Link
              to="/app"
              className="inline-block bg-gradient-to-r from-purple-600 to-pink-500 text-white px-6 py-3 rounded-xl text-sm"
              style={{ fontWeight: 600 }}
            >
              Explorar vagas
            </Link>
          </div>
        ) : (
          <div className="flex flex-col gap-3">
            {favoriteJobs.map((job) => (
              <div
                key={job.id}
                className="bg-white rounded-2xl p-4 shadow-sm border border-slate-100"
              >
                {/* Top row */}
                <div className="flex items-start justify-between gap-2 mb-3">
                  <div className="flex items-center gap-3 min-w-0">
                    <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-100 to-pink-100 flex items-center justify-center shrink-0">
                      <span className="text-purple-700 text-sm" style={{ fontWeight: 700 }}>
                        {job.company.charAt(0)}
                      </span>
                    </div>
                    <div className="min-w-0">
                      <p className="text-[11px] text-slate-400 truncate">{job.company}</p>
                      <p
                        className="text-slate-900 truncate"
                        style={{ fontWeight: 600, fontSize: "14px" }}
                      >
                        {job.title}
                      </p>
                    </div>
                  </div>
                  <button
                    onClick={() => handleRemoveFavorite(job.id)}
                    className="p-2 rounded-xl bg-red-50 text-red-400 active:bg-red-100 shrink-0"
                    aria-label="Remover favorito"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>

                {/* Info chips */}
                <div className="flex flex-wrap gap-2 mb-3">
                  <span className="text-xs text-slate-500 flex items-center gap-1 bg-slate-50 px-2 py-1 rounded-full">
                    <MapPin className="w-3 h-3" /> {job.city}
                  </span>
                  <span className="text-xs text-purple-600 flex items-center gap-1 bg-purple-50 px-2 py-1 rounded-full">
                    <Clock className="w-3 h-3" /> {scheduleLabel[job.schedule] ?? job.schedule}
                  </span>
                  <span className="text-xs text-green-600 flex items-center gap-1 bg-green-50 px-2 py-1 rounded-full">
                    <DollarSign className="w-3 h-3" /> R$ {job.salary.toLocaleString("pt-BR")}
                  </span>
                </div>

                <Link
                  to={`/app/vaga/${job.id}`}
                  className="flex items-center justify-center gap-2 w-full py-2.5 rounded-xl border border-purple-200 text-purple-600 text-sm"
                  style={{ fontWeight: 500 }}
                >
                  Ver detalhes <ArrowRight className="w-3.5 h-3.5" />
                </Link>
              </div>
            ))}
          </div>
        )
      ) : applications.length === 0 ? (
        <div className="text-center py-16">
          <p className="text-5xl mb-4">📋</p>
          <p className="text-slate-700 mb-1" style={{ fontWeight: 600 }}>
            Nenhuma candidatura enviada
          </p>
          <p className="text-sm text-slate-400 mb-6">
            Candidate-se às vagas e acompanhe aqui
          </p>
          <Link
            to="/app"
            className="inline-block bg-gradient-to-r from-purple-600 to-pink-500 text-white px-6 py-3 rounded-xl text-sm"
            style={{ fontWeight: 600 }}
          >
            Ver vagas disponíveis
          </Link>
        </div>
      ) : (
        <div className="flex flex-col gap-3">
          {applications.map((app) => (
            <div
              key={app.jobId}
              className="bg-white rounded-2xl p-4 shadow-sm border border-slate-100"
            >
              <div className="flex items-start justify-between gap-2 mb-1">
                <div className="min-w-0 flex-1">
                  <span className="text-[10px] bg-green-100 text-green-700 px-2 py-0.5 rounded-full inline-block mb-1.5" style={{ fontWeight: 600 }}>
                    ✓ Candidatura enviada
                  </span>
                  <p
                    className="text-slate-900 truncate"
                    style={{ fontWeight: 600, fontSize: "14px" }}
                  >
                    {app.jobTitle}
                  </p>
                  <p className="text-xs text-slate-400">{app.company}</p>
                </div>
                <button
                  onClick={() => handleRemoveApplication(app.jobId)}
                  className="p-2 rounded-xl bg-red-50 text-red-400 active:bg-red-100 shrink-0"
                  aria-label="Remover candidatura"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>

              <div className="flex items-center gap-1.5 text-xs text-slate-400 mb-3 mt-2">
                <Calendar className="w-3 h-3" />
                {new Date(app.appliedAt).toLocaleDateString("pt-BR", {
                  day: "2-digit",
                  month: "long",
                  year: "numeric",
                })}
              </div>

              <Link
                to={`/app/vaga/${app.jobId}`}
                className="flex items-center justify-center gap-2 w-full py-2.5 rounded-xl border border-purple-200 text-purple-600 text-sm"
                style={{ fontWeight: 500 }}
              >
                Ver detalhes <ArrowRight className="w-3.5 h-3.5" />
              </Link>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}


