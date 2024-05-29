package com.joey.memememelandia.controller;

import com.joey.memememelandia.domain.Meme;
import com.joey.memememelandia.records.MemeDtoRequest;
import com.joey.memememelandia.records.OnlyMemeDTO;
import com.joey.memememelandia.records.RestResponse;
import com.joey.memememelandia.service.MemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meme")
public class MemeRestController {

    private final MemeService memeService;

    @Autowired
    public MemeRestController(MemeService memeService) {
        this.memeService = memeService;
    }

    @PostMapping
    public ResponseEntity<Meme> create (@RequestBody MemeDtoRequest memeDtoRequest) {
        RestResponse<Meme> restResponse = this.memeService.create(memeDtoRequest);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @PostMapping("/{userId}/{categoryId}")
    public ResponseEntity<Meme> create (
                    @PathVariable(name = "userId", required = true) String userId,
                    @PathVariable(name = "categoryId", required = true) String categoryId,
                    @RequestBody OnlyMemeDTO onlyMemeDTO) {

        RestResponse<Meme> restResponse = this.memeService.create(userId, categoryId, onlyMemeDTO);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @GetMapping
    public ResponseEntity<Iterable<Meme>> getAll() {
        RestResponse<Iterable<Meme>> restResponse = this.memeService.findAll();
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @GetMapping("/qCategory={category}")
    public ResponseEntity<Iterable<Meme>> getAllByCategory(@PathVariable String category) {
        RestResponse<Iterable<Meme>> restResponse = this.memeService.findAllByCategory(category);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @GetMapping("/qCreator={creator}")
    public ResponseEntity<Iterable<Meme>> getAllByCreator(@PathVariable String creator) {
        RestResponse<Iterable<Meme>> restResponse = this.memeService.findAllByCreatedBy(creator);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meme> getById(@PathVariable String id) {
        RestResponse<Meme> restResponse = this.memeService.findById(id);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @GetMapping("/qe={name}")
    public ResponseEntity<Meme> getByName(@PathVariable String name) {
        RestResponse<Meme> restResponse = this.memeService.findByName(name);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meme> updateMeme(@PathVariable String id,
                                           @RequestBody OnlyMemeDTO onlyMemeDTO) {

        RestResponse<Meme> restResponse = this.memeService.update(id, onlyMemeDTO);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @PutMapping("/creator/{id}")
    public ResponseEntity<Meme> updateCreatorMeme (@PathVariable String id,
                                                   @RequestBody String creatorName) {

        RestResponse<Meme> restResponse = this.memeService.updateCreator(id, creatorName);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }
    @PutMapping("/category/{id}")
    public ResponseEntity<Meme> updateCategoryMeme (@PathVariable String id,
                                                    @RequestBody String categoryName) {

        RestResponse<Meme> restResponse = this.memeService.updateCategory(id, categoryName);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        RestResponse<Boolean> restResponse = this.memeService.delete(id);
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }

    @GetMapping("/meme_of_the_day")
    public ResponseEntity<Meme> memeOfTheDay() {
        RestResponse<Meme> restResponse = this.memeService.memeOfTheDay();
        return ResponseEntity.status(restResponse.httpStatus()).body(restResponse.body());
    }
}
