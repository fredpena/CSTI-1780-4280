package com.pucmm.csti.model.relationships;

import androidx.constraintlayout.helper.widget.Carousel;
import androidx.room.Embedded;
import androidx.room.Relation;
import com.pucmm.csti.model.Product;

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
