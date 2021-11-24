package com.pucmm.csti.roomviewmodel.activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pucmm.csti.databinding.ActivityMainRoomBinding;
import com.pucmm.csti.roomviewmodel.adaptors.PersonAdaptor;
import com.pucmm.csti.roomviewmodel.database.PersonDao;
import com.pucmm.csti.roomviewmodel.model.Person;
import com.pucmm.csti.roomviewmodel.viewmodel.PersonViewModel;
import com.pucmm.csti.roomviewmodel.viewmodel.PersonViewModelFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainRoomActivity extends AppCompatActivity {

    private ActivityMainRoomBinding mBinding;
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private PersonAdaptor mPersonAdaptor;
    private PersonDao personDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
//        mBinding = ActivityMainRoomBinding.inflate(getLayoutInflater());
//        setContentView(mBinding.getRoot());
//
//        personDao = AppDataBase.getInstance(getApplicationContext()).personDao();
//
//        mFloatingActionButton = mBinding.addFAB;
//        mFloatingActionButton.setOnClickListener(v -> startActivity(new Intent(MainRoomActivity.this, EditRoomActivity.class)));
//
//        mRecyclerView = mBinding.recyclerView;
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//
//        mPersonAdaptor = new PersonAdaptor(this);
//        mRecyclerView.setAdapter(mPersonAdaptor);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//                AppExecutors.getInstance().diskIO().execute(() -> {
//                    final int position = viewHolder.getAdapterPosition();
//                    final Person person = mPersonAdaptor.getPerson(position);
//
//                    personDao.delete(person);
//                });
//
////                if (direction == ItemTouchHelper.LEFT) {
////                    final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            switch (which) {
////                                case DialogInterface.BUTTON_POSITIVE:
////                                    System.err.println("BUTTON_POSITIVE");
////                                    break;
////                                case DialogInterface.BUTTON_NEGATIVE:
////                                    System.err.println("BUTTON_NEGATIVE");
////                                    break;
////                                default:
////                                    System.err.println("BUTTON_NEUTRAL");
////                            }
////
////                        }
////                    };
////                    final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
////                    builder.setMessage("Desea borrar el registro?").setPositiveButton("Si", listener)
////                            .setNegativeButton("No", listener)
////                            .create()
////                            .show();
////                }
//
//
//            }
//        });
//
//        touchHelper.attachToRecyclerView(mRecyclerView);

        retrieveTasks();

    }

    private void retrieveTasks() {
        PersonViewModel personViewModel = new ViewModelProvider(this, new PersonViewModelFactory(personDao))
                .get(PersonViewModel.class);

        personViewModel.getPersonListLiveData().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                mPersonAdaptor.setPersons(people);
            }
        });

    }
}