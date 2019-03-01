package com.h2.fitness.h2fitness.appTranslate;

import android.os.Handler;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class ApiTranslate {

    private static final String API_KEY = "MY_API_KEY";

    final Handler textViewHandler = new Handler();

    private void translate(String textToTranslate, String targetLanguage, TranslateCallback callback) {
        try {
            TranslateOptions options = TranslateOptions.newBuilder()
                    .setApiKey("AIzaSyBoIR0Jruha8SuqkZQNCJgV4Blbj8dRiBE")
                    .build();
            Translate trService = options.getService();
            Translation translation = trService.translate(textToTranslate, Translate.TranslateOption.targetLanguage(targetLanguage));
            callback.onSuccess(translation.getTranslatedText());
        } catch (Exception e) {
            callback.onFailure();
        }
    }

}
