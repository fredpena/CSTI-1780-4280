package com.pucmm.csti.demo.ui.category;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.csti.R;
import com.pucmm.csti.databinding.FragmentCategoryBinding;
import com.pucmm.csti.databinding.FragmentCategoryManagerBinding;
import com.pucmm.csti.demo.activity.RegisterActivity;
import com.pucmm.csti.demo.database.AppDataBase;
import com.pucmm.csti.demo.database.AppExecutors;
import com.pucmm.csti.demo.database.CategoryDao;
import com.pucmm.csti.demo.model.Category;
import com.pucmm.csti.demo.model.Userr;
import com.pucmm.csti.demo.networksync.FirebaseNetwork;
import com.pucmm.csti.demo.networksync.NetResponse;
import com.pucmm.csti.demo.retrofit.UserApiService;
import com.pucmm.csti.demo.utils.*;
import com.pucmm.csti.roomviewmodel.database.PersonDao;
import com.pucmm.csti.roomviewmodel.model.Person;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;


public class CategoryFragmentManager extends Fragment {

    private static final String TAG = "CategoryFragmentManager";

    private FragmentCategoryManagerBinding binding;
    private Uri uri;
    private Category element;
    private CategoryDao categoryDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryDao = AppDataBase.getInstance(getContext()).categoryDao();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryManagerBinding.inflate(inflater, container, false);
        binding.active.setChecked(true);
        View root = binding.getRoot();

        element = (Category) getArguments().getSerializable(ConstantsUtil.CATEGORY);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        if (element != null) {
            binding.name.setText(element.getName());
            binding.active.setChecked(element.isActive());

            if (element.getPhoto() != null && !element.getPhoto().isEmpty()) {
                FirebaseNetwork.obtain().download(element.getPhoto(), new NetResponse<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        binding.avatar.setImageBitmap(response);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
            }

        }

        binding.save.setOnClickListener(v -> {
            if (ValidUtil.isEmpty(getContext(), binding.name)) {
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Confirm dialog save!")
                    .setMessage("You are about to save record. Do you really want to proceed?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            createOrUpdate();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

        });

        binding.back.setOnClickListener(v -> NavHostFragment.findNavController(CategoryFragmentManager.this)
                .navigate(R.id.action_nav_category_man_to_nav_category));

        if (element != null && element.getUid() != 0) {
            binding.avatar.setOnClickListener(v -> photoOptions());
        } else {
            binding.avatar.setOnClickListener(v -> FancyToast.makeText(getContext(), "You must create an object before assigning the photo", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show());
        }
    }


    private void createOrUpdate() {
        if (element == null) {
            element = new Category();
        }

        element.setName(binding.name.getText().toString());
        element.setActive(binding.active.isChecked());

        final KProgressHUD progressDialog = new KProgressHUDUtils(getActivity()).showConnecting();

        AppExecutors.getInstance().diskIO().execute(() -> {
            if (Integer.valueOf(element.getUid()).equals(0)) {
                categoryDao.insert(element);
            } else {
                categoryDao.update(element);
            }
            consumer.accept(progressDialog);
        });
    }

    private final Consumer<KProgressHUD> consumer = new Consumer<KProgressHUD>() {
        @Override
        public void accept(KProgressHUD progressDialog) {
            if (element.getUid() != 0 && uri != null) {
                FirebaseNetwork.obtain().upload(uri, String.format("category/%s.jpg", element.getUid()),
                        new NetResponse<String>() {
                            @Override
                            public void onResponse(String response) {
                                FancyToast.makeText(getContext(), "Successfully upload image", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                element.setPhoto(response);
                                AppExecutors.getInstance().diskIO().execute(() -> {
                                    categoryDao.update(element);
                                });
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                progressDialog.dismiss();
                                FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                        });
            } else {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void photoOptions() {
        final CharSequence[] options = SystemProperties.getOptions();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose your category picture");

        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals(PhotoOptions.TAKE_PHOTO.getValue())) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureResultLauncher.launch(takePicture);

            } else if (options[item].equals(PhotoOptions.CHOOSE_GALLERY.getValue())) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickAndChoosePictureResultLauncher.launch(pickPhoto);

            } else if (options[item].equals(PhotoOptions.CHOOSE_FOLDER.getValue())) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                pickAndChoosePictureResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));

            } else if (options[item].equals(PhotoOptions.CANCEL.getValue())) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private ActivityResultLauncher<Intent> pickAndChoosePictureResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            uri = result.getData().getData();
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            binding.avatar.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private ActivityResultLauncher<Intent> takePictureResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        uri = result.getData().getData();
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        binding.avatar.setImageBitmap(bitmap);

                    }
                }
            });


}