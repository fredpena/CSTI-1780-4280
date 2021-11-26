package com.pucmm.csti.ui.product;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import com.pucmm.csti.R;
import com.pucmm.csti.database.AppDataBase;
import com.pucmm.csti.database.ProductDao;
import com.pucmm.csti.databinding.FragmentProductBinding;
import com.pucmm.csti.utils.Constants;
import com.pucmm.csti.utils.UserSession;
import com.pucmm.csti.model.Userr;


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
            bundle.putSerializable(Constants.PRODUCT, null);

            NavHostFragment.findNavController(ProductFragment.this)
                    .navigate(R.id.action_nav_product_to_nav_product_man, bundle);
        });

        if (!this.user.getRol().equals(Userr.ROL.SELLER)) {
            binding.fab.setVisibility(View.INVISIBLE);
        }

        return root;
    }



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