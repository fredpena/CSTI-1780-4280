package com.pucmm.csti.listener;

import android.view.View;

public interface OptionsMenuListener<T> {

    void onCreateOptionsMenu(View view, T element);
}
