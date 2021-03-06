package org.donntu.android.lab2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    private String russian;
    private String english;

    public static Word of(WordResponse wordResponse){
        return new Word(wordResponse.getRussian(), wordResponse.getEnglish());
    }
}
