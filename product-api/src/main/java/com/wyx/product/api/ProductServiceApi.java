package com.wyx.product.api;

import com.wyx.product.bean.Product;

/**
 * @Author: yuxiang
 * @Description:
 * @Date: Create in 2019/1/22
 */
public interface ProductServiceApi {

    public Product queryProductById(Long id);
}
