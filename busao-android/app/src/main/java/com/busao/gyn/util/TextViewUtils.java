package com.busao.gyn.util;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by cezar.carneiro on 15/08/2017.
 */

public class TextViewUtils {

    public static void highlight(@NonNull String text, @NonNull String span, @NonNull TextView textView){
        if (StringUtils.isEmpty(span)) {
            textView.setText(text);
            return;
        }
        int start = text.toLowerCase().indexOf(span.toLowerCase());
        int end = start + span.length();

        highlight(text, start, end, textView);
    }

    public static void highlight(@NonNull String text, int start, int end, @NonNull TextView textView){
        if (start < 0) {
            textView.setText(text);
            return;
        }

        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new BackgroundColorSpan(0xFFFF9100), start, end, 0);
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, 0);
        textView.setText(spannable);
    }

}
