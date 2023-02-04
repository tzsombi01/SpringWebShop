package com.tzsombi.webshop.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tzsombi.webshop.constants.Constants;
import com.tzsombi.webshop.exceptions.ProductBadRequestException;
import com.tzsombi.webshop.models.*;

public class ProductFactory {

    public static Product makeProduct(String rawProduct) {
        ObjectMapper mapper = JsonMapper
                .builder()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true).build();

        try {
            Product product = mapper.readValue(rawProduct, Product.class);
            return product;
        } catch (JsonProcessingException e) {
            throw new ProductBadRequestException(Constants.PRODUCT_CANNOT_BE_CREATED);
        }
    }
}
