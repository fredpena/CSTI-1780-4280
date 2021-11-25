package com.pucmm.csti.demo.ui.category;

import android.os.Bundle;
import android.widget.TextView;
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
import com.pucmm.csti.R;
import com.pucmm.csti.databinding.FragmentCategoryBinding;
import com.pucmm.csti.databinding.FragmentHomeBinding;
import com.pucmm.csti.demo.model.Userr;
import com.pucmm.csti.demo.ui.home.HomeViewModel;
import com.pucmm.csti.demo.utils.UserSession;
import org.jetbrains.annotations.NotNull;


public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";

    private FragmentCategoryBinding binding;

    //to get user session data
    private UserSession session;
    private Userr user;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve session values and display
        retrieveSession();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_category, container, false);

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(v -> NavHostFragment.findNavController(CategoryFragment.this)
                .navigate(R.id.action_nav_category_to_nav_category_man));

        if (!this.user.getRol().equals(Userr.ROL.SELLER)) {
            binding.fab.setVisibility(View.INVISIBLE);
        }


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        int spanCount = 2;

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        //binding.recyclerView.setAdapter(adapter);
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