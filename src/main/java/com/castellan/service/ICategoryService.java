package com.castellan.service;

import com.castellan.common.ServerResponse;

public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse resetCategoryName(String categoryNameNew, Integer categoryId);

    ServerResponse getChildrenParallelCategory( int categoryId);

    ServerResponse getChildernCategoryById(int categoryId);

}
