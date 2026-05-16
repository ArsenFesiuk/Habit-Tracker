import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer,
  BarChart, Bar, Cell, LabelList,
} from "recharts";
import * as statsApi from "../api/stats";
import type { StatsDTO } from "../types/Stats";
import HeaderMenu from "../components/HeaderMenu";
import "../styles/statsPage.css";

function pad2(n: number) { return n < 10 ? `0${n}` : `${n}`; }
function isoDate(d: Date) {
  return `${d.getFullYear()}-${pad2(d.getMonth() + 1)}-${pad2(d.getDate())}`;
}

export default function StatsPage() {
  const navigate = useNavigate();
  const [stats, setStats] = useState<StatsDTO | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const to = new Date();
    const from = new Date(to);
    from.setDate(from.getDate() - 29);
    statsApi.fetchStats(isoDate(from), isoDate(to))
      .then(setStats)
      .catch(() => setError("Failed to load stats"));
  }, []);

  return (
    <div className="stats-page">
      <div className="container">
        <div className="page-header">
          <button className="back-btn" onClick={() => navigate("/home")} type="button">
            ← Back
          </button>
          <h1 className="stats-title">My Stats</h1>
          <HeaderMenu />
        </div>

        {error && <div className="banner-error">{error}</div>}

        {!stats && !error && <p className="muted">Loading…</p>}

        {stats && (
          <>
            <div className="stats-cards">
              <div className="stats-card">
                <span className="stats-card-value">{stats.completionRate}%</span>
                <span className="stats-card-label">Completion</span>
              </div>
              <div className="stats-card">
                <span className="stats-card-value">{stats.totalCompletions}</span>
                <span className="stats-card-label">Completions</span>
              </div>
              <div className="stats-card">
                <span className="stats-card-value">{stats.bestCurrentStreak}🔥</span>
                <span className="stats-card-label">Best now</span>
              </div>
            </div>

            <section className="stats-section">
              <h2 className="stats-section-title">Weekly trend</h2>
              <ResponsiveContainer width="100%" height={160}>
                <AreaChart
                  data={stats.weeklyTrend}
                  margin={{ top: 8, right: 8, left: -28, bottom: 0 }}
                >
                  <defs>
                    <linearGradient id="trendFill" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#fff" stopOpacity={0.2} />
                      <stop offset="95%" stopColor="#fff" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <XAxis
                    dataKey="weekStart"
                    tick={{ fontSize: 11, fill: "#888" }}
                    tickFormatter={v => (v as string).slice(5)}
                  />
                  <YAxis domain={[0, 100]} tick={{ fontSize: 11, fill: "#888" }} />
                  <Tooltip
                    formatter={(v: number) => [`${v}%`, "Completion"]}
                    contentStyle={{ background: "#141414", border: "1px solid #222", borderRadius: 8 }}
                    labelStyle={{ color: "#888" }}
                    itemStyle={{ color: "#fff" }}
                  />
                  <Area
                    type="monotone"
                    dataKey="rate"
                    stroke="#fff"
                    strokeWidth={2}
                    fill="url(#trendFill)"
                    dot={false}
                  />
                </AreaChart>
              </ResponsiveContainer>
            </section>

            {stats.habitBreakdown.length > 0 && (
              <section className="stats-section">
                <h2 className="stats-section-title">Habits comparison</h2>
                <ResponsiveContainer
                  width="100%"
                  height={Math.max(80, stats.habitBreakdown.length * 44)}
                >
                  <BarChart
                    layout="vertical"
                    data={stats.habitBreakdown}
                    margin={{ top: 4, right: 48, left: 8, bottom: 4 }}
                  >
                    <XAxis type="number" domain={[0, 100]} hide />
                    <YAxis
                      type="category"
                      dataKey="name"
                      tick={{ fontSize: 13, fill: "#ccc" }}
                      width={90}
                    />
                    <Tooltip
                      formatter={(v: number) => [`${v}%`, "Completion"]}
                      contentStyle={{ background: "#141414", border: "1px solid #222", borderRadius: 8 }}
                      itemStyle={{ color: "#fff" }}
                    />
                    <Bar dataKey="rate" radius={[0, 4, 4, 0]}>
                      {stats.habitBreakdown.map((entry, i) => (
                        <Cell key={i} fill={entry.color ?? "#fff"} />
                      ))}
                      <LabelList
                        dataKey="rate"
                        position="right"
                        formatter={(v: number) => `${v}%`}
                        style={{ fontSize: 12, fill: "#888" }}
                      />
                    </Bar>
                  </BarChart>
                </ResponsiveContainer>
              </section>
            )}
          </>
        )}
      </div>
    </div>
  );
}
