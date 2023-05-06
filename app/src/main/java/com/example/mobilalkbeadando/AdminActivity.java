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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private CollectionReference mItems;


    private RecyclerView mRecyclerView;
    private ArrayList<KurzusItem> mItemList;
    private AdminAdapter mAdapter;
    private int gridNumber = 1;
    private int cartItems = 0;

    private FrameLayout redCircle ;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            //todo
            finish();
        }else{

        }





        mRecyclerView = findViewById(R.id.adminRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new AdminAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);


        mFireStore = FirebaseFirestore.getInstance();
        mItems = mFireStore.collection("Courses");

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

    public void deleteItem(KurzusItem item){
        DocumentReference ref = mItems.document(item._getId());

        ref.delete().addOnSuccessListener(success ->{

            Toast.makeText(this, "Sikeres törlés: "+item._getId(), Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(failure ->{
            Toast.makeText(this, "Nem sikerült a törlés", Toast.LENGTH_SHORT).show();
                });

        queryData();
    }

    public void updateAr(KurzusItem item, EditText ujArET){

        String ujAr = ujArET.getText().toString()+" valami";
            mItems.document(item._getId()).update("price", ujAr).addOnSuccessListener(success ->{
                Toast.makeText(this, "Sikeres ár változtatás", Toast.LENGTH_SHORT).show();
            });

        queryData();
    }

    public void createItem(String name, String desc, String price){
        mItems.add(new KurzusItem(name,desc,price,(float)0.0, this.getResources().getIdentifier("kutya.png","drawable",getPackageName())));
    }

    private void initializeData() {
        // Get the resources from the XML file.
        String[] itemsList = getResources()
                .getStringArray(R.array.shopping_item_names);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.shopping_item_desc);
        String[] itemsPrice = getResources()
                .getStringArray(R.array.shopping_item_price);
        TypedArray itemsImageResources =
                getResources().obtainTypedArray(R.array.shopping_item_images);
        TypedArray itemRate = getResources().obtainTypedArray(R.array.shopping_item_rates);



        for (int i = 0; i < itemsList.length; i++) {
            mItems.add((new KurzusItem(itemsList[i], itemsInfo[i], itemsPrice[i], itemRate.getFloat(i, 0),
                    itemsImageResources.getResourceId(i, 0))));
        }

        // Recycle the typed array.
        itemsImageResources.recycle();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.felso_menu, menu);
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
        } else if (item.getItemId() ==R.id.adminCreateButton) {
            if (Objects.equals(user.getEmail(), "admin@admin.admin")){

                Intent intent = new Intent(this,AddItemActivity.class);
                startActivity(intent);
            }
            return true;

        } else if (item.getItemId() ==R.id.cart) {
            return true;

        }else {
            return super.onOptionsItemSelected(item);
        }

    }
}