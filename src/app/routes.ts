import { createBrowserRouter } from "react-router";
import { Root } from "./components/Root";
import { Home } from "./pages/Home";
import { JobDetails } from "./pages/JobDetails";
import { About } from "./pages/About";
import { Applications } from "./pages/Applications";
import { Profile } from "./pages/Profile";
import { Login } from "./pages/Login";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: Login,
  },
  {
    path: "/login",
    Component: Login,
  },
  {
    path: "/app",
    Component: Root,
    children: [
      { index: true, Component: Home },
      { path: "vaga/:id", Component: JobDetails },
      { path: "sobre", Component: About },
      { path: "candidaturas", Component: Applications },
      { path: "perfil", Component: Profile },
    ],
  },
]);

