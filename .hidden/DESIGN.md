---
name: Botanical Logistics
colors:
  surface: '#f9f9ff'
  surface-dim: '#cfdaf2'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f0f3ff'
  surface-container: '#e7eeff'
  surface-container-high: '#dee8ff'
  surface-container-highest: '#d8e3fb'
  on-surface: '#111c2d'
  on-surface-variant: '#414846'
  inverse-surface: '#263143'
  inverse-on-surface: '#ecf1ff'
  outline: '#717976'
  outline-variant: '#c1c8c4'
  surface-tint: '#43655c'
  primary: '#01261f'
  on-primary: '#ffffff'
  primary-container: '#1a3c34'
  on-primary-container: '#83a69c'
  inverse-primary: '#aacec3'
  secondary: '#5e5e5c'
  on-secondary: '#ffffff'
  secondary-container: '#e1dfdc'
  on-secondary-container: '#636360'
  tertiary: '#112235'
  on-tertiary: '#ffffff'
  tertiary-container: '#27374b'
  on-tertiary-container: '#90a0b9'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c5eadf'
  primary-fixed-dim: '#aacec3'
  on-primary-fixed: '#00201a'
  on-primary-fixed-variant: '#2b4d44'
  secondary-fixed: '#e4e2de'
  secondary-fixed-dim: '#c8c6c3'
  on-secondary-fixed: '#1b1c1a'
  on-secondary-fixed-variant: '#474744'
  tertiary-fixed: '#d3e4fe'
  tertiary-fixed-dim: '#b7c8e1'
  on-tertiary-fixed: '#0b1c30'
  on-tertiary-fixed-variant: '#38485d'
  background: '#f9f9ff'
  on-background: '#111c2d'
  surface-variant: '#d8e3fb'
  forest-deep: '#1A3C34'
  forest-leaf: '#2D5A4E'
  cream-base: '#FDFBF7'
  cream-surface: '#F5F2ED'
  slate-text: '#334155'
  slate-muted: '#64748B'
  border-subtle: '#E2E8F0'
typography:
  headline-lg:
    fontFamily: Merriweather
    fontSize: 32px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Merriweather
    fontSize: 24px
    fontWeight: '700'
    lineHeight: '1.3'
  headline-sm:
    fontFamily: Merriweather
    fontSize: 20px
    fontWeight: '700'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.5'
  data-tabular:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '500'
    lineHeight: '1.4'
    letterSpacing: 0.01em
  label-caps:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: '1'
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: '1'
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  unit: 4px
  container-padding: 32px
  gutter: 24px
  element-gap-sm: 8px
  element-gap-md: 16px
  section-gap: 48px
---

## Brand & Style

This design system is built for a professional inventory management environment that values precision, heritage, and organic sophistication. It moves away from the sterile, high-tech aesthetic typical of SaaS products, instead embracing a **Corporate / Modern** style infused with **Minimalist** and **Tactile** sensibilities.

The target audience consists of operations managers and luxury retail stakeholders who require high data density without the cognitive load of "busy" interfaces. The UI evokes a sense of calm authority and reliability through the use of deep, natural tones and high-quality typography. The visual narrative focuses on "The New Office"—a space where digital efficiency meets physical craft.

Key visual principles include:
- **Serene Functionalism:** Using ample white space and a cream-based palette to reduce eye strain during long-form data management.
- **Organic Precision:** Combining sharp data grids with soft-cornered containers and serif headings.
- **Material Contrast:** Utilizing deep forest greens against off-white backgrounds to create a high-contrast, premium information hierarchy.

## Colors

The color palette is anchored by **Forest Deep**, a sophisticated dark green that serves as the primary brand anchor for navigation and primary actions. The background utilizes **Cream Base** instead of pure white to create a softer, more "paper-like" tactile feel, reducing the harshness of a desktop screen.

- **Primary (Forest Deep):** Used for key calls-to-action, active navigation states, and brand-heavy components.
- **Secondary (Cream Surface):** Used for card backgrounds and secondary UI containers to create subtle depth against the Cream Base.
- **Neutral (Slate Text):** A balanced gray-blue scale used for body copy and UI labels to ensure high legibility without the starkness of pure black.
- **Accents:** Muted leaf greens are reserved for success states, while slate grays handle utility icons and disabled states.

## Typography

This system employs a dual-font strategy to balance character with utility. 

**Merriweather** (Serif) is utilized for page titles, section headers, and high-level summaries. It provides a traditional, authoritative voice that suggests quality and heritage.

**Inter** (Sans-Serif) is the workhorse font for the actual inventory data, labels, and inputs. It was selected for its exceptional legibility in data-dense tables. 

**Usage Notes:**
- Use `label-caps` for table headers and small metadata tags.
- Use `data-tabular` for numerical values within inventory lists to ensure vertical alignment.
- All serif headings should favor slightly tighter letter-spacing to maintain a sophisticated "editorial" look.

## Layout & Spacing

The layout follows a **Fixed Grid** philosophy for the main content area (max-width: 1440px) to maintain readable line lengths for data tables. 

- **Structure:** A persistent 280px left sidebar for primary navigation, with a fluid content area that adheres to a 12-column grid.
- **Rhythm:** An 8px base unit is used for most spacing, but 4px increments are allowed for tight UI components like status badges or input icons.
- **Desktop Density:** Given this is a desktop application, margins are generous (`32px`) to provide visual "breathing room," but internal card padding remains efficient (`16px-24px`) to maximize data visibility.

## Elevation & Depth

This design system avoids heavy drop shadows in favor of **Tonal Layers** and **Subtle Outlines**. Depth is communicated through color hierarchy and extremely soft, large-radius ambient shadows.

- **Level 0 (Base):** `Cream-Base` (#FDFBF7).
- **Level 1 (Cards/Surface):** `Cream-Surface` (#F5F2ED) with a 1px border of `Border-Subtle` (#E2E8F0).
- **Level 2 (Overlays/Modals):** Pure white background with a 12% opacity Forest Deep shadow (20px blur, 0px offset).

This approach maintains a "flat-plus" aesthetic that feels modern and organized without the clutter of traditional skeuomorphism.

## Shapes

The shape language is defined as **Soft**. It utilizes small, intentional corner radii to take the edge off the "industrial" feel of an inventory tool while maintaining a professional structure.

- **Small elements (Inputs, Buttons, Badges):** 4px radius.
- **Medium elements (Cards, Modals):** 8px radius.
- **Large elements (Sidebar, Main Containers):** 12px radius.

Interactive elements should never be fully sharp (0px) nor fully round (pill), as the "Soft" setting reinforces the balance between organic and organized.

## Components

### Buttons
- **Primary:** Solid `Forest-Deep` background with White text. Hover state shifts to `Forest-Leaf`.
- **Secondary (Outline):** 1px border of `Forest-Deep`, transparent background, `Forest-Deep` text.
- **Ghost:** No background or border, `Slate-Muted` text, shifting to `Forest-Deep` on hover.

### Input Fields
- **Default State:** `Cream-Base` background with a 1px `Border-Subtle`.
- **Focus State:** 1px border of `Forest-Deep` and a 2px outer glow of `Forest-Leaf` at 10% opacity.
- **Labels:** Use `label-caps` typography positioned above the field.

### Cards
- Used to group inventory categories or item details.
- Background: `Cream-Surface`.
- Border: 1px `Border-Subtle`.
- Padding: 24px for headers, 16px for body content.

### Data Tables
- Header row: `Cream-Surface` background with `label-caps` text.
- Row divider: 1px horizontal line of `Border-Subtle`.
- Hover state: Row background changes to `Cream-Base` to highlight the current selection.

### Status Chips
- Rounded 4px.
- Use low-saturation background tints (e.g., pale sage for "In Stock", pale slate for "Archived").