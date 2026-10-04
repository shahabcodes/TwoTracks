# Two Tracks — couple's migration progress tracker (Android)

Plan v3 · 4 Oct 2026 · derived from `DOC-20260928-WA0043.pdf` ("Moving Abroad as a Couple")

Change from v2: the app is now schedule-aware. Each person's real weekly availability drives a
session planner, capacity-vs-required checks, and reminders at the exact study slot.

## 1. Your real schedules (seeded, editable)

| | You (Qatar, UTC+3) | Her (Pakistan, UTC+5) |
|---|---|---|
| Work / school | Sun–Thu 07:00–15:30, home 16:15 | Mon–Fri 08:00–14:00 (assumed Mon–Fri; editable) |
| Fixed study | Sun–Thu 16:15–17:30 self-study slot (75 min) | Mon–Fri 16:00–18:00 IELTS class (120 min) |
| Weekend | Fri, Sat | Sat, Sun |
| Weekly fixed capacity | ~6 h (5 × 75 min) | ~10 h class + self-study you choose |

Timezones are stored per user. Partner view shows times in the viewer's zone ("her class ends at 16:00 your time").

## 2. What the schedule means for the guide's targets (the app must show this)

Hours needed Oct 2026 → Sep 2027 checkpoint (~48 weeks), from the guide and v2 defaults:

| Goal | Hours | Weekly need over 48 wks |
|---|---|---|
| German A1 → B1 (you) | 350–500 | 7.3–10.4 h |
| AZ-204 (you) | 60 | 1.3 h |
| IELTS General (you) | 150 | 3.1 h |
| Job applications, 10–15/wk from Jan 2027 at ~20 min each (you) | ~120 | 3.3–5 h (Jan–Sep) |
| **Your total** | **680–830** | **≈ 14–17 h/week** |
| Your fixed capacity | ~290 (6 h × 48) | **6 h/week** |

**Gap: roughly 2.5× over capacity on weekdays alone.** The planner has to make this visible and offer the levers:

1. Weekend blocks: 3 h Fri + 3 h Sat adds 6 h → 12 h/week, which makes German B1 + AZ-204 feasible if IELTS General moves to the lower end (it only matters for Canada/Australia, both backup routes for you).
2. Reprioritise: the guide already orders it Oct–Dec German A1 + AZ-204, Jan–Mar German A2 + AZ-204 exam + applications, Apr–Jun IELTS General. The app enforces this order by default.
3. Accept B1 by Sep 2027 may slip to A2 + "B1 in progress"; the guide says B1 only speeds PR (21 vs 27 months) and is not needed for the visa.

Her side fits: IELTS Academic ≈ 160 h at 10 h/week of class plus ~30 min/day review finishes in ~12 weeks, i.e. by early Jan 2027, in time for the Jan–Mar 2027 test window. After that her class slot frees up for French (if Canada) or German A1.

## 3. Study tracking model (unchanged from v2, plus schedule fields)

```
Availability          per user: weekly template of blocks
  Block               dayOfWeek, start, end, kind (work | class | study | free), label, timezone

StudyGoal             "IELTS Academic", "German B1", "AZ-204" …
  owner, kind, targetDate, priority (ordering for the planner)
  components[]        Reading / Writing / Listening / Speaking …, each with hoursRequired, targetScore

PlannedSession        generated from study blocks + goal priority
  owner, date, start, end, goalId, componentId, status (planned | done | skipped | moved)

StudyLog              actual minutes per component per day (multiple per day allowed)
MockScore             practice-test results per component
```

Derived, computed client-side:
- Hours logged vs required per component and per goal.
- Weekly capacity (sum of study blocks) vs weekly need (remaining hours ÷ weeks to target date).
- Pace needed per session and forecast finish date at the 14-day average.
- Feasibility flag per goal: fits / tight / does not fit, with suggested lever.

## 4. Session planner

- You set a **weekly template**: which goal and component goes into each study block. Seeded default for you,
  Oct–Dec 2026: Sun German, Mon AZ-204, Tue German, Wed AZ-204, Thu German (A1: vocabulary/grammar/listening
  rotating; speaking on weekends if you add a block).
- The app **auto-advances the template by roadmap quarter** (Jan 2027: add applications, Apr 2027: swap AZ-204
  for IELTS General), and lets you override any week.
- **Her class is a recurring planned session.** At 18:00 she gets a prompt: "Today's class covered…" with the four
  skills as toggles and minutes split, so class time is logged per component without manual timing.
- **Reminder at slot start**: your notification fires at 16:15 Sun–Thu with "Start German: Grammar (60 min)";
  one tap starts the timer. Hers fires at 15:45 for class and at an evening time she picks for self-study.
- **Missed-session handling**: a skipped slot shows as a deficit on the goal; the app suggests the next free block
  (e.g. Friday morning) to make it up rather than silently resetting.

## 5. Seeded goals and hour budgets

Unchanged from v2. German and French hours from the guide; others are editable defaults.

| Goal | Owner | Components | Hours | Target (p.12) |
|---|---|---|---|---|
| German A1 / A2 / B1 | You | Vocabulary, Grammar, Listening, Speaking (+ Exam practice at B1) | 100 / 120 / 200 | Dec 2026 / Mar 2027 / Sep 2027 |
| AZ-204 | You | Compute, Storage, Security, Monitoring, Connect & consume, Practice exams | 60 | Mar 2027 |
| IELTS General | You | L / R / W / S | 30 / 40 / 50 / 30 | Jun 2027 |
| Extra certs | You | see §5a — seeded as inactive goals you switch on one at a time | per cert | — |
| IELTS Academic | Her | L / R / W / S | 30 / 40 / 50 / 40 | Mar 2027 (likely done Jan) |
| IELTS General (if Canada) | Her | L / R / W / S | 100 | 2027 H2 |
| French NCLC 7 (if Canada) | Her | L / R / W / S | 575 | 12–18 months from start |
| German A1 (optional) | Her | 4 components | 100 | before move |
| NAATI CCL | Both | Dialogue practice | 30 | 2027 |

Work habits (counts): applications/week with company + country + status; GitHub sessions; her teaching days
toward 12 months.

## 5a. Certification track for you (Germany / Ireland / Netherlands)

Chosen for your profile (.NET, Angular, T-SQL, Java/Kotlin, Android, REST/SOAP, data migration, SSRS) and for
what German, Irish and Dutch job ads actually ask for. Seeded as **inactive** goals; the planner only schedules
a cert once you activate it, and it warns if activating one pushes German below its weekly need.
Exam fees are approximate 2026 prices and feed the Budget screen. Verify names and fees on the vendor site
before paying; Microsoft in particular retires and renames exams.

### Tier 1 — do before the Sep 2027 checkpoint (small, high signal)

| Cert | Why it helps | Components (exam domains) | Hours | Fee (approx) | When |
|---|---|---|---|---|---|
| **AZ-204 Azure Developer Associate** (already seeded) | The guide's pick; Azure dominates .NET shops in DE/IE/NL | Compute · Storage · Security · Monitoring & troubleshooting · Connect & consume services · Practice exams | 60 | USD 165 | Oct 2026–Mar 2027 |
| **PSM I — Professional Scrum Master** (Scrum.org) | "Scrum/agile" appears in most German and Irish dev ads; cheap, no renewal, 1–2 weeks of evenings | Scrum Guide · Roles & events · Open assessments | 15 | USD 200 | any 2-week gap, e.g. Dec 2026 |

### Tier 2 — next after German A2 and AZ-204 are done, or during the job-offer wait

| Cert | Why it helps | Components | Hours | Fee (approx) | Notes |
|---|---|---|---|---|---|
| **CKAD — Certified Kubernetes Application Developer** | German ads for .NET/Java roles routinely list Docker + Kubernetes; the guide's own GitHub project uses Docker | Application design & build · Deployment · Observability · Environment, config & security · Services & networking · kubectl practice | 50 | USD 445 | Hands-on exam; pairs with the GitHub project |
| **AZ-400 DevOps Engineer Expert** | CI/CD + IaC is the most common "nice to have" after Azure itself; AZ-204 is a prerequisite | Process & communication · Source control · Pipelines · Security & compliance · Instrumentation | 80 | USD 165 | Only after AZ-204 passes |
| **Oracle Java SE 21 Developer (1Z0-830)** | Java is the single largest backend language in Germany; proves your Java/Kotlin claim | Language basics · OOP · Collections & generics · Streams & lambdas · Concurrency · Modules & I/O · Practice exams | 80 | USD 245 | Pick this over AZ-400 if targeting Java-heavy employers (SAP ecosystem, automotive, banks) |

### Tier 3 — country or niche specific, pick at most one

| Cert | Why it helps | Components | Hours | Fee (approx) | Notes |
|---|---|---|---|---|---|
| **AWS Certified Developer – Associate (DVA-C02)** | Ireland: AWS Dublin and many fintechs are AWS-first; Netherlands also mixed cloud | Development with AWS services · Security · Deployment · Troubleshooting & optimisation | 70 | USD 150 | Choose if Ireland becomes the lead market |
| **DP-300 Azure Database Administrator** | Direct fit for your T-SQL + data-migration experience; banks/insurers in DE/IE value it | Plan & implement · Monitor & optimise · Automate · HA/DR · T-SQL admin | 50 | USD 165 | Alternative: **PL-300 Power BI Data Analyst** (40 h) if you want to convert SSRS experience to Power BI |
| **AI-102 Azure AI Engineer Associate** | 2026 ads increasingly ask for "LLM / AI integration" from .NET developers | Plan & manage · Generative AI (Azure OpenAI) · Computer vision · NLP · Knowledge mining | 60 | USD 165 | Only if you also add an AI feature to the GitHub project |
| **HashiCorp Terraform Associate (003)** | Small, common in Dutch and German platform-team ads | IaC concepts · Terraform basics · CLI & workflow · State · Modules · HCP | 30 | USD 70 | Cheap add-on after AZ-400 or CKAD |

### Not recommended as goals

- **AZ-900 / DP-900 fundamentals** — too junior for a 6-year developer; employers ignore them.
- **Angular / .NET vendor certs** — none carry weight; the guide's GitHub project (.NET API + Angular + Docker + Azure) is the proof instead, tracked as a work habit.
- **ISTQB, ITIL, PMP** — not relevant to developer roles.

### Fit with your capacity

Tier 1 adds 15 h on top of AZ-204 (already budgeted). Tier 2 adds 50–80 h each: at your 6 h/week weekday
capacity that is 8–13 weeks with **no German study**, so the planner only allows one Tier 2 cert active at a time
and shows the German deficit it causes. Realistic sequence given the Sep 2027 checkpoint:

| Quarter | Certs active | Alongside |
|---|---|---|
| Oct–Dec 2026 | AZ-204 (2 slots/wk), PSM I (2 weeks in Dec) | German A1 (3 slots/wk) |
| Jan–Mar 2027 | AZ-204 exam | German A2, applications start |
| Apr–Jun 2027 | one of CKAD / AZ-400 / Java SE 21 only if weekend blocks exist | IELTS General, German A2→B1 |
| Jul–Sep 2027 | none new; interviews | German B1 |
| After an offer / during visa wait | Tier 2 remainder, Tier 3 pick | — |

## 6. Screens

1. **Today** — today's planned sessions for you (and partner's, read-only), one-tap start/log, work-habit
   counters, streak, next deadline.
2. **Week** — calendar strip of blocks (work / class / study / free) with planned sessions; drag to move a
   session to a free block; capacity vs need bar for the week.
3. **Study** — goals with %, pace status and feasibility flag; goal detail with per-component bars, 7/30-day
   history, forecast date, target vs latest mock score.
4. **Progress** — weekly hours across goals, applications vs target, teaching months, countdowns.
5. **Roadmap** — p.12 quarters, You / Her, status; Sep 2027 checkpoint.
6. **Countries** — six cards with checkable what-to-do and risks.
7. **Tests & scores** — official results vs targets, expiry reminders.
8. **Documents** — document × person × status grid.
9. **Budget** — planned vs spent.
10. **Settings** — profile, timezone, availability template, pair partner, reminder times, theme, reset seed.

## 7. Tech stack (reuse FMS)

Kotlin 2.1, Compose, Material3, Hilt, Flow, Navigation Compose. Firebase Auth + Firestore (offline).
WorkManager + AlarmManager (exact alarm permission) for slot-start reminders. java.time with per-user ZoneId.
Convention plugins, theme and CI copied from `D:\FMS`. Repo `D:\PA`, package `com.shahaabapps.twotracks`.
Modules: `app`, `core:model`, `core:ui`, `core:data`, `core:seed`, `core:planner` (pure Kotlin, unit-tested).

## 8. Firestore layout

```
couples/{coupleId}
  members[], inviteCode
  profiles/{uid}: name, role, dob, nationality, timezone, photoUrl
  availability/{uid}: blocks[]
  studyGoals/{goalId}: owner, kind, title, priority, targetDate, components[]
  plannedSessions/{yyyy-MM-dd_uid_slot}: owner, goalId, componentId, start, end, status
  studyLogs/{yyyy-MM-dd_goalId_componentId_n}: owner, minutes, note
  mockScores/{id}, habits/{id}, habitLogs/{id}, milestones/{id}, countries/{code},
  tests/{id}, documents/{id}, budget/{id}
```

Rules: access only if `request.auth.uid in members`.

## 9. Build order

1. **Scaffold** — project, theme, nav, CI, Firebase.
2. **Auth + pairing + profile** — incl. timezone and availability template editor (seeded with the schedules above).
3. **Seed content** — goals, components, hour budgets, weekly templates per quarter, roadmap, countries, tests, documents, budget.
4. **Planner + logging (MVP)** — `core:planner` generates sessions from blocks + priorities; Today and Week screens; timer; per-component logging; her post-class split prompt; capacity-vs-need and feasibility flags.
5. **Reminders** — slot-start alarms, moved up because the 16:15 trigger is central to the habit.
6. **Mock scores + Progress** — trends, countdowns, streaks.
7. **Roadmap, Countries, Tests, Documents, Budget**.
8. **Polish + release** — dark mode, tests for planner/pace/feasibility, signed APK.

## 10. Decisions taken (change any)

- Her school days assumed Mon–Fri with Sat–Sun weekend; your weekend Fri–Sat. Both editable.
- Your slot is modelled as 75 min with 60 min of focused study by default.
- Planner follows the guide's quarter ordering by default (German + AZ-204 first, IELTS General from Apr 2027).
- The app never hides an infeasible target; it shows the gap and the levers instead.
- Shared Firebase backend, email/password auth, English UI, Android only.
