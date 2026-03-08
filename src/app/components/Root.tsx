import { Outlet, Navigate } from "react-router";
import { Navbar } from "./Navbar";
import { useAuth } from "../contexts/AuthContext";

export function Root() {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <Navbar />
      {/* pb-20 gives space for the fixed bottom nav */}
      <main className="pb-20 max-w-2xl mx-auto">
        <Outlet />
      </main>
    </div>
  );
}

