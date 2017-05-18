package com.apress.gerber.use_maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_SP;


public class BottomView extends LinearLayout implements View.OnClickListener {

    private Context context;

    private float density;      //密度

    private LinearLayout topLinearLayout;       //上半部分布局
    private View partitionView;        //分割线
    private LinearLayout middleLinearLayout;        //中半部分布局
    private LinearLayout bottomLinearLayout;        //下半部分布局

    private TextView addressTextView;   //地址
    private TextView addTextView;  //上传
    private ImageView backImageView;    //返回
    private TextView dateTextView;  //日期
    private TextView moodTextView;  //心情

    private int flipInterval;   //轮播间隔时间
    private int inAnimation;    //显示动画
    private int outAnimation;   //隐藏动画

    private View leftView;      //左边部分
    private ViewFlipper viewFlipper;     //轮播区域
    private View rightView;     //右边部分


    public interface OnBottomViewListener {
        public void back();

        public void add();

        public void viewFillper(ViewFlipper viewFlipper);

        public void mood(LinearLayout bottomLinearLayout);
    }

    private OnBottomViewListener onBottomViewListener;

    public BottomView(Context context) {
        this(context, null);
    }

    public BottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;


        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BottomView);
        flipInterval = array.getInteger(R.styleable.BottomView_flipInterval, 2000);
        inAnimation = array.getResourceId(R.styleable.BottomView_inAnimation, android.R.anim.slide_in_left);
        outAnimation = array.getResourceId(R.styleable.BottomView_outAnimation, android.R.anim.slide_out_right);
        array.recycle();


        initialize(context);


    }


    private void initialize(Context context) {
        //获取密度
        density = getDensity(context);
        //设置垂直方向分布
        this.setOrientation(VERTICAL);
        //设置背景图
        this.setBackgroundColor(0xFFFAFAFA);

        initializeTop(context);
        initializePartition(context);

        initializeMiddle(context);

        initializeBottom(context);
        backImageView.setOnClickListener(this);
        addTextView.setOnClickListener(this);
        viewFlipper.setOnClickListener(this);
        moodTextView.setOnClickListener(this);
    }

    /**
     * 初始化上半部分
     *
     * @param context 上下文
     */
    private void initializeTop(Context context) {

        topLinearLayout = new LinearLayout(context);
        topLinearLayout.setOrientation(HORIZONTAL);

        top_addressTextView(context);
        top_addTextView(context);
        top_backImageView(context);

        LayoutParams topLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.2
        );
        topLayoutParams.leftMargin = (int) (15 * density);
        topLayoutParams.rightMargin = (int) (12 * density);
        topLayoutParams.gravity = Gravity.TOP;
        //添加上半部分布局
        addView(topLinearLayout, topLayoutParams);
    }


    /**
     * 初始化地址
     *
     * @param context 上下文
     */
    private void top_addressTextView(Context context) {
        addressTextView = new TextView(context);
        addressTextView.setGravity(Gravity.CENTER_VERTICAL);
        addressTextView.setTextSize(COMPLEX_UNIT_SP, 13);
        addressTextView.setPadding((int) (5 * density), 0, (int) (5 * density), 0);
        addressTextView.setText("广东省广州市天河区华南师范大学石牌桥校区西区");

        LayoutParams addressTextViewParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 5
        );
        addressTextViewParams.gravity = Gravity.CENTER_VERTICAL;

        topLinearLayout.addView(addressTextView, addressTextViewParams);
    }


    /**
     * 初始化编辑
     *
     * @param context 上下文
     */
    private void top_addTextView(Context context) {

        addTextView = new TextView(context);
        addTextView.setGravity(Gravity.CENTER);
        addTextView.setPadding((int) (10 * density), (int) (10 * density), (int) (10 * density), (int) (10 * density));
        addTextView.setTextColor(0xFF2B89E4);
        addTextView.setText("编辑");


        addTextView.setTextSize(COMPLEX_UNIT_SP, 14);

        LayoutParams addTextViewParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.5
        );
        addTextViewParams.gravity = Gravity.CENTER;
        addTextViewParams.leftMargin = (int) (5 * density);
        addTextViewParams.rightMargin = (int) (5 * density);
        addTextViewParams.topMargin = (int) (5 * density);
        addTextViewParams.bottomMargin = (int) (5 * density);


        topLinearLayout.addView(addTextView, addTextViewParams);


    }

    /**
     * 初始化返回
     *
     * @param context 上下文
     */
    private void top_backImageView(Context context) {
        backImageView = new ImageView(context);
        backImageView.setPadding(0, (int) (5 * density), 0, (int) (25 * density));
        backImageView.setImageResource(R.drawable.back);


        LayoutParams backImageViewParams = new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0.5
        );
        backImageViewParams.gravity = Gravity.END;
        backImageViewParams.gravity |= Gravity.TOP;

        topLinearLayout.addView(backImageView, backImageViewParams);

    }


    /**
     * 分割线
     *
     * @param context 上下文
     */
    private void initializePartition(Context context) {
        partitionView = new View(context);
        LayoutParams middleViewParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (1 * density), 0
        );
        partitionView.setBackgroundColor(0xFFCCCCCC);
        addView(partitionView, middleViewParams);
    }


    /**
     * 初始化中半部分
     *
     * @param context 上下文
     */
    private void initializeMiddle(Context context) {

        middleLinearLayout = new LinearLayout(context);
        middleLinearLayout.setOrientation(HORIZONTAL);


        middle_leftView(context);
        middle_viewFlipper(context);
        middle_rightView(context);

        LayoutParams middleLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 5
        );

        //添加下半部分布局
        addView(middleLinearLayout, middleLayoutParams);

    }

    /**
     * 初始化中半部分左边部分
     *
     * @param context 上下文
     */
    private void middle_leftView(Context context) {
        leftView = new View(context);


        LayoutParams leftViewParams = new LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1
        );
        leftViewParams.gravity = Gravity.START;
        middleLinearLayout.addView(leftView, leftViewParams);

    }


    /**
     * 初始化轮播
     *
     * @param context 上下文
     */
    private void middle_viewFlipper(Context context) {
        viewFlipper = new ViewFlipper(context);
        viewFlipper.setFlipInterval(flipInterval);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(context, inAnimation);
        viewFlipper.setOutAnimation(context, outAnimation);


        LayoutParams viewFlipperParams = new LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 3
        );
        viewFlipperParams.topMargin = (int) (10 * density);
        viewFlipperParams.bottomMargin = (int) (10 * density);

        middleLinearLayout.addView(viewFlipper, viewFlipperParams);

    }


    /**
     * 初始化中半部分右边部分
     *
     * @param context 上下文
     */
    private void middle_rightView(Context context) {
        rightView = new View(context);

        LayoutParams rightViewParams = new LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1
        );
        rightViewParams.gravity = Gravity.END;

        middleLinearLayout.addView(rightView, rightViewParams);

    }


    /**
     * 初始化下半部分
     *
     * @param context 上下文
     */
    private void initializeBottom(Context context) {
        bottomLinearLayout = new LinearLayout(context);
        bottomLinearLayout.setOrientation(VERTICAL);


        bottom_dateTextView(context);
        bottom_moodTextView(context);

        LayoutParams bottomLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.6
        );
        bottomLayoutParams.gravity = Gravity.BOTTOM;
        bottomLayoutParams.leftMargin = (int) (15 * density);
        bottomLayoutParams.rightMargin = (int) (12 * density);
        //添加下半部分布局
        addView(bottomLinearLayout, bottomLayoutParams);
    }


    /**
     * 初始化日期
     *
     * @param context 上下文
     */
    private void bottom_dateTextView(Context context) {
        dateTextView = new TextView(context);
        dateTextView.setGravity(Gravity.CENTER_VERTICAL);
        dateTextView.setTextSize(COMPLEX_UNIT_SP, 14);
        TextPaint textPaint = dateTextView.getPaint();
        textPaint.setFakeBoldText(true);
        dateTextView.setText("2017年1月18日");
        LayoutParams dateTextViewParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1
        );
        dateTextViewParams.gravity = Gravity.TOP;
        bottomLinearLayout.addView(dateTextView, dateTextViewParams);

    }


    /**
     * 初始化心情
     *
     * @param context 上下文
     */
    private void bottom_moodTextView(Context context) {
        moodTextView = new TextView(context);
        moodTextView.setTextSize(COMPLEX_UNIT_SP, 13);
        ///////////////////////////////////////////////////////////////
        moodTextView.setMaxLines(3);
        /////////////////////////////////////////////////////////////
        moodTextView.setEllipsize(TextUtils.TruncateAt.END);
        moodTextView.setPadding((int) (5 * density), 0, 0, 0);
        moodTextView.setText("旅游心情");

        LayoutParams moodTextViewParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 2
        );
        moodTextViewParams.gravity = Gravity.BOTTOM;
        bottomLinearLayout.addView(moodTextView, moodTextViewParams);

    }


    /**
     * 获取密度
     *
     * @param context 上下文
     * @return 密度
     */
    private float getDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }


    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddressText(String address) {
        addressTextView.setText(address);
    }


    /**
     * 添加图片至ViewFlipper
     *
     * @param views 图片集合
     */
    public void addViewToViewFillper(List<View> views) {
        for (int i = 0; i < views.size(); i++) {
            viewFlipper.addView(views.get(i), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 设置日期
     *
     * @param date 日期
     */
    public void setDateText(String date) {
        dateTextView.setText(date);
    }

    /**
     * 设置心情
     *
     * @param mood 心情
     */
    public void setMoodText(String mood) {
        moodTextView.setText(mood);
    }


    /**
     * 移除ViewFlipper中的View
     */
    public void removeViewOfViewFillper() {
        viewFlipper.removeAllViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setOnAddAndBackListener(OnBottomViewListener onBottomViewListener) {
        this.onBottomViewListener = onBottomViewListener;
    }

    @Override
    public void onClick(View v) {

        if (v == backImageView) {

            if (onBottomViewListener != null) {
                onBottomViewListener.back();
            }
        } else if (v == addTextView) {
            addTextView.setBackgroundColor(0xFFCCCCCC);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addTextView.setBackgroundColor(0xFFFDFDFD);
                }
            }, 100);
            if (onBottomViewListener != null) {
                onBottomViewListener.add();
            }
        } else if (v == viewFlipper) {
            if (onBottomViewListener != null) {
                onBottomViewListener.viewFillper(viewFlipper);
            }
        } else if (v == moodTextView) {
            if (onBottomViewListener != null) {
                onBottomViewListener.mood(bottomLinearLayout);
            }
        }
    }
}
