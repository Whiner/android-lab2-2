package org.donntu.android.lab2.service;

import android.content.Context;

import org.donntu.android.lab2.dto.FullWordInfo;
import org.donntu.android.lab2.dto.WordWithAnswerVariants;
import org.donntu.android.lab2.dto.WordWithTranslation;
import org.donntu.android.lab2.utils.OpenFileDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileService {
    private final String FILE_REGEX_STRUCT = "\\[(.+)]:\\[(.+)]";
    private final int GROUPS_COUNT = 2;

    public void exportToFile(String path, String filename, List<WordWithTranslation> words) throws Exception {
        File file = new File(path, filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (WordWithTranslation word : words) {
                bufferedWriter.write(word.toFileForm() + "\n");
            }
        }
    }

    public List<WordWithTranslation> importFromFile(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new Exception("Файл " + file.getPath() + " не существует");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            Pattern pattern = Pattern.compile(FILE_REGEX_STRUCT);
            String line;
            List<WordWithTranslation> words = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.groupCount() == GROUPS_COUNT) {
                    if (matcher.find()) {
                        WordWithTranslation word = new WordWithTranslation();
                        word.setRussian(matcher.group(1));
                        word.setEnglish(matcher.group(2));
                        words.add(word);
                    }
                }
            }
            return words;
        }
    }

    public void openFileDialog(Context context, Consumer<Object> listener) {
        OpenFileDialog openFileDialog = new OpenFileDialog(context);
        openFileDialog.setOpenDialogListener(listener::accept).show();
    }
}
