package com.pucmm.csti.demo.model.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.pucmm.csti.demo.model.Category;
import com.pucmm.csti.demo.model.Product;

public class CategoryAndProduct {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "uid",
            entityColumn = "category"
    )
    public Product product;
}
