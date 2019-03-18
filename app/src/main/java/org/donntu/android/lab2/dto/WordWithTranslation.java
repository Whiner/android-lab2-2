package org.donntu.android.lab2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordWithTranslation {
    private String russian;
    private String english;

    public String toFileForm() {
        return "[" + russian + "]:[" + english + "]";
    }
}