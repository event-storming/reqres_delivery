package com.example.template;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
@FeignClient(name ="product", url="${api.url.product}")
public interface ProductService {

    @RequestMapping(method = RequestMethod.PUT, value = "/product/decreaseStock/{productId}", consumes = "application/json")
    void decreaseStock(@PathVariable("productId") Long productId, int quantity);

    @RequestMapping(method = RequestMethod.PUT, value = "/product/increaseStock/{productId}", consumes = "application/json")
    void increaseStock(@PathVariable("productId") Long productId, int quantity);
}
