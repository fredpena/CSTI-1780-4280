package com.pucmm.csti.demo.ui.category;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pucmm.csti.databinding.ItemCategoryBinding;
import com.pucmm.csti.demo.listener.OptionsMenuListener;
import com.pucmm.csti.demo.model.Category;
import com.pucmm.csti.demo.model.Userr;
import com.pucmm.csti.demo.networksync.FirebaseNetwork;
import com.pucmm.csti.demo.networksync.NetResponse;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.MyViewHolder> {
    private static final String TAG = "CategoryAdaptor";

    private Userr user;
    private List<Category> elements;
    private ItemCategoryBinding binding;
    private OptionsMenuListener optionsMenuListener;

    public CategoryAdaptor(Userr user) {
        this.user = user;
    }

    @NonNull
    @NotNull
    @Override
    public CategoryAdaptor.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        binding = ItemCategoryBinding.inflate(layoutInflater, parent, false);

        return new CategoryAdaptor.MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdaptor.MyViewHolder holder, int position) {
        final Category element = elements.get(position);
        holder.name.setText(element.getName());

        if (!user.getRol().equals(Userr.ROL.SELLER)) {
            holder.action.setVisibility(View.INVISIBLE);
        }

        holder.action.setOnClickListener(v -> {
            if (optionsMenuListener != null) {
                optionsMenuListener.onCreateOptionsMenu(holder.action, element, position);
            }
        });

        if (element.getPhoto() != null && !element.getPhoto().isEmpty()) {
            FirebaseNetwork.obtain().download(element.getPhoto(), new NetResponse<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    holder.avatar.setImageBitmap(response);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (elements == null) {
            return 0;
        }
        return elements.size();
    }

    public void setElements(List<Category> elements) {
        this.elements = elements;
        notifyDataSetChanged();
    }

    public void setOptionsMenuListener(OptionsMenuListener optionsMenuListener) {
        this.optionsMenuListener = optionsMenuListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView avatar, action;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.name;
            avatar = binding.avatar;
            action = binding.manager;
        }
    }
}
