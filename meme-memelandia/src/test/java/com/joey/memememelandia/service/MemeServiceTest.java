package com.joey.memememelandia.service;

import com.joey.memememelandia.Utils.MemeUtils;
import com.joey.memememelandia.config.API_Gateway_Discovery;
import com.joey.memememelandia.domain.CategoryDTO;
import com.joey.memememelandia.domain.Meme;
import com.joey.memememelandia.domain.UserDTO;
import com.joey.memememelandia.exceptions.CategoryNotFoundException;
import com.joey.memememelandia.exceptions.MemeNotFoundException;
import com.joey.memememelandia.exceptions.MissingFieldsException;
import com.joey.memememelandia.exceptions.UserNotFoundException;
import com.joey.memememelandia.records.MemeDTO;
import com.joey.memememelandia.records.MemeDtoRequest;
import com.joey.memememelandia.records.OnlyMemeDTO;
import com.joey.memememelandia.records.RestResponse;
import com.joey.memememelandia.repository.MemeRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemeServiceTest {

    @Mock
    private MemeRepository memeRepository;

    @Mock
    private API_Gateway_Discovery apiGatewayDiscovery;

    @Autowired
    @InjectMocks
    private MemeService memeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWithPathVariableTest_HappyPath() {
        MemeDtoRequest memeDtoRequest = new MemeDtoRequest("troll face", "url troll face", "memes 2010", "creator@mail");
        MemeDTO  memeDTO = new MemeDTO("troll face", "url troll face", "memes 2010", "creator");
        Meme meme = new Meme(memeDTO);

        UserDTO userDTO = new UserDTO("111", "creator@mail", "creator", null, null);
        CategoryDTO categoryDTO = new CategoryDTO("112", "memes 2010", "memes desc", null, null);

        when(this.apiGatewayDiscovery.getUserByEmailFromUserService(memeDtoRequest.creatorEmail())).thenReturn(userDTO);
        when(this.apiGatewayDiscovery.getCategoryByNameFromCategoryService(memeDtoRequest.category())).thenReturn(categoryDTO);
        when(this.memeRepository.save(any())).thenReturn(meme);

        RestResponse<Meme> restResponse = this.memeService.create(memeDtoRequest);
        HttpStatus httpStatus = restResponse.httpStatus();
        Meme memeCreated = restResponse.body();

        assertEquals(HttpStatus.CREATED, httpStatus);
        assertEquals(meme, memeCreated);

        verify(this.apiGatewayDiscovery, times(1)).getUserByEmailFromUserService(memeDtoRequest.creatorEmail());
        verify(this.apiGatewayDiscovery, times(1)).getCategoryByNameFromCategoryService(memeDtoRequest.category());
        verify(this.memeRepository, times(1)).save(any());
    }

    @Test
    void createWithPathVariableTest_whenMemeDtoRequestIsNull_throwNullPointerException() {
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            this.memeService.create(null);
        });
        assertEquals("The variable(s) can´t be null!", thrown.getMessage());
    }

    @Test
    void createWithPathVariableTest_whenAnyFieldMemeDtoRequestIsNull_throwNullPointerException() {
        Exception thrown = Assertions.assertThrows(MissingFieldsException.class, () -> {
            this.memeService.create(new MemeDtoRequest("name", "url", "cat", null));
        });
        assertEquals("Missing field not null able!", thrown.getMessage());


        thrown = Assertions.assertThrows(MissingFieldsException.class, () -> {
            this.memeService.create(new MemeDtoRequest("name", "url", "cat", ""));
        });
        assertEquals("Missing field not null able!", thrown.getMessage());
    }

    @Test
    void createWithPathVariableTest_whenUserNotFound_throwUserNotFoundException() {
        Exception thrown = Assertions.assertThrows(UserNotFoundException.class, () -> {
            when(this.apiGatewayDiscovery.getUserByEmailFromUserService("email@Fake")).thenReturn(null);
            this.memeService.create(new MemeDtoRequest("name", "url", "cat", "email@Fake"));
        });
        assertEquals("User not found for email: email@Fake", thrown.getMessage());
    }

    @Test
    void createWithPathVariableTest_whenCategoryNotFound_throwCategoryNotFoundException() {
        Exception thrown = Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            MemeDtoRequest memeDtoRequest = new MemeDtoRequest("name", "url", "cat", "email@Fake");

            UserDTO userDTO = new UserDTO("1", "email@Fake", "name", null, null);
            when(this.apiGatewayDiscovery.getUserByEmailFromUserService(memeDtoRequest.creatorEmail())).thenReturn(userDTO);
            when(this.apiGatewayDiscovery.getCategoryByNameFromCategoryService("cat")).thenReturn(null);

            this.memeService.create(memeDtoRequest);
        });
        assertEquals("Category not found for name: cat", thrown.getMessage());
    }

    @Test
    void CreateTest_HappyPath() {
        OnlyMemeDTO onlyMemeDTO = new OnlyMemeDTO("troll", "url");
        String userId = "111";
        String categoryId = "222";

        UserDTO userDTO = new UserDTO("111", "creator@mail", "creator name", null, null);
        CategoryDTO categoryDTO = new CategoryDTO("222", "memes 2010", "memes desc", null, null);

        when(this.apiGatewayDiscovery.getUserByIdFromUserService(userId)).thenReturn(userDTO);
        when(this.apiGatewayDiscovery.getCategoryByIdFromCategoryService(categoryId)).thenReturn(categoryDTO);

        Meme meme = new Meme(new MemeDTO("troll", "url", "memes 2010", "creator name"));

        when(this.memeRepository.save(any())).thenReturn(meme);

        RestResponse<Meme> restResponse = this.memeService.create(userId, categoryId, onlyMemeDTO);
        HttpStatus httpStatus = restResponse.httpStatus();
        Meme memeCreated = restResponse.body();

        assertEquals(HttpStatus.CREATED, httpStatus);
        assertEquals(meme, memeCreated);

        verify(this.apiGatewayDiscovery, times(1)).getUserByIdFromUserService(userId);
        verify(this.apiGatewayDiscovery, times(1)).getCategoryByIdFromCategoryService(categoryId);
        verify(this.memeRepository, times(1)).save(any());
    }

    @Test
    void findAllTest_HappyPath() {
        MemeDTO memeDTO = new MemeDTO("meme", "urlMeme", "cat", "creator");
        MemeDTO memeDTO_2 = new MemeDTO("meme2", "urlMeme2", "cat", "creator");
        ArrayList<Meme> memeArrayList = new ArrayList<>();
        Meme meme_1 = new Meme(memeDTO);
        Meme meme_2 = new Meme(memeDTO_2);
        memeArrayList.add(meme_1);
        memeArrayList.add(meme_2);

        when(this.memeRepository.findAll()).thenReturn(memeArrayList);

        RestResponse<Iterable<Meme>> restResponse = this.memeService.findAll();
        HttpStatus httpStatus = restResponse.httpStatus();
        List<Meme> memeList= (List<Meme>) restResponse.body();

        assertEquals(HttpStatus.OK, httpStatus);
        assertEquals(memeArrayList.size(), memeList.size());
        assertEquals(meme_1, memeList.get(0));
        assertEquals(meme_2, memeList.get(1));

        verify(this.memeRepository, times(1)).findAll();
    }

    @Test
    void findAllByCategoryTest_HappyPath() {
        Meme meme1 = new Meme(new MemeDTO("meme", "urlMeme", "cat", "creator"));
        Meme meme2 = new Meme(new MemeDTO("meme2", "urlMeme2", "cat", "creator"));
        Meme meme3 = new Meme(new MemeDTO("meme3", "urlMeme2", "cat", "creator"));
        Meme[] memeList = new Meme[]{meme1, meme2, meme3};
        ArrayList<Meme> memeArrayList = new ArrayList<>(List.of(memeList));

        when(this.memeRepository.findAllByCategory("cat")).thenReturn(memeArrayList);

        RestResponse<Iterable<Meme>> restResponse = this.memeService.findAllByCategory("cat");
        HttpStatus httpStatus = restResponse.httpStatus();
        List<Meme> memeList_response = (List<Meme>) restResponse.body();

        assertEquals(HttpStatus.OK, httpStatus);
        assertEquals(memeArrayList.size(), memeList_response.size());
        for (Meme meme : memeList_response) {
            if (meme.getName().equals(meme1.getName())) {
                assertEquals(meme, meme1);
            } else if (meme.getName().equals(meme2.getName())) {
                assertEquals(meme, meme2);
            } else {
                assertEquals(meme, meme3);
            }
        }

        verify(this.memeRepository, times(1)).findAllByCategory(any());
    }

    @Test
    void findAllByCreatorTest_HappyPath() {
        Meme meme1 = new Meme(new MemeDTO("meme", "urlMeme", "cat", "creator"));
        Meme meme2 = new Meme(new MemeDTO("meme2", "urlMeme2", "cat", "creator"));
        Meme meme3 = new Meme(new MemeDTO("meme3", "urlMeme2", "cat", "creator"));
        Meme[] memeList = new Meme[]{meme1, meme2, meme3};
        ArrayList<Meme> memeArrayList = new ArrayList<>(List.of(memeList));


        when(this.memeRepository.findAllByCreatedBy("creator")).thenReturn(memeArrayList);

        RestResponse<Iterable<Meme>> restResponse = this.memeService.findAllByCreatedBy("creator");
        HttpStatus httpStatus = restResponse.httpStatus();
        List<Meme> memeList_response = (List<Meme>) restResponse.body();

        assertEquals(HttpStatus.OK, httpStatus);
        assertEquals(memeArrayList.size(), memeList_response.size());
        for (Meme meme : memeList_response) {
            if (meme.getName().equals(meme1.getName())) {
                assertEquals(meme, meme1);
            } else if (meme.getName().equals(meme2.getName())) {
                assertEquals(meme, meme2);
            } else {
                assertEquals(meme, meme3);
            }
        }

        verify(this.memeRepository, times(1)).findAllByCreatedBy(any());
    }

    @Test
    void findByIdTest_HappyPath() {
        MemeDTO memeDTO = new MemeDTO("meme", "urlMeme", "cat", "creator");
        Meme meme = new Meme(memeDTO);

        when(this.memeRepository.findById(meme.getId())).thenReturn(Optional.of(meme));

        RestResponse<Meme> restResponse = this.memeService.findById(meme.getId());
        HttpStatus httpStatus = restResponse.httpStatus();
        Meme memeFound = restResponse.body();

        assertEquals(HttpStatus.OK, httpStatus);
        assertEquals(meme, memeFound);

        verify(this.memeRepository, times(1)).findById(any());
    }

    @Test
    void findByIdTest_whenTheIdIsNull() {
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            this.memeService.findById(null);
        });
        assertEquals("The variable(s) can´t be null!", thrown.getMessage());
    }

    @Test
    void findByIdTest_whenTheIdDoNotMatch() {
        Exception thrown = Assertions.assertThrows(MemeNotFoundException.class, () -> {
            when(this.memeRepository.findById(any())).thenReturn(Optional.empty());
            this.memeService.findById("111");
        });
        assertEquals("Meme not found for id: 111", thrown.getMessage());
        verify(this.memeRepository).findById(any());
    }

    @Test
    void findByNameTest_HappyPath() {
        MemeDTO memeDTO = new MemeDTO("meme", "urlMeme", "cat", "creator");
        Meme meme = new Meme(memeDTO);

        when(this.memeRepository.findByName("meme")).thenReturn(Optional.of(meme));

        RestResponse<Meme> restResponse = this.memeService.findByName("meme");
        HttpStatus httpStatus = restResponse.httpStatus();
        Meme memeFound = restResponse.body();

        assertEquals(HttpStatus.OK, httpStatus);
        assertEquals(meme, memeFound);

        verify(this.memeRepository, times(1)).findByName(any());
    }

    @Test
    void updateTest_HappyPath() {
        OnlyMemeDTO onlyMemeDTO = new OnlyMemeDTO("name updated", "url updated");
        Meme meme = new Meme(new MemeDTO("name", "url", "category", "creator"));
        String memeId = meme.getId();

        when(this.memeRepository.findById(memeId)).thenReturn(Optional.of(meme));

        MemeUtils.updateMeme(meme, onlyMemeDTO);

        when(this.memeRepository.save(meme)).thenReturn(meme);

        RestResponse<Meme> restResponse = this.memeService.update(memeId, onlyMemeDTO);
        HttpStatus httpStatus = restResponse.httpStatus();
        Meme memeFound = restResponse.body();

        assertEquals(HttpStatus.OK, httpStatus);
        assertEquals(meme, memeFound);
        assertEquals("name updated", memeFound.getName());
        assertEquals("url updated", memeFound.getMemeUrl());

        verify(this.memeRepository, times(1)).findById(any());
        verify(this.memeRepository, times(1)).save(any());
    }

    @Test
    void deleteTest_HappyPath() {
        MemeDTO memeDTO = new MemeDTO("meme", "urlMeme", "cat", "creator");
        Meme meme = new Meme(memeDTO);

        when(this.memeRepository.findById(meme.getId())).thenReturn(Optional.of(meme));

        RestResponse<Boolean> restResponse = this.memeService.delete(meme.getId());
        HttpStatus httpStatus = restResponse.httpStatus();
        Boolean idDeleted = restResponse.body();

        assertEquals(HttpStatus.OK, httpStatus);
        assertTrue(idDeleted);

        verify(this.memeRepository, times(1)).delete(any());
    }
}