package com.tj24.keyboad;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
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
 * @Description:自定义键盘NumKeyboard的管理类，
 * 提供一个构造方法（需传入keyboardview和activity两个参数）
 * 对外暴露若干方法进行绑定或增加需要弹出自定义键盘的 edittext
 * 需注意：
 * bindEditext(...) 会将之前所绑定移除，重新绑定所传入
 * addEditext(...)  会将所传入edittext加入绑定集合
 * @Createdtime:2019/1/24 10:10
 * @Author:TangJiang
 * @Version: V.1.0.0
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
     * @param keyBoardView
     * @param context
     */
    public KeyBoardManager(Activity context,NumKeyBoardView keyBoardView) {
        this.keyBoardView = keyBoardView;
        this.context = context;
    }

    /**
     * 绑定activity所有edittext
     */
    public void bindEdits(){
        unBindEditor();
        edts = getAllEdittext(context);
        initEdts(edts);
    }

    /**
     * 绑定fragment所有edittext
     */
    public void bindEdits(Fragment fragment){
        unBindEditor();
        edts = getAllEdittext(fragment.getView());
        initEdts(edts);
    }

    /**
     * 绑定单个editText
     * @param editText
     */
    public void bindEdits(EditText editText){
        unBindEditor();
        edts.clear();
        edts.add(editText);
        initEdts(edts);
    }

    /**
     * 绑定editText 集合
     * @param editTexts
     */
    public void bindEdits(List<EditText> editTexts){
        unBindEditor();
        edts = editTexts;
        initEdts(edts);
    }

    /**
     * 绑定viewgroup内所有editText
     * @param viewGroup
     */
    public void bindEdits(View viewGroup){
        unBindEditor();
        edts.clear();
        edts = getAllEdittext(viewGroup);
        initEdts(edts);
    }

    /**
     * 增加fragment所有edittext
     */
    public void addEdits(Fragment fragment){
        edts.addAll(getAllEdittext(fragment.getView()));
        initEdts(edts);
    }

    /**
     * 增加单个editText
     * @param editText
     */
    public void addEdits(EditText editText){
        edts.add(editText);
        initEdts(edts);
    }

    /**
     * 增加editText 集合
     * @param editTexts
     */
    public void addEdits(List<EditText> editTexts){
        edts.addAll(editTexts);
        initEdts(edts);
    }

    /**
     * 增加viewgroup内所有editText
     * @param viewGroup
     */
    public void addEdits(View viewGroup){
        edts.addAll(getAllEdittext(viewGroup));
        initEdts(edts);
    }

    /**
     * 初始化edittexts
     * @param edts
     */
    private void initEdts(final List<EditText> edts) {
        if(edts==null|| edts.size()<=0) return;
        currentEdt = edts.get(0);
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
        bindToEditor();
    }

    /**
     * 绑定editext
     */
    private void bindToEditor() {
        if(currentEdt==null)return;
        currentEdt.setOnFocusChangeListener(editorFocusChangeListener);
        currentEdt.setFocusable(true);
        currentEdt.setFocusableInTouchMode(true);
        currentEdt.requestFocus();
        currentEdt.performClick();
        if(currentEdt.getText().toString()!=null){
            currentEdt.setSelection(currentEdt.getText().toString().length());
        }
        if(currentEdt.hasFocus()){
            showSoftKeyboard();
        }
        keyBoardView.setOnKeyPressListener(this);
    }

    /**
     * 解绑所有的edittext
     */
    private void unBindEditor(){
        for(EditText et : edts){
            et.setOnFocusChangeListener(null);
            et.setOnTouchListener(null);
        }
    }

    /**
     * 指定光标位置追加一个字符
     * @param text
     */
    @Override
    public void onInertKey(String text) {
//        自己写的逻辑
        String content = currentEdt.getText().toString();
        int position = getEditTextCursorIndex(currentEdt);
        if(!text.equals(".")){
            if(!content.equals("0")){
                currentEdt.getText().insert(position,text);
            }
        }else if(content!=null && content.length()>0 && !content.contains(".")){
            currentEdt.getText().insert(position,text);
        }

    }

    /**
     * 删除键
     */
    @Override
    public void onDeleteKey() {
//        自己写的逻辑
//        String content = currentEdt.getText().toString();
//        int position = getEditTextCursorIndex(currentEdt);
//        if(content!=null && !content.equals("") && position>0){
//          currentEdt.getText().delete(position-1, position);
//        }

//        利用系统的方法
        int keyCode = KeyEvent.KEYCODE_DEL;
        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        currentEdt.onKeyDown(keyCode, keyEventDown);
        currentEdt.onKeyUp(keyCode, keyEventUp);
    }

    /**
     * 微调增加的方法
     */
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

    /**
     * 微调减少的方法
     */
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

    /**
     * 获取下一个编辑项
     */
    @Override
    public void onNext() {
        if(edts.size()>next+1){  //还有下一项
            next++;
            currentEdt = edts.get(next);
            KeyBoardManager.this.bindToEditor();
        }
    }

    /**
     * 取消
     */
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
                    et.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showSoftKeyboard();
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
     * @param
     */
    public void showSoftKeyboard() {
//        if(context.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE){
            hideSystemSoftKeyboard(edts);
//        }
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
    private void hideSystemSoftKeyboard(List<EditText> viewList) {
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

    /**
     * 获取EditText光标所在的位置
     * @param mEditText
     * @return
     */
    private int getEditTextCursorIndex(EditText mEditText){
        return mEditText.getSelectionStart();
    }
}
