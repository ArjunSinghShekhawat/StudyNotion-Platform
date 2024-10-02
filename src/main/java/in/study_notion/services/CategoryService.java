package in.study_notion.services;

import in.study_notion.domain.CategoryDto;
import in.study_notion.models.Category;
import org.bson.types.ObjectId;

import java.util.List;

public interface CategoryService {


    Category createCategory(CategoryDto categoryDto);
    List<Category>showAllCategories();
    Category updateCategory(CategoryDto categoryDto, ObjectId categoryId);
    boolean deleteCategory(ObjectId categoryId);
}
