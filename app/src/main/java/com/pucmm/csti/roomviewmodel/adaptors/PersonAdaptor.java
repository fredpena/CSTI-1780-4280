package com.pucmm.csti.roomviewmodel.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pucmm.csti.databinding.ActivityMainRoomBinding;
import com.pucmm.csti.databinding.PersonItemBinding;
import com.pucmm.csti.roomviewmodel.activities.EditRoomActivity;
import com.pucmm.csti.roomviewmodel.constants.Constants;
import com.pucmm.csti.roomviewmodel.model.Person;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PersonAdaptor extends RecyclerView.Adapter<PersonAdaptor.MyViewHolder> {

    private Context mContext;
    private List<Person> mPersonList;
    private PersonItemBinding mBinding;

    public PersonAdaptor(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        mBinding = PersonItemBinding.inflate(layoutInflater, parent, false);

        return new MyViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final Person person = mPersonList.get(position);
        holder.name.setText(person.getName());
        holder.email.setText(person.getEmail());
        holder.number.setText(person.getNumber());
        holder.pinCode.setText(person.getPinCode() + "");
        holder.city.setText(person.getCity());
    }

    @Override
    public int getItemCount() {
        if (mPersonList == null) {
            return 0;
        }
        return mPersonList.size();
    }

    public void setPersons(List<Person> people) {
        this.mPersonList = people;
        notifyDataSetChanged();
    }

    public Person getPerson(int position) {
        return mPersonList.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, email, number, city, pinCode;
        private ImageView editBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = mBinding.personName;
            email = mBinding.personEmail;
            number = mBinding.personNumber;
            city = mBinding.personCity;
            pinCode = mBinding.personPincode;

            editBtn = mBinding.editImage;
            editBtn.setOnClickListener(v -> {
                int id = mPersonList.get(getAdapterPosition()).getId();

                Intent intent = new Intent(mContext, EditRoomActivity.class);
                intent.putExtra(Constants.UPDATE_PERSON, id);
                mContext.startActivity(intent);
            });

        }
    }
}
