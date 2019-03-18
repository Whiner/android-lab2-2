package org.donntu.android.lab2.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;
import static android.support.v4.content.ContextCompat.startActivity;

public class SpeechService {
    private TextToSpeech textToSpeech;
    private Context context;

    public SpeechService(Context context) {
        textToSpeech = new TextToSpeech(context, status -> Log.d("tss_debug", String.valueOf(status)));
        this.context = context;
    }

    public void speech(String text, Locale locale) {
        textToSpeech.setLanguage(locale);
        textToSpeech.speak(text, QUEUE_FLUSH, null, String.valueOf(text.hashCode()));
    }

    public void destroy() {
        textToSpeech.shutdown();
    }
}
