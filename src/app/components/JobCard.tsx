import { Link } from "react-router";
import { MapPin, Clock, DollarSign, Users, Heart } from "lucide-react";
import { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { isFavorite, toggleFavorite } from "../utils/userStorage";

interface Job {
  id: number;
  title: string;
  company: string;
  city: string;
  schedule: string;
  salary: number;
  number_of_positions: number;
}

interface JobCardProps {
  job: Job;
}

const scheduleLabel: Record<string, string> = {
  "full-time": "Tempo integral",
  "part-time": "Meio período",
};

export function JobCard({ job }: JobCardProps) {
  const { user } = useAuth();
  const email = user?.email ?? "";

  const [favorited, setFavorited] = useState(() => isFavorite(email, job.id));

  const handleFavorite = (e: React.MouseEvent) => {
    e.preventDefault();
    const next = toggleFavorite(email, job.id);
    setFavorited(next);
  };

  return (
    <Link to={`/app/vaga/${job.id}`} className="block">
      <div className="bg-white rounded-2xl p-4 shadow-sm active:shadow-md transition-shadow border border-slate-100">
        {/* Header row */}
        <div className="flex justify-between items-start mb-3">
          <div className="flex items-center gap-3 flex-1 min-w-0">
            <div className="w-11 h-11 rounded-xl bg-gradient-to-br from-purple-100 to-pink-100 flex items-center justify-center shrink-0">
              <span className="text-purple-700 text-base" style={{ fontWeight: 700 }}>
                {job.company.charAt(0)}
              </span>
            </div>
            <div className="min-w-0">
              <p className="text-xs text-slate-400 truncate">{job.company}</p>
              <h3 className="text-slate-900 truncate" style={{ fontWeight: 600, fontSize: "15px" }}>
                {job.title}
              </h3>
            </div>
          </div>

          <button
            onClick={handleFavorite}
            className={`ml-2 p-2 rounded-xl transition-colors shrink-0 ${
              favorited ? "bg-pink-100 text-pink-500" : "bg-slate-100 text-slate-300"
            }`}
            aria-label={favorited ? "Remover dos favoritos" : "Adicionar aos favoritos"}
          >
            <Heart className="w-5 h-5" fill={favorited ? "currentColor" : "none"} />
          </button>
        </div>

        {/* Tags */}
        <div className="flex flex-wrap gap-2 mb-3">
          <span className="inline-flex items-center gap-1 text-xs bg-slate-100 text-slate-600 px-2.5 py-1 rounded-full">
            <MapPin className="w-3 h-3" /> {job.city}
          </span>
          <span className="inline-flex items-center gap-1 text-xs bg-purple-50 text-purple-600 px-2.5 py-1 rounded-full">
            <Clock className="w-3 h-3" /> {scheduleLabel[job.schedule] ?? job.schedule}
          </span>
          <span className="inline-flex items-center gap-1 text-xs bg-green-50 text-green-600 px-2.5 py-1 rounded-full">
            <Users className="w-3 h-3" /> {job.number_of_positions} vaga{job.number_of_positions > 1 ? "s" : ""}
          </span>
        </div>

        {/* Footer */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-1 text-slate-700">
            <DollarSign className="w-4 h-4 text-purple-500" />
            <span style={{ fontWeight: 600, fontSize: "15px" }}>
              R$ {job.salary.toLocaleString("pt-BR")}
            </span>
            <span className="text-xs text-slate-400">/mês</span>
          </div>
          <span className="text-xs text-purple-600 bg-purple-50 px-3 py-1 rounded-full" style={{ fontWeight: 500 }}>
            Ver detalhes {"->"}
          </span>
        </div>
      </div>
    </Link>
  );
}


