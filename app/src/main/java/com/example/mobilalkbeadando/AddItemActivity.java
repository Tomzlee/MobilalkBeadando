package com.example.mobilalkbeadando;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddItemActivity extends AppCompatActivity {
    private CollectionReference mItems;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;

    private EditText nameET;
    private EditText infoET;
    private EditText priceET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mFireStore = FirebaseFirestore.getInstance();
        mItems = mFireStore.collection("Courses");

        nameET = findViewById(R.id.addItemName);
        infoET = findViewById(R.id.addItemInfo);
        priceET = findViewById(R.id.addItemPrice);
    }

   /* public void addItem(String name, String desc, String price){
        mItems.add(new KurzusItem(name,desc,price,(float)0.0, this.getResources().getIdentifier("kutya.png","drawable",getPackageName())));
    }*/


    public void itemAdd(View view) {
        String name = nameET.getText().toString();
        String info = infoET.getText().toString();
        String price = priceET.getText().toString()+" valami" ;

        mItems.add(new KurzusItem(name,info,price,(float)0.0, this.getResources().getIdentifier("kutya.png","drawable",getPackageName())));

        Intent intent = new Intent(this,AdminActivity.class);
        startActivity(intent);
    }
}