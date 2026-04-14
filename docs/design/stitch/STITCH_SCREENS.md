# Stitch design handoff — FocusFlow (Remix)

**Project:** `projects/5138740844196226618` — **Remix of Onboarding - Live Study**  
**Previous reference project:** `projects/13822193857986936431` (superseded for UI source; same `screenId`s).

## Design tokens (`get_project` Remix, 2026-04-15)

| Token | Value |
|-------|-------|
| Primary seed | `#2060df` |
| Color mode | `LIGHT` |
| Body / UI font (Stitch) | `INTER` |
| Roundness | `ROUND_EIGHT` → **8.dp** default corners |
| Saturation | `3` (Stitch metadata) |

Optional neutrals and spacing: mine `screen.html` per screen under each `screenId/` folder (URLs expire; see download log).

## Home dashboard

**Baseline:** **Home Dashboard** screen **`b145a281b657482abb7f772746bfee81`** (Stitch “Home Dashboard” / former variant B). Home A (`5dafe768…`) is **not** in the Remix 11-screen set; add later only if re-imported.

## Screen → module map

| screenId | Stitch title | Module |
|----------|--------------|--------|
| 7c6bba8eea1541f895ce3f88ed763756 | Onboarding - Find Partner | feature:onboarding |
| a96335ce95244bf081a369a87af56c6a | Onboarding - Accountability | feature:onboarding |
| e7ab2ba952524d97ac1cf1cbeb20f1e2 | Onboarding - Get Started | feature:onboarding |
| 1311e24fc58848f8b7bcc463cd3bff94 | Authentication | feature:auth |
| ab8e8e4a96534d1baaaeffa7256a7b55 | Profile Setup - Basic Info | feature:profile |
| 46156ee9148a4725a7d54ca60f566c74 | Profile Setup - Goals | feature:profile |
| b145a281b657482abb7f772746bfee81 | Home Dashboard | feature:home |
| ae397c70928b475eb02bdafd1646ae3d | Find Partner | feature:partner |
| 16a2eaf873dd4ff3a2bbb5f9d0565f51 | Chat (Minimal) | feature:chat |
| 8f4282bcb4a348649f00863275101241 | Profile Screen | feature:profile |
| 7a4e0328c60b41009236f3b7cbf9d8e9 | Settings | feature:profile |

## Assets & download log

Each folder `docs/design/stitch/<screenId>/` should contain:

- `screenshot.png` — Stitch PNG export  
- `screen.html` — Stitch HTML export  

**Last bulk download:** 2026-04-15 via Cursor MCP `get_project` / `get_screen` (Remix `5138740844196226618`) + `curl.exe -L` (see [remix-download-urls.json](remix-download-urls.json) for URLs used).

If downloads fail (403 / expired Googleusercontent or contribution URLs), re-run MCP `get_screen` for that `screenId`, update `remix-download-urls.json`, and re-run curl.

## PRD vs Stitch reconciliation

- **Copy / labels:** Prefer [PRD.txt](../../PRD.txt) (e.g. **Create Study Session**, **Join Study Room**) when Stitch wording differs; keep Stitch **layout** (search, chips, card stacks, spacing).
- **Matching:** PRD requires **both parties** before **Active** chat. UI must not imply “instant chat” after tap **Start Session**; host **Accept / Decline** is required (see app Home pending-join banner).
- **Partners (`ae397c70928b475eb02bdafd1646ae3d`):** Compose matches **Remix HTML** — sticky header (**Focus Flow**, back, search), horizontal pills (**All Partners**, Time stub, Subject/Level with expand + optional chip rows), **Recommended Partners** + dynamic **Online Now** count, bordered **12dp** cards (56dp avatar + online dot, **more_horiz**, divider, **Start Session** with play icon, primary `#2060df`). **Deviations:** list rows are **session requests** (PRD marketplace), not Stitch’s static “people” cards (Pro badge, stacked avatars, offline **Notify Me**). App **bottom nav** stays PRD three tabs (Home / Partner / Profile); Stitch’s four-tab bar (Focus / Partners / Stats / Profile) is not duplicated.

## Build / sign-off (automated)

Run from repo root: `./gradlew :app:assembleDebug`, `:core:domain:test`, `:app:testDebugUnitTest`, `lint`.  
Manual: side-by-side PNG vs emulator per screen above; PRD §5.1–5.9 checklist.
