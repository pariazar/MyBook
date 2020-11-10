package com.example.ketabeman21.Utils;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.regex.Pattern;

public class UsernameValidator implements TextWatcher {
    /**
     * Email validation pattern.
     */
    public static final Pattern Username_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_]{5,30}"
    );
    private boolean mIsValid;
    public boolean isValid() {
        return mIsValid;
    }

    public static boolean isValidUsername(CharSequence username) {
        return username != null && Username_PATTERN.matcher(username).matches();
    }
    @Override
    final public void afterTextChanged(Editable editableText) {
        mIsValid = isValidUsername(editableText);
    }
    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {/*No-op*/}
    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {/*No-op*/}
}