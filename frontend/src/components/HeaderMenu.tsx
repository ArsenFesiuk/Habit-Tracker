import { useEffect, useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function HeaderMenu() {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!open) return;
    const onDocClick = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    document.addEventListener("mousedown", onDocClick);
    return () => document.removeEventListener("mousedown", onDocClick);
  }, [open]);

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/login", { replace: true });
  };

  return (
    <div className="header-menu" ref={ref}>
      <button
        type="button"
        className="header-menu-trigger"
        onClick={() => setOpen(o => !o)}
        aria-label="menu"
        aria-expanded={open}
      >
        ⋯
      </button>
      {open && (
        <ul className="header-menu-list">
          <li>
            <Link to="/stats" onClick={() => setOpen(false)}>Stats</Link>
          </li>
          <li>
            <button type="button" onClick={logout}>Log out</button>
          </li>
        </ul>
      )}
    </div>
  );
}
