package in.studyNotion.services.implement;

import in.studyNotion.domain.CategoryDto;
import in.studyNotion.models.Category;
import in.studyNotion.repositories.CategoryRepository;
import in.studyNotion.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImple implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Category createCategory(CategoryDto categoryDto) {

        Category category = new Category();

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return this.categoryRepository.save(category);
    }

    @Override
    public List<Category> showAllCategories() {
        return this.categoryRepository.findAll();
    }

}
