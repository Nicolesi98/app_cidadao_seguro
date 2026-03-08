export interface Job {
  id: number;
  title: string;
  company: string;
  company_website: string;
  number_of_positions: number;
  city: string;
  schedule: string;
  salary: number;
  description: string;
  requirements: string;
  created_at: string;
  updated_at: string;
}

const BASE_URL = "https://apis.codante.io/api/job-board/jobs";

/**
 * Fetches ALL jobs across all pages from the paginated API.
 * Response structure: { data: Job[], links: { next: string | null }, meta: {...} }
 */
export async function fetchAllJobs(): Promise<Job[]> {
  let allJobs: Job[] = [];
  let nextUrl: string | null = BASE_URL;

  while (nextUrl) {
    const response = await fetch(nextUrl);
    const json = await response.json();

    // The API always returns { data: [...], links: { next: ... }, meta: {...} }
    const pageJobs: Job[] = Array.isArray(json) ? json : json.data ?? [];
    allJobs = [...allJobs, ...pageJobs];

    // Follow pagination — links.next is null on the last page
    nextUrl = json?.links?.next ?? null;
  }

  return allJobs;
}

/**
 * Fetches a single job by ID using the direct endpoint /jobs/{id}.
 * Response structure: { data: Job }
 */
export async function fetchJobById(id: number | string): Promise<Job | null> {
  const response = await fetch(`${BASE_URL}/${id}`);
  if (!response.ok) return null;
  const json = await response.json();
  // The API may return { data: {...} } or the object directly
  return json?.data ?? (json?.id ? json : null);
}
