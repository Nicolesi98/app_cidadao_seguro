import { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router";
import {
  MapPin,
  Clock,
  DollarSign,
  Users,
  ExternalLink,
  ArrowLeft,
  Calendar,
  Heart,
  Loader2,
  CheckCircle,
} from "lucide-react";
import { fetchJobById, type Job } from "../utils/api";
import { useAuth } from "../contexts/AuthContext";
import {
  isFavorite,
  toggleFavorite,
  isApplied,
  addApplication,
} from "../utils/userStorage";

const scheduleLabel: Record<string, string> = {
  "full-time": "Tempo integral",
  "part-time": "Meio período",
};

export function JobDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const email = user?.email ?? "";

  const [job, setJob] = useState<Job | null>(null);
  const [loading, setLoading] = useState(true);
  const [favorited, setFavorited] = useState(false);
  const [applied, setApplied] = useState(false);
  const [justApplied, setJustApplied] = useState(false);

  useEffect(() => {
    fetchJobDetails();
  }, [id]);

  useEffect(() => {
    if (job) {
      setFavorited(isFavorite(email, job.id));
      setApplied(isApplied(email, job.id));
    }
  }, [job, email]);

  const fetchJobDetails = async () => {
    try {
      setLoading(true);
      const foundJob = await fetchJobById(Number(id));
      setJob(foundJob);
    } catch (error) {
      console.error("Erro ao carregar detalhes da vaga:", error);
      setJob(null);
    } finally {
      setLoading(false);
    }
  };

  const handleFavorite = () => {
    if (!job) return;
    const next = toggleFavorite(email, job.id);
    setFavorited(next);
  };

  const handleApply = () => {
    if (!job || applied) return;
    addApplication(email, {
      jobId: job.id,
      jobTitle: job.title,
      company: job.company,
      appliedAt: new Date().toISOString(),
    });
    setApplied(true);
    setJustApplied(true);
    setTimeout(() => setJustApplied(false), 3000);
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] gap-3">
        <Loader2 className="w-8 h-8 animate-spin text-purple-500" />
        <p className="text-sm text-slate-400">Carregando vaga...</p>
      </div>
    );
  }

  if (!job) {
    return (
      <div className="px-4 py-16 text-center">
        <p className="text-4xl mb-3">Ops</p>
        <p className="text-slate-600 mb-6">Vaga não encontrada</p>
        <Link
          to="/app"
          className="inline-block bg-gradient-to-r from-purple-600 to-pink-500 text-white px-6 py-3 rounded-xl text-sm"
          style={{ fontWeight: 600 }}
        >
          Ver todas as vagas
        </Link>
      </div>
    );
  }

  return (
    <div className="pb-4">
      {/* Header gradient */}
      <div className="bg-gradient-to-r from-purple-600 to-pink-500 px-4 pt-4 pb-8">
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 text-white/80 mb-4 text-sm active:text-white"
        >
          <ArrowLeft className="w-4 h-4" />
          Voltar
        </button>
        <div className="flex justify-between items-start">
          <div className="flex-1 min-w-0 pr-3">
            <p className="text-purple-200 text-sm mb-0.5">{job.company}</p>
            <h1 className="text-white text-xl" style={{ fontWeight: 700 }}>
              {job.title}
            </h1>
          </div>
          <button
            onClick={handleFavorite}
            className={`p-2.5 rounded-xl transition-colors shrink-0 ${
              favorited ? "bg-white/25 text-white" : "bg-white/15 text-white/70"
            }`}
            aria-label="Favoritar"
          >
            <Heart className="w-5 h-5" fill={favorited ? "currentColor" : "none"} />
          </button>
        </div>
      </div>

      {/* Info cards */}
      <div className="px-4 -mt-4">
        <div className="grid grid-cols-2 gap-2 mb-4">
          <div className="bg-white rounded-xl p-3 shadow-sm flex items-center gap-2">
            <div className="bg-purple-100 p-2 rounded-lg">
              <MapPin className="w-4 h-4 text-purple-600" />
            </div>
            <div className="min-w-0">
              <p className="text-[10px] text-slate-400">Localização</p>
              <p className="text-sm text-slate-800 truncate" style={{ fontWeight: 500 }}>
                {job.city}
              </p>
            </div>
          </div>

          <div className="bg-white rounded-xl p-3 shadow-sm flex items-center gap-2">
            <div className="bg-blue-100 p-2 rounded-lg">
              <Clock className="w-4 h-4 text-blue-600" />
            </div>
            <div className="min-w-0">
              <p className="text-[10px] text-slate-400">Contrato</p>
              <p className="text-sm text-slate-800 truncate" style={{ fontWeight: 500 }}>
                {scheduleLabel[job.schedule] ?? job.schedule}
              </p>
            </div>
          </div>

          <div className="bg-white rounded-xl p-3 shadow-sm flex items-center gap-2">
            <div className="bg-green-100 p-2 rounded-lg">
              <DollarSign className="w-4 h-4 text-green-600" />
            </div>
            <div className="min-w-0">
              <p className="text-[10px] text-slate-400">Salário</p>
              <p className="text-sm text-slate-800 truncate" style={{ fontWeight: 500 }}>
                R$ {job.salary.toLocaleString("pt-BR")}
              </p>
            </div>
          </div>

          <div className="bg-white rounded-xl p-3 shadow-sm flex items-center gap-2">
            <div className="bg-pink-100 p-2 rounded-lg">
              <Users className="w-4 h-4 text-pink-600" />
            </div>
            <div className="min-w-0">
              <p className="text-[10px] text-slate-400">Vagas</p>
              <p className="text-sm text-slate-800" style={{ fontWeight: 500 }}>
                {job.number_of_positions} posição{job.number_of_positions > 1 ? "s" : ""}
              </p>
            </div>
          </div>
        </div>

        {/* Description */}
        <div className="bg-white rounded-2xl p-4 shadow-sm mb-3">
          <h2 className="text-slate-800 mb-2" style={{ fontWeight: 600, fontSize: "15px" }}>
            Sobre a vaga
          </h2>
          <p className="text-sm text-slate-600 leading-relaxed">{job.description}</p>
        </div>

        {/* Requirements */}
        <div className="bg-white rounded-2xl p-4 shadow-sm mb-3">
          <h2 className="text-slate-800 mb-3" style={{ fontWeight: 600, fontSize: "15px" }}>
            Requisitos
          </h2>
          <ul className="space-y-2">
            {job.requirements
              .split("\n")
              .filter((r) => r.trim())
              .map((req, i) => (
                <li key={i} className="flex items-start gap-2 text-sm text-slate-600">
                  <span className="text-purple-400 mt-0.5 shrink-0">-</span>
                  <span>{req.replace(/^[-•]\s*/, "")}</span>
                </li>
              ))}
          </ul>
        </div>

        {/* Published date */}
        <div className="flex items-center gap-2 text-xs text-slate-400 mb-5 px-1">
          <Calendar className="w-3.5 h-3.5" />
          Publicada em {new Date(job.created_at).toLocaleDateString("pt-BR")}
        </div>

        {/* Action buttons */}
        <div className="space-y-2">
          <button
            onClick={handleApply}
            disabled={applied}
            className={`w-full flex items-center justify-center gap-2 py-4 rounded-2xl text-white text-base transition-all ${
              applied
                ? "bg-green-500"
                : "bg-gradient-to-r from-purple-600 to-pink-500 active:opacity-90"
            }`}
            style={{ fontWeight: 600 }}
          >
            {applied ? (
              <>
                <CheckCircle className="w-5 h-5" />
                {justApplied ? "Candidatura enviada!" : "Já candidatada(o)"}
              </>
            ) : (
              "Candidatar-se agora"
            )}
          </button>

          <a
            href={job.company_website}
            target="_blank"
            rel="noopener noreferrer"
            className="w-full flex items-center justify-center gap-2 py-4 rounded-2xl border-2 border-purple-200 text-purple-700 text-base transition-all active:bg-purple-50"
            style={{ fontWeight: 600 }}
          >
            <ExternalLink className="w-4 h-4" />
            Site da empresa
          </a>
        </div>
      </div>
    </div>
  );
}


