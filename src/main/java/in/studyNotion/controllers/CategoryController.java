package in.studyNotion.controllers;

import in.studyNotion.domain.CategoryDto;
import in.studyNotion.models.Category;
import in.studyNotion.services.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<Category>createCategory(@Valid @RequestBody CategoryDto categoryDto){

        try{
            Category category = this.categoryService.createCategory(categoryDto);
            return new ResponseEntity<>(category, HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error occurred while create new category {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/show-all")
    public ResponseEntity<List<Category>>showAllCategory(){

        try{
            List<Category> categories = this.categoryService.showAllCategories();
            return new ResponseEntity<>(categories, HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error occurred while show all categories {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<Category>updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable ObjectId categoryId){

        try{
             Category updateCategory = this.categoryService.updateCategory(categoryDto,categoryId);
            return new ResponseEntity<>(updateCategory, HttpStatus.OK);

        }catch (Exception e){
            log.error("Error occurred while update category {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<Boolean>deleteCategory(@PathVariable ObjectId categoryId){

        try{
            boolean isDelete =  this.categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(isDelete, HttpStatus.OK);

        }catch (Exception e){
            log.error("Error occurred while delete category {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
