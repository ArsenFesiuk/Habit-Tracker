import type { HabitDay } from "../types/HabitDay";

type Props = {
  days: HabitDay[];
  onToggle: (date: string) => void;
};

export default function HabitDots({ days, onToggle }: Props) {
  return (
    <div className="habit-dots">
      {days.map((d, idx) => {
        const isToday = idx === days.length - 1;
        return (
          <button
            key={d.date}
            className={`habit-dot${d.done ? " done" : ""}${isToday ? " today" : ""}`}
            onClick={() => onToggle(d.date)}
            aria-label={`${d.date} ${d.done ? "виконано" : "не виконано"}`}
            type="button"
          />
        );
      })}
    </div>
  );
}
