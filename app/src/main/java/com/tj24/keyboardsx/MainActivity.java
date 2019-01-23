package com.tj24.keyboardsx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tj24.keyboad.KeyBoardManager;
import com.tj24.keyboad.NumKeyBoardView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    EditText et5;
    NumKeyBoardView keyBoardView;
    KeyBoardManager keyBoardManager;
    LinearLayout etContainer;
    Button btnSingle;
    Button btnMany;
    Button btnView;
    Button btnFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        et3 = (EditText)findViewById(R.id.et3);
        et4 = (EditText)findViewById(R.id.et4);
        et5 = (EditText)findViewById(R.id.et5);
        btnSingle = (Button) findViewById(R.id.btn_single);
        btnMany = (Button) findViewById(R.id.btn_many);
        btnView = (Button) findViewById(R.id.btn_view);
        btnFragment = (Button) findViewById(R.id.btn_fragment);
        btnSingle.setOnClickListener(this);
        btnMany.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnFragment.setOnClickListener(this);
        keyBoardView = (NumKeyBoardView) findViewById(R.id.keyboad);
        etContainer = (LinearLayout) findViewById(R.id.etContainer);
    }

    @Override
    public void onBackPressed() {
        if(keyBoardView.getVisibility()== View.VISIBLE){
            keyBoardManager.hideSoftKeyboard();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_single:
                keyBoardManager = new KeyBoardManager(keyBoardView,this,et);
                break;
            case R.id.btn_many:
                List<EditText> ets =new ArrayList<>();
                ets.add(et);
                ets.add(et1);
                ets.add(et2);
                ets.add(et3);
                ets.add(et4);
                ets.add(et5);
                keyBoardManager = new KeyBoardManager(keyBoardView,this,ets);
                break;
            case R.id.btn_view:
                keyBoardManager = new KeyBoardManager(keyBoardView,this,etContainer);
                break;
            case R.id.btn_fragment:
                startActivity(new Intent(this,FragmentActivity.class));
                break;
        }
    }
}
