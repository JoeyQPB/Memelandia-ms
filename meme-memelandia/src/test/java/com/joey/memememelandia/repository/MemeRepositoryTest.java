package com.joey.memememelandia.repository;

import com.joey.memememelandia.domain.Meme;
import com.joey.memememelandia.records.MemeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.data.cassandra.CassandraInvalidQueryException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataCassandraTest
@ActiveProfiles("test")
class MemeRepositoryTest {

    @Autowired
    private MemeRepository memeRepository;

    @BeforeEach
    public void clearDB() {
        Iterable<Meme> userIterable = this.memeRepository.findAll();
        userIterable.forEach(meme -> this.memeRepository.delete(meme));
    }

    @Test
    void findByNameTest_HappyPath() {
        Meme meme = createMemeMethod(new MemeDTO("meme_name", "url", "cat", "creator"));
        Optional<Meme> memeFromDB = this.memeRepository.findByName("meme_name");
        assertEquals(meme, memeFromDB.get());
    }

    @Test
    public void findByName_FindingNoOne() {
        Optional<Meme> memeFromDB = this.memeRepository.findByName("not a meme");
        assertEquals(Optional.empty(), memeFromDB);
    }

    @Test
    public void findByName_WhenNameIsNull_CassandraInvalidQueryException() {
        assertThrows(CassandraInvalidQueryException.class, () -> {
            this.memeRepository.findByName(null);
        }, "findByName should throw CassandraInvalidQueryException for null name");
    }

    @Test
    void findAllByCategoryName_HappyPath() {
        Meme meme1 = createMemeMethod(new MemeDTO("meme_name", "url", "cat", "creator"));
        Meme meme2 = createMemeMethod(new MemeDTO("meme_name2", "url2", "cat", "creator"));
        Meme meme3 = createMemeMethod(new MemeDTO("meme_name3", "url3", "cat2", "creator3"));
        Meme meme4 = createMemeMethod(new MemeDTO("meme_name4", "url4", "cat2", "creator4"));
        Meme meme5 = createMemeMethod(new MemeDTO("meme_name5", "url5", "cat3", "creator"));

        List<Meme> memes_cat = (List<Meme>) this.memeRepository.findAllByCategory("cat");
        List<Meme> memes_cat2 = (List<Meme>) this.memeRepository.findAllByCategory("cat2");
        List<Meme> memes_cat3 = (List<Meme>) this.memeRepository.findAllByCategory("cat3");

        // cat
        assertEquals(2, memes_cat.size());
        if (memes_cat.get(0).getName().equals(meme1.getName())) {
            assertEquals(meme1, memes_cat.get(0));
            assertEquals(meme2, memes_cat.get(1));
        } else {
            assertEquals(meme1, memes_cat.get(1));
            assertEquals(meme2, memes_cat.get(0));
        }
        System.out.println("memes_cat: ");
        memes_cat.forEach(System.out::println);

        // cat 2
        assertEquals(2, memes_cat2.size());
        if (memes_cat2.get(0).getName().equals(meme3.getName())) {
            assertEquals(meme3, memes_cat2.get(0));
            assertEquals(meme4, memes_cat2.get(1));
        } else {
            assertEquals(meme3, memes_cat2.get(1));
            assertEquals(meme4, memes_cat2.get(0));
        }
        System.out.println("memes_cat2: ");
        memes_cat2.forEach(System.out::println);

        // cat 3
        assertEquals(1,memes_cat3.size());
        assertEquals(meme5, memes_cat3.get(0));
        System.out.println("memes_cat3: ");
        memes_cat3.forEach(System.out::println);
    }


    @Test
    void findAllByCreatedBy_HappyPath() {
        Meme meme1 = createMemeMethod(new MemeDTO("meme_name", "url", "cat", "creator"));
        Meme meme2 = createMemeMethod(new MemeDTO("meme_name2", "url2", "cat", "creator"));
        Meme meme3 = createMemeMethod(new MemeDTO("meme_name3", "url3", "cat2", "creator2"));
        Meme meme4 = createMemeMethod(new MemeDTO("meme_name4", "url4", "cat2", "creator3"));
        Meme meme5 = createMemeMethod(new MemeDTO("meme_name5", "url5", "cat3", "creator"));

        List<Meme> memes_createdBy_creator = (List<Meme>) this.memeRepository.findAllByCreatedBy("creator");
        List<Meme> memes_createdBy_creator2 = (List<Meme>) this.memeRepository.findAllByCreatedBy("creator2");
        List<Meme> memes_createdBy_creator3 = (List<Meme>) this.memeRepository.findAllByCreatedBy("creator3");

        // creator
        assertEquals(3, memes_createdBy_creator.size());
        for (Meme meme : memes_createdBy_creator) {
            if (meme.getName().equals(meme1.getName())) {
                assertEquals(meme1, meme);
            } else if (meme.getName().equals(meme2.getName())) {
                assertEquals(meme2, meme);
            } else if (meme.getName().equals(meme5.getName())) {
                assertEquals(meme5, meme);
            } else {
                fail("unexpected meme on list");
            }
        }
//        System.out.println("creator: ");
//        memes_createdBy_creator.forEach(System.out::println);

        // creator2
        assertEquals(1, memes_createdBy_creator2.size());
        assertEquals(meme3, memes_createdBy_creator2.get(0));
//        System.out.println("creator2: ");
//        memes_createdBy_creator2.forEach(System.out::println);

        // creator3
        assertEquals(1, memes_createdBy_creator3.size());
        assertEquals(meme4, memes_createdBy_creator3.get(0));
//        System.out.println("creator3: ");
//        memes_createdBy_creator3.forEach(System.out::println);

    }

    private Meme createMemeMethod(MemeDTO memeDTO) {
        Meme meme = new Meme(memeDTO);
        return this.memeRepository.save(meme);
    }
}