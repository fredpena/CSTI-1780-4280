package com.pucmm.csti.demo.listener;

import android.view.View;

public interface OptionsMenuListener<T> {

    void onCreateOptionsMenu(View view, T element, int position);
}
