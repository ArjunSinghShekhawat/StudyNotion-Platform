package in.studyNotion.services;

import in.studyNotion.domain.CategoryDto;
import in.studyNotion.models.Category;
import org.bson.types.ObjectId;

import java.util.List;

public interface CategoryService {


    Category createCategory(CategoryDto categoryDto);
    List<Category>showAllCategories();
    Category updateCategory(CategoryDto categoryDto, ObjectId categoryId);
    boolean deleteCategory(ObjectId categoryId);
}
