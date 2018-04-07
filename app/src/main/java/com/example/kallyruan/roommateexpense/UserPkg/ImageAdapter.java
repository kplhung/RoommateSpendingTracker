package com.example.kallyruan.roommateexpense.UserPkg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kallyruan.roommateexpense.R;

/**
 * Created by kallyruan on 7/4/18.
 */

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);

                //imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
                /*
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.width = 85;
                lp.height = 85;
                imageView.requestLayout();
*/
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.mipmap.usericon_8, R.mipmap.usericon_9,
                R.mipmap.usericon_3, R.mipmap.usericon_2,
                R.mipmap.usericon_7, R.mipmap.usericon_6,
                R.mipmap.usericon_4, R.mipmap.usericon_1
        };
    }