package com.example.template;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveryRepository extends CrudRepository<Delivery, Long> {

    List<Delivery> findByOrderIdOrderByDeliveryIdDesc(Long orderId);
    Delivery findDeliveryIdByOrderId(Long orderId);
}
