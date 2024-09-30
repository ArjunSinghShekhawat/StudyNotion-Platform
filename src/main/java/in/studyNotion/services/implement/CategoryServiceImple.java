package in.studyNotion.services.implement;

import in.studyNotion.domain.CategoryDto;
import in.studyNotion.models.Category;
import in.studyNotion.repositories.CategoryRepository;
import in.studyNotion.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Override
    public Category updateCategory(CategoryDto categoryDto, ObjectId categoryId) {

        Optional<Category> category = this.categoryRepository.findById(categoryId);
        Category updatedCategory = null;

        if(category.isPresent()){
            category.get().setDescription(categoryDto.getDescription()!=null && !categoryDto.getDescription().equals("")? categoryDto.getDescription() :category.get().getDescription());
            category.get().setName(categoryDto.getName()!=null && !categoryDto.getName().equals("")?categoryDto.getName():category.get().getName());

            updatedCategory = this.categoryRepository.save(category.get());
        }
        return updatedCategory;
    }

    @Override
    public boolean deleteCategory(ObjectId categoryId) {

        boolean remove = false;

        try{
            Optional<Category> category = this.categoryRepository.findById(categoryId);

            if(category.isPresent()){
                this.categoryRepository.deleteById(categoryId);
                remove=true;
            }
        }
        catch (Exception e){
            log.error("Error occur while delete a category {} ",e.getMessage());
        }
        return remove;
    }

}
