import api from "./axios";
import type { Habit } from "../types/Habit";
import type { HabitDay } from "../types/HabitDay";

export async function listHabits(): Promise<Habit[]> {
  const res = await api.get<Habit[]>("/habits/me");
  return res.data;
}

export async function createHabit(name: string): Promise<Habit> {
  const res = await api.post<Habit>("/habits", { name });
  return res.data;
}

export async function renameHabit(id: number, name: string): Promise<Habit> {
  const res = await api.patch<Habit>(`/habits/${id}`, { name });
  return res.data;
}

export async function deleteHabit(id: number): Promise<void> {
  await api.delete(`/habits/${id}`);
}

export async function markDay(id: number, date: string): Promise<Habit> {
  const res = await api.put<Habit>(`/habits/${id}/entries/${date}`);
  return res.data;
}

export async function unmarkDay(id: number, date: string): Promise<Habit> {
  const res = await api.delete<Habit>(`/habits/${id}/entries/${date}`);
  return res.data;
}

export async function getEntries(id: number, from: string, to: string): Promise<HabitDay[]> {
  const res = await api.get<HabitDay[]>(`/habits/${id}/entries`, {
    params: { from, to },
  });
  return res.data;
}
