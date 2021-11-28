package com.pucmm.csti.model.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.pucmm.csti.model.Category;
import com.pucmm.csti.model.Product;

import java.io.Serializable;

public class CategoryAndProduct  implements Serializable {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "uid",
            entityColumn = "category"
    )
    public Product product;
}
