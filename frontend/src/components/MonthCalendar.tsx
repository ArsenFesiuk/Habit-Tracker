type Props = {
  year: number;
  month: number; // 0-indexed (Jan = 0)
  doneDates: Set<string>; // ISO strings YYYY-MM-DD
  today: Date;
  onPrev: () => void;
  onNext: () => void;
  canGoNext: boolean;
};

const WEEKDAYS = ["Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Нд"];
const MONTH_NAMES = [
  "Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень",
  "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень",
];

function pad2(n: number): string {
  return n < 10 ? `0${n}` : `${n}`;
}

function iso(year: number, month: number, day: number): string {
  return `${year}-${pad2(month + 1)}-${pad2(day)}`;
}

export default function MonthCalendar({
  year, month, doneDates, today, onPrev, onNext, canGoNext,
}: Props) {
  const firstOfMonth = new Date(year, month, 1);
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  const firstWeekday = (firstOfMonth.getDay() + 6) % 7;

  const cells: (number | null)[] = [];
  for (let i = 0; i < firstWeekday; i++) cells.push(null);
  for (let d = 1; d <= daysInMonth; d++) cells.push(d);
  while (cells.length % 7 !== 0) cells.push(null);

  const todayISO = iso(today.getFullYear(), today.getMonth(), today.getDate());

  return (
    <div className="month-calendar">
      <div className="month-header">
        <button className="icon-btn" onClick={onPrev} aria-label="попередній місяць" type="button">‹</button>
        <span className="month-title">{MONTH_NAMES[month]} {year}</span>
        <button
          className="icon-btn"
          onClick={onNext}
          aria-label="наступний місяць"
          disabled={!canGoNext}
          type="button"
        >›</button>
      </div>

      <div className="weekday-row">
        {WEEKDAYS.map(w => <div key={w} className="weekday">{w}</div>)}
      </div>

      <div className="day-grid">
        {cells.map((d, i) => {
          if (d === null) return <div key={i} className="day empty" />;
          const dateStr = iso(year, month, d);
          const done = doneDates.has(dateStr);
          const isToday = dateStr === todayISO;
          const isFuture = dateStr > todayISO;
          const cls = [
            "day",
            done ? "done" : "",
            isToday ? "today" : "",
            isFuture ? "future" : "",
          ].filter(Boolean).join(" ");
          return <div key={i} className={cls}>{d}</div>;
        })}
      </div>
    </div>
  );
}
