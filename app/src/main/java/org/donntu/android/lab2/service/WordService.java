package org.donntu.android.lab2.service;

import org.donntu.android.lab2.dto.NextWordResponse;
import org.donntu.android.lab2.dto.TranslationType;
import org.donntu.android.lab2.dto.WordResponse;
import org.donntu.android.lab2.dto.WordWithAnswerVariants;

import java.io.IOException;
import java.util.List;

public class WordService {
    private final RequestService requestService = new RequestService();

    public WordWithAnswerVariants nextWord(TranslationType type, int answersVersionsCount) throws InterruptedException {
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


    //TODO: реализовать эти методы со стороны сервера и потом клиента
    /*private int countWordsInArchive() {

    }

    public int getAvailableWordsCount() {

    }*/

}
