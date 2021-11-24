package com.pucmm.csti.demo.ui.category;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pucmm.csti.R;
import com.pucmm.csti.databinding.FragmentCategoryBinding;
import com.pucmm.csti.databinding.FragmentCategoryManagerBinding;
import com.pucmm.csti.demo.model.Userr;
import com.pucmm.csti.demo.utils.UserSession;


public class CategoryFragmentManager extends Fragment {

    private static final String TAG = "CategoryFragmentManager";

    private FragmentCategoryManagerBinding binding;

    //to get user session data
    private UserSession session;
    private Userr user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


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