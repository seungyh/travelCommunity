package com.travel.community.pages.account.service;

import java.security.SecureRandom;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.travel.community.pages.account.entity.NickNameWordEntity;
import com.travel.community.pages.account.enums.NickNamePart;
import com.travel.community.pages.account.repository.NickNameRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NickNameCreator {

    private final NickNameRepository repository;
    private final Random random = new Random();

    // 메모리에 닉네임 생성 단어 적재(요청 올때마다 꺼내서 닉네임 생성)
    private Map<NickNamePart, List<String>> allWords = new EnumMap<>(NickNamePart.class);

    @PostConstruct
    public void loadNickNameWords() {
        List<NickNameWordEntity> nickNameWordList = repository.findByIsDelFalse();

        allWords = nickNameWordList.stream()
                // part별로 map 생성
                // enum을 key로 사용하므로 EnumMap으로 그룹화
                .collect(Collectors.groupingBy(NickNameWordEntity::getPart, () -> new EnumMap<>(NickNamePart.class),
                        Collectors.mapping(NickNameWordEntity::getWord, Collectors.toList())));
    }

    /**
     * 닉네임 생성 단어 최신화
     */
    public void refreshNickNameWords() {
        loadNickNameWords();
    }

    /**
     * 랜덤 닉네임 생성하여 반환
     * 
     * @return
     */
    public String getNickName() {
        String first = getRandomWord(allWords.get(NickNamePart.FIRST));
        String second = getRandomWord(allWords.get(NickNamePart.SECOND));
        String third = getRandomWord(allWords.get(NickNamePart.THIRD));

        return String.join(" ", first, second, third);
    }

    /**
     * list에서 랜덤 단어를 반환
     * 
     * @param words
     * @return
     */
    private String getRandomWord(List<String> words) {

        int randomIdx = random.nextInt(words.size());
        return words.get(randomIdx);
    }

}
