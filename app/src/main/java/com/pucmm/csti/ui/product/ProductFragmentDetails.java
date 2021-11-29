package com.pucmm.csti.ui.product;

import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.csti.R;
import com.pucmm.csti.databinding.FragmentProductDetailsBinding;
import com.pucmm.csti.model.relationships.ProductWithCarousel;
import com.pucmm.csti.networksync.FirebaseNetwork;
import com.pucmm.csti.networksync.NetResponse;
import com.pucmm.csti.utils.Constants;
import com.pucmm.csti.utils.FormattedUtil;
import com.pucmm.csti.utils.KProgressHUDUtils;
import com.pucmm.csti.utils.UserSession;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ProductFragmentDetails extends Fragment {

    private static final String TAG = "ProductFragmentDetails";

    private UserSession session;

    private FragmentProductDetailsBinding binding;
    private ProductWithCarousel element;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        element = (ProductWithCarousel) getArguments().getSerializable(Constants.PRODUCT_CAROUSEL);

        binding.qty.setText("1");

        if (element.carousels != null && !element.carousels.isEmpty()) {
            final KProgressHUD progressDialog = new KProgressHUDUtils(getActivity()).showDownload();
            FirebaseNetwork.obtain().downloads(element.carousels, new NetResponse<List<Bitmap>>() {
                @Override
                public void onResponse(List<Bitmap> response) {
                    ArrayList<Drawable> drawables = new ArrayList<>();
                    for (Bitmap bitmap : response) {
                        drawables.add(new BitmapDrawable(getContext().getResources(), bitmap));
                    }
                    carouselView.accept(drawables);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    progressDialog.dismiss();
                }
            });
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        session = new UserSession(getContext());

        binding.itemName.setText(element.product.getItemName());
        binding.price.setText(FormattedUtil.getDecimalValue(element.product.getPrice()));

        binding.remove.setOnClickListener(v -> {
            if ((Integer.valueOf(binding.qty.getText().toString())) > 1) {
                binding.qty.setText(String.valueOf(Integer.valueOf(binding.qty.getText().toString()) - 1));
            }
        });

        binding.add.setOnClickListener(v -> {
            binding.qty.setText(String.valueOf(Integer.valueOf(binding.qty.getText().toString()) + 1));
        });

        binding.action.setOnClickListener(v -> {

            //session.addToCart(product, Integer.valueOf(qty.getText().toString()));

            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            //getContext().registerReceiver(BroadcastReceiverManager.obtain().getReceiverBadge(), filter);

        });


    }

    private final Consumer<ArrayList<Drawable>> carouselView = new Consumer<ArrayList<Drawable>>() {
        @Override
        public void accept(ArrayList<Drawable> drawables) {
            binding.avatar.setSize(drawables.size());
            binding.avatar.setCarouselViewListener((view1, position) -> {
                ImageView imageView = view1.findViewById(R.id.imageView);
                imageView.setImageDrawable(drawables.get(position));
            });
            binding.avatar.show();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}