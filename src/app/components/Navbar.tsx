import { Link, useLocation, useNavigate } from "react-router";
import { Home, Info, Heart, User, Sparkles, LogOut } from "lucide-react";
import { useAuth } from "../contexts/AuthContext";
import { useState } from "react";

const navItems = [
  { path: "/app", icon: Home, label: "Vagas" },
  { path: "/app/sobre", icon: Info, label: "Sobre" },
  { path: "/app/candidaturas", icon: Heart, label: "Favoritas" },
  { path: "/app/perfil", icon: User, label: "Perfil" },
];

export function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [showLogout, setShowLogout] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/", { replace: true });
  };

  return (
    <>
      {/* Top Header */}
      <header className="bg-white shadow-sm sticky top-0 z-50">
        <div className="max-w-2xl mx-auto px-4 h-14 flex items-center justify-between">
          {/* Brand */}
          <div className="flex items-center gap-2.5">
            <div className="bg-gradient-to-r from-purple-600 to-pink-500 p-1.5 rounded-lg">
              <Sparkles className="w-5 h-5 text-white" />
            </div>
            <span
              className="text-xl text-slate-900"
              style={{ fontWeight: 700, letterSpacing: "-0.5px" }}
            >
              Afirma<span className="text-purple-600">+</span>
            </span>
          </div>

          {/* User avatar / logout */}
          <div className="relative">
            <button
              onClick={() => setShowLogout((v) => !v)}
              className="flex items-center gap-2 bg-slate-100 rounded-xl px-3 py-1.5 active:bg-slate-200 transition-colors"
            >
              <div className="w-6 h-6 rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center">
                <span className="text-white text-[11px]" style={{ fontWeight: 700 }}>
                  {user?.name?.charAt(0).toUpperCase() ?? "?"}
                </span>
              </div>
              <span
                className="text-sm text-slate-700 max-w-[90px] truncate hidden sm:block"
                style={{ fontWeight: 500 }}
              >
                {user?.name?.split(" ")[0]}
              </span>
            </button>

            {/* Dropdown */}
            {showLogout && (
              <>
                {/* Backdrop to close */}
                <div
                  className="fixed inset-0 z-40"
                  onClick={() => setShowLogout(false)}
                />
                <div className="absolute right-0 top-full mt-2 bg-white rounded-2xl shadow-xl border border-slate-100 overflow-hidden z-50 min-w-[180px]">
                  <div className="px-4 py-3 border-b border-slate-100">
                    <p className="text-xs text-slate-400">Logada(o) como</p>
                    <p
                      className="text-sm text-slate-800 truncate"
                      style={{ fontWeight: 600 }}
                    >
                      {user?.name}
                    </p>
                    <p className="text-xs text-slate-400 truncate">{user?.email}</p>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="w-full flex items-center gap-3 px-4 py-3 text-sm text-red-600 active:bg-red-50 transition-colors"
                    style={{ fontWeight: 500 }}
                  >
                    <LogOut className="w-4 h-4" />
                    Sair da conta
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      </header>

      {/* Bottom Navigation Bar */}
      <nav className="fixed bottom-0 left-0 right-0 z-50 bg-white border-t border-slate-200">
        <div className="max-w-2xl mx-auto flex">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive =
              item.path === "/app"
                ? location.pathname === "/app"
                : location.pathname.startsWith(item.path);

            return (
              <Link
                key={item.path}
                to={item.path}
                className={`flex-1 flex flex-col items-center justify-center gap-0.5 py-2.5 transition-colors ${
                  isActive ? "text-purple-600" : "text-slate-400"
                }`}
              >
                <div
                  className={`p-1 rounded-xl transition-all ${
                    isActive ? "bg-purple-100" : ""
                  }`}
                >
                  <Icon
                    className="w-5 h-5"
                    fill={
                      isActive && item.path === "/app/candidaturas"
                        ? "currentColor"
                        : "none"
                    }
                  />
                </div>
                <span
                  className={`text-[10px] ${
                    isActive ? "text-purple-600" : "text-slate-400"
                  }`}
                  style={{ fontWeight: isActive ? 600 : 400 }}
                >
                  {item.label}
                </span>
              </Link>
            );
          })}
        </div>
      </nav>
    </>
  );
}

