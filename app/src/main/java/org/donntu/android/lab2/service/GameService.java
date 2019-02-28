package org.donntu.android.lab2.service;

import android.widget.TextView;

import org.donntu.android.lab2.dto.TranslationType;
import org.donntu.android.lab2.dto.WordWithAnswerVariants;

import java.util.List;
import java.util.Random;

public class GameService {
    public static final String RUS_TO_ENG = "Русский -> Английский";
    public static final String ENG_TO_RUS = "Английский -> Русский";

    private final WordService wordService = new WordService();
    private final Random random = new Random();

    private TranslationType translationType = TranslationType.RUS_TO_ENG;

    private int answersVersionsCount = 4;

    private int lastRightAnswerTextViewId = -1;
    private long lastRightAnswerWordId = -1;


    public void fillTextViews(TextView main, List<TextView> variants) throws Exception {
        if(variants.size() != answersVersionsCount) {
            throw new Exception("Количество TextView должно быть равно " +
                    "answersVersionsCount (" + answersVersionsCount + ")");
        }

        WordWithAnswerVariants word = wordService.nextWord(translationType, answersVersionsCount);

        this.lastRightAnswerWordId = word.getWordId();
        main.setText(word.getWord());

        int rightVariantTextViewIndex = random.nextInt(answersVersionsCount);

        List<String> answerVersions = word.getAnswerVersions();

        for (int i = 0, j = 0; i < answerVersions.size(); i++) {
            if(i == rightVariantTextViewIndex) {
                variants.get(i).setText(word.getWordTranslation());
            } else {
                variants.get(i).setText(answerVersions.get(j));
                j++;
            }
        }
    }

    public boolean checkAnswer(TextView textView) {
        if(textView.getId() == lastRightAnswerTextViewId) {
            wordService.processRightAnswer(lastRightAnswerWordId);
            return true;
        }

        return false;
    }
}
