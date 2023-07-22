package com.enigma.procurement.service;

import com.enigma.procurement.entity.Category;
import com.enigma.procurement.entity.Vendor;

import java.util.List;

public interface CategoryService {
    Category create(Category category);

    Category getById(String id);

    List<Category> getAll();
}
