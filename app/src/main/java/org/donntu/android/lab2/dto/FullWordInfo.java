package org.donntu.android.lab2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullWordInfo {
    private int id;
    private String russianTranslate;
    private String englishTranslate;
    private boolean isInArchive;
    private int correctAnswersCount = 0;


    public String toFileForm() {
        return "[" + russianTranslate + "]:[" + englishTranslate + "]:[" + isInArchive + "]";
    }
}
