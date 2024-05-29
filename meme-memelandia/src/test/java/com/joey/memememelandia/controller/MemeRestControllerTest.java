package com.joey.memememelandia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joey.memememelandia.Utils.MemeUtils;
import com.joey.memememelandia.domain.Meme;
import com.joey.memememelandia.exceptions.MissingFieldsException;
import com.joey.memememelandia.records.MemeDTO;
import com.joey.memememelandia.records.MemeDtoRequest;
import com.joey.memememelandia.records.OnlyMemeDTO;
import com.joey.memememelandia.records.RestResponse;
import com.joey.memememelandia.service.MemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemeRestController.class)
class MemeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemeService memeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTest_HappyPath() throws Exception {
        MemeDtoRequest memeDtoRequest = new MemeDtoRequest("meme", "url", "category", "email");
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        RestResponse<Meme> responseService = new RestResponse<>(HttpStatus.CREATED, meme);
        Meme memeCreated = responseService.body();

        when(this.memeService.create(any())).thenReturn(responseService);

        ResultActions result = mockMvc.perform(post("/api/meme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memeDtoRequest)));

        verify(this.memeService, times(1)).create(any());

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(memeCreated.getId()))
                .andExpect(jsonPath("$.name").value(memeCreated.getName()))
                .andExpect(jsonPath("$.memeUrl").value(memeCreated.getMemeUrl()))
                .andExpect(jsonPath("$.category").value(memeCreated.getCategory()))
                .andExpect(jsonPath("$.createdBy").value(memeCreated.getCreatedBy()));
    }

    @Test
    void createTest_whenThereMissingFields() throws Exception {
        MemeDtoRequest memeDtoRequest = new MemeDtoRequest("meme", "url", "", "email");
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        RestResponse<Meme> responseService = new RestResponse<>(HttpStatus.CREATED, meme);
        Meme memeCreated = responseService.body();

        when(this.memeService.create(memeDtoRequest)).thenThrow(MissingFieldsException.class);

        ResultActions result = mockMvc.perform(post("/api/meme")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memeDtoRequest)));

        verify(this.memeService, times(1)).create(any());

        result.andExpect(status().isBadRequest());
//                .andExpect(jsonPath("$.msg").value(memeCreated.getId()))
    }

    @Test
    void Create_withPathVariableTest_HappyPath() throws Exception {
        OnlyMemeDTO onlyMemeDTO = new OnlyMemeDTO("meme", "url");
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        RestResponse<Meme> responseService = new RestResponse<>(HttpStatus.CREATED, meme);
        Meme memeCreated = responseService.body();

        when(this.memeService.create("111", "222", onlyMemeDTO)).thenReturn(responseService);

        ResultActions result = mockMvc.perform(post("/api/meme/111/222")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(onlyMemeDTO)));

        verify(this.memeService, times(1)).create("111", "222", onlyMemeDTO);

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(memeCreated.getId()))
                .andExpect(jsonPath("$.name").value(memeCreated.getName()))
                .andExpect(jsonPath("$.memeUrl").value(memeCreated.getMemeUrl()))
                .andExpect(jsonPath("$.category").value(memeCreated.getCategory()))
                .andExpect(jsonPath("$.createdBy").value(memeCreated.getCreatedBy()));
    }

    @Test
    void getAllTest_HappyPath() throws Exception {
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        Meme meme2 = new Meme(new MemeDTO("meme 2", "url 2", "category 2", "creator 2"));
        Iterable<Meme> memes = List.of(meme, meme2);
        RestResponse<Iterable<Meme>> responseService = new RestResponse<>(HttpStatus.OK, memes);
        when(this.memeService.findAll()).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/meme"));

        verify(this.memeService, times(1)).findAll();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(meme.getId()))
                .andExpect(jsonPath("$[0].name").value("meme"))
                .andExpect(jsonPath("$[0].memeUrl").value("url"))
                .andExpect(jsonPath("$[0].category").value("category"))
                .andExpect(jsonPath("$[0].createdBy").value("creator"))
                .andExpect(jsonPath("$[1].id").value(meme2.getId()))
                .andExpect(jsonPath("$[1].name").value("meme 2"))
                .andExpect(jsonPath("$[1].memeUrl").value("url 2"))
                .andExpect(jsonPath("$[1].category").value("category 2"))
                .andExpect(jsonPath("$[1].createdBy").value("creator 2"));
    }

    @Test
    void getAllByCategoryTest_HappyPath() throws Exception {
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        Meme meme2 = new Meme(new MemeDTO("meme 2", "url 2", "category", "creator 2"));
        Iterable<Meme> memes = List.of(meme, meme2);
        RestResponse<Iterable<Meme>> responseService = new RestResponse<>(HttpStatus.OK, memes);
        when(this.memeService.findAllByCategory("category")).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/meme/qCategory=category"));

        verify(this.memeService, times(1)).findAllByCategory(any());

        System.out.println(result);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(meme.getId()))
                .andExpect(jsonPath("$[0].name").value("meme"))
                .andExpect(jsonPath("$[0].memeUrl").value("url"))
                .andExpect(jsonPath("$[0].category").value("category"))
                .andExpect(jsonPath("$[0].createdBy").value("creator"))
                .andExpect(jsonPath("$[1].id").value(meme2.getId()))
                .andExpect(jsonPath("$[1].name").value("meme 2"))
                .andExpect(jsonPath("$[1].memeUrl").value("url 2"))
                .andExpect(jsonPath("$[1].category").value("category"))
                .andExpect(jsonPath("$[1].createdBy").value("creator 2"));
    }

    @Test
    void getAllByCreatorTest_HappyPath() throws Exception {
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        Meme meme2 = new Meme(new MemeDTO("meme 2", "url 2", "category 2", "creator"));
        Iterable<Meme> memes = List.of(meme, meme2);
        RestResponse<Iterable<Meme>> responseService = new RestResponse<>(HttpStatus.OK, memes);
        when(this.memeService.findAllByCreatedBy("creator")).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/meme/qCreator=creator"));

        verify(this.memeService, times(1)).findAllByCreatedBy(any());

        System.out.println(result);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(meme.getId()))
                .andExpect(jsonPath("$[0].name").value("meme"))
                .andExpect(jsonPath("$[0].memeUrl").value("url"))
                .andExpect(jsonPath("$[0].category").value("category"))
                .andExpect(jsonPath("$[0].createdBy").value("creator"))
                .andExpect(jsonPath("$[1].id").value(meme2.getId()))
                .andExpect(jsonPath("$[1].name").value("meme 2"))
                .andExpect(jsonPath("$[1].memeUrl").value("url 2"))
                .andExpect(jsonPath("$[1].category").value("category 2"))
                .andExpect(jsonPath("$[1].createdBy").value("creator"));
    }

    @Test
    void getByIdTest_HappyPath() throws Exception {
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        RestResponse<Meme> responseService = new RestResponse<>(HttpStatus.OK, meme);
        when(this.memeService.findById(meme.getId())).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/meme/" + meme.getId()));

        verify(this.memeService, times(1)).findById(meme.getId());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(meme.getId()))
                .andExpect(jsonPath("$.name").value(meme.getName()))
                .andExpect(jsonPath("$.memeUrl").value(meme.getMemeUrl()))
                .andExpect(jsonPath("$.category").value(meme.getCategory()))
                .andExpect(jsonPath("$.createdBy").value(meme.getCreatedBy()));
    }

    @Test
    void getByNameTest_HappyPath() throws Exception {
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        RestResponse<Meme> responseService = new RestResponse<>(HttpStatus.OK, meme);
        when(this.memeService.findByName(meme.getName())).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/meme/qe=" + meme.getName()));

        verify(this.memeService, times(1)).findByName(meme.getName());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(meme.getId()))
                .andExpect(jsonPath("$.name").value(meme.getName()))
                .andExpect(jsonPath("$.memeUrl").value(meme.getMemeUrl()))
                .andExpect(jsonPath("$.category").value(meme.getCategory()))
                .andExpect(jsonPath("$.createdBy").value(meme.getCreatedBy()));
    }

    @Test
    void updateMemeTest_HappyPath() throws Exception {
        Meme meme = new Meme(new MemeDTO("meme", "url", "category", "creator"));
        OnlyMemeDTO onlyMemeDTO = new OnlyMemeDTO("name up", "url date");

        MemeUtils.updateMeme(meme, onlyMemeDTO);

        RestResponse<Meme> responseService = new RestResponse<>(HttpStatus.OK, meme);

        when(this.memeService.update(meme.getId(), onlyMemeDTO)).thenReturn(responseService);

        ResultActions result = mockMvc.perform(put("/api/meme/" + meme.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(onlyMemeDTO)));

        verify(this.memeService, times(1)).update(meme.getId(), onlyMemeDTO);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(meme.getId()))
                .andExpect(jsonPath("$.name").value(meme.getName()))
                .andExpect(jsonPath("$.memeUrl").value(meme.getMemeUrl()))
                .andExpect(jsonPath("$.category").value(meme.getCategory()))
                .andExpect(jsonPath("$.createdBy").value(meme.getCreatedBy()));
    }

    @Test
    void deleteTest_HappyPath() throws Exception {
        RestResponse<Boolean> responseService = new RestResponse<>(HttpStatus.OK, true);
        when(this.memeService.delete("1")).thenReturn(responseService);

        ResultActions result = mockMvc.perform(delete("/api/meme/1"));

        verify(this.memeService, times(1)).delete("1");

        result.andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}