package com.inventory.models;

import java.util.UUID;
import java.sql.Timestamp;

public class PurchaseOrder {
    private int id;
    private String uuid;
    private String itemUuid;
    private String supplierName;
    private int quantity;
    private String status;
    private Timestamp orderDate;

    // Full constructor for database reading
    public PurchaseOrder(int id, String uuid, String itemUuid, String supplierName, int quantity, String status, Timestamp orderDate) {
        this.id = id;
        this.uuid = uuid;
        this.itemUuid = itemUuid;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Constructor for new PO
    public PurchaseOrder(String itemUuid, String supplierName, int quantity, String status) {
        this.id = 0;
        this.uuid = UUID.randomUUID().toString();
        this.itemUuid = itemUuid;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.status = status;
        this.orderDate = new Timestamp(System.currentTimeMillis());
    }

    public int getId() { return id; }
    public String getUuid() { return uuid; }
    public String getItemUuid() { return itemUuid; }
    public String getSupplierName() { return supplierName; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public Timestamp getOrderDate() { return orderDate; }
}
