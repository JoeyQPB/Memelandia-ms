package com.joey.memememelandia.Utils;

import com.joey.memememelandia.domain.Meme;
import com.joey.memememelandia.records.MemeDTO;
import com.joey.memememelandia.records.MemeDtoRequest;
import com.joey.memememelandia.records.OnlyMemeDTO;

public class MemeUtils {

    public static Boolean isThereAnyMemeDTOFieldEmpty(MemeDtoRequest memeDtoRequest) {
        if (memeDtoRequest.name() == null || memeDtoRequest.name().isEmpty()) return true;
        if (memeDtoRequest.memeUrl() == null || memeDtoRequest.memeUrl().isEmpty()) return true;
        if (memeDtoRequest.category() == null || memeDtoRequest.category().isEmpty()) return true;
        if (memeDtoRequest.creatorEmail() == null || memeDtoRequest.creatorEmail().isEmpty()) return true;

        return false;
    }

    public static boolean isThereAnyOnlyMemeDTOFieldEmpty(OnlyMemeDTO onlyMemeDTO) {
        if (onlyMemeDTO.name() == null || onlyMemeDTO.name().isEmpty()) return true;
        if (onlyMemeDTO.memeUrl() == null || onlyMemeDTO.memeUrl().isEmpty()) return true;

        return false;
    }

    public static boolean isAllOnlyMemeDTOFieldEmpty(OnlyMemeDTO onlyMemeDTO) {
        return (onlyMemeDTO.name() == null || onlyMemeDTO.name().isEmpty()) &&
                (onlyMemeDTO.memeUrl() == null || onlyMemeDTO.memeUrl().isEmpty());
    }

    public static void updateMeme(Meme memeFromDb, OnlyMemeDTO onlyMemeDTO) {
        if (!onlyMemeDTO.name().isEmpty()) memeFromDb.setName(onlyMemeDTO.name());
        if (!onlyMemeDTO.memeUrl().isEmpty()) memeFromDb.setMemeUrl(onlyMemeDTO.memeUrl());
        memeFromDb.markUpdated();
    }
}
