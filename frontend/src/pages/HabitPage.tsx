import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import type { Habit } from "../types/Habit";
import * as api from "../api/habits";
import MonthCalendar from "../components/MonthCalendar";
import "../styles/habitPage.css";

function pad2(n: number): string {
  return n < 10 ? `0${n}` : `${n}`;
}

function isoDay(year: number, month: number, day: number): string {
  return `${year}-${pad2(month + 1)}-${pad2(day)}`;
}

export default function HabitPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const habitId = Number(id);

  const today = useMemo(() => new Date(), []);
  const [year, setYear] = useState(today.getFullYear());
  const [month, setMonth] = useState(today.getMonth());

  const [habit, setHabit] = useState<Habit | null>(null);
  const [doneDates, setDoneDates] = useState<Set<string>>(new Set());
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!Number.isFinite(habitId)) {
      navigate("/home", { replace: true });
      return;
    }
    let cancelled = false;
    api.listHabits()
      .then(list => {
        if (cancelled) return;
        const h = list.find(x => x.id === habitId);
        if (!h) navigate("/home", { replace: true });
        else setHabit(h);
      })
      .catch(() => { if (!cancelled) setError("Не вдалося завантажити звичку"); });
    return () => { cancelled = true; };
  }, [habitId, navigate]);

  useEffect(() => {
    if (!habit) return;
    const from = isoDay(year, month, 1);
    const lastDay = new Date(year, month + 1, 0).getDate();
    const to = isoDay(year, month, lastDay);
    let cancelled = false;
    api.getEntries(habit.id, from, to)
      .then(days => {
        if (cancelled) return;
        const set = new Set<string>();
        days.forEach(d => { if (d.done) set.add(d.date); });
        setDoneDates(set);
      })
      .catch(() => { if (!cancelled) setError("Не вдалося завантажити місяць"); });
    return () => { cancelled = true; };
  }, [habit, year, month]);

  const canGoNext =
    year < today.getFullYear() ||
    (year === today.getFullYear() && month < today.getMonth());

  const goPrev = () => {
    if (month === 0) { setMonth(11); setYear(y => y - 1); }
    else setMonth(m => m - 1);
  };

  const goNext = () => {
    if (!canGoNext) return;
    if (month === 11) { setMonth(0); setYear(y => y + 1); }
    else setMonth(m => m + 1);
  };

  return (
    <div className="habit-page">
      <div className="container">
        <button className="back-btn" onClick={() => navigate("/home")} type="button">← Назад</button>
        {error && <div className="banner-error">{error}</div>}
        {habit && (
          <>
            <h1 className="habit-title">{habit.name}</h1>
            <p className="muted">{habit.totalCount} виконань</p>
            <MonthCalendar
              year={year}
              month={month}
              doneDates={doneDates}
              today={today}
              onPrev={goPrev}
              onNext={goNext}
              canGoNext={canGoNext}
            />
          </>
        )}
      </div>
    </div>
  );
}
