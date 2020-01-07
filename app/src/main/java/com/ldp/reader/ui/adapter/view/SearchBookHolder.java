package com.ldp.reader.ui.adapter.view;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ldp.reader.R;
import com.ldp.reader.model.bean.BookSearchResult;
import com.ldp.reader.ui.base.adapter.ViewHolderImpl;

/**
 * Created by ldp on 17-6-2.
 */

public class SearchBookHolder extends ViewHolderImpl<BookSearchResult> {

    private ImageView mIvCover;
    private TextView mTvName;
    private TextView mTvBrief;

    @Override
    public void initView() {
        mIvCover = findById(R.id.search_book_iv_cover);
        mTvName = findById(R.id.search_book_tv_name);
        mTvBrief = findById(R.id.search_book_tv_brief);
    }

    @Override
    public void onBind(BookSearchResult book, int pos) {
        //显示图片
//        Glide.with(getContext())
//                .load(Constant.IMG_BASE_URL + data.getCover())
//                .placeholder(R.drawable.ic_book_loading)
//                .error(R.drawable.ic_load_error)
//                .into(mIvCover);
        Glide.with(getContext())
                .load(book.getCover())
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .into(mIvCover);
        mTvName.setText(book.getTitle());

//        mTvBrief.setText(getContext().getString(R.string.nb_search_book_brief,
//                data.getLatelyFollower(),data.getRetentionRatio(),data.getAuthor()));
        mTvBrief.setText(getContext().getString(R.string.nb_search_book_brief,
               book.getAuthor(),"",book.getDesc()));
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_search_book;
    }
}
