import { useNavigate } from "react-router-dom";
import { useHabits } from "../hooks/useHabits";
import HabitCard from "../components/HabitCard";
import AddHabitInput from "../components/AddHabitInput";
import "../styles/homePage.css";

export default function HomePage() {
  const navigate = useNavigate();
  const { habits, loading, error, createHabit, renameHabit, deleteHabit, toggleDay } = useHabits();

  return (
    <div className="home-page">
      <div className="container">
        <h1>Мої звички</h1>

        {error && <div className="banner-error">{error}</div>}

        {loading ? (
          <p className="muted">Завантаження…</p>
        ) : habits.length === 0 ? (
          <p className="muted">Поки що звичок немає</p>
        ) : (
          <div className="habits-list">
            {habits.map(h => (
              <HabitCard
                key={h.id}
                habit={h}
                onToggleDay={toggleDay}
                onRename={renameHabit}
                onDelete={deleteHabit}
                onOpen={id => navigate(`/habits/${id}`)}
              />
            ))}
          </div>
        )}

        <AddHabitInput onAdd={createHabit} />
      </div>
    </div>
  );
}
