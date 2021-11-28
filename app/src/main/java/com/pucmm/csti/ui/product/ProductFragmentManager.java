package com.pucmm.csti.ui.product;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.csti.R;
import com.pucmm.csti.database.AppDataBase;
import com.pucmm.csti.database.AppExecutors;
import com.pucmm.csti.database.CategoryDao;
import com.pucmm.csti.database.ProductDao;
import com.pucmm.csti.databinding.FragmentProductManagerBinding;
import com.pucmm.csti.model.Carousel;
import com.pucmm.csti.model.Category;
import com.pucmm.csti.model.Product;
import com.pucmm.csti.model.Userr;
import com.pucmm.csti.model.relationships.ProductWithCarousel;
import com.pucmm.csti.networksync.CarouselUpload;
import com.pucmm.csti.networksync.FirebaseNetwork;
import com.pucmm.csti.networksync.NetResponse;
import com.pucmm.csti.utils.CommonUtil;
import com.pucmm.csti.utils.Constants;
import com.pucmm.csti.utils.KProgressHUDUtils;
import com.pucmm.csti.utils.ValidUtil;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ProductFragmentManager extends Fragment {

    private static final String TAG = "ProductFragmentManager";

    private FragmentProductManagerBinding binding;
    private ArrayList<Uri> arrayUri;
    private int position = 0;
    private ProductWithCarousel element;
    private ProductDao productDao;
    private CategoryDao categoryDao;
    private Category category;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productDao = AppDataBase.getInstance(getContext()).productDao();
        categoryDao = AppDataBase.getInstance(getContext()).categoryDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductManagerBinding.inflate(inflater, container, false);
        binding.active.setChecked(true);
        View root = binding.getRoot();

        element = (ProductWithCarousel) getArguments().getSerializable(Constants.PRODUCT_CAROUSEL);
        Userr user = (Userr) getArguments().getSerializable(Constants.USER);
        arrayUri = new ArrayList<>();

        categoryDao.findAll().observe(this, categories -> {
            final Stream<Category> stream = user.getRol().equals(Userr.ROL.CUSTOMER)
                    ? categories.stream().filter(f -> (f.isActive()))
                    : categories.stream();
            final ArrayAdapter<Category> adapter = new ArrayAdapter<>(getContext(), R.layout.auto_complete_item, stream.collect(Collectors.toList()));
            binding.category.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        if (element != null) {

            binding.itemName.setText(element.product.getItemName());
            binding.price.setText(String.valueOf(element.product.getPrice()));
            binding.active.setChecked(element.product.isActive());

            if (element.carousels != null && !element.carousels.isEmpty()) {
                FirebaseNetwork.obtain().downloads(element.carousels, new NetResponse<List<Bitmap>>() {
                    @Override
                    public void onResponse(List<Bitmap> response) {
                        for (Bitmap bitmap : response) {
                            arrayUri.add(CommonUtil.getImageUri(getContext(), bitmap));
                        }
                        binding.image.setImageURI(arrayUri.get(0));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            }
        }
        binding.image.setFactory(() -> new ImageView(getContext()));

        // click here to select next image
        binding.next.setOnClickListener(v -> {
            if (position < arrayUri.size() - 1) {
                binding.image.setImageURI(arrayUri.get(++position));
            } else {
                FancyToast.makeText(getContext(), "Last Image Already Shown", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        });

        // click here to view previous image
        binding.previous.setOnClickListener(v -> {
            if (position > 0) {
                binding.image.setImageURI(arrayUri.get(--position));
            } else {
                FancyToast.makeText(getContext(), "First Image Already Shown", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
            }
        });

        binding.select.setOnClickListener(v -> photoOptions());


        binding.save.setOnClickListener(v -> {
            if (ValidUtil.isEmpty(getContext(), binding.itemName)) {
                return;
            }

            binding.category.setOnItemClickListener((parent, view1, position, id) -> category = (Category) parent.getItemAtPosition(position));
            binding.category.setError(null);
            if (category == null) {
                binding.category.setError(getContext().getString(R.string.error_field_required));
                binding.category.requestFocus();
                return;
            }

            if (ValidUtil.isEmpty(getContext(), binding.price)) {
                return;
            }

            CommonUtil.alertDialog(getContext(), "Confirm dialog save!",
                    "You are about to save record. Do you really want to proceed?",
                    () -> createOrUpdate());
        });

        binding.back.setOnClickListener(v -> NavHostFragment.findNavController(ProductFragmentManager.this)
                .navigate(R.id.action_nav_product_man_to_nav_product));

    }


    private void createOrUpdate() {
        if (element == null) {
            element = new ProductWithCarousel();
        }

        element.product.setItemName(binding.itemName.getText().toString());
        element.product.setCategory(category.getUid());
        element.product.setPrice(Double.valueOf(binding.price.getText().toString()));
        element.product.setActive(binding.active.isChecked());

        final KProgressHUD progressDialog = new KProgressHUDUtils(getActivity()).showConnecting();

        AppExecutors.getInstance().diskIO().execute(() -> {
            if (Integer.valueOf(element.product.getItemCode()).equals(0)) {
                long uid = productDao.insert(element.product);
                element.product.setItemCode((int) uid);
            } else {
                productDao.update(element.product);
            }
            List<CarouselUpload> uploads = new ArrayList<>();

            productDao.deleteCarousels(element.product.getItemCode());
            final List<Carousel> carousels = new ArrayList<>();
            for (int index = 0; index < arrayUri.size(); index++) {
                Carousel carousel = new Carousel(element.product.getItemCode(), index, String.format("products/%s/%s.jpg", element.product.getItemCode(), index));
                carousels.add(carousel);
                uploads.add(new CarouselUpload(arrayUri.get(index), carousel));
            }
            productDao.insertCarousels(carousels);

            if (arrayUri != null && !arrayUri.isEmpty() && element.product.getItemCode() != 0) {
                function.apply(uploads).accept(progressDialog);
            } else {
                progressDialog.dismiss();
            }
        });
    }

    private final Function<List<CarouselUpload>, Consumer<KProgressHUD>> function = uploads -> progress -> {
        FirebaseNetwork.obtain().uploads(uploads, new NetResponse<Void>() {
            @Override
            public void onResponse(Void response) {
                FancyToast.makeText(getContext(), "Successfully upload images", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                progress.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                progress.dismiss();
                FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });

    };


    private void photoOptions() {
        // initialising intent
        Intent intent = new Intent();
        // setting type to select to be image
        intent.setType("image/*");
        // allowing multiple image to be selected
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        pickAndChoosePictureResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> pickAndChoosePictureResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        final ClipData clipData = result.getData().getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                // adding imageuri in array
                                final Uri uri = clipData.getItemAt(i).getUri();
                                arrayUri.add(uri);
                            }
                            // setting 1st selected image into image switcher
                        } else {
                            Uri uri = result.getData().getData();
                            arrayUri.add(uri);
                        }
                        binding.image.setImageURI(arrayUri.get(0));
                        position = 0;
                    }
                }
            });


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}