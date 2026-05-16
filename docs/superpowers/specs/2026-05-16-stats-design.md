# Stats Page — Design Spec

**Date:** 2026-05-16  
**Status:** Approved

## Goal

Add a `/stats` page that gives the user a visual overview of their habit performance over the last 30 days. Target: portfolio showcase — visually impressive, technically clean.

## Architecture

- One new backend endpoint: `GET /api/stats?from=YYYY-MM-DD&to=YYYY-MM-DD`
- Frontend makes a single request on page load and renders all blocks from one response
- Charts library: **Recharts** (React-native, composable, supports custom colors)

## Backend

### New endpoint

`GET /api/stats?from=YYYY-MM-DD&to=YYYY-MM-DD`  
Auth: JWT (same as all other `/api/**` routes)  
Scope: current user's habits only

### Response shape

```json
{
  "completionRate": 72,
  "totalCompletions": 47,
  "activeHabits": 5,
  "bestCurrentStreak": 14,
  "weeklyTrend": [
    { "weekStart": "2026-04-21", "rate": 65 },
    { "weekStart": "2026-04-28", "rate": 80 },
    { "weekStart": "2026-05-05", "rate": 70 },
    { "weekStart": "2026-05-12", "rate": 72 }
  ],
  "habitBreakdown": [
    { "name": "Reading",  "color": "#FF6B6B", "rate": 90 },
    { "name": "Exercise", "color": "#4ECDC4", "rate": 60 }
  ]
}
```

### Field definitions

| Field | Description |
|---|---|
| `completionRate` | % of (habit × day) slots marked done across the whole period |
| `totalCompletions` | absolute count of done entries in the period |
| `activeHabits` | number of habits the user has |
| `bestCurrentStreak` | highest `streak` value among all habits right now |
| `weeklyTrend` | 4 data points; each covers a Mon–Sun week that overlaps the 30-day window; partial weeks (first/last) use only the days within the window for both numerator and denominator |
| `habitBreakdown` | per-habit completion rate for the period; color is the habit's stored color hex value |
| `bestCurrentStreak` | max of `habit.streak` across all user habits (streak is already maintained by the existing mark/unmark logic — no recalculation needed) |

### New Java classes

- `StatsDTO` — record with the fields above
- `WeeklyTrendPointDTO` — `{ weekStart: LocalDate, rate: int }`
- `HabitBreakdownDTO` — `{ name: String, color: String, rate: int }`
- `StatsController` — `@GetMapping("/api/stats")`
- `StatsService` / `StatsServiceJPA` — queries `HabitEntryRepository`, groups by week

## Frontend

### New files

| File | Purpose |
|---|---|
| `src/pages/StatsPage.tsx` | Page component |
| `src/api/stats.ts` | `fetchStats(from, to): Promise<StatsDTO>` |
| `src/styles/statsPage.css` | Page-specific styles |

### UI layout

```
┌─────────────────────────────────────────┐
│  ← Back          My Stats         ⋯    │
├─────────────────────────────────────────┤
│  ┌──────────┐ ┌──────────┐ ┌─────────┐ │
│  │   72%    │ │    47    │ │   14🔥  │ │
│  │Completion│ │Completions│ │Best now │ │
│  └──────────┘ └──────────┘ └─────────┘ │
├─────────────────────────────────────────┤
│  Weekly trend (last 4 weeks)            │
│       AreaChart (line + fill)           │
├─────────────────────────────────────────┤
│  Habits comparison                      │
│       Horizontal BarChart               │
│       each bar colored by habit color   │
└─────────────────────────────────────────┘
```

### Chart details

- **AreaChart** (`recharts`): X axis = week label (e.g. "Apr 21"), Y axis = 0–100%, single area with accent color + 20% opacity fill
- **BarChart** (`recharts`): horizontal layout, each bar uses the habit's own color, label shows `rate%` at end of bar

### Navigation

- `HeaderMenu` gets a new "Stats" `<Link>` above "Log out"
- `StatsPage` has a `← Back` button that navigates to `/home`
- Route added in `App.tsx`: `/stats`

### Default period

- `from` = today − 29 days, `to` = today (30-day window, computed in `StatsPage` on mount)

## Out of scope

- Period selector (7 / 30 / 90) — can be added later
- Per-habit stats tab on the habit detail page
- Export / share
