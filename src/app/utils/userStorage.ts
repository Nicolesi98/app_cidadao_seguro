/**
 * Armazenamento isolado por usuário.
 * Estrutura no localStorage:
 * {
 *   "email@exemplo.com": {
 *     profile: { ... },
 *     favorites: [1, 2, 3],
 *     applications: [{ jobId, jobTitle, company, appliedAt }]
 *   }
 * }
 */

const STORAGE_KEY = "afirma_user_data";

export interface UserProfile {
  name: string;
  email: string;
  phone: string;
  city: string;
  profession: string;
  bio: string;
  gender: string;
  race: string;
}

export interface Application {
  jobId: number;
  jobTitle: string;
  company: string;
  appliedAt: string;
}

export interface UserData {
  profile: UserProfile;
  favorites: number[];
  applications: Application[];
}

const defaultData = (email: string, name?: string): UserData => ({
  profile: {
    name: name ?? "",
    email,
    phone: "",
    city: "",
    profession: "",
    bio: "",
    gender: "",
    race: "",
  },
  favorites: [],
  applications: [],
});

// ---------- root read/write ----------

function readAll(): Record<string, UserData> {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY) || "{}");
  } catch {
    return {};
  }
}

function writeAll(data: Record<string, UserData>) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
}

// ---------- user-level helpers ----------

export function getUserData(email: string, name?: string): UserData {
  const all = readAll();
  if (!all[email]) {
    all[email] = defaultData(email, name);
    writeAll(all);
  }
  return all[email];
}

function setUserData(email: string, data: UserData) {
  const all = readAll();
  all[email] = data;
  writeAll(all);
}

// ---------- profile ----------

export function getProfile(email: string, name?: string): UserProfile {
  return getUserData(email, name).profile;
}

export function saveProfile(email: string, profile: UserProfile) {
  const data = getUserData(email);
  data.profile = profile;
  setUserData(email, data);
}

// ---------- favorites ----------

export function getFavorites(email: string): number[] {
  return getUserData(email).favorites;
}

export function isFavorite(email: string, jobId: number): boolean {
  return getFavorites(email).includes(jobId);
}

export function toggleFavorite(email: string, jobId: number): boolean {
  const data = getUserData(email);
  const idx = data.favorites.indexOf(jobId);
  if (idx === -1) {
    data.favorites.push(jobId);
    setUserData(email, data);
    return true; // agora é favorito
  } else {
    data.favorites.splice(idx, 1);
    setUserData(email, data);
    return false; // removido
  }
}

export function removeFavorite(email: string, jobId: number) {
  const data = getUserData(email);
  data.favorites = data.favorites.filter((id) => id !== jobId);
  setUserData(email, data);
}

// ---------- applications ----------

export function getApplications(email: string): Application[] {
  return getUserData(email).applications;
}

export function isApplied(email: string, jobId: number): boolean {
  return getApplications(email).some((a) => a.jobId === jobId);
}

export function addApplication(email: string, app: Application) {
  const data = getUserData(email);
  if (!data.applications.find((a) => a.jobId === app.jobId)) {
    data.applications.push(app);
    setUserData(email, data);
  }
}

export function removeApplication(email: string, jobId: number) {
  const data = getUserData(email);
  data.applications = data.applications.filter((a) => a.jobId !== jobId);
  setUserData(email, data);
}
