package com.wyx.product;

import com.wyx.product.api.ProductServiceApi;
import com.wyx.product.bean.Product;

/**
 * @Author: yuxiang
 * @Description:
 * @Date: Create in 2019/1/22
 */
public class ProductWeb implements ProductServiceApi {

    @Override
    public Product queryProductById(Long id) {
        return new Product(id,"glod",282.8);
    }
}
