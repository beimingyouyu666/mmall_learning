package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    CategoryMapper categoryMapper;

    public ServerResponse<List<Category>> getCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.getCategoryByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse<String> addCategory(Integer  parentId,String categoryName){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    public ServerResponse<String> setCategoryName(Integer categoryId,String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    public ServerResponse<List<Integer>> getDeepCategory(Integer categoryId){
        Set<Category> categorySet = new HashSet<Category>();
        getDeep(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     * 递归查询出节点信息和所有子节点
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> getDeep(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            return categorySet;
        }
        categorySet.add(category);
        List<Category> categoryList = categoryMapper.getCategoryByParentId(categoryId);
        if (!CollectionUtils.isEmpty(categoryList)){
            for (Category item : categoryList ){
                getDeep(categorySet,item.getId());
            }
        }
        return categorySet;
    }


}
