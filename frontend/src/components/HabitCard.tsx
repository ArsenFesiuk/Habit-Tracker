import { useState } from "react";
import type { Habit } from "../types/Habit";
import HabitDots from "./HabitDots";
import HabitNameEditor from "./HabitNameEditor";

type Props = {
  habit: Habit;
  onToggleDay: (id: number, date: string) => void;
  onRename: (id: number, name: string) => void;
  onDelete: (id: number) => void;
  onOpen: (id: number) => void;
};

export default function HabitCard({ habit, onToggleDay, onRename, onDelete, onOpen }: Props) {
  const [editing, setEditing] = useState(false);

  const handleDelete = () => {
    if (window.confirm(`Видалити «${habit.name}»?`)) onDelete(habit.id);
  };

  return (
    <div className="habit-card">
      <div className="habit-card-top">
        {editing ? (
          <HabitNameEditor
            initialName={habit.name}
            onSave={name => { onRename(habit.id, name); setEditing(false); }}
            onCancel={() => setEditing(false)}
          />
        ) : (
          <button className="habit-name" onClick={() => onOpen(habit.id)} type="button">
            {habit.name}
          </button>
        )}
        <div className="habit-card-meta">
          <span className="habit-total">{habit.totalCount}x</span>
          <button
            className="icon-btn"
            onClick={() => setEditing(true)}
            aria-label="перейменувати"
            type="button"
          >✏</button>
          <button
            className="icon-btn"
            onClick={handleDelete}
            aria-label="видалити"
            type="button"
          >🗑</button>
        </div>
      </div>
      <HabitDots days={habit.days} onToggle={date => onToggleDay(habit.id, date)} />
    </div>
  );
}
