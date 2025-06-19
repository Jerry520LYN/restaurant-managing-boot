package com.example.jerry.restaurant.pojo;

import java.math.BigDecimal;

public class Menu {
    private String dishId;         // 菜品ID
    private String dishName;       // 菜品名称
    private BigDecimal price;      // 价格
    private String description;    // 描述
    private String imageUrl;       // 图片链接

    public Menu() {}

    public Menu(String dishId, String dishName, BigDecimal price, String description, String imageUrl) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getter 和 Setter 方法
    public String getDishId() { return dishId; }
    public void setDishId(String dishId) { this.dishId = dishId; }

    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "Menu{" +
                "dishId='" + dishId + '\'' +
                ", dishName='" + dishName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}