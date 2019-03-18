package org.donntu.android.lab2.service;

import org.donntu.android.lab2.dto.NextWordResponse;
import org.donntu.android.lab2.dto.TranslationType;
import org.donntu.android.lab2.dto.WordResponse;
import org.donntu.android.lab2.dto.WordWithAnswerVariants;
import org.donntu.android.lab2.dto.WordWithTranslation;

import java.util.List;
import java.util.stream.Collectors;

public class WordService {
    private final RequestService requestService = new RequestService();

    public WordWithAnswerVariants nextWord(TranslationType type, int answersVersionsCount) throws Exception {
        NextWordResponse nextWordResponse = requestService.getNextWord(answersVersionsCount);

        WordWithAnswerVariants word = new WordWithAnswerVariants();

        List<WordResponse> answerVersions = nextWordResponse.getAnswerVersions();

        WordResponse rightAnswer = answerVersions
                .get(nextWordResponse.getRightAnswerIndex());

        word.setWordId(rightAnswer.getId());

        switch (type) {
            case RUS_TO_ENG:
                word.setWord(rightAnswer.getRussian());
                word.setAnswerIndex(nextWordResponse.getRightAnswerIndex());
                for (WordResponse wordResponse : nextWordResponse.getAnswerVersions()) {
                    word.getAnswerVersions().add(wordResponse.getEnglish());
                }
                break;
            case ENG_TO_RUS:
                word.setWord(rightAnswer.getEnglish());
                for (WordResponse wordResponse : nextWordResponse.getAnswerVersions()) {
                    word.getAnswerVersions().add(wordResponse.getRussian());
                }
                break;
        }
        word.setAnswerIndex(nextWordResponse.getRightAnswerIndex());
        return word;
    }

    public void processRightAnswer(Long wordId) {
        requestService.incRightAnswer(wordId);
    }


    /*private int countWordsInArchive() {

    }*/

    public int getAvailableWordsCount() throws InterruptedException {
        return requestService.getAvailableWordsCount();
    }

    public void addAll(List<WordWithTranslation> words) {
        requestService.add(words);
    }

    public List<WordWithTranslation> getAll() throws InterruptedException {
        return requestService.getAll().
                stream()
                .map(fullWordInfo ->
                                new WordWithTranslation(
                                        fullWordInfo.getRussianTranslate(),
                                        fullWordInfo.getEnglishTranslate()))
                .collect(Collectors.toList());
    }
}
