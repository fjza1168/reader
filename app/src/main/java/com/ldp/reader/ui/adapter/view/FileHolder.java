package com.ldp.reader.ui.adapter.view;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldp.reader.R;
import com.ldp.reader.model.local.BookRepository;
import com.ldp.reader.ui.base.adapter.ViewHolderImpl;
import com.ldp.reader.utils.Constant;
import com.ldp.reader.utils.FileUtils;
import com.ldp.reader.utils.MD5Utils;
import com.ldp.reader.utils.StringUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by ldp on 17-5-27.
 */

public class FileHolder extends ViewHolderImpl<File> {
    private ImageView mIvIcon;
    private CheckBox mCbSelect;
    private TextView mTvName;
    private LinearLayout mLlBrief;
    private TextView mTvTag;
    private TextView mTvSize;
    private TextView mTvDate;
    private TextView mTvSubCount;

    private HashMap<File,Boolean> mSelectedMap;
    public FileHolder(HashMap<File,Boolean> selectedMap){
        mSelectedMap = selectedMap;
    }

    @Override
    public void initView() {
        mIvIcon = findById(R.id.file_iv_icon);
        mCbSelect = findById(R.id.file_cb_select);
        mTvName = findById(R.id.file_tv_name);
        mLlBrief = findById(R.id.file_ll_brief);
        mTvTag = findById(R.id.file_tv_tag);
        mTvSize = findById(R.id.file_tv_size);
        mTvDate = findById(R.id.file_tv_date);
        mTvSubCount = findById(R.id.file_tv_sub_count);
    }

    @Override
    public void onBind(File data, int pos) {
        //判断是文件还是文件夹
        if (data.isDirectory()){
            setFolder(data);
        }
        else {
            setFile(data);
        }
    }

    private void setFile(File file){
        //选择
        String id = MD5Utils.strToMd5By16(file.getAbsolutePath());

        if (BookRepository.getInstance().getCollBook(id) != null){
            mIvIcon.setImageResource(R.drawable.ic_file_loaded);
            mIvIcon.setVisibility(View.VISIBLE);
            mCbSelect.setVisibility(View.GONE);
        }
        else {
            boolean isSelected = mSelectedMap.get(file);
            mCbSelect.setChecked(isSelected);
            mIvIcon.setVisibility(View.GONE);
            mCbSelect.setVisibility(View.VISIBLE);
        }

        mLlBrief.setVisibility(View.VISIBLE);
        mTvSubCount.setVisibility(View.GONE);

        mTvName.setText(file.getName());
        mTvSize.setText(FileUtils.getFileSize(file.length()));
        mTvDate.setText(StringUtils.dateConvert(file.lastModified(), Constant.FORMAT_FILE_DATE));
    }

    public void setFolder(File folder){
        //图片
        mIvIcon.setVisibility(View.VISIBLE);
        mCbSelect.setVisibility(View.GONE);
        mIvIcon.setImageResource(R.drawable.ic_dir);
        //名字
        mTvName.setText(folder.getName());
        //介绍
        mLlBrief.setVisibility(View.GONE);
        mTvSubCount.setVisibility(View.VISIBLE);

        mTvSubCount.setText(getContext().getString(R.string.nb_file_sub_count,folder.list().length));
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_file;
    }
}
