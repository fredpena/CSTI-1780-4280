package com.pucmm.csti.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import com.pucmm.csti.R;
import com.pucmm.csti.listener.VoidListener;
import com.pucmm.csti.networksync.FirebaseNetwork;
import com.pucmm.csti.networksync.NetResponse;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommonUtil {

    private static final String TAG = "CommonUtil";


    public static void popupMenu(@NonNull Context context, @NonNull View view, @NonNull VoidListener manager, @NonNull VoidListener delete) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.inflate(R.menu.action_menu);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_manager:
                    manager.accept();
                    return true;
                case R.id.action_delete:
                    delete.accept();
                    return true;
                default:
                    return false;
            }
        });
        popup.show();

    }

    public static void photoOptions(@NonNull Context context, @NonNull Function<PhotoOptions, Consumer<Intent>> consumer) {
        final CharSequence[] options = SystemProperties.getOptions();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your category picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals(PhotoOptions.TAKE_PHOTO.getValue())) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                consumer.apply(PhotoOptions.TAKE_PHOTO).accept(takePicture);

            } else if (options[item].equals(PhotoOptions.CHOOSE_GALLERY.getValue())) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                consumer.apply(PhotoOptions.CHOOSE_GALLERY).accept(pickPhoto);

            } else if (options[item].equals(PhotoOptions.CHOOSE_FOLDER.getValue())) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                consumer.apply(PhotoOptions.CHOOSE_FOLDER).accept(Intent.createChooser(intent, "Select Picture"));

            } else if (options[item].equals(PhotoOptions.CANCEL.getValue())) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void alertDialog(@NonNull Context context, @NonNull String title, @NonNull String message, @NonNull VoidListener consumer) {
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> consumer.accept())
                .setNegativeButton("No", (dialog, which) -> dialog.cancel()).show();

    }

    public static void downloadImage(@NonNull String photo, @NonNull ImageView imageView) {
        if (photo != null && !photo.isEmpty()) {
            FirebaseNetwork.obtain().download(photo, new NetResponse<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    imageView.setImageBitmap(response);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }
    }

    public static Uri getImageUri(@NonNull Context context, @NonNull Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null);
        return Uri.parse(path);
    }
}
