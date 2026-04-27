import { useEffect, useRef, useState } from "react";

type Props = {
  initialName: string;
  onSave: (name: string) => void;
  onCancel: () => void;
};

export default function HabitNameEditor({ initialName, onSave, onCancel }: Props) {
  const [value, setValue] = useState(initialName);
  const ref = useRef<HTMLInputElement>(null);

  useEffect(() => {
    ref.current?.focus();
    ref.current?.select();
  }, []);

  const commit = () => {
    const trimmed = value.trim();
    if (!trimmed || trimmed === initialName) onCancel();
    else onSave(trimmed);
  };

  return (
    <input
      ref={ref}
      className="habit-name-editor"
      value={value}
      onChange={e => setValue(e.target.value)}
      onKeyDown={e => {
        if (e.key === "Enter") commit();
        if (e.key === "Escape") onCancel();
      }}
      onBlur={commit}
    />
  );
}
