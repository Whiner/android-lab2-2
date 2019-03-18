package org.donntu.android.lab2.service;

import android.widget.TextView;

import org.donntu.android.lab2.dto.TranslationType;
import org.donntu.android.lab2.dto.WordWithAnswerVariants;
import org.donntu.android.lab2.dto.WordWithTranslation;
import org.donntu.android.lab2.exception.NotEnoughWordsException;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import lombok.Getter;

public class GameService {
    public static final String RUS_TO_ENG = "Русский -> Английский";
    public static final String ENG_TO_RUS = "Английский -> Русский";

    private final WordService wordService = new WordService();
    private final FileService fileService = new FileService();

    @Getter
    private TranslationType translationType = TranslationType.RUS_TO_ENG;

    private int answersVersionsCount = 4;

    private int lastRightAnswerTextViewId = -1;
    private long lastRightAnswerWordId = -1;


    public void fillTextViews(TextView main, List<TextView> variants) throws Exception {
        if (variants.size() != answersVersionsCount) {
            throw new Exception("Количество TextView должно быть равно " +
                    "answersVersionsCount (" + answersVersionsCount + ")");
        }

        WordWithAnswerVariants word = wordService.nextWord(translationType, answersVersionsCount);

        this.lastRightAnswerWordId = word.getWordId();
        this.lastRightAnswerTextViewId = variants
                .get(word.getAnswerIndex())
                .getId();
        main.setText(word.getWord());

        List<String> answerVersions = word.getAnswerVersions();

        for (int i = 0; i < answerVersions.size(); i++) {
            variants.get(i).setText(answerVersions.get(i));
        }
    }

    public boolean checkAnswer(TextView textView) {
        if (textView.getId() == lastRightAnswerTextViewId) {
            wordService.processRightAnswer(lastRightAnswerWordId);
            return true;
        }

        return false;
    }

    public String revertLang() {
        switch (translationType) {
            case RUS_TO_ENG:
                this.translationType = TranslationType.ENG_TO_RUS;
                return ENG_TO_RUS;
            case ENG_TO_RUS:
                this.translationType = TranslationType.RUS_TO_ENG;
                return RUS_TO_ENG;
            default:
                return "";
        }
    }


    public void updateAvailableWordsCount(TextView textView) throws InterruptedException {
        textView.setText(String.valueOf(wordService.getAvailableWordsCount()));
    }

    public void checkAvailableWords() throws InterruptedException, NotEnoughWordsException {
        if (wordService.getAvailableWordsCount() < answersVersionsCount) {
            throw new NotEnoughWordsException("Недостаточно слов в базе. Добавьте еще или очистите архив");
        }
    }

    public void importWords(String filename) throws Exception {
        wordService.addAll(fileService.importFromFile(filename));
    }

    public void exportWords(String path, String filename) throws Exception {
        fileService.exportToFile(path, filename, wordService.getAll());
    }

    public void addWord(String russian, String english) {
        wordService.addAll(Collections.singletonList(new WordWithTranslation(russian, english)));
    }

    public void refreshArchive() {
        wordService.refreshArchive();
    }
}
