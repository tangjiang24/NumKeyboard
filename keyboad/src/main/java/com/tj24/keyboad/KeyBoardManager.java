package com.tj24.keyboad;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by energy on 2019/1/21.
 */

public class KeyBoardManager implements NumKeyBoardView.OnKeyPressListener {
    private NumKeyBoardView keyBoardView;
    private Activity context;
    TranslateAnimation mShowAction;
    TranslateAnimation mHideAction;
    int next = 0;
    List <EditText> edts = new ArrayList();
    EditText currentEdt;

    /**
     * 适用于 单个edittext都需要调用此键盘的情况
     * @param keyBoardView
     * @param context
     * @param editText
     */
    public KeyBoardManager(NumKeyBoardView keyBoardView, Activity context,EditText editText) {
        this.keyBoardView = keyBoardView;
        this.context = context;
        initAnimation();
        edts.clear();
        edts.add(editText);
        initEdts(edts);
    }
    /**
     * 适用于 指定edittext都需要调用此键盘的情况
     * @param keyBoardView
     * @param context
     * @param editTexts
     */
    public KeyBoardManager(NumKeyBoardView keyBoardView, Activity context,List<EditText> editTexts) {
        this.keyBoardView = keyBoardView;
        this.context = context;
        initAnimation();
        edts = editTexts;
        initEdts(edts);
    }
    /**
     * 适用于 activity所有edittext都需要调用此键盘的情况
     * @param keyBoardView
     * @param context
     */
    public KeyBoardManager(NumKeyBoardView keyBoardView, Activity context) {
        this.keyBoardView = keyBoardView;
        this.context = context;
        initAnimation();
        edts =getAllEdittext(context);
        initEdts(edts);
    }
    /**
     * 适用于 fragment所有edittext都需要调用此键盘的情况
     * @param keyBoardView
     * @param context
     * @param fragment
     */
    public KeyBoardManager(NumKeyBoardView keyBoardView, Activity context,Fragment fragment){
        this.keyBoardView = keyBoardView;
        this.context = context;
        initAnimation();
        edts = getAllEdittext(fragment.getView());
        initEdts(edts);
    }

    /**
     * 适用于 viewGroup子view中所有edittext都需要调用此键盘的情况
     * @param keyBoardView
     * @param context
     * @param viewGroup
     */
    public KeyBoardManager(NumKeyBoardView keyBoardView, Activity context,View viewGroup){
        this.keyBoardView = keyBoardView;
        this.context = context;
        initAnimation();
        edts = getAllEdittext(viewGroup);
        initEdts(edts);
    }

    /**
     * 初始化edittexts
     * @param edts
     */
    private void initEdts(final List<EditText> edts) {
        if(edts==null|| edts.size()<=0) return;
        currentEdt = edts.get(0);
        bindToEditor();
        for(final EditText et : edts){
            forbidenSystemSoftKey(et);
            et.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    for(int i = 0; i< edts.size(); i++){
                        if(edts.get(i).equals(et)){
                            next = i;
                        }
                    }
                    currentEdt = et;
                    bindToEditor();
                    return false;
                }
            });
        }
    }

    /**
     * 绑定editext
     */
    private void bindToEditor() {
        if(currentEdt==null)return;
        currentEdt.setOnFocusChangeListener(editorFocusChangeListener);
        if(currentEdt.hasFocus()){
            showSoftKeyboard(currentEdt);
        }
        keyBoardView.setOnKeyPressListener(this);
    }

    @Override
    public void onInertKey(String text) {
        String content = currentEdt.getText().toString();
        if(!text.equals(".")){
            if(!content.equals("0")){
                currentEdt.append(text);
            }
        }else if(content!=null && content.length()>0 && !content.contains(".")){
            currentEdt.append(text);
        }
    }

    @Override
    public void onDeleteKey() {
        String content = currentEdt.getText().toString();
        if(content!=null && content.length()>0){
            content = content.substring(0,content.length()-1);
            currentEdt.setText(content);
            currentEdt.setSelection(content.length());
        }
    }

    @Override
    public void onAddData() {
        String content = currentEdt.getText().toString();
        if(content==null || content.length()<=0 || content.endsWith("."))return;
        if(!content.contains(".")){
            currentEdt.setText(Integer.parseInt(content)+1+"");
            currentEdt.setSelection(currentEdt.getText().toString().length());
        }else {
            int scale = (content.length()-content.indexOf(".")-1);
            double data = Math.pow(0.1,scale);
            String value = String.valueOf(Arith.add(Double.parseDouble(content),data));
            if(value.length() - value.indexOf(".")-1 <scale){
                currentEdt.setText(value+"0");
            }else {
                currentEdt.setText(value);
            }
            currentEdt.setSelection(currentEdt.getText().toString().length());
        }
    }

    @Override
    public void onDelData() {
        String content = currentEdt.getText().toString();
        if(content==null || content.length()<=0 || content.endsWith("."))return;
        if(!content.contains(".")){
            currentEdt.setText(Integer.parseInt(content)-1+"");
            currentEdt.setSelection(currentEdt.getText().toString().length());
        }else {
            int scale = (content.length()-content.indexOf(".")-1);
            double data = Math.pow(0.1,scale);
            String value = String.valueOf(Arith.sub(Double.parseDouble(content),data));
            if(value.length() - value.indexOf(".")-1 <scale){
                currentEdt.setText(value+"0");
            }else {
                currentEdt.setText(value);
            }
            currentEdt.setSelection(currentEdt.getText().toString().length());
        }
    }

    @Override
    public void onNext() {
        if(edts.size()>next+1){  //还有下一项
            next++;
            currentEdt = edts.get(next);
            currentEdt.setFocusable(true);
            currentEdt.setFocusableInTouchMode(true);
            currentEdt.requestFocus();
            KeyBoardManager.this.bindToEditor();
        }
    }

    @Override
    public void onCancle() {
        hideSoftKeyboard();
    }

    /**
     * 监听焦点变化（获取焦点时）
     */
    private final View.OnFocusChangeListener editorFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View v, boolean hasFocus) {
            if (v instanceof EditText) {
                EditText et = (EditText) v;
                if (hasFocus) {
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showSoftKeyboard((EditText) v);
                        }
                    },300);
                }else {
                    if(et.equals(currentEdt)){   //失去焦点 并且没有再绑定新的edittext
                        hideSoftKeyboard();
                    }
                }
            }
        }
    };

    /**
     * 显示软键盘
     * @param editText
     */
    public void showSoftKeyboard(EditText editText) {
        hideSoftKeyboard(edts);
        if(keyBoardView.getVisibility()==View.VISIBLE){
            return;
        }
        keyBoardView.setVisibility(View.VISIBLE);
        keyBoardView.clearAnimation();
        keyBoardView.setAnimation(mHideAction);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard(){
        if(keyBoardView.getVisibility()==View.GONE){
            return;
        }
        keyBoardView.setVisibility(View.GONE);
        keyBoardView.clearAnimation();
        keyBoardView.setAnimation(mHideAction);
    }

    /**
     * 禁止Edittext弹出系统软件盘，光标依然正常显示。
     */
    private void forbidenSystemSoftKey(EditText et) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            et.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception e) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 隐藏系统软键盘 只适用于Activity，不适用于Fragment
     */
    private void hideSystemSoftKeyboard() {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     * @param viewList 可触发系统软键盘弹出的view集合
     */
    public void hideSoftKeyboard(List<EditText> viewList) {
        if (viewList == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 初始化动画
     */
    private void initAnimation() {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                , 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(600);
        mHideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF
                , 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHideAction.setDuration(600);
    }

    /**
     * 获取整个activity的editext
     * @return
     */
    private List<EditText> getAllEdittext(Activity activity) {
        View view  = activity.getWindow().getDecorView();
        return getAllEdittext(view);
    }
    private List<EditText> getAllEdittext(View view) {
        List<EditText> allEdittext = new ArrayList<EditText>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                if(viewchild instanceof EditText){
                    allEdittext.add((EditText) viewchild);
                }
                allEdittext.addAll(getAllEdittext(viewchild));
            }
        }
        return allEdittext;
    }
}
