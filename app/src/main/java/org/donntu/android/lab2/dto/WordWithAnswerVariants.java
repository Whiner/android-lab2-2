package org.donntu.android.lab2.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordWithAnswerVariants {
    private long wordId;
    private String word;
    private String wordTranslation;
    private List<String> answerVersions = new ArrayList<>();
}
