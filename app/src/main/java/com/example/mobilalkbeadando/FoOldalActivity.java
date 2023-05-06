package com.example.mobilalkbeadando;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
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
import java.util.Objects;
//admin felhasználó: e-mail:admin@admin.admin , jelszó: admin123

public class FoOldalActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private CollectionReference mItems;


    private RecyclerView mRecyclerView;
    private ArrayList<KurzusItem> mItemList;
    private KurzusItemAdapter mAdapter;
    private int gridNumber = 1;
    private int cartItems = 0;

    private FrameLayout redCircle ;
    private TextView countTextView;

    private CollectionReference mFeliratkozasok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fo_oldal);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            //todo
            finish();
        }else{

        }





        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new KurzusItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);


        mFireStore = FirebaseFirestore.getInstance();
        mItems = mFireStore.collection("Courses");
        mFeliratkozasok = mFireStore.collection("Subscribes");

        queryData();
    }

    private void queryData() {
        mItemList.clear();

        mItems.orderBy("name").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                KurzusItem item = document.toObject(KurzusItem.class);
                item.setId(document.getId());
                mItemList.add(item);
            }
            if (mItemList.size()==0){
                initializeData();
                queryData();
            }
            mAdapter.notifyDataSetChanged();

        });
    }

    private void initializeData() {
        // Get the resources from the XML file.
        String[] itemsList = getResources()
                .getStringArray(R.array.shopping_item_names);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.shopping_item_desc);

        String[] itemsPrice = getResources().getStringArray(R.array.shopping_item_price);

        TypedArray itemsImageResources =
                getResources().obtainTypedArray(R.array.shopping_item_images);
        TypedArray itemRate = getResources().obtainTypedArray(R.array.shopping_item_rates);

        // Clear the existing data (to avoid duplication).
        //mItemList.clear();

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < itemsList.length; i++) {
            mItems.add((new KurzusItem(itemsList[i], itemsInfo[i], itemsPrice[i], itemRate.getFloat(i, 0),
                    itemsImageResources.getResourceId(i, 0))));
        }

        // Recycle the typed array.
        itemsImageResources.recycle();

        // Notify the adapter of the change.
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fo_oldal_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() ==R.id.log_out_button){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() ==R.id.setting_button) {
            //admin felhasználó: e-mail:admin@admin.admin , jelszó: admin123
            if (Objects.equals(user.getEmail(), "admin@admin.admin")){
                Intent intent = new Intent(this,AdminActivity.class);
                startActivity(intent);
            }
            return true;

        } else if (item.getItemId() ==R.id.cart) {
            Intent intent = new Intent(this,SubscribedCourses.class);
            startActivity(intent);
            return true;

        }else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    public void updateAlertIcon(KurzusItem item){
        cartItems = (cartItems+1);
        if (cartItems > 0){
            countTextView.setText(String.valueOf(cartItems));
        }else {
            countTextView.setText("");
        }

        redCircle.setVisibility((cartItems > 0) ? View.VISIBLE : View.GONE);

        //mFeliratkozasok.add(new Subscribtion(item.getName(), user.getEmail().toString()));
        mFeliratkozasok.whereEqualTo("email", user.getEmail().toString())
                .whereEqualTo("kurzus", item.getName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (querySnapshot.isEmpty()) {

                            mFeliratkozasok.add(new Subscribtion(item.getName(), user.getEmail().toString()));
                        } else {
                            //todo valami log v toast
                        }
                    }
                });

    }
}