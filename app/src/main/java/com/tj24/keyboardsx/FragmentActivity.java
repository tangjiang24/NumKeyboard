package com.tj24.keyboardsx;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tj24.keyboad.KeyBoardManager;

public class FragmentActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    KeyBoardFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        fragment = new KeyBoardFragment();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
       KeyboardView keyboardView = (KeyboardView) fragment.getView().findViewById(R.id.keyboad);
       KeyBoardManager keyBoardManager = fragment.keyBoardManager;
        if(keyboardView.getVisibility()== View.VISIBLE){
            keyBoardManager.hideSoftKeyboard();
        }else {
            super.onBackPressed();
        }
    }
}
