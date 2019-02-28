package org.donntu.android.lab2.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NextWordResponse {
    private int rightAnswerIndex;
    private List<WordResponse> answerVersions = new ArrayList<>();
}
