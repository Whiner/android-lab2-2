package org.donntu.android.lab2.dto;

import org.donntu.android.lab2.service.GameService;
import org.donntu.android.lab2.service.WordService;

import lombok.Getter;

@Getter
public enum TranslationType {
    RUS_TO_ENG(GameService.RUS_TO_ENG), ENG_TO_RUS(GameService.ENG_TO_RUS);

    private String value;
    TranslationType(String value) {
        this.value = value;
    }
}
