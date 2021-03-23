package ua.com.onyx.titlevaluespinner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alexander on 02.10.17.
 */

public class TitleValueSpinner extends RelativeLayout {

    private TextView titleView, valueView;
    private ImageView imageView;
    private View bottomLineView;

    private String title, value;
    private Drawable image;
    private int imageSize;
    private int titleTextColor, valueTextColor;
    private int titleTextSize, valueTextSize;
    private int bottomLineHeight;
    private int bottomLineColor;

    private boolean isShowing = false;

    private AlertDialog dialog;

    private OnSelectListener onSelectListener;
    private OnPreShowListener onPreShowListener;
    private OnShowListener onShowListener;
    private OnDismissListener onDismissListener;

    private BaseAdapter adapter;

    public TitleValueSpinner(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TitleValueSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TitleValueSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.title_value_spinner, this);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        setClickable(true);
        setBackgroundResource(typedValue.resourceId);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleValueSpinner, defStyleAttr, 0);

            title = typedArray.getString(R.styleable.TitleValueSpinner_title);
            value = typedArray.getString(R.styleable.TitleValueSpinner_value);
            image = typedArray.getDrawable(R.styleable.TitleValueSpinner_image);
            if (image == null) {
                image = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_down);
            }
            imageSize = typedArray.getDimensionPixelSize(R.styleable.TitleValueSpinner_imageSize,
                    getResources().getDimensionPixelSize(R.dimen.title_value_spinner_image_size));
            titleTextColor = typedArray.getColor(R.styleable.TitleValueSpinner_titleTextColor,
                    ContextCompat.getColor(context, R.color.title_value_spinner_title_text_color));
            valueTextColor = typedArray.getColor(R.styleable.TitleValueSpinner_valueTextColor,
                    ContextCompat.getColor(context, R.color.title_value_spinner_value_text_color));
            titleTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleValueSpinner_titleTextSize,
                    getResources().getDimensionPixelSize(R.dimen.title_value_spinner_title_text_size));
            valueTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleValueSpinner_valueTextSize,
                    getResources().getDimensionPixelSize(R.dimen.title_value_spinner_value_text_size));
            bottomLineHeight = typedArray.getDimensionPixelSize(R.styleable.TitleValueSpinner_bottomLineHeight,
                    getResources().getDimensionPixelSize(R.dimen.title_value_spinner_bottom_line_height));
            bottomLineColor = typedArray.getColor(R.styleable.TitleValueSpinner_bottomLineColor,
                    ContextCompat.getColor(context, R.color.title_value_spinner_bottom_line_color));
//            int id = typedArray.getResourceId(R.styleable.TitleValueSpinner_values, 0);
//            if (id != 0)
//                values = getResources().getStringArray(id);

            typedArray.recycle();
        } else {
            imageSize = getResources().getDimensionPixelSize(R.dimen.title_value_spinner_image_size);
            titleTextColor = ContextCompat.getColor(context, R.color.title_value_spinner_title_text_color);
            valueTextColor = ContextCompat.getColor(context, R.color.title_value_spinner_value_text_color);
            titleTextSize = getResources().getDimensionPixelSize(R.dimen.title_value_spinner_title_text_size);
            valueTextSize = getResources().getDimensionPixelSize(R.dimen.title_value_spinner_value_text_size);
            bottomLineHeight = getResources().getDimensionPixelSize(R.dimen.title_value_spinner_bottom_line_height);
            bottomLineColor = ContextCompat.getColor(context, R.color.title_value_spinner_bottom_line_color);
        }

        imageView = findViewById(R.id.image_view);
        imageView.setImageDrawable(image);
        LayoutParams imageViewLP = (LayoutParams) imageView.getLayoutParams();
        imageViewLP.width = imageSize;
        imageViewLP.height = imageSize;
        imageView.setLayoutParams(imageViewLP);

        titleView = findViewById(R.id.tv_title_view);
        titleView.setText(title);
        titleView.setTextColor(titleTextColor);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);

        valueView = findViewById(R.id.tv_value_view);
        valueView.setText(value);
        valueView.setTextColor(valueTextColor);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);

        bottomLineView = new View(getContext());
        bottomLineView.setBackgroundColor(bottomLineColor);
        LayoutParams bottomLineLP = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bottomLineHeight);
        bottomLineLP.addRule(ALIGN_PARENT_BOTTOM);
        bottomLineView.setLayoutParams(bottomLineLP);
        addView(bottomLineView);


    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(width, height);
//    }


    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        if (dialog != null) {
            dialog.getListView().setAdapter(adapter);
        }
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }


    public void setValue(String value) {
        this.value = value;
        valueView.setText(value);
    }

    public void setTitle(String title) {
        this.title = title;
        titleView.setText(title);
        if (dialog != null) {
            dialog.setTitle(title);
        }
    }

    public void setImage(Drawable image) {
        this.image = image;
        imageView.setImageDrawable(image);
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        titleView.setTextColor(titleTextColor);
    }

    public void setValueTextColor(int valueTextColor) {
        this.valueTextColor = valueTextColor;
        valueView.setTextColor(valueTextColor);
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
    }

    public void setValueTextSize(int valueTextSize) {
        this.valueTextSize = valueTextSize;
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
    }

    public void setBottomLineColor(int bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
        bottomLineView.setBackgroundColor(bottomLineColor);
    }

    public void setBottomLineHeight(int bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
        LayoutParams bottomLineLP = (LayoutParams) bottomLineView.getLayoutParams();
        bottomLineLP.height = bottomLineHeight;
        bottomLineView.setLayoutParams(bottomLineLP);
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
        LayoutParams imageLP = (LayoutParams) imageView.getLayoutParams();
        imageLP.width = imageSize;
        imageLP.height = imageSize;
        imageView.setLayoutParams(imageLP);
    }

    public String getValue() {
        return value;
    }


    public String getTitle() {
        return title;
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    public void setOnPreShowListener(OnPreShowListener onPreShowListener) {
        this.onPreShowListener = onPreShowListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Deprecated
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    @Override
    public boolean performClick() {
        if (!isShowing) {

            show();

        }
        return super.performClick();
    }

    public void show() {
        if (onPreShowListener != null && !onPreShowListener.onPreShow(this)) {
            return;
        }
        if (dialog == null) {
            dialog = new AlertDialog.Builder(getContext())
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onSelectListener != null) {
                                onSelectListener.onSelect(TitleValueSpinner.this, which);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setTitle(title)
                    .setNegativeButton("Cancel", null)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            isShowing = false;
                            if (onDismissListener != null) {
                                onDismissListener.onDismiss(TitleValueSpinner.this);
                            }
                        }
                    })
                    .create();
        }
        dialog.show();
        isShowing = true;
        if (onShowListener != null) {
            onShowListener.onShow(this);
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
        isShowing = false;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public interface OnSelectListener {
        void onSelect(TitleValueSpinner titleValueSpinner, int which);
    }

    public interface OnPreShowListener {
        boolean onPreShow(TitleValueSpinner titleValueSpinner);
    }

    public interface OnShowListener {
        void onShow(TitleValueSpinner titleValueSpinner);
    }

    public interface OnDismissListener {
        void onDismiss(TitleValueSpinner titleValueSpinner);
    }





}



