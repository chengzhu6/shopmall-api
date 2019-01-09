package com.castellan.service;

import com.castellan.common.ServerResponse;
import com.castellan.pojo.Category;

import java.util.List;
import java.util.Set;

public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse resetCategoryName(String categoryNameNew, Integer categoryId);

    ServerResponse getChildrenParallelCategory( int categoryId);

    ServerResponse<List<Integer>> getChildernCategoryById(int categoryId);

}
