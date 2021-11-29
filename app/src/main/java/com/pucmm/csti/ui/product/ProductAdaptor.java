package com.pucmm.csti.ui.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pucmm.csti.databinding.ItemProductBinding;
import com.pucmm.csti.listener.OnItemTouchListener;
import com.pucmm.csti.listener.OptionsMenuListener;
import com.pucmm.csti.model.Carousel;
import com.pucmm.csti.model.Product;
import com.pucmm.csti.model.Userr;
import com.pucmm.csti.model.relationships.ProductWithCarousel;
import com.pucmm.csti.utils.CommonUtil;
import com.pucmm.csti.utils.FormattedUtil;
import com.pucmm.csti.utils.ProductUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.MyViewHolder> {
    private static final String TAG = "ProductAdaptor";

    private Userr user;
    private List<ProductWithCarousel> elements;
    private ItemProductBinding binding;
    private OptionsMenuListener optionsMenuListener;
    private OnItemTouchListener onItemTouchListener;

    public ProductAdaptor(Userr user) {
        this.user = user;
    }

    @NonNull
    @NotNull
    @Override
    public ProductAdaptor.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        binding = ItemProductBinding.inflate(layoutInflater, parent, false);

        return new ProductAdaptor.MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final ProductWithCarousel element = elements.get(position);
        holder.itemCode.setText(ProductUtils.completeLeft(element.product.getItemCode()));
        holder.itemName.setText(element.product.getItemName());
        holder.price.setText(FormattedUtil.getDecimalValue(element.product.getPrice()));


        if (!user.getRol().equals(Userr.ROL.SELLER)) {
            holder.action.setVisibility(View.INVISIBLE);
        }

        holder.action.setOnClickListener(v -> {
            if (optionsMenuListener != null) {
                optionsMenuListener.onCreateOptionsMenu(holder.action, element);
            }
        });

        holder.avatar.setOnClickListener(v -> {
            if (onItemTouchListener != null) {
                onItemTouchListener.onClick(element);
            }
        });

        holder.itemCode.setOnClickListener(v -> {
            if (onItemTouchListener != null) {
                onItemTouchListener.onClick(element);
            }
        });

        holder.itemName.setOnClickListener(v -> {
            if (onItemTouchListener != null) {
                onItemTouchListener.onClick(element);
            }
        });

        holder.price.setOnClickListener(v -> {
            if (onItemTouchListener != null) {
                onItemTouchListener.onClick(element);
            }
        });


        if (element.carousels != null && !element.carousels.isEmpty()) {
            Optional<Carousel> optional = element.carousels.stream()
                    .sorted((a1, a2) -> Integer.valueOf(a1.getLineNum()).compareTo(a2.getLineNum()))
                    .findFirst();

            if (optional.isPresent()) {
                CommonUtil.downloadImage(optional.get().getPhoto(), binding.avatar);
            }
        }
    }


    @Override
    public int getItemCount() {
        if (elements == null) {
            return 0;
        }
        return elements.size();
    }

    public void setElements(List<ProductWithCarousel> elements) {
        this.elements = elements;
        notifyDataSetChanged();
    }

    public void setOptionsMenuListener(OptionsMenuListener optionsMenuListener) {
        this.optionsMenuListener = optionsMenuListener;
    }

    public void setOnItemTouchListener(OnItemTouchListener onItemTouchListener) {
        this.onItemTouchListener = onItemTouchListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCode, itemName, price;
        private ImageView avatar, action;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCode = binding.itemCode;
            itemName = binding.itemName;
            price = binding.price;
            avatar = binding.avatar;
            action = binding.manager;
        }
    }
}
