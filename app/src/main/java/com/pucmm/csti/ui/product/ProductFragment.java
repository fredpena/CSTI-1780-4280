package com.pucmm.csti.ui.product;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pucmm.csti.R;
import com.pucmm.csti.database.AppDataBase;
import com.pucmm.csti.database.AppExecutors;
import com.pucmm.csti.database.ProductDao;
import com.pucmm.csti.databinding.FragmentProductBinding;
import com.pucmm.csti.listener.OptionsMenuListener;
import com.pucmm.csti.model.Category;
import com.pucmm.csti.model.Product;
import com.pucmm.csti.model.relationships.ProductWithCarousel;
import com.pucmm.csti.ui.category.CategoryViewModel;
import com.pucmm.csti.ui.ViewModelFactory;
import com.pucmm.csti.utils.CommonUtil;
import com.pucmm.csti.utils.Constants;
import com.pucmm.csti.utils.KProgressHUDUtils;
import com.pucmm.csti.utils.UserSession;
import com.pucmm.csti.model.Userr;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment";

    private FragmentProductBinding binding;
    private ProductDao productDao;
    //to get user session data
    private UserSession session;
    private Userr user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retrieve session values and display
        retrieveSession();

        productDao = AppDataBase.getInstance(getContext()).productDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(v -> {

            final Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.PRODUCT_CAROUSEL, null);
            bundle.putSerializable(Constants.USER, user);

            NavHostFragment.findNavController(ProductFragment.this)
                    .navigate(R.id.action_nav_product_to_nav_product_man, bundle);
        });

        if (!this.user.getRol().equals(Userr.ROL.SELLER)) {
            binding.fab.setVisibility(View.INVISIBLE);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        final ProductAdaptor adapter = new ProductAdaptor(user);
        binding.recyclerView.setAdapter(adapter);

        adapter.setOptionsMenuListener((OptionsMenuListener<ProductWithCarousel>) (view1, element) -> {
            CommonUtil.popupMenu(getContext(), view1, () -> {
                final Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PRODUCT_CAROUSEL, element);
                bundle.putSerializable(Constants.USER, user);

                NavHostFragment.findNavController(ProductFragment.this)
                        .navigate(R.id.action_nav_product_to_nav_product_man, bundle);
            }, () -> {
                CommonUtil.alertDialog(getContext(), "Confirm dialog delete!",
                        "You are about to delete record. Do you really want to proceed?",
                        () -> delete(element));
            });
        });

        ProductViewModel productViewModel = new ViewModelProvider(this, new ViewModelFactory(getContext()))
                .get(ProductViewModel.class);


        productViewModel.getListLiveData().observe(this, elements -> {
            final Stream<ProductWithCarousel> stream = user.getRol().equals(Userr.ROL.CUSTOMER)
                    ? elements.stream().filter(f -> (f.product.isActive()))
                    : elements.stream();

            adapter.setElements(stream.collect(Collectors.toList()));
        });
    }

    private void delete(ProductWithCarousel element) {
        final KProgressHUD progressDialog = new KProgressHUDUtils(getActivity()).showConnecting();
        function.apply(progressDialog).apply(true).accept(element.product);
//
//        if (element.getPhoto() != null && !element.getPhoto().isEmpty()) {
//            FirebaseNetwork.obtain().delete(element.getPhoto(), new NetResponse<String>() {
//                @Override
//                public void onResponse(String response) {
//                    function.apply(progressDialog).apply(true).accept(element);
//                }
//
//                @Override
//                public void onFailure(Throwable t) {
//                    function.apply(progressDialog).apply(false).accept(element);
//                    FancyToast.makeText(getContext(), t.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                }
//            });
//        } else {
//            function.apply(progressDialog).apply(true).accept(element);
//        }

    }

    private final Function<KProgressHUD, Function<Boolean, Consumer<Product>>> function = progress -> success -> element -> {
        if (success) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                productDao.delete(element);
                getActivity().runOnUiThread(() -> FancyToast.makeText(getContext(), "Successfully deleted!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show());
            });
        }
        progress.dismiss();
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void retrieveSession() {
        //create new session object by passing application context
        session = new UserSession(getContext());
        //get User details if logged in
        user = session.getUserSession();
    }
}