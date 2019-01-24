package com.tj24.keyboardsx;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tj24.keyboad.KeyBoardManager;
import com.tj24.keyboad.NumKeyBoardView;

/**
 * Created by energy on 2019/1/22.
 */

public class KeyBoardFragment extends Fragment {
    Context context;
    EditText et;
    NumKeyBoardView keyBoardView;
    public KeyBoardManager keyBoardManager;
    LinearLayout etContainer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_keyboard,null);
        et = (EditText) view.findViewById(R.id.et);
        keyBoardView = (NumKeyBoardView) view.findViewById(R.id.keyboad);
        etContainer = (LinearLayout) view.findViewById(R.id.etContainer);
        keyBoardManager = new KeyBoardManager((Activity) context,keyBoardView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keyBoardManager.bindEdits(this);
    }
}
