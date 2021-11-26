package com.pucmm.csti.demo.ui.category;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
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
import com.pucmm.csti.demo.activity.LoginActivity;
import com.pucmm.csti.demo.database.AppDataBase;
import com.pucmm.csti.demo.database.CategoryDao;
import com.pucmm.csti.demo.listener.OptionsMenuListener;
import com.pucmm.csti.demo.model.Category;
import com.pucmm.csti.demo.model.Userr;
import com.pucmm.csti.demo.ui.home.HomeViewModel;
import com.pucmm.csti.demo.utils.ConstantsUtil;
import com.pucmm.csti.demo.utils.UserSession;
import com.pucmm.csti.roomviewmodel.model.Person;
import com.pucmm.csti.roomviewmodel.viewmodel.PersonViewModel;
import com.pucmm.csti.roomviewmodel.viewmodel.PersonViewModelFactory;
import com.shashank.sony.fancytoastlib.FancyToast;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";

    private FragmentCategoryBinding binding;
    private CategoryDao categoryDao;
    //to get user session data
    private UserSession session;
    private Userr user;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve session values and display
        categoryDao = AppDataBase.getInstance(getContext()).categoryDao();

        //retrieve session values and display
        retrieveSession();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(v -> {
            final Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantsUtil.CATEGORY, null);

            NavHostFragment.findNavController(CategoryFragment.this)
                    .navigate(R.id.action_nav_category_to_nav_category_man, bundle);
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

        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        }

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        final CategoryAdaptor adapter = new CategoryAdaptor(user);
        binding.recyclerView.setAdapter(adapter);

        adapter.setOptionsMenuListener((OptionsMenuListener<Category>) (view1, element, position) -> {
            PopupMenu popup = new PopupMenu(getContext(), view1);
            popup.inflate(R.menu.action_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_manager:
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable(ConstantsUtil.CATEGORY, element);

                        NavHostFragment.findNavController(CategoryFragment.this)
                                .navigate(R.id.action_nav_category_to_nav_category_man, bundle);
                        return true;
                    case R.id.action_delete:
                        FancyToast.makeText(getContext(), "Coming soon", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                        //delete(element, adapter, position);
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });

        CategoryViewModel categoryViewModel = new ViewModelProvider(this, new CategoryViewModelFactory(categoryDao))
                .get(CategoryViewModel.class);

        categoryViewModel.getListLiveData().observe(this, elements -> adapter.setElements(elements));
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