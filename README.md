# Botanical Logistics / Aramweer 🌿

A premium, cross-platform Java Swing Desktop application designed for inventory and supply chain management of organic botanical goods.

![Java Version](https://img.shields.io/badge/Java-21+-orange.svg)
![Database](https://img.shields.io/badge/Database-SQLite3-blue.svg)

---

## ✨ Features
* **Modern Aesthetic**: Built on a bespoke design system featuring FlatLaf, custom typography (Inter & Merriweather), anti-aliased UI components, rounded bento cards, and Mac/iOS style scroll physics.
* **Localization Ready**: Completely dynamically driven by a centralized `metadata/app.properties` file. Currently themed for East African operations (Kenyan Shillings - KSh).
* **High-Fidelity Assets**: Ships with embedded ultra high-quality macro photography assets for African botanicals.
* **Unified Database**: Runs on a single, lightning-fast SQLite file with strict migration tracking schemas (`schema_migrations`).
* **Purchase Order Analytics**: Integrates real-time aggregation of pending deliveries directly into the dashboard matrices.

---

## 📚 Documentation
Please view the `/docs` folder for detailed architectural manuals:
* **[Getting Started](docs/getting_started.md)**: Setup, build, and database initialization guide.
* **[Design System](docs/design_system.md)**: Breakdown of palettes, typography, and animation engines.
* **[Purchase Orders](docs/purchase_orders.md)**: Documentation on the PO tracking architecture.
* **[Architecture Details](docs/architecture_details.md)**: Java packaging strategy and routing logic.
* **[User Management](docs/user_management.md)**: Details on the bcrypt authentication middleware.

---

## 🚀 Quick Start
To immediately build and launch the application on MacOS or Linux:
```bash
./build.sh && ./run.sh
```

---

## 👨‍💻 Author & Credits
* **Developer**: Ssekabira Nicholas
* **Company**: code8

## 📄 License
This project is licensed under the **MIT License**. See the `LICENSE` file for details.
