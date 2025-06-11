package com.foryourlife.admin.sales.rules.infrastructure.httpControllers;

public class RuleRequest {
    private String userId;
    private String productId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
