import { useState, useEffect } from "react";
import {
  User,
  Briefcase,
  Save,
  CheckCircle,
  ChevronDown,
  Phone,
  MapPin,
  Mail,
  FileText,
  Heart,
  Star,
  LogOut,
} from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { useNavigate } from "react-router";
import {
  getProfile,
  saveProfile,
  getFavorites,
  getApplications,
  type UserProfile,
} from "../utils/userStorage";

const defaultProfile = (email: string, name: string): UserProfile => ({
  name,
  email,
  phone: "",
  city: "",
  profession: "",
  bio: "",
  gender: "",
  race: "",
});

const genderOptions = [
  { value: "mulher-cis", label: "Mulher cisgênero" },
  { value: "mulher-trans", label: "Mulher transgênero" },
  { value: "homem-cis", label: "Homem cisgênero" },
  { value: "homem-trans", label: "Homem transgênero" },
  { value: "nao-binario", label: "Não-binário" },
  { value: "outro", label: "Outro" },
  { value: "prefiro-nao-dizer", label: "Prefiro não dizer" },
];

const raceOptions = [
  { value: "preta", label: "Preta" },
  { value: "parda", label: "Parda" },
  { value: "branca", label: "Branca" },
  { value: "amarela", label: "Amarela" },
  { value: "indigena", label: "Indígena" },
  { value: "prefiro-nao-dizer", label: "Prefiro não dizer" },
];

export function Profile() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const email = user?.email ?? "";
  const name = user?.name ?? "";

  const [profile, setProfile] = useState<UserProfile>(() =>
    getProfile(email, name)
  );
  const [saved, setSaved] = useState(false);
  const [favCount, setFavCount] = useState(0);
  const [appCount, setAppCount] = useState(0);

  useEffect(() => {
    const p = getProfile(email, name);
    // Garante que nome/email do cadastro preenchem o perfil se estiver vazio
    setProfile({
      ...p,
      name: p.name || name,
      email: p.email || email,
    });
    setFavCount(getFavorites(email).length);
    setAppCount(getApplications(email).length);
  }, [email, name]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    setProfile((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    saveProfile(email, profile);
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  const handleLogout = () => {
    logout();
    navigate("/", { replace: true });
  };

  const filledFields = Object.entries(profile).filter(
    ([, v]) => String(v).trim() !== ""
  ).length;
  const totalFields = Object.keys(profile).length;
  const completionPct = Math.round((filledFields / totalFields) * 100);

  const genderLabel =
    genderOptions.find((g) => g.value === profile.gender)?.label ?? "-";
  const raceLabel =
    raceOptions.find((r) => r.value === profile.race)?.label ?? "-";

  return (
    <div className="px-4 pt-5 pb-4">
      {/* Profile hero card */}
      <div className="bg-gradient-to-r from-purple-600 to-pink-500 rounded-2xl p-5 mb-4 text-white">
        <div className="flex items-center gap-4 mb-4">
          <div className="w-16 h-16 rounded-2xl bg-white/20 flex items-center justify-center shrink-0">
            <span className="text-3xl text-white" style={{ fontWeight: 700 }}>
              {(profile.name || name).charAt(0).toUpperCase()}
            </span>
          </div>
          <div className="min-w-0 flex-1">
            <p className="text-white text-lg truncate" style={{ fontWeight: 700 }}>
              {profile.name || name}
            </p>
            <p className="text-purple-200 text-sm truncate">
              {profile.profession || "Adicione sua profissão"}
            </p>
            <p className="text-purple-300 text-xs truncate">{email}</p>
          </div>
          <button
            onClick={handleLogout}
            className="p-2 rounded-xl bg-white/15 text-white/80 active:bg-white/25 shrink-0"
            aria-label="Sair"
          >
            <LogOut className="w-4 h-4" />
          </button>
        </div>

        {/* Progress bar */}
        <div>
          <div className="flex justify-between text-xs text-purple-200 mb-1">
            <span>Perfil completo</span>
            <span style={{ fontWeight: 600 }}>{completionPct}%</span>
          </div>
          <div className="h-1.5 bg-white/20 rounded-full overflow-hidden">
            <div
              className="h-1.5 bg-white rounded-full transition-all duration-500"
              style={{ width: `${completionPct}%` }}
            />
          </div>
        </div>
      </div>

      {/* Stats row */}
      <div className="grid grid-cols-3 gap-2 mb-5">
        <div className="bg-white rounded-2xl p-3 shadow-sm text-center border border-slate-100">
          <Heart className="w-4 h-4 text-pink-500 mx-auto mb-1" fill="currentColor" />
          <p className="text-xl text-slate-900" style={{ fontWeight: 700 }}>
            {favCount}
          </p>
          <p className="text-[11px] text-slate-400">Favoritas</p>
        </div>
        <div className="bg-white rounded-2xl p-3 shadow-sm text-center border border-slate-100">
          <Briefcase className="w-4 h-4 text-purple-500 mx-auto mb-1" />
          <p className="text-xl text-slate-900" style={{ fontWeight: 700 }}>
            {appCount}
          </p>
          <p className="text-[11px] text-slate-400">Candidaturas</p>
        </div>
        <div className="bg-white rounded-2xl p-3 shadow-sm text-center border border-slate-100">
          <Star className="w-4 h-4 text-yellow-400 mx-auto mb-1" fill="currentColor" />
          <p className="text-xl text-slate-900" style={{ fontWeight: 700 }}>
            {completionPct}%
          </p>
          <p className="text-[11px] text-slate-400">Completo</p>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="space-y-3">
        {/* Informações Pessoais */}
        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="flex items-center gap-2 px-4 py-3 border-b border-slate-100">
            <div className="bg-purple-100 p-1.5 rounded-lg">
              <User className="w-4 h-4 text-purple-600" />
            </div>
            <p className="text-slate-800" style={{ fontWeight: 600, fontSize: "14px" }}>
              Informações Pessoais
            </p>
          </div>

          <div className="p-4 space-y-4">
            <Field
              icon={<User className="w-3.5 h-3.5 text-slate-400" />}
              label="Nome completo"
            >
              <input
                name="name"
                value={profile.name}
                onChange={handleChange}
                placeholder="Seu nome"
                className="field-input"
              />
            </Field>

            <Field
              icon={<Mail className="w-3.5 h-3.5 text-slate-400" />}
              label="E-mail"
            >
              <input
                name="email"
                type="email"
                value={profile.email}
                onChange={handleChange}
                placeholder="seu@email.com"
                className="field-input"
                readOnly
              />
            </Field>

            <div className="grid grid-cols-2 gap-3">
              <Field
                icon={<Phone className="w-3.5 h-3.5 text-slate-400" />}
                label="Telefone"
              >
                <input
                  name="phone"
                  value={profile.phone}
                  onChange={handleChange}
                  placeholder="(00) 00000-0000"
                  inputMode="tel"
                  className="field-input"
                />
              </Field>

              <Field
                icon={<MapPin className="w-3.5 h-3.5 text-slate-400" />}
                label="Cidade"
              >
                <input
                  name="city"
                  value={profile.city}
                  onChange={handleChange}
                  placeholder="Sua cidade"
                  className="field-input"
                />
              </Field>
            </div>
          </div>
        </div>

        {/* Informações Profissionais */}
        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="flex items-center gap-2 px-4 py-3 border-b border-slate-100">
            <div className="bg-blue-100 p-1.5 rounded-lg">
              <Briefcase className="w-4 h-4 text-blue-600" />
            </div>
            <p className="text-slate-800" style={{ fontWeight: 600, fontSize: "14px" }}>
              Informações Profissionais
            </p>
          </div>

          <div className="p-4 space-y-4">
            <Field
              icon={<Briefcase className="w-3.5 h-3.5 text-slate-400" />}
              label="Profissão / Área de atuação"
            >
              <input
                name="profession"
                value={profile.profession}
                onChange={handleChange}
                placeholder="Ex: Desenvolvedora de Software"
                className="field-input"
              />
            </Field>

            <Field
              icon={<FileText className="w-3.5 h-3.5 text-slate-400" />}
              label="Sobre você"
            >
              <textarea
                name="bio"
                value={profile.bio}
                onChange={handleChange}
                placeholder="Sua experiência e objetivos profissionais..."
                rows={3}
                className="field-input resize-none"
              />
            </Field>
          </div>
        </div>

        {/* Vagas Afirmativas */}
        <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
          <div className="flex items-center gap-2 px-4 py-3 border-b border-slate-100">
            <div className="bg-pink-100 p-1.5 rounded-lg">
              <CheckCircle className="w-4 h-4 text-pink-600" />
            </div>
            <div>
              <p className="text-slate-800" style={{ fontWeight: 600, fontSize: "14px" }}>
                Vagas Afirmativas
              </p>
              <p className="text-[11px] text-slate-400">
                Nos ajuda a encontrar as melhores oportunidades para você
              </p>
            </div>
          </div>

          <div className="p-4 space-y-4">
            <div>
              <label
                className="text-xs text-slate-500 mb-1.5 block"
                style={{ fontWeight: 600 }}
              >
                Identidade de gênero
              </label>
              <div className="relative">
                <select
                  name="gender"
                  value={profile.gender}
                  onChange={handleChange}
                  className="w-full appearance-none rounded-xl border border-slate-200 px-3 py-3 text-sm bg-slate-50 outline-none focus:ring-2 focus:ring-purple-300"
                >
                  <option value="">Selecione</option>
                  {genderOptions.map((o) => (
                    <option key={o.value} value={o.value}>
                      {o.label}
                    </option>
                  ))}
                </select>
                <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
              </div>
            </div>

            <div>
              <label
                className="text-xs text-slate-500 mb-1.5 block"
                style={{ fontWeight: 600 }}
              >
                Raça/Cor
              </label>
              <div className="relative">
                <select
                  name="race"
                  value={profile.race}
                  onChange={handleChange}
                  className="w-full appearance-none rounded-xl border border-slate-200 px-3 py-3 text-sm bg-slate-50 outline-none focus:ring-2 focus:ring-purple-300"
                >
                  <option value="">Selecione</option>
                  {raceOptions.map((o) => (
                    <option key={o.value} value={o.value}>
                      {o.label}
                    </option>
                  ))}
                </select>
                <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
              </div>
            </div>

            {/* Preview chips when filled */}
            {(profile.gender || profile.race) && (
              <div className="flex flex-wrap gap-2 pt-1">
                {profile.gender && (
                  <span className="text-xs bg-purple-50 text-purple-600 px-3 py-1 rounded-full" style={{ fontWeight: 500 }}>
                    {genderLabel}
                  </span>
                )}
                {profile.race && (
                  <span className="text-xs bg-pink-50 text-pink-600 px-3 py-1 rounded-full" style={{ fontWeight: 500 }}>
                    {raceLabel}
                  </span>
                )}
              </div>
            )}
          </div>
        </div>

        {/* Save button */}
        <button
          type="submit"
          className={`w-full flex items-center justify-center gap-2 py-4 rounded-2xl text-white text-base transition-all ${
            saved
              ? "bg-green-500"
              : "bg-gradient-to-r from-purple-600 to-pink-500 active:opacity-90"
          }`}
          style={{ fontWeight: 700 }}
        >
          {saved ? (
            <>
              <CheckCircle className="w-5 h-5" />
              Perfil salvo com sucesso!
            </>
          ) : (
            <>
              <Save className="w-5 h-5" />
              Salvar perfil
            </>
          )}
        </button>
      </form>
    </div>
  );
}

// Helper component
function Field({
  icon,
  label,
  children,
}: {
  icon: React.ReactNode;
  label: string;
  children: React.ReactNode;
}) {
  return (
    <div>
      <label
        className="text-xs text-slate-500 mb-1.5 flex items-center gap-1"
        style={{ fontWeight: 600 }}
      >
        {icon}
        {label}
      </label>
      <style>{`
        .field-input {
          width: 100%;
          border-radius: 12px;
          border: 1px solid #e2e8f0;
          padding: 12px;
          font-size: 14px;
          background: #f8fafc;
          outline: none;
          transition: box-shadow 0.15s;
        }
        .field-input:focus {
          box-shadow: 0 0 0 2px #c4b5fd;
          border-color: #c4b5fd;
        }
      `}</style>
      {children}
    </div>
  );
}


