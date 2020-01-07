package com.ldp.reader.ui.adapter.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldp.reader.R;
import com.ldp.reader.model.bean.SectionBean;
import com.ldp.reader.ui.base.adapter.ViewHolderImpl;

/**
 * Created by ldp on 17-4-16.
 */

public class SectionHolder extends ViewHolderImpl<SectionBean>{

    private ImageView mIvIcon;
    private TextView mTvName;

    @Override
    public void initView(){
        mIvIcon =findById(R.id.section_iv_icon);
        mTvName =findById(R.id.section_tv_name);
    }

    @Override
    public void onBind(SectionBean value, int pos) {
        mIvIcon.setImageResource(value.getDrawableId());
        mIvIcon.setOnClickListener(view -> {
            float curTranslationY = mIvIcon.getTranslationY();
            float curTranslationX = mIvIcon.getTranslationX();
            ObjectAnimator animatorX
                    = ObjectAnimator.ofFloat(mIvIcon, "translationY",
                    curTranslationY, curTranslationY + 100f);
            ObjectAnimator animatorY
                    = ObjectAnimator.ofFloat(mIvIcon, "translationX",
                    curTranslationX, curTranslationY + 100f);

            animatorX.setDuration(2000);
            animatorY.setDuration(2000);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animatorX,animatorY);
            animatorSet.start();
        });
        mTvName.setText(value.getName());
        mTvName.setOnClickListener(view -> {
            float curTranslationY = mTvName.getTranslationY();
            float curTranslationX = mTvName.getTranslationX();
            ObjectAnimator animatorX
                    = ObjectAnimator.ofFloat(mTvName, "translationY",
                    curTranslationY, curTranslationY + 100f);
            ObjectAnimator animatorY
                    = ObjectAnimator.ofFloat(mTvName, "translationX",
                    curTranslationX, curTranslationY + 100f);

            animatorX.setDuration(2000);
            animatorY.setDuration(2000);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animatorX,animatorY);
            animatorSet.start();
        });
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_section;
    }
}
