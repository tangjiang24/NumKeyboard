package com.tj24.keyboad;

import android.content.Context;
import android.content.res.TypedArray;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * Created by energy on 2019/1/20.
 */

public class NumKeyBoardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    private int KEYCODE_ADD = -100;
    private int KEYCODE_DEL = -101;
    private int KEYCODE_NEXT = -102;
    public NumKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public NumKeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.NumKeyView);
        //        mBgColor=ta.getColor(R.styleable.NumKeyView_gbColor, Color.RED);
        //        mDeleteDrawable=ta.getDrawable(R.styleable.NumKeyView_deleteDrawable);
        ta.recycle();
        //获取xml中的按键布局
        Keyboard keyboard=new Keyboard(context,R.xml.numkeyboad);
        setKeyboard(keyboard);
        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(this);
    }

    //回调接口
    public interface OnKeyPressListener{
        //添加数据回调
        void onInertKey(String text);
        //删除数据回调
        void onDeleteKey();
        //微调数据+
        void onAddData();
        //微调数据-
        void onDelData();
        //下一项
        void onNext();
        //取消
        void onCancle();
    }
    private OnKeyPressListener mOnkeyPressListener;
    public void setOnKeyPressListener(OnKeyPressListener onKeyPressListener){
        if(onKeyPressListener!=null){
            mOnkeyPressListener = onKeyPressListener;
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if(mOnkeyPressListener ==null) return;
        if (primaryCode==KEYCODE_ADD ){
            mOnkeyPressListener.onAddData();
        }else if (primaryCode==KEYCODE_DEL){
            mOnkeyPressListener.onDelData();
        }else if(primaryCode == KEYCODE_NEXT){
            mOnkeyPressListener.onNext();
        }else if(primaryCode == Keyboard.KEYCODE_CANCEL){
            mOnkeyPressListener.onCancle();
        }else if(primaryCode == Keyboard.KEYCODE_DELETE){
            mOnkeyPressListener.onDeleteKey();
        }else {
            mOnkeyPressListener.onInertKey(Character.toString((char) primaryCode));
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {
        super.swipeLeft();
    }

    @Override
    public void swipeRight() {
        super.swipeRight();
    }

    @Override
    public void swipeDown() {
        super.swipeDown();
    }

    @Override
    public void swipeUp() {
        super.swipeUp();
    }
}
