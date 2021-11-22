package com.pucmm.csti.demo.networksync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.*;
import com.google.firebase.storage.*;

public class FirebaseNetwork {

    private static final String TAG = "FirebaseNetwork";

    private static final String URL_DOWNLOAD = "gs://e-commerce-addaa.appspot.com/images/e_commerce/";
    private static final String PATH_UPLOAD = "test/";
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

    public void delete(final String key, final Response.Listener response, final Response.ErrorListener errorListener) {
        final StorageReference reference = getStorageReference().child(PATH_UPLOAD + key);
        reference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "delete:onSuccess");
                        response.onResponse("Successfully deleted on Firebase");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "delete:onFailure");
                        errorListener.onErrorResponse(e);
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "delete:onComplete");
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.i(TAG, "delete:onCanceled");
                    }
                });
        // imageCache.deleteBitmapFromCache(key);
    }

    public void upload(final Uri uri, final String key, final Response.Listener response, final Response.ErrorListener errorListener) {
        final StorageReference reference = getStorageReference().child(PATH_UPLOAD + key);

        reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "upload:onSuccess");
                        taskSnapshot.getUploadSessionUri();

                        taskSnapshot.getStorage().getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        // imageCache.addBitmapToCache(key, bitmap);
                                    }
                                });

                        response.onResponse(key);
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.i(TAG, "upload:onCanceled");
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Log.i(TAG, "upload:onComplete");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "upload:onFailure");
                        errorListener.onErrorResponse(e);
                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "upload:onPaused");
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "upload:onProgress");
                    }
                });
    }


    public void download(final String key, final Response.Listener response, final Response.ErrorListener errorListener) {

        Log.e(TAG, "Bitmap not in memory");
        final StorageReference reference = getStorage().getReferenceFromUrl(URL_DOWNLOAD + key);

        reference.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.i(TAG, "download:onSuccess");
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        // imageCache.addBitmapToCache(key, bitmap);
                        response.onResponse(bitmap);
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.i(TAG, "download:onCanceled");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        Log.i(TAG, "download:onComplete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "download:onFailure");
                        errorListener.onErrorResponse(e);
                    }
                });
    }
}
