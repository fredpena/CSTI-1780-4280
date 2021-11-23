package com.pucmm.csti.demo.model.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.pucmm.csti.demo.model.Carousel;
import com.pucmm.csti.demo.model.Product;

import java.util.List;

public class ProductWithCarousel {

    @Embedded
    public Product product;
    @Relation(
            parentColumn = "itemCode",
            entityColumn = "product"
    )
    public List<Carousel> carousels;
}
