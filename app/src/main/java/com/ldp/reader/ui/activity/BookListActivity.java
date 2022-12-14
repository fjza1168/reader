package com.ldp.reader.ui.activity;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;

import com.ldp.reader.R;
import com.ldp.reader.RxBus;
import com.ldp.reader.event.BookSubSortEvent;
import com.ldp.reader.model.bean.BookTagBean;
import com.ldp.reader.model.flag.BookListType;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.ui.adapter.HorizonTagAdapter;
import com.ldp.reader.ui.adapter.TagGroupAdapter;
import com.ldp.reader.ui.base.BaseTabActivity;
import com.ldp.reader.ui.fragment.BookListFragment;
import com.ldp.reader.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ldp on 17-5-1.
 */

public class BookListActivity extends BaseTabActivity {
    private static final int RANDOM_COUNT = 5;
    @BindView(R.id.book_list_rv_tag_horizon)
    RecyclerView mRvTag;
    @BindView(R.id.book_list_cb_filter)
    CheckBox mCbFilter;
    @BindView(R.id.book_list_rv_tag_filter)
    RecyclerView mRvFilter;
    /*************************************/
    private HorizonTagAdapter mHorizonTagAdapter;
    private TagGroupAdapter mTagGroupAdapter;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    /************Params*******************/
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    @Override
    protected int getContentId() {
        return R.layout.activity_book_list;
    }

    @Override
    protected List<Fragment> createTabFragments() {
        List<Fragment> fragments = new ArrayList<>(BookListType.values().length);
        for (BookListType type : BookListType.values()){
            fragments.add(BookListFragment.newInstance(type));
        }
        return fragments;
    }

    @Override
    protected List<String> createTabTitles() {
        List<String> titles = new ArrayList<>(BookListType.values().length);
        for (BookListType type : BookListType.values()){
            titles.add(type.getTypeName());
        }
        return titles;
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("????????????");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTag();
    }

    private void initTag(){
        //?????????
        mHorizonTagAdapter = new HorizonTagAdapter();
        LinearLayoutManager tagManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRvTag.setLayoutManager(tagManager);
        mRvTag.setAdapter(mHorizonTagAdapter);

        //?????????
        mTagGroupAdapter = new TagGroupAdapter(mRvFilter,4);
        mRvFilter.setAdapter(mTagGroupAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //?????????Tag
        mHorizonTagAdapter.setOnItemClickListener(
                (view,pos) -> {
                    String bookSort = mHorizonTagAdapter.getItem(pos);
                    RxBus.getInstance().post(new BookSubSortEvent(bookSort));
                }
        );

        //??????
        mCbFilter.setOnCheckedChangeListener(
                (btn,checked)->{
                    if (mTopInAnim == null || mTopOutAnim == null){
                        mTopInAnim = AnimationUtils.loadAnimation(this,R.anim.slide_top_in);
                        mTopOutAnim = AnimationUtils.loadAnimation(this,R.anim.slide_top_out);
                    }

                    if (checked){
                        mRvFilter.setVisibility(View.VISIBLE);
                        mRvFilter.startAnimation(mTopInAnim);
                    }
                    else {
                        mRvFilter.startAnimation(mTopOutAnim);
                        mRvFilter.setVisibility(View.GONE);
                    }
                }
        );

        //????????????
        mTagGroupAdapter.setOnChildItemListener(
                (view, groupPos, childPos) -> {
                    String bean = mTagGroupAdapter.getChildItem(groupPos, childPos);
                    //???????????????
                    List<String> tags =  mHorizonTagAdapter.getItems();
                    boolean isExist = false;
                    for (int i=0; i<tags.size(); ++i){
                        if (bean.equals(tags.get(i))){
                            mHorizonTagAdapter.setCurrentSelected(i);
                            mRvTag.getLayoutManager().scrollToPosition(i);
                            isExist = true;
                        }
                    }
                    if (!isExist){
                        //?????????1?????????,?????????????????????
                        mHorizonTagAdapter.addItem(1,bean);
                        mHorizonTagAdapter.setCurrentSelected(1);
                        mRvTag.getLayoutManager().scrollToPosition(1);
                    }
                    mCbFilter.setChecked(false);
                }
        );
    }


    @Override
    protected void processLogic() {
        super.processLogic();
        refreshTag();
    }

    private void refreshTag(){
        Disposable refreshDispo = RemoteRepository.getInstance()
                .getBookTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (tagBeans)-> {
                            refreshHorizonTag(tagBeans);
                            refreshGroupTag(tagBeans);
                        },
                        (e) ->{
                            LogUtils.e(e);
                        }
                );
        mDisposable.add(refreshDispo);
    }

    private void refreshHorizonTag(List<BookTagBean> tagBeans){
        List<String> randomTag = new ArrayList<>(RANDOM_COUNT);
        randomTag.add("??????");
        int caculator = 0;
        //????????????4,5??????
        final int tagBeanCount = tagBeans.size();
        for (int i=0; i<tagBeanCount; ++i){
            List<String> tags = tagBeans.get(i).getTags();
            final int tagCount = tags.size();
            for (int j=0; j<tagCount; ++j){
                if (caculator < RANDOM_COUNT){
                    randomTag.add(tags.get(j));
                    ++caculator;
                }
                else {
                    break;
                }
            }
            if (caculator >= RANDOM_COUNT){
                break;
            }
        }
        mHorizonTagAdapter.addItems(randomTag);
    }

    private void refreshGroupTag(List<BookTagBean> tagBeans){
        //????????????????????????????????????????????????????????????
        BookTagBean bean = new BookTagBean();
        bean.setName(getResources().getString(R.string.nb_tag_sex));
        bean.setTags(Arrays.asList(getResources().getString(R.string.nb_tag_boy),getResources().getString(R.string.nb_tag_girl)));
        tagBeans.add(0,bean);

        mTagGroupAdapter.refreshItems(tagBeans);
    }
}
