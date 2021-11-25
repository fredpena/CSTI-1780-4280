package com.pucmm.csti.demo.ui.category;

import android.app.Activity;
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
import com.pucmm.csti.demo.database.AppDataBase;
import com.pucmm.csti.demo.database.AppExecutors;
import com.pucmm.csti.demo.database.CategoryDao;
import com.pucmm.csti.demo.model.Category;
import com.pucmm.csti.demo.model.Userr;
import com.pucmm.csti.demo.networksync.FirebaseNetwork;
import com.pucmm.csti.demo.networksync.NetResponse;
import com.pucmm.csti.demo.utils.*;
import com.pucmm.csti.roomviewmodel.database.PersonDao;
import com.pucmm.csti.roomviewmodel.model.Person;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


public class CategoryFragmentManager extends Fragment {

    private static final String TAG = "CategoryFragmentManager";
    private static final int PICK_IMAGE = 1;
    private static final int CHOOSE_GALLERY = 2;
    private static final int TAKE_PHOTO = 3;

    private FragmentCategoryManagerBinding binding;
    private Category element;

    private Uri uri;

    //to get user session data
    private UserSession session;
    private Userr user;
    private CategoryDao categoryDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryDao = AppDataBase.getInstance(getContext()).categoryDao();

        System.out.println("categoryDao");
        categoryDao.findAll().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> people) {
                people.forEach(System.out::println);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        if (element != null) {
            binding.name.setText(element.getName());
            binding.active.setChecked(element.isActive());
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

        binding.save.setOnClickListener(v -> {
            if (ValidUtil.isEmpty(getContext(), binding.name)) {
                return;
            }
            createOrUpdate();
        });

        binding.back.setOnClickListener(v -> NavHostFragment.findNavController(CategoryFragmentManager.this)
                .navigate(R.id.action_nav_category_man_to_nav_category));

        binding.avatar.setOnClickListener(v -> photoOptions());
    }


    private void createOrUpdate() {
        if (element == null) {
            element = new Category();
        }

        element.setName(binding.name.getText().toString());
        element.setActive(binding.active.isChecked());

        final KProgressHUD progressDialog = new KProgressHUDUtils(getActivity()).showConnecting();

        AppExecutors.getInstance().diskIO().execute(() -> {
            progressDialog.dismiss();
            categoryDao.insert(element);

            System.out.println("element: " + element.toString());
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void photoOptions() {
        final CharSequence[] options = SystemProperties.getOptions();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    private void retrieveSession() {
        //create new session object by passing application context
        session = new UserSession(getContext());
        //get User details if logged in
        user = session.getUserSession();
    }
}