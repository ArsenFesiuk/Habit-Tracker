export interface WeeklyTrendPoint {
  weekStart: string; // YYYY-MM-DD
  rate: number;
}

export interface HabitBreakdown {
  name: string;
  color: string;
  rate: number;
}

export interface StatsDTO {
  completionRate: number;
  totalCompletions: number;
  activeHabits: number;
  bestCurrentStreak: number;
  weeklyTrend: WeeklyTrendPoint[];
  habitBreakdown: HabitBreakdown[];
}
