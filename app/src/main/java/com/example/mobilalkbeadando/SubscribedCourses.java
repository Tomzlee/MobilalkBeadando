package com.example.mobilalkbeadando;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubscribedCourses extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private CollectionReference mItems;


    private RecyclerView mRecyclerView;
    private ArrayList<KurzusItem> mItemList;
    private SubscriptionAdapter mAdapter;
    private int gridNumber = 1;
    private int cartItems = 0;

    private FrameLayout redCircle ;
    private TextView countTextView;

    private CollectionReference mFeliratkozasok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_courses);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            //todo
            finish();
        }else{

        }


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new SubscriptionAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);


        mFireStore = FirebaseFirestore.getInstance();
        mItems = mFireStore.collection("Courses");
        mFeliratkozasok = mFireStore.collection("Subscribes");

        queryData();
    }

    private void queryData() {


        mFeliratkozasok.whereEqualTo("email", user.getEmail().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<String> subscribedCourses = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String course = document.getString("kurzus");
                            subscribedCourses.add(course);
                        }
                        // do something with subscribedCourses list

                        if (subscribedCourses.size()==0){
                            //todo
                        }else {
                            mItemList.clear();
                            for (String nev : subscribedCourses) {
                                mItems.whereEqualTo("name",nev).get().addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                        KurzusItem item = document.toObject(KurzusItem.class);
                                        item.setId(document.getId());
                                        mItemList.add(item);
                                    }
                                    if (mItemList.size()==0){
                                        //todo
                                    }
                                    mAdapter.notifyDataSetChanged();

                                });

                            }


                        }


                    }
                });
    }
}