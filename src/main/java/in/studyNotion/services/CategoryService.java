package in.studyNotion.services;

import in.studyNotion.domain.CategoryDto;
import in.studyNotion.models.Category;

import java.util.List;

public interface CategoryService {


    Category createCategory(CategoryDto categoryDto);
    List<Category>showAllCategories();
}
