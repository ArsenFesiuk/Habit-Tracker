import { useCallback, useEffect, useRef, useState } from "react";
import type { Habit } from "../types/Habit";
import * as api from "../api/habits";

export function useHabits() {
  const [habits, setHabits] = useState<Habit[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const inFlight = useRef<Set<string>>(new Set());

  const showError = useCallback((msg: string) => {
    setError(msg);
    window.setTimeout(() => setError(null), 3000);
  }, []);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const data = await api.listHabits();
      setHabits(data);
    } catch {
      showError("Не вдалося завантажити звички");
    } finally {
      setLoading(false);
    }
  }, [showError]);

  useEffect(() => {
    void load();
  }, [load]);

  const createHabit = useCallback(async (name: string) => {
    try {
      const created = await api.createHabit(name);
      setHabits(prev => [...prev, created]);
    } catch {
      showError("Не вдалося створити звичку");
    }
  }, [showError]);

  const renameHabit = useCallback(async (id: number, name: string) => {
    try {
      const updated = await api.renameHabit(id, name);
      setHabits(prev => prev.map(h => h.id === id ? updated : h));
    } catch {
      showError("Не вдалося перейменувати");
    }
  }, [showError]);

  const deleteHabit = useCallback(async (id: number) => {
    try {
      await api.deleteHabit(id);
      setHabits(prev => prev.filter(h => h.id !== id));
    } catch {
      showError("Не вдалося видалити");
    }
  }, [showError]);

  const toggleDay = useCallback(async (id: number, date: string) => {
    const key = `${id}|${date}`;
    if (inFlight.current.has(key)) return;
    inFlight.current.add(key);

    let prevSnapshot: Habit[] = [];
    let willBeDone = false;
    setHabits(prev => {
      prevSnapshot = prev;
      return prev.map(h => {
        if (h.id !== id) return h;
        const wasDone = h.days.find(d => d.date === date)?.done ?? false;
        willBeDone = !wasDone;
        const days = h.days.map(d =>
          d.date === date ? { ...d, done: willBeDone } : d
        );
        const totalDelta = willBeDone ? 1 : -1;
        return { ...h, days, totalCount: h.totalCount + totalDelta };
      });
    });

    try {
      const updated = willBeDone
        ? await api.markDay(id, date)
        : await api.unmarkDay(id, date);
      setHabits(prev => prev.map(h => h.id === id ? updated : h));
    } catch {
      setHabits(prevSnapshot);
      showError("Не вдалося зберегти зміну");
    } finally {
      inFlight.current.delete(key);
    }
  }, [showError]);

  return { habits, loading, error, createHabit, renameHabit, deleteHabit, toggleDay };
}
