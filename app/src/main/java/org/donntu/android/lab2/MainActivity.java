package org.donntu.android.lab2;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.donntu.android.lab2.dto.TranslationType;
import org.donntu.android.lab2.service.FileService;
import org.donntu.android.lab2.service.GameService;
import org.donntu.android.lab2.service.SpeechService;
import org.donntu.android.lab2.utils.CallBackHandler;
import org.donntu.android.lab2.utils.LambdaWrapper;
import org.donntu.android.lab2.utils.MyTimerTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import static org.donntu.android.lab2.utils.LambdaWrapper.lambdaWrapper;
//TODO: допилить сброс архива. протестить все остальное. а то поляков доебет потом
public class MainActivity extends AppCompatActivity {
    private String EXPORT_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String EXPORT_FILE_NAME = "words.txt";

    private SpeechService speechService;
    private Timer timer = new Timer();

    private Menu optionsMenu;

    private List<TextView> textViews = new ArrayList<>();
    private TextView answerTextView;

    private FileService fileService = new FileService();
    private GameService gameService = new GameService();
    private Locale ru = new Locale("ru");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        speechService = new SpeechService(this);
        LambdaWrapper.setExceptionHandler(this::showExceptionDialog);
        CallBackHandler.setExceptionHandler(this::showExceptionDialog);
        initMainLayout();
    }

    private void initMainLayout() {
        setDisableOptionsMenu(false);
        updateAvailableWordsCount();
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(event -> initGameLayout());

        Button addNewWordButton = findViewById(R.id.addNewWordButton);
        addNewWordButton.setOnClickListener(v -> initNewWordLayout());

        Button changeLangButton = findViewById(R.id.changeLangButton);
        TextView langTextBox = findViewById(R.id.langTextbox);
        langTextBox.setText(gameService.getTranslationType().getValue());
        changeLangButton.setOnClickListener(
                event -> langTextBox.setText(gameService.revertLang())
        );
    }

    private void initGameLayout() {
        if (isAvailableWordsExist()) {
            setDisableOptionsMenu(true);
            setContentView(R.layout.game);
            nextWord();

            setLayoutListener(R.id.word1_layout);
            setLayoutListener(R.id.word2_layout);
            setLayoutListener(R.id.word3_layout);
            setLayoutListener(R.id.word4_layout);


            Button playButton = findViewById(R.id.playWord);
            playButton.setOnClickListener(v -> {
                TextView word = findViewById(R.id.word);
                listenWord(word.getText().toString());
            });

            Button stopButton = findViewById(R.id.stopButton);
            stopButton.setOnClickListener(v -> setMainLayout());
        }
    }

    private void initNewWordLayout() {
        setDisableOptionsMenu(true);
        setContentView(R.layout.add);
        Button addButton = findViewById(R.id.addButton);
        TextView russian = findViewById(R.id.russianWord);
        TextView english = findViewById(R.id.englishWord);
        addButton.setOnClickListener(v1 -> {
            addWord(russian.getText().toString(), english.getText().toString());
            russian.setText("");
            english.setText("");
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v1 -> {
            russian.setText("");
            english.setText("");
            setMainLayout();
        });
    }

    private void nextWord() {
        try {
            textViews.clear();
            Collections.addAll(
                    textViews,
                    findViewById(R.id.word1),
                    findViewById(R.id.word2),
                    findViewById(R.id.word3),
                    findViewById(R.id.word4)
            );

            answerTextView = findViewById(R.id.word);

            gameService.fillTextViews(answerTextView, textViews);
        } catch (Exception e) {
            Log.e("fill-error", e.getMessage());
            setMainLayout();
        }
    }

    private void setMainLayout() {
        setContentView(R.layout.main);
        initMainLayout();
    }

    private void setDisableOptionsMenu(boolean disable) {
        if(optionsMenu != null) {
            optionsMenu.getItem(0).setEnabled(!disable);
            optionsMenu.getItem(1).setEnabled(!disable);
            optionsMenu.getItem(2).setEnabled(!disable);
        }
    }

    private void addWord(String russian, String english) {
        gameService.addWord(russian, english);
    }

    private void listenWord(String text) {
        if (gameService.getTranslationType() == TranslationType.RUS_TO_ENG) {
            speechService.speech(text, ru);
        } else {
            speechService.speech(text, Locale.ENGLISH);
        }
    }

    private boolean isAvailableWordsExist() {
        try {
            gameService.checkAvailableWords();
            return true;
        } catch (Exception e) {
            showExceptionDialog(e.getMessage());
            return false;
        }
    }

    private void setLayoutListener(int layout) {
        LinearLayout word_layout = findViewById(layout);
        word_layout.setOnClickListener(event -> {
            boolean right = gameService.checkAnswer(
                    (TextView) word_layout.getChildAt(0)
            );
            showResult(right);
            nextWord();
        });
    }

    private void showExceptionDialog(String message) {
        this.runOnUiThread(() -> new AlertDialog.Builder(this)
                .setMessage(message)
                .setNeutralButton("Хорошо", (dialog, which) -> this.setMainLayout())
                .create()
                .show()
        );

    }

    private void updateAvailableWordsCount() {
        TextView wordsCount = findViewById(R.id.availableWordsCountTextView);
        try {
            gameService.updateAvailableWordsCount(wordsCount);
        } catch (InterruptedException e) {
            showExceptionDialog(e.getMessage());
        }
    }

    private void showMessage(String text, int color) {
        TextView message = findViewById(R.id.message);
        if (message != null) {
            MyTimerTask myTimerTask = new MyTimerTask(message, this);
            timer.schedule(myTimerTask, 1000);
            message.setText(text);
            message.setTextColor(color);
            message.setVisibility(View.VISIBLE);
        }
    }

    private void showResult(boolean success) {
        if (success) {
            showMessage("Правильно!", Color.GREEN);
        } else {
            showMessage("Не то(", Color.RED);
        }
    }

    public void importWords(MenuItem item) {
        fileService.openFileDialog(this,
                    lambdaWrapper(fileName -> {
                        try {
                            gameService.importWords(fileName.toString());
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }));
        updateAvailableWordsCount();
    }

    public void exportWords(MenuItem item) {
        try {
            gameService.exportWords(EXPORT_FILE_PATH, EXPORT_FILE_NAME);
        } catch (Exception e) {
            showExceptionDialog(e.getMessage());
        }
    }

    public void refreshArchive(MenuItem item) {
        gameService.refreshArchive();
        updateAvailableWordsCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        optionsMenu = menu;
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechService.destroy();
    }

}
