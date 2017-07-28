package cn.coolcat.dragviewanddelete;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DragGridView dragGridView = (DragGridView) findViewById(R.id.dragGridView);
        textView = (TextView) findViewById(R.id.textview);
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(i);
        }
/**
 * 使用View的post方法获取组件的宽度和高度
 *
 */
myAdapter=new MyAdapter(list);
dragGridView.setAdapter(myAdapter);
        //定义DisplayMetrics 对象
        DisplayMetrics  dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口高度
        final int screenHeight = dm.heightPixels;
        textView.post(new Runnable() {
                @Override
                public void run() {
                    int height = textView.getHeight();
                    dragGridView.setViewHeight(screenHeight-height*2);
                }
            });


    }


    private class MyAdapter extends BaseAdapter implements DragGridView.DragGridBaseAdapter {
        private ArrayList<Integer> mList;
        public int mHidePosition = -1;

        private int colors[] = {android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_orange_light};

        public MyAdapter(ArrayList<Integer> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Integer getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
                holder = MyHolder.create(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MyHolder) convertView.getTag();
            }

            holder.mTextView.setText(getItem(position).toString());
            holder.mImageView.setImageResource(colors[getItem(position) % 3]);
            //隐藏被拖动的
            if (position == mHidePosition) {
                convertView.setVisibility(View.INVISIBLE);
            } else {
                convertView.setVisibility(View.VISIBLE);
            }

            return convertView;
        }


        @Override
        public void reorderItems(int oldPosition, int newPosition) {
            int temp = mList.get(oldPosition);
            if (oldPosition < newPosition) {
                for (int i = oldPosition; i < newPosition; i++) {
                    Collections.swap(mList, i, i + 1);
                }
            } else if (oldPosition > newPosition) {
                for (int i = oldPosition; i > newPosition; i--) {
                    Collections.swap(mList, i, i - 1);
                }
            }

            mList.set(newPosition, temp);
        }

        @Override
        public void setHideItem(int hidePosition) {
            mHidePosition = hidePosition;
            notifyDataSetChanged();
        }

        @Override
        public void deleteItem(int deletePosition) {
            if (null != mList && deletePosition < mList.size()) {
                mList.remove(deletePosition);
                notifyDataSetChanged();
            }
        }
    }

    private static class MyHolder {
        public ImageView mImageView;
        public TextView mTextView;


        public MyHolder(ImageView imageView, TextView textView) {
            this.mImageView = imageView;
            this.mTextView = textView;
        }


        public static MyHolder create(View rootView) {
            ImageView image = (ImageView) rootView.findViewById(R.id.image);
            TextView text = (TextView) rootView.findViewById(R.id.text);
            return new MyHolder(image, text);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
