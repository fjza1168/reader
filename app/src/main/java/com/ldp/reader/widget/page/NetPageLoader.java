package com.ldp.reader.widget.page;


import android.util.Log;

import com.ldp.reader.model.bean.BookChapterBean;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.local.BookRepository;
import com.ldp.reader.utils.BookManager;
import com.ldp.reader.utils.Constant;
import com.ldp.reader.utils.FileUtils;
import com.ldp.reader.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ldp on 17-5-29.
 * 网络页面加载器
 */

public class NetPageLoader extends PageLoader {
    private static final String TAG = "PageFactory";

    public NetPageLoader(PageView pageView, CollBookBean collBook) {
        super(pageView, collBook);
      firstTime = true;
    }
    private List<TxtChapter> convertTxtChapter(List<BookChapterBean> bookChapters) {
        List<TxtChapter> txtChapters = new ArrayList<>(bookChapters.size());
        for (BookChapterBean bean : bookChapters) {
            TxtChapter chapter = new TxtChapter();
            chapter.bookId = bean.getBookId();
            chapter.title = bean.getTitle();
            chapter.link = bean.getLink();
//            Log.d("+转换链接",bean.getLink());
            txtChapters.add(chapter);
        }
        return txtChapters;
    }

    @Override
    public void refreshChapterList() {
        Log.e(TAG,"refreshChapterList");
        if (mCollBook.getBookChapters() == null) return;

        // 将 BookChapter 转换成当前可用的 Chapter
        mChapterList = convertTxtChapter(mCollBook.getBookChapters());
        isChapterListPrepare = true;

        // 目录加载完成，执行回调操作。
        if (mPageChangeListener != null) {
            mPageChangeListener.onCategoryFinish(mChapterList);
        }

        // 如果章节未打开
        if (!isChapterOpen()) {
            // 打开章节
            Log.e(TAG,"+refreshChapterList" );

            openChapter();
        }
    }

    @Override
    protected BufferedReader getChapterReader(TxtChapter chapter) throws Exception {
        File file = new File(Constant.BOOK_CACHE_PATH + mCollBook.get_id()
                + File.separator + chapter.title + FileUtils.SUFFIX_NB);
        if (!file.exists()) return null;

        Reader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        return br;
    }

    @Override
    protected boolean hasChapterData(TxtChapter chapter) {
        return BookManager.isChapterCached(mCollBook.get_id(), chapter.title);
    }

    // 装载上一章节的内容
    @Override
    boolean parsePrevChapter() {
        boolean isRight = super.parsePrevChapter();

        if (mStatus == STATUS_FINISH) {
            loadPrevChapter();
        } else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载当前章内容。
    boolean firstTime = true;

    @Override
    boolean parseCurChapter() {
        Log.d("+打开章节NetPage", "parseCurChapter");

        boolean isRight = super.parseCurChapter();

        if ((mStatus == STATUS_LOADING )&& (firstTime||isInit)) {
            firstTime =false;
            Log.d(TAG, "STATUS_LOADING");
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载下一章节的内容
    @Override
    boolean parseNextChapter() {
        boolean isRight = super.parseNextChapter();

        if (mStatus == STATUS_FINISH) {
            loadNextChapter();
        } else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }

        return isRight;
    }

    /**
     * 加载当前页的前面两个章节
     */
    private void loadPrevChapter() {
        if (mPageChangeListener != null) {
            int end = mCurChapterPos;
            int begin = end - 2;
            if (begin < 0) {
                begin = 0;
            }
            Log.e(TAG,"+loadPrevChapter" + "begin:"+begin +"   end:"+end);

            requestChapters(begin, end);
        }
    }

    /**
     * 加载前一页，当前页，后一页。
     */
   synchronized private void loadCurrentChapter() {
        Log.e(TAG,"+NetloadCurrentChapter");
        if (mPageChangeListener != null) {
            int begin = mCurChapterPos;
            int end = mCurChapterPos;

            // 是否当前不是最后一章
            if (end < mChapterList.size()) {
                end = end + 1;
                if (end >= mChapterList.size()) {
                    end = mChapterList.size() - 1;
                }
            }

            // 如果当前不是第一章
            if (begin != 0) {
                begin = begin - 1;
                if (begin < 0) {
                    begin = 0;
                }
            }
            Log.e(TAG,"+loadCurrentChapter" + "begin:"+begin +"   end:"+end);

            requestChapters(begin, end);
        }
    }

    /**
     * 加载当前页的后两个章节
     */

    private void loadNextChapter() {
        if (mPageChangeListener != null) {

            // 提示加载后两章
            int begin = mCurChapterPos + 1;
            int end = begin + 1;

            // 判断是否大于最后一章
            if (begin >= mChapterList.size()) {
                // 如果下一章超出目录了，就没有必要加载了
                return;
            }

            if (end > mChapterList.size()) {
                end = mChapterList.size() - 1;
            }
            Log.e(TAG,"+loadNextChapter" + "begin:"+begin +"   end:"+end);

            requestChapters(begin, end);
        }
    }

   synchronized private void requestChapters(int start, int end) {
     Log.e(TAG,"+requestChapters  "  + "start:"+start + "   end:"+ end);
        // 检验输入值
        if (start < 0) {
            start = 0;
        }

        if (end >= mChapterList.size()) {
            end = mChapterList.size() - 1;
        }


        List<TxtChapter> chapters = new ArrayList<>();
          if(start + 1 >= mChapterList.size()){
              return;
          }


        chapters.add(mChapterList.get(start + 1));
        Log.e(TAG,"+requestChapters  " +mChapterList.get(start + 1).getTitle());

//        // 过滤，哪些数据已经加载了
        for (int i = start; i <= end; ++i) {
            TxtChapter txtChapter = mChapterList.get(i);
            if (i == start + 1) {
               continue;
            }
            if (!hasChapterData(txtChapter)) {
                chapters.add(txtChapter);
                Log.e(TAG,"+requestChapters  " +txtChapter.getTitle());

            }
        }

        if (!chapters.isEmpty()) {
            mPageChangeListener.requestChapters(chapters);
        }
    }

    @Override
    public void saveRecord() {
        super.saveRecord();
        if (mCollBook != null && isChapterListPrepare) {
            //表示当前CollBook已经阅读
            mCollBook.setIsUpdate(false);
            mCollBook.setLastRead(StringUtils.
                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            //直接更新
            BookRepository.getInstance().saveCollBook(mCollBook);
        }
    }
}

