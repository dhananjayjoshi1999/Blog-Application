package com.springboot.blog.controller;


import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( value = "/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @SecurityRequirement(
            name = "Bear Authentication"
    )
    //Build Post Category Rest APi
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> addCategory (@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto savedCategory= categoryService.addCategory(categoryDto);
        return  new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }


    // get Category by id REST api
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") Long categoryId){
        CategoryDto categoryDto=categoryService.getCategory(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    //Get all category REST api
    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        List<CategoryDto> categoryDto= categoryService.getAllCategories();
        return ResponseEntity.ok(categoryDto);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    // Update category REST api
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                                      @PathVariable("id") Long categoryId){
        CategoryDto savedCategory= categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bear Authentication"
    )
    // Delete Category by Id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory (@PathVariable("id") Long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

}
