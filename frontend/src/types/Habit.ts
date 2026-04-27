import type { HabitDay } from "./HabitDay";

export interface Habit {
  id: number;
  name: string;
  totalCount: number;
  days: HabitDay[]; // length 7, [today-6 ... today]
}
