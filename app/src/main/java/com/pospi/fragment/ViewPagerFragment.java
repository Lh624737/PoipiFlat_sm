package com.pospi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pospi.dao.TableDao;
import com.pospi.dto.Tabledto;
import com.pospi.pai.yunpos.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewPagerFragment extends Fragment implements View.OnTouchListener {
    @Bind(R.id.container)
    RelativeLayout contain;
    private Context context;
    private List<Tabledto> tables;
    private TableDao dao;
    private Tabledto tabledto;
    private List<Tabledto> loval_tables;
    private int lastX;
    private int lastY;

//    private MyClickListener listener;

    /*添加餐桌弹窗的控件*/
    private TextView square_table;
    private TextView circle_table;
    private EditText et_square_table_name;
    private EditText et_circle_table_name;
    private TextView square_table_width;


    private TextView square_table_height;
    private TextView circle_table_width;
    private TextView square_table_width_jian;
    private LinearLayout ll_square_table;
    private LinearLayout ll_circle_table;
    private TextView square_table_height_jian;
    private TextView circle_table_width_jian;

//    public ViewPagerFragment(Context context, List<Tabledto> tables) {
//        this.context = context;
//        this.dao = new TableDao(context);
//        this.tabledto = new Tabledto();
//
//        loval_tables = new ArrayList<>();
//        loval_tables = dao.findTableInfo();
////        this.tables = tables;
//        this.tables = new ArrayList<>();
//        for (Tabledto mtabledto : tables) {
//            this.tables.add(mtabledto);
//        }
//        listener = new MyClickListener();
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("11111111111111111", "11111111111111111");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("11111111111111111", "22222222222222222");
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ButterKnife.bind(this, view);

//        for (Tabledto table : tables) {
//            if (table.getShape() == 0) {
//                createCircleTable(table);
//            } else if (table.getShape() == 1) {
//                createRectangleTable(table);
//            }
//        }
//        tables.clear();
        return view;
    }

//    /**
//     * 新建一张方形桌台
//     */
//    public void createRectangleTable(Tabledto table) {
//        // 创建一个线性布局
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOnClickListener(listener);
//        layout.setId(table.getId());
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setX((float) table.getX_distance());
//        layout.setY((float) table.getY_distance());
//        layout.setLayoutParams(new LinearLayout.LayoutParams(table.getWidth() * 100, table.getHeight() * 100));
//        layout.setBackgroundColor(getResources().getColor(R.color.blue));
//
//        // 创建一个TextView并添加
//        layout.addView(createTextViAew(table.getTableNumber()));
//
//        layout.setOnClickListener(new LinearLayout.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("onClickLinearLayout", "onClickLinearLayout");
//                showPopupWindow(v);
//            }
//        });
//        layout.setOnTouchListener(this);
//        contain.addView(layout);
//    }
//
//    public static int px2dip(Context context, double pxValue) {
//        final double scale = context.getResources().getDisplayMetrics().density;
//        return (int) (pxValue / scale + 0.5f);
//    }
//
//
//    /**
//     * 新建一张圆形桌台
//     */
//    public void createCircleTable(Tabledto table) {
//        // 创建一个线性布局
//        LinearLayout mlayout = new LinearLayout(context);
//        mlayout.setId(table.getId());
//        mlayout.setOrientation(LinearLayout.VERTICAL);
//        mlayout.setX((float) table.getX_distance());
//        mlayout.setY((float) table.getY_distance());
//        mlayout.setLayoutParams(new LinearLayout.LayoutParams((int) table.getWidth(), (int) table.getHeight())); //width=宽； height=高
//        mlayout.setBackground(getResources().getDrawable(R.drawable.shape_circle));
//
//        // 创建一个TextView并添加
//        mlayout.addView(createTextViAew(table.getTableNumber()));
//
//        mlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupWindow(v);
//            }
//        });
//        mlayout.setOnTouchListener(this);
//        contain.addView(mlayout);
//    }

//    /**
//     * 新建一个显示桌名的TextView
//     */
//    private TextView createTextViAew(String tableName) {
//        TextView mTextView = new TextView(context);
//        mTextView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        mTextView.setMaxLines(1);
//        mTextView.setTextSize(16);
//        mTextView.setTextColor(Color.WHITE);
//        mTextView.setGravity(Gravity.CENTER);
//        mTextView.setPadding(1, 1, 1, 1);//left, top, right, bottom
//        mTextView.setText(tableName);
//        return mTextView;
//    }

    /**
     * 新建一个TextView
     */
//    private TextView createTextView(String text) {
//        TextView mTextView = new TextView(context);
//        setTextViewAttribute(mTextView);
//        mTextView.setText(text);
//        return mTextView;
//    }

//    /**
//     * 设置TextView的属性
//     */
//    public void setTextViewAttribute(TextView textView) {
//        textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
//        textView.setMaxLines(1);
//        textView.setTextSize(16);
//        textView.setTextColor(Color.WHITE);
//        textView.setGravity(Gravity.CENTER);
//        textView.setPadding(1, 1, 1, 1);//left, top, right, bottom
//    }
//
    @Override
    public boolean onTouch(View view, MotionEvent event) {

        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
//                showPopupWindow(view);
                break;
//            case MotionEvent.ACTION_MOVE:
//                //计算移动的距离
//                int offX = x - lastX;
//                int offY = y - lastY;
//                //调用layout方法来重新放置它的位置
//                view.layout(view.getLeft() + offX, view.getTop() + offY,
//                        view.getRight() + offX, view.getBottom() + offY);
//
//                tabledto = loval_tables.get(view.getId() - 1);
//                tabledto.setX_distance(view.getX());
//                tabledto.setY_distance(view.getY());
//                dao.updateTableInfo(view.getId(), tabledto);//没问题
            case MotionEvent.ACTION_UP:
                view.clearFocus();
                tabledto = new Tabledto();
                break;
        }
        return true;
    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
//
//    public void showPopupWindow(View v) {
//        Log.i("11111111111111111", "click");
//        View contentView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pop_edit_table, null);
//        // R.layout.pop为PopupWindow 的布局文件
//        PopupWindow addTablePop = new PopupWindow(contentView, dp2px(230), dp2px(246));
//        addTablePop.setBackgroundDrawable(new BitmapDrawable());
//        // 指定 PopupWindow 的背景
//        addTablePop.setFocusable(true);
//        addTablePop.showAsDropDown(v, 0, 0);
//
//        TextView cancel = (TextView) contentView.findViewById(R.id.tv_edit_table_cancel);
//        cancel.setOnClickListener(listener);
//        TextView add = (TextView) contentView.findViewById(R.id.tv_edit_table_update);
//        add.setOnClickListener(listener);
//        square_table = (TextView) contentView.findViewById(R.id.tv_edit_square_table);
//        square_table.setOnClickListener(listener);
//        circle_table = (TextView) contentView.findViewById(R.id.tv_edit_circle_table);
//        circle_table.setOnClickListener(listener);
//        ll_square_table = (LinearLayout) contentView.findViewById(R.id.ll_edit_square_table);
//        ll_circle_table = (LinearLayout) contentView.findViewById(R.id.ll_edit_circle_table);
//        et_square_table_name = (EditText) contentView.findViewById(R.id.et_edit_square_table_name);
//        et_circle_table_name = (EditText) contentView.findViewById(R.id.et_edit_circle_table_name);
//        square_table_width = (TextView) contentView.findViewById(R.id.tv_edit_square_table_width);
//        square_table_width_jian = (TextView) contentView.findViewById(R.id.tv_edit_square_table_width_jian);
//        square_table_width_jian.setOnClickListener(listener);
//        TextView square_table_width_jia = (TextView) contentView.findViewById(R.id.tv_edit_square_table_width_jia);
//        square_table_width_jia.setOnClickListener(listener);
//        square_table_height = (TextView) contentView.findViewById(R.id.tv_edit_square_table_height);
//        square_table_height_jian = (TextView) contentView.findViewById(R.id.tv_edit_square_table_height_jian);
//        square_table_height_jian.setOnClickListener(listener);
//        TextView square_table_height_jia = (TextView) contentView.findViewById(R.id.tv_edit_square_table_height_jia);
//        square_table_height_jia.setOnClickListener(listener);
//        circle_table_width = (TextView) contentView.findViewById(R.id.tv_edit_circle_table_width);
//        circle_table_width_jian = (TextView) contentView.findViewById(R.id.tv_edit_circle_table_width_jian);
//        circle_table_width_jian.setOnClickListener(listener);
//        TextView circle_table_width_jia = (TextView) contentView.findViewById(R.id.tv_edit_circle_table_width_jia);
//        circle_table_width_jia.setOnClickListener(listener);
//
////        square_width = Integer.parseInt(square_table_width.getText().toString());
////        square_height = Integer.parseInt(square_table_height.getText().toString());
////        circle_width = Integer.parseInt(circle_table_width.getText().toString());
////        square_name = et_square_table_name.getText().toString().trim();
////        circle_name = et_circle_table_name.getText().toString().trim();
//    }
//
//    class MyClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            Log.i("onClickonClick", "onClickonClick");
//            switch (v.getId()) {
//                case R.id.tv_add_table_cancel://添加餐桌窗里的取消
//                    Log.i("onClickonClick", "onClickonClick");
////                    addTablePop.dismiss();
//                    break;
//            }
//        }
//    }
//
//    private int dp2px(int dp) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
//                getResources().getDisplayMetrics());
//    }
}
