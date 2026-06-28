# Brand & Design System Implementation

This document describes the design tokens, fonts, colors, and reusable UI components implemented to align the Java Swing interface with the **Botanical Logistics** design system.

---

## 🎨 Palette & Color Tokens

Our colors are anchored by natural, rich tones designed to reduce eye strain and provide a premium "paper-like" tactile feel:

| Token | Hex Code | Usage |
|---|---|---|
| **Primary (Forest Deep)** | `#01261F` | Persistent sidebar, primary call-to-actions, active links. |
| **Hover Primary (Forest Leaf)**| `#2D5A4E` | Button hover state, interactive boundaries. |
| **Level 0 Bg (Cream Base)** | `#FDFBF7` | General application canvas background. |
| **Level 1 Surface (Cream Surface)**| `#F5F2ED` | Card backgrounds, table headers, sidebar canvas. |
| **Neutral Text (Slate Text)** | `#334155` | General body, table text, input text. |
| **Neutral Muted (Slate Muted)** | `#64748B` | Table headers, secondary details, text inputs placeholders. |
| **Border Subtle** | `#E2E8F0` | Soft 1px divider and outline bounds. |

---

## 🔤 Typography & Font Strategy

We employ a dual-font strategy:
1. **Merriweather** (Serif): Used for high-level page titles and brand headings. It establishes traditional, authoritative branding.
2. **Inter** (Sans-Serif): Used for numbers, item names, table records, inputs, and actions. It offers clean, high-density legibility.

### Font Registration
Font assets are registered dynamically at startup inside [FontLoader.java](file:///home/code8/Desktop/inventory/src/com/inventory/components/FontLoader.java). This ensures that custom typefaces render uniformly across macOS, Windows, and Linux environments without requiring local system installation:
```java
GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Inter-Regular.ttf")));
```

---

## 🧩 Custom Components & Styling Helpers

To keep design styles centralized, developers must style Swing components using [Theme.java](file:///home/code8/Desktop/inventory/src/com/inventory/components/Theme.java) static functions instead of configuring properties locally:

### 1. Buttons
* **Primary Button**: Filled with `FOREST_DEEP` color, white text, and a transition to `FOREST_LEAF` on mouse hover.
  ```java
  Theme.stylePrimaryButton(button);
  ```
* **Secondary Button (Outline)**: Transparent background, 1px subtle border, and green text.
  ```java
  Theme.styleSecondaryButton(button);
  ```

### 2. Text Inputs
* Styled with white backgrounds, slate text, and a soft 1px border. Focus indicators toggle the border outline to `FOREST_DEEP` for interactive feedback.
  ```java
  Theme.styleTextField(inputField);
  ```

### 3. Reusable Card Panel ([CardPanel.java](file:///home/code8/Desktop/inventory/src/com/inventory/components/CardPanel.java))
* Inherits from `JPanel` but overrides `paintComponent` to draw an anti-aliased rounded rectangle (`8px` corner radius) filled with the `Cream-Surface` color, outline bordered by `Border-Subtle`.

---

## 💫 UI Physics & Animations

To make the desktop application feel as smooth and modern as a web application, we utilize native Swing overrides:

### 1. Skeleton Loading Shimmer
* Located in `SkeletonPanel.java`, this component uses a native `javax.swing.Timer` to animate a `LinearGradientPaint` back and forth at 60fps. Drop this component anywhere data or images are loading to give users a premium tactile response.

### 2. Smooth Scrolling & Scrollbars
* We leverage FlatLaf's scroll engine to enable global native smooth scrolling:
  `UIManager.put("ScrollPane.smoothScrolling", true);`
* Ugly native desktop scrollbar tracks and buttons are completely stripped and replaced by beautiful, auto-hiding Mac/iOS style rounded pills:
  `UIManager.put("ScrollBar.thumbArc", 999);`
