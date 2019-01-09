package com.castellan.service.impl;

import com.castellan.common.Const;
import com.castellan.common.ServerResponse;
import com.castellan.dao.CategoryMapper;
import com.castellan.pojo.Category;
import com.castellan.service.ICategoryService;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    public ServerResponse addCategory(String categoryName,Integer parentId){
        if (parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        int resultCount = 0;
        resultCount = categoryMapper.selectCategoryByParentIdAndName(categoryName,parentId);
        if (resultCount > 0){
            return ServerResponse.createByErrorMessage("类别已经存在");
        }
        if (!parentId.equals(Const.ROOT_NODE)){

            resultCount = categoryMapper.selectCategoryById(parentId);
            if (resultCount == 0){
                return ServerResponse.createByErrorMessage("参数错误,父节点不存在");
            }


        }
        resultCount = categoryMapper.insertSelective(category);
        if (resultCount > 0){
            return ServerResponse.createBySuccessMessage("节点添加成功");
        }
        return ServerResponse.createByErrorMessage("");


    }

    /**
     * 更改类别的名称
     * @param categoryNameNew
     * @param categoryId
     * @return
     */
    public ServerResponse resetCategoryName(String categoryNameNew, Integer categoryId){
        if (categoryId == null || StringUtils.isBlank(categoryNameNew)){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryNameNew);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);

        if (resultCount > 0){
            return ServerResponse.createBySuccessMessage("分类名重置成功");
        }
        return ServerResponse.createByErrorMessage("分类名重置失败");
    }


    public ServerResponse getChildrenParallelCategory( int categoryId){
        List<Category> categories = categoryMapper.selectCategoryByParentId(categoryId);
        return ServerResponse.createBySuccess(categories);
    }


    public ServerResponse getChildernCategoryById(int categoryId){
        List<Category> categories = new ArrayList<>();
        getChildrenCategory(categories,categoryId);


        Set<Integer> integerSet = Sets.newHashSet();
        Iterator<Category> iterator = categories.iterator();
        while (iterator.hasNext()){
            integerSet.add(iterator.next().getId());
        }
        return ServerResponse.createBySuccess(integerSet);
    }

    private List<Category>  getChildrenCategory(List<Category> categories,int categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            categories.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(categoryId);
        for (Category categoryItem: categoryList) {
            getChildrenCategory(categories,categoryItem.getId());
        }
        return categories;

    }
}
