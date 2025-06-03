package com.example.jerry.restaurant.pojo;

public class DiningTable {
    private Integer tableId;
    private String tableStatus;
    private Integer capacity;

    public DiningTable() {
    }

    public DiningTable(String tableStatus, Integer capacity) {
        this.tableStatus = tableStatus;
        this.capacity = capacity;
    }

    // Getter & Setter
    public Integer getTableId() { return tableId; }
    public void setTableId(Integer tableId) { this.tableId = tableId; }
    
    public String getTableStatus() { return tableStatus; }
    public void setTableStatus(String tableStatus) { this.tableStatus = tableStatus; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "DiningTable{" +
                "tableId=" + tableId +
                ", tableStatus='" + tableStatus + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}