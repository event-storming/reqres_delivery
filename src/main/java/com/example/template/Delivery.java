package com.example.template;


import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.persistence.*;

@Entity
public class Delivery {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long deliveryId;
    private Long orderId;
    private int quantity;
    private Long productId;
    private String productName;
    private String customerId;
    private String customerName;
    private String deliveryAddress;
    private String deliveryState;

    @PrePersist
    private void deliveryCheck(){
        if( "".equals(this.deliveryState) || this.deliveryState == null ){
            this.setDeliveryState(DeliveryStatus.DeliveryStarted.name());
        }
    }
    @PostPersist
    private void callProductApi() {

        // 1. 상품 서비스에 수량을 직접 변경하여 JPA call
        RestTemplate restTemplate = Application.applicationContext.getBean(RestTemplate.class);
        Environment env = Application.applicationContext.getEnvironment();
        String productUrl = env.getProperty("api.url.product") + "/products/" + productId;

        // 상품 서비스의 재고량 조회
        ResponseEntity<Product> productEntity = restTemplate.getForEntity(productUrl, Product.class);
        Product product = productEntity.getBody();

        int stock = product.getStock();
        // 재고량 변경
        int remainStock = stock - quantity;
        product.setStock(remainStock);
        String result = restTemplate.patchForObject(productUrl , product, String.class);
        System.out.println("상품 수량 변경 api call " + stock + " => " + remainStock);


        // 2. 상품 서비스의 controller 를 call 하여 재고량은 상품서비스에서 직접 변경하는 방식
//        ProductService productService = Application.applicationContext.getBean(ProductService.class);
//        productService.decreaseStock(productId, quantity);

    }
    @PostUpdate
    private void deliveryUpdate(){
        if( DeliveryStatus.DeliveryCancelled.name().equals(this.getDeliveryState())){
            // 상품 수량 변경
            ProductService productService = Application.applicationContext.getBean(ProductService.class);
            productService.increaseStock(productId, quantity);
        }
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

}
