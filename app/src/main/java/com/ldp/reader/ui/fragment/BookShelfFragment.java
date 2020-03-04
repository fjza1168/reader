package com.ldp.reader.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.ldp.reader.R;
import com.ldp.reader.RxBus;
import com.ldp.reader.event.BookSyncEvent;
import com.ldp.reader.event.DeleteResponseEvent;
import com.ldp.reader.event.DeleteTaskEvent;
import com.ldp.reader.event.DownloadMessage;
import com.ldp.reader.event.RecommendBookEvent;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.local.BookRepository;
import com.ldp.reader.presenter.BookShelfPresenter;
import com.ldp.reader.presenter.contract.BookShelfContract;
import com.ldp.reader.ui.activity.ReadActivity;
import com.ldp.reader.ui.activity.SearchActivity;
import com.ldp.reader.ui.adapter.CollBookAdapter;
import com.ldp.reader.ui.base.BaseMVPFragment;
import com.ldp.reader.utils.RxUtils;
import com.ldp.reader.utils.SharedPreUtils;
import com.ldp.reader.utils.ToastUtils;
import com.ldp.reader.widget.adapter.WholeAdapter;
import com.ldp.reader.widget.itemdecoration.DividerItemDecoration;
import com.ldp.reader.widget.refresh.ScrollRefreshRecyclerView;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ldp on 17-4-15.
 */

public class BookShelfFragment extends BaseMVPFragment<BookShelfContract.Presenter>
        implements BookShelfContract.View {
    private static final String TAG = "BookShelfFragment";
    @BindView(R.id.book_shelf_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    /************************************/
    private CollBookAdapter mCollBookAdapter;
    private FooterItemView mFooterItem;
    //是否是第一次进入
    private boolean isInit = true;

    @Override
    protected int getContentId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected BookShelfContract.Presenter bindPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        //添加Footer
        mCollBookAdapter = new CollBookAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
        mRvContent.setColorSchemeColors(getResources().getColor(R.color.light_pink));
    }

    @Override
    protected void initClick() {
        super.initClick();
        //推荐书籍
        Disposable recommendDisp = RxBus.getInstance()
                .toObservable(RecommendBookEvent.class)
                .subscribe(
                        event -> {
                            mRvContent.startRefresh();
//                            mPresenter.loadRecommendBooks(event.sex);
                        }
                );
        addDisposable(recommendDisp);

        Disposable downloadDisp = RxBus.getInstance()
                .toObservable(DownloadMessage.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            //使用Toast提示
                            ToastUtils.show(event.message);
                        }
                );
        addDisposable(downloadDisp);


        Disposable bookSyncDisp = RxBus.getInstance()
                .toObservable(BookSyncEvent.class)
                .subscribe(new Consumer<BookSyncEvent>() {
                    @Override
                    public void accept(BookSyncEvent bookSyncEvent) throws Exception {
                        String token = SharedPreUtils.getInstance().getString("token");
                        if (TextUtils.isEmpty(token)) {
                            ToastUtils.show("请登录");
                            return;
                        } else if ("password".equals(SharedPreUtils.getInstance().getString("loginType"))) {
                            mPresenter.getBookShelf(token);
                        } else {
                            String mobile = SharedPreUtils.getInstance().getString("userName");
                            mPresenter.getBookShelfByMobile(mobile, token);
                        }
                    }
                });
        addDisposable(bookSyncDisp);


        //删除书籍 (写的丑了点)
        Disposable deleteDisp = RxBus.getInstance()
                .toObservable(DeleteResponseEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            if (event.isDelete) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("正在删除中");
                                progressDialog.show();
                                BookRepository.getInstance().deleteCollBookInRx(event.collBook)
                                        .compose(RxUtils::toSimpleSingle)
                                        .subscribe(
                                                (Void) -> {
                                                    mCollBookAdapter.removeItem(event.collBook);
                                                    progressDialog.dismiss();
                                                }
                                        );
                            } else {
                                //弹出一个Dialog
                                AlertDialog tipDialog = new AlertDialog.Builder(getContext())
                                        .setTitle("您的任务正在加载")
                                        .setMessage("先请暂停任务再进行删除")
                                        .setPositiveButton("确定", (dialog, which) -> {
                                            dialog.dismiss();
                                        }).create();
                                tipDialog.show();
                            }
                        }
                );
        addDisposable(deleteDisp);

        mRvContent.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        mPresenter.updateCollBooks(mCollBookAdapter.getItems());
                    }
                }
        );

        mCollBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //如果是本地文件，首先判断这个文件是否存在
                    CollBookBean collBook = mCollBookAdapter.getItem(pos);
                    if (collBook.isLocal()) {
                        //id表示本地文件的路径
                        String path = collBook.getCover();
                        File file = new File(path);
                        //判断这个本地文件是否存在
                        if (file.exists() && file.length() != 0) {
                            ReadActivity.startActivity(getContext(),
                                    mCollBookAdapter.getItem(pos), true);
                        } else {
                            String tip = getContext().getString(R.string.nb_bookshelf_book_not_exist);
                            //提示(从目录中移除这个文件)
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getResources().getString(R.string.nb_common_tip))
                                    .setMessage(tip)
                                    .setPositiveButton(getResources().getString(R.string.nb_common_sure),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    deleteBook(collBook);
                                                }
                                            })
                                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                                    .show();
                        }
                    } else {
                        ReadActivity.startActivity(getContext(),
                                mCollBookAdapter.getItem(pos), true);
                    }
                }
        );

        mCollBookAdapter.setOnItemLongClickListener(
                (v, pos) -> {
                    //开启Dialog,最方便的Dialog,就是AlterDialog
                    openItemDialog(mCollBookAdapter.getItem(pos));
                    return true;
                }
        );
    }

     RxPermissions rxPermissions ;

    @Override
    protected void processLogic() {

        super.processLogic();
      //  mRvContent.startRefresh();
    }

    @SuppressLint("CheckResult")
    private void getPermission() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                });
    }

    private void openItemDialog(CollBookBean collBook) {
        String[] menus;
        if (collBook.isLocal()) {
            menus = getResources().getStringArray(R.array.nb_menu_local_book);
        } else {
            menus = getResources().getStringArray(R.array.nb_menu_net_book);
        }
        AlertDialog collBookDialog = new AlertDialog.Builder(getContext())
                .setTitle(collBook.getTitle())
                .setAdapter(new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1, menus),
                        (dialog, which) -> onItemMenuClick(menus[which], collBook))
                .setNegativeButton(null, null)
                .setPositiveButton(null, null)
                .create();

        collBookDialog.show();
    }

    private void onItemMenuClick(String which, CollBookBean collBook) {
        switch (which) {
            //置顶
            case "置顶":
                break;
            //缓存
            case "缓存":
                //2. 进行判断，如果CollBean中状态为未更新。那么就创建Task，加入到Service中去。
                //3. 如果状态为finish，并且isUpdate为true，那么就根据chapter创建状态
                //4. 如果状态为finish，并且isUpdate为false。
                downloadBook(collBook);
                break;
            //删除
            case "删除":
                deleteBook(collBook);
                break;
            //批量管理
            case "批量管理":
                break;
            default:
                break;
        }
    }

    private void downloadBook(CollBookBean collBook) {
        //创建任务
        mPresenter.createDownloadTask(collBook);
    }

    /**
     * 默认删除本地文件
     *
     * @param collBook
     */
    private void deleteBook(CollBookBean collBook) {
        if (collBook.isLocal()) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_delete, null);
            CheckBox cb = (CheckBox) view.findViewById(R.id.delete_cb_select);
            new AlertDialog.Builder(getContext())
                    .setTitle("删除文件")
                    .setView(view)
                    .setPositiveButton(getResources().getString(R.string.nb_common_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isSelected = cb.isSelected();
                            if (isSelected) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("正在删除中");
                                progressDialog.show();
                                //删除
                                File file = new File(collBook.getCover());
                                if (file.exists()) file.delete();
                                BookRepository.getInstance().deleteCollBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());
                                List<CollBookBean> collBookBeans = BookRepository.getInstance().getCollBooks();
                                List<String> bookIds = new ArrayList<>();
                                for (CollBookBean collBookBean:collBookBeans ) {
                                    bookIds.add(collBookBean.get_id());
                                }
                                if ("password".equals(SharedPreUtils.getInstance().getString("loginType"))){
                                    mPresenter.setBookShelf(bookIds);
                                }else {
                                    String mobile  =  SharedPreUtils.getInstance().getString("userName");
                                    String mobileToken =  SharedPreUtils.getInstance().getString("token");
                                    mPresenter.setBookShelfByMobile(bookIds,mobile,mobileToken);
                                }
                                //从Adapter中删除
                                mCollBookAdapter.removeItem(collBook);
                                progressDialog.dismiss();
                            } else {
                                BookRepository.getInstance().deleteCollBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());
                                //从Adapter中删除
                                mCollBookAdapter.removeItem(collBook);
                            }
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                    .show();
        } else {
            BookRepository.getInstance().deleteCollBook(collBook);
            BookRepository.getInstance().deleteBookRecord(collBook.get_id());
            //从Adapter中删除
            mCollBookAdapter.removeItem(collBook);
            RxBus.getInstance().post(new DeleteTaskEvent(collBook));
        }
    }


    /*******************************************************************8*/

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        if (mCollBookAdapter.getItemCount() > 0 && mFooterItem == null) {
            mFooterItem = new FooterItemView();
            mCollBookAdapter.addFooterView(mFooterItem);
        }

        if (mRvContent.isRefreshing()) {
            mRvContent.finishRefresh();
        }
    }

    @Override
    public void finishRefresh(List<CollBookBean> collBookBeans) {
        mCollBookAdapter.refreshItems(collBookBeans);
        //如果是初次进入，则更新书籍信息
        if (isInit) {
            isInit = false;
            mRvContent.post(
                    () -> mPresenter.updateCollBooks(mCollBookAdapter.getItems())
            );
        }
    }

    @Override
    public void finishUpdate() {
        //重新从数据库中获取数据
        mCollBookAdapter.refreshItems(BookRepository
                .getInstance().getCollBooks());
    }

    @Override
    public void finishSyncBook() {
        ToastUtils.show("书架同步成功");
    }


    @Override
    public void showErrorTip(String error) {
        mRvContent.setTip(error);
        mRvContent.showTip();
    }

    /*****************************************************************/
    class FooterItemView implements WholeAdapter.ItemView {
        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.footer_book_shelf, parent, false);
            view.setOnClickListener(
                    (v) -> {
                        Intent intent = new Intent(getContext(),SearchActivity.class);
                        startActivity(intent);
                        //设置RxBus回调
                    }
            );
            return view;
        }

        @Override
        public void onBindView(View view) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        rxPermissions = new RxPermissions(this);
        getPermission();
        mPresenter.refreshCollBooks();
        getRegId();
    }

    String registrationId;
    private void getRegId(){
        Log.d(TAG, "preDirectLogin: registrationId");
        registrationId = SharedPreUtils.getInstance().getString("registrationId");
        Log.d(TAG, "onCallback: registrationId  " + registrationId);
        if (TextUtils.isEmpty(registrationId)){
            MobPush.getRegistrationId(new MobPushCallback<String>() {
                @Override
                public void onCallback(String s) {
                    Log.d(TAG, "onCallback: registrationId  " + s);
                    registrationId = s;
                    SharedPreUtils.getInstance().putString("registrationId",registrationId);
                }
            });
        }
    }
}
