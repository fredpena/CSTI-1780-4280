package com.pucmm.csti.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import com.pucmm.csti.R;

public final class ValidUtil {

    private ValidUtil() {
    }

    public static boolean isEmpty(final Context context, final View... views) {
        for (View view : views) {
            if (view instanceof EditText) {
                final EditText editText = (EditText) view;
                // Reset errors.
                editText.setError(null);
                if (TextUtils.isEmpty(editText.getText())) {
                    editText.setError(context.getString(R.string.error_field_required));
                    editText.requestFocus();
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isPasswordValid(final EditText view, final String pass) {
        if (pass.length() < 4 || pass.length() > 20) {
            view.setError("Password Must consist of 4 to 20 characters");
            return false;
        }
        return true;
    }

    public static boolean isEmailValid(final EditText view, final String email) {
        if (email.length() < 4 || email.length() > 30) {
            view.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!email.matches("^[A-za-z0-9.@]+")) {
            view.setError("Only . and @ characters allowed");
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            view.setError("Email must contain @ and .");
            return false;
        }
        return true;
    }
}
