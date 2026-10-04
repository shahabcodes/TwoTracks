# Changelog

All notable changes to Two Tracks. Versions follow `version.properties`; the
build number is appended by CI (for example `0.1.0 (build 12)`).

## [Unreleased]

## [0.2.0] - 2026-10-04

### Changed
- UI restyled to match the Browse Safe browser app's iOS design language: iOS blue accent with Pink and Meadow options, grouped gray backdrop (#F2F2F7 light, #121316 dark), flat white inset cards with hairline dividers, coloured icon tiles on settings rows, translucent gray fills for text fields, pills and segmented controls, 31sp large titles, centred-title nav bars with "Back", a flat tab bar with a hairline, 14dp dialogs and 16dp sheets with an iOS grabber, and the blue-violet edge-to-edge hero.
- Default accent is now Blue.

## [0.1.0] - 2026-10-04

### Added
- Email sign-in, shared couple space with 6-letter invite code, Tech / Teacher roles and per-person timezone.
- Seeded content from the "Moving Abroad as a Couple" guide: study goals with per-skill hour budgets (IELTS L/R/W/S, German A1 to B1, AZ-204, PSM I, tiered extra certifications, French, NAATI), weekly schedules, roadmap, six country tracks, tests, document checklist, budget, deadlines.
- Today: planned sessions with timer or minute logging, class-time split across skills, count habits, partner's day in their timezone, next deadline.
- Week: template of work / class / study blocks filled by the planner, capacity needed versus available, re-plan, pin a slot to a goal, add weekend blocks.
- Study: goals with logged versus required hours, weekly need, recent pace, forecast date, on-track / tight / behind flag; goal detail with per-skill bars, 14-day history, mock scores; goal editor.
- Progress: hours per week for eight weeks, streaks, counts, countdowns.
- Roadmap, Countries, Tests and scores, Documents, Budget, Settings (profile, weekly template editor, theme, reset guide content).
- Reminders at the start of each study slot, before class, and after class.
- Firebase emulator support for local testing (`-PfirebaseEmulator=true`).
