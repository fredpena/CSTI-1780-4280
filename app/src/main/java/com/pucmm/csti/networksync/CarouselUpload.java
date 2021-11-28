package com.pucmm.csti.networksync;

import android.net.Uri;
import com.pucmm.csti.model.Carousel;


public class CarouselUpload {
    public Uri uri;
    public Carousel carousel;

    public CarouselUpload(Uri uri, Carousel carousel) {
        this.uri = uri;
        this.carousel = carousel;
    }
}
