import { useState } from "react";

type Props = { onAdd: (name: string) => void };

export default function AddHabitInput({ onAdd }: Props) {
  const [name, setName] = useState("");

  const submit = () => {
    const trimmed = name.trim();
    if (!trimmed) return;
    onAdd(trimmed);
    setName("");
  };

  return (
    <div className="add-habit">
      <input
        placeholder="+ нова звичка"
        value={name}
        onChange={e => setName(e.target.value)}
        onKeyDown={e => { if (e.key === "Enter") submit(); }}
      />
      <button type="button" onClick={submit} aria-label="додати">+</button>
    </div>
  );
}
