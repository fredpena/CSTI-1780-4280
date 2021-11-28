package com.pucmm.csti.networksync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import com.google.firebase.storage.*;
import com.pucmm.csti.model.Carousel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class FirebaseNetwork {

    private static final String TAG = "FirebaseNetwork";

    private static final String PATH_UPLOAD = "test/";
    private static final String URL_DOWNLOAD = String.format("gs://e-commerce-addaa.appspot.com/%s", PATH_UPLOAD);
    private static final long ONE_MEGABYTE = 1024 * 1024;

    private static FirebaseNetwork sInstance;

    public static FirebaseNetwork obtain() {
        if (sInstance == null) {
            sInstance = new FirebaseNetwork();
        }
        return sInstance;
    }

    public FirebaseStorage getStorage() {
        return FirebaseStorage.getInstance();
    }

    public StorageReference getStorageReference() {
        return getStorage().getReference();
    }

    public void delete(final String key, final NetResponse<String> response) {
        final StorageReference reference = getStorageReference().child(PATH_UPLOAD + key);
        reference.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.i(TAG, "delete:onSuccess");
                    response.onResponse("Successfully deleted on Firebase");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "delete:onFailure");
                    response.onFailure(e);
                })
                .addOnCompleteListener(task -> Log.i(TAG, "delete:onComplete"))
                .addOnCanceledListener(() -> Log.i(TAG, "delete:onCanceled"));
    }


    public void upload(final Uri uri, final String key, final NetResponse<String> response) {
        final StorageReference reference = getStorageReference().child(PATH_UPLOAD + key);

        reference.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.i(TAG, "upload:onSuccess");
                    taskSnapshot.getUploadSessionUri();
                    response.onResponse(key);
                }).addOnCanceledListener(() -> Log.i(TAG, "upload:onCanceled"))
                .addOnCompleteListener(task -> Log.i(TAG, "upload:onComplete"))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "upload:onFailure");
                    response.onFailure(e);
                })
                .addOnPausedListener(taskSnapshot -> Log.i(TAG, "upload:onPaused"))
                .addOnProgressListener(taskSnapshot -> Log.i(TAG, "upload:onProgress"));
    }


    public void downloads(final List<Carousel> carousels, final NetResponse<List<Bitmap>> response) {
        AtomicInteger atomic = new AtomicInteger(carousels.size());
        List<Bitmap> bitmaps = new ArrayList<>();
        for (Carousel carousel : carousels) {
            final StorageReference reference = getStorage().getReferenceFromUrl(URL_DOWNLOAD + carousel.getPhoto());

            reference.getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(bytes -> {
                        Log.i(TAG, "download:onSuccess");
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        bitmaps.add(bitmap);
                        if (atomic.decrementAndGet() == 0) {
                            response.onResponse(bitmaps);
                        }
                    })
                    .addOnCanceledListener(() -> Log.i(TAG, "download:onCanceled"))
                    .addOnCompleteListener(task -> Log.i(TAG, "download:onComplete"))
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "download:onFailure");
                        response.onFailure(e);
                    });
        }
    }

    public void uploads(final List<CarouselUpload> uploads, final NetResponse<Void> response) {
        AtomicInteger atomic = new AtomicInteger(uploads.size());
        for (CarouselUpload upload : uploads) {
            final StorageReference reference = getStorageReference().child(PATH_UPLOAD + upload.carousel.getPhoto());
            reference.putFile(upload.uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.i(TAG, "upload:onSuccess");
                        taskSnapshot.getUploadSessionUri();
                        if (atomic.decrementAndGet() == 0) {
                            response.onResponse(null);
                        }
                    }).addOnCanceledListener(() -> Log.i(TAG, "upload:onCanceled"))
                    .addOnCompleteListener(task -> Log.i(TAG, "upload:onComplete"))
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "upload:onFailure");
                        response.onFailure(e);
                    })
                    .addOnPausedListener(taskSnapshot -> Log.i(TAG, "upload:onPaused"))
                    .addOnProgressListener(taskSnapshot -> Log.i(TAG, "upload:onProgress"));
        }
    }

    public void download(final String key, final NetResponse<Bitmap> response) {
        final StorageReference reference = getStorage().getReferenceFromUrl(URL_DOWNLOAD + key);

        reference.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    Log.i(TAG, "download:onSuccess");
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    response.onResponse(bitmap);
                })
                .addOnCanceledListener(() -> Log.i(TAG, "download:onCanceled"))
                .addOnCompleteListener(task -> Log.i(TAG, "download:onComplete"))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "download:onFailure");
                    response.onFailure(e);
                });
    }
}
