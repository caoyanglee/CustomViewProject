package io.weimu.www.widget.pwd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import io.weimu.www.R;


public class PayPwdView extends FrameLayout {
    //model
    private Context mContext;
    private List<TextView> tvList = new ArrayList<>();
    private String payPassword;//支付密码

    private EditText etPwd;


    private OnInputFinishListener onInputFinishListener;

    private int inputCodeNumber = 6;//输入的个数
    private int horizontalSpacing = 24;//code之间的间距
    private int textBg = R.drawable.bg_pwd;//编辑框的背景
    private int textColor = Color.BLACK;//字体颜色
    private int textSize = 20;//字体大小
    private boolean isShowNumber = true;//是否显示数字


    public PayPwdView(Context context) {
        this(context, null, 0);
    }

    public PayPwdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayPwdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);
        initPayPwdView();
    }


    private void initPayPwdView() {
        //editText
        etPwd = new EditText(mContext);
        etPwd.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        etPwd.setBackground(null);
        etPwd.setAlpha(0);
        etPwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);//必须放在addView后面
        etPwd.setCursorVisible(false);
        etPwd.setLongClickable(false);
        etPwd.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                payPassword = s.toString();
                if (payPassword.length() > inputCodeNumber) {
                    etPwd.setText(payPassword.substring(0, payPassword.length() - 1));
                }
                if (payPassword.length() == inputCodeNumber && onInputFinishListener != null) {
                    hideKeyBoard(etPwd);
                    onInputFinishListener.onInputFinish(payPassword);
                }
                showTvs(payPassword);
                etPwd.setSelection(payPassword.length());
            }
        });
        addView(etPwd);

        //Views
        LinearLayout linearLayout = new LinearLayout(mContext);
        //linearLayout.setBackgroundResource(R.drawable.bg_trade_pwd);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(horizontalSpacing, 0, 0, 0);
        for (int i = 0; i < inputCodeNumber; i++) {
            TextView tv = new TextView(mContext);
            tv.setTextSize(textSize);
            tv.setGravity(Gravity.CENTER);
            if (textBg != -1)
                tv.setBackgroundResource(textBg);
            tv.setTextColor(textColor);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            layoutParams.setMargins(0, 0, horizontalSpacing, 0);
            tv.setLayoutParams(layoutParams);
            tvList.add(tv);

            linearLayout.addView(tv);
        }
        addView(linearLayout);
    }

    private void showTvs(String payPassword) {
        int showSize = payPassword.length();
        for (int i = 0; i < inputCodeNumber; i++) {
            char targetChar;
            if (i < showSize) {
                if (isShowNumber)
                    targetChar = payPassword.toCharArray()[i];
                else
                    targetChar = '●';
            } else {
                targetChar = ' ';
            }
            TextView textView = tvList.get(i);
            textView.setText(targetChar + "");
        }
    }


    public void setText(String str) {
        etPwd.setText(str);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public interface OnInputFinishListener {
        void onInputFinish(String pwd);
    }

    public void setOnInputFinishListener(OnInputFinishListener onInputFinishListener) {
        this.onInputFinishListener = onInputFinishListener;
    }

    /**
     * @param editText 目标编辑框
     * @description 隐藏键盘
     */
    public static void hideKeyBoard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive(editText)) {
            editText.clearFocus();
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
        }
    }
}
