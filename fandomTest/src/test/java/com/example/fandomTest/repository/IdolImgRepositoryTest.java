package com.example.fandomTest.repository;

import com.example.fandomTest.entity.Idol;
import com.example.fandomTest.entity.IdolImg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IdolImgRepositoryTest {

    @Autowired
    IdolImgRepository idolImgRepository;
    @Test
    void findByIdolIdTest(){

        Idol idol = Idol.builder()
                .idolID(1L)
                .idolName("NewJeans")
                .idolMainImg("https//www.youtube.com")
                .build();

        List<IdolImg> byIdolId = idolImgRepository.findByIdolId(idol);

        out.println(byIdolId.size());
    }
}