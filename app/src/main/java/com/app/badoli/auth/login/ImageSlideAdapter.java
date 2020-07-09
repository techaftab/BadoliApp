package com.app.badoli.auth.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.badoli.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImageSlideAdapter extends PagerAdapter {

    private Context mCtx;
    private Integer[] images;
    private String TAG=ImageSlideAdapter.class.getSimpleName();


    public ImageSlideAdapter(Context context, Integer[] images) {
        this.mCtx = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.intro_slider_adapter_design, container, false);
        }
        ImageView imageView = null;
        if (view != null) {
            imageView = view.findViewById(R.id.iv_slidingImages);
        }

       /* if (imageView != null) {
            Glide.with(mCtx).load(images.get(position)).placeholder(R.drawable.placeholder_image).into(imageView);
        }*/
        // Log.e(TAG,"IMAGES BANNER--->"+images.get(position));
        if (imageView != null) {
            imageView.setImageResource(images[position]);
        }
        container.addView(view);
        return view;

    }
}
