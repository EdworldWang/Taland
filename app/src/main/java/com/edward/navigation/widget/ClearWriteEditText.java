package com.edward.navigation.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.edward.navigation.R;
import com.edward.navigation.util.UIUtils;


/**
 * Created by AMing on 15/11/2.
 * Company RongCloud
 */
public class ClearWriteEditText extends AppCompatEditText implements View.OnFocusChangeListener , TextWatcher {

    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    private Drawable mEyeDrawable;
   private Drawable mClearAndEyeDrawable;//弃用=。=
    private Drawable mLayerDrawable;
    private boolean IsPasswordView;
    private boolean PasswordVisible = false;
    public ClearWriteEditText(Context context) {
        this(context, null);
    }

    public ClearWriteEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearWriteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLayerDrawable = getResources().getDrawable(R.drawable.mylayer);
        mClearDrawable = getResources().getDrawable(R.drawable.search_clear_pressed_write);
        UIUtils.showToast("clear "+getClearDrawable().getIntrinsicHeight()+"   "+
                getClearDrawable().getIntrinsicWidth());
        mEyeDrawable = getResources().getDrawable(R.mipmap.white_eye_edward);
       mClearAndEyeDrawable = getResources().getDrawable(R.drawable.clearandeye_46);

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth()-60, mClearDrawable.getIntrinsicHeight()-60);
        mEyeDrawable.setBounds(60-mEyeDrawable.getIntrinsicWidth(), 0, 0, mEyeDrawable.getIntrinsicHeight()-60);
     mClearAndEyeDrawable.setBounds(0, 0, mClearAndEyeDrawable.getIntrinsicWidth()-120, mClearAndEyeDrawable.getIntrinsicHeight()-60);
       mLayerDrawable.setBounds(0,0,mLayerDrawable.getIntrinsicWidth(),mLayerDrawable.getIntrinsicHeight());
        setClearIconVisible(true);
        setEyeIconVisible(true);
       // setClearAndEyeIconVisible(false);
        IsPasswordView = false;//默认不是密码
        this.setOnFocusChangeListener(this);
        this.addTextChangedListener(this);
UIUtils.showToast("mix "+mLayerDrawable.getIntrinsicHeight()+"   "+
        mLayerDrawable.getIntrinsicWidth());
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if(IsPasswordView)
            setClearAndEyeIconVisible(s.length() > 0);
        else
            setClearIconVisible(s.length() > 0);
    }


    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                             getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
    protected void setEyeIconVisible(boolean visible) {
        Drawable right = visible ? mEyeDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
    protected void setClearAndEyeIconVisible(boolean visible) {
        Drawable right = visible ? mClearAndEyeDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
    protected void setLayerVisible(boolean visible) {
        Drawable right = visible ? mLayerDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable =
                        event.getX() > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth()+60)
                                    && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
                if(IsPasswordView){
                    boolean pwdtouchable =
                            event.getX() > (getWidth() - getPaddingRight() - 2*mClearDrawable.getIntrinsicWidth()+120)
                                    && (event.getX() < ((getWidth() - getPaddingRight()-mClearDrawable.getIntrinsicWidth()+60)));
                    if (pwdtouchable) {
                        if(PasswordVisible){
                            this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }else {
                            //原本不可见
                            this.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        }
                        PasswordVisible = !PasswordVisible;
                    }
                }


            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if(IsPasswordView)//密码框
                setClearAndEyeIconVisible(getText().length() > 0);
            else
                setClearIconVisible(getText().length() > 0);
           // setLayerVisible(true);
        } else {
            if(IsPasswordView){
                setClearAndEyeIconVisible(true);
            }else{
                setClearIconVisible(false);
            }
          //  setLayerVisible(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.startAnimation(shakeAnimation(3));
    }



    /**
     * 晃动动画
     * @param counts 半秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }
    public void IsPassword(Boolean isPasswordView){
        IsPasswordView = isPasswordView;
    }
    public Drawable getClearDrawable() {
        return mClearDrawable;
    }
    public Drawable getClearAndEyeDrawable() {
        return mClearAndEyeDrawable;
    }

    public void setClearDrawable(Drawable mClearDrawable) {
        this.mClearDrawable = mClearDrawable;
    }
}
