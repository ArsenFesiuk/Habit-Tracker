import api from "./axios";
import type { StatsDTO } from "../types/Stats";

export async function fetchStats(from: string, to: string): Promise<StatsDTO> {
  const res = await api.get<StatsDTO>("/stats", { params: { from, to } });
  return res.data;
}
