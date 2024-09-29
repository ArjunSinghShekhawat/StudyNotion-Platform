package in.studyNotion.controllers;

import in.studyNotion.domain.CategoryDto;
import in.studyNotion.models.Category;
import in.studyNotion.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<Category>createCategory(@RequestBody CategoryDto categoryDto){

        try{
            Category category = this.categoryService.createCategory(categoryDto);
            return new ResponseEntity<>(category, HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error occurred while create new category {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/show-all")
    public ResponseEntity<List<Category>>showAllCategory(@RequestBody CategoryDto categoryDto){

        try{
            List<Category> categories = this.categoryService.showAllCategories();
            return new ResponseEntity<>(categories, HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error occurred while show all categories {} ",e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
