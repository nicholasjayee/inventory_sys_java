# Purchase Order Tracking

The Botanical Logistics application features a centralized Purchase Order (PO) tracking system. This system allows operations managers to log incoming inventory shipments and predict stock volume before trucks arrive at the warehouse.

---

## 🏗 Architecture

### Database Schema
* **Table**: `purchase_orders`
* **Columns**: `uuid` (Primary Key), `item_uuid` (Foreign Key -> Items), `supplier_name`, `quantity`, `status`, `order_date`.
* **Schema Location**: `migrations/002_purchase_orders.sql`

### Service Layer
The PO Service (`com.inventory.services.POService`) handles standard CRUD interactions for Purchase Orders. 
It also provides highly efficient aggregate methods like `getPendingArrivalsCount()`, which calculates the total volume of goods currently `PENDING` without loading every order into memory.

### Data Synchronization
The application ensures high data integrity. There is only one central database source of truth. When a Purchase Order is logged, it instantly synchronizes:
* **The Dashboard** (Metrics are recalculated)
* **The Raw Items Page** ("Pending Arrivals" aggregate updates immediately)

---

## 🚀 Creating a PO
1. Navigate to the **Raw Items** page.
2. Click **Log PO** in the top right.
3. Select an item, specify the supplier name, and input the quantity en route.
4. The system will auto-calculate metrics for pending deliveries upon save.
