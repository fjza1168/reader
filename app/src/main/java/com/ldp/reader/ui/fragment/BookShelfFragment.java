package com.ldp.reader.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.Objects;

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
    //????????????????????????
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
        //??????Footer
        mCollBookAdapter = new CollBookAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
        mRvContent.setColorSchemeColors(getResources().getColor(R.color.light_pink));
    }

    @Override
    protected void initClick() {
        super.initClick();
        //????????????
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
                            //??????Toast??????
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
                            ToastUtils.show("?????????");
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


        //???????????? (???????????????)
        Disposable deleteDisp = RxBus.getInstance()
                .toObservable(DeleteResponseEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            if (event.isDelete) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("???????????????");
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
                                //????????????Dialog
                                AlertDialog tipDialog = new AlertDialog.Builder(getContext())
                                        .setTitle("????????????????????????")
                                        .setMessage("?????????????????????????????????")
                                        .setPositiveButton("??????", (dialog, which) -> {
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
                    //????????????????????????????????????????????????????????????
                    CollBookBean collBook = mCollBookAdapter.getItem(pos);
                    if (collBook.isLocal()) {
                        //id???????????????????????????
                        String path = collBook.getCover();
                        File file = new File(path);
                        //????????????????????????????????????
                        if (file.exists() && file.length() != 0) {
                            ReadActivity.startActivity(getContext(),
                                    mCollBookAdapter.getItem(pos), true);
                        } else {
                            String tip = getContext().getString(R.string.nb_bookshelf_book_not_exist);
                            //??????(??????????????????????????????)
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
                    //??????Dialog,????????????Dialog,??????AlterDialog
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
            //??????
            case "??????":
                break;
            //??????
            case "??????":
                //2. ?????????????????????CollBean???????????????????????????????????????Task????????????Service?????????
                //3. ???????????????finish?????????isUpdate???true??????????????????chapter????????????
                //4. ???????????????finish?????????isUpdate???false???
                downloadBook(collBook);
                break;
            //??????
            case "??????":
                deleteBook(collBook);
                break;
            //????????????
            case "????????????":
                break;
            default:
                break;
        }
    }

    private void downloadBook(CollBookBean collBook) {
        //????????????
        mPresenter.createDownloadTask(collBook);
    }

    /**
     * ????????????????????????
     *
     * @param collBook
     */
    private void deleteBook(CollBookBean collBook) {
        if (collBook.isLocal()) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_delete, null);
            CheckBox cb = (CheckBox) view.findViewById(R.id.delete_cb_select);
            new AlertDialog.Builder(getContext())
                    .setTitle("????????????")
                    .setView(view)
                    .setPositiveButton(getResources().getString(R.string.nb_common_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isSelected = cb.isSelected();
                            if (isSelected) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("???????????????");
                                progressDialog.show();
                                //??????
                                File file = new File(collBook.getCover());
                                if (file.exists()) file.delete();
                                BookRepository.getInstance().deleteCollBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());

                                //???Adapter?????????
                                mCollBookAdapter.removeItem(collBook);
                                progressDialog.dismiss();
                            } else {
                                BookRepository.getInstance().deleteCollBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());
                                //???Adapter?????????
                                mCollBookAdapter.removeItem(collBook);
                            }
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                    .show();
        } else {
            BookRepository.getInstance().deleteCollBook(collBook);
            BookRepository.getInstance().deleteBookRecord(collBook.get_id());
            //???Adapter?????????
            mCollBookAdapter.removeItem(collBook);
            synBook();
            RxBus.getInstance().post(new DeleteTaskEvent(collBook));
        }
    }

    private void  synBook(){
        List<CollBookBean> collBookBeans = BookRepository.getInstance().getCollBooks();
        List<String> bookIds = new ArrayList<>();
        for (CollBookBean collBookBean:collBookBeans ) {
            if(!collBookBean.isLocal()){
                bookIds.add(collBookBean.get_id());
            }
        }
        if ("password".equals(SharedPreUtils.getInstance().getString("loginType"))){
            mPresenter.setBookShelf(bookIds);
        }else {
            Log.e(TAG, "onClick:  ??????????????? ????????????");
            String mobile  =  SharedPreUtils.getInstance().getString("userName");
            String mobileToken =  SharedPreUtils.getInstance().getString("token");
            mPresenter.setBookShelfByMobile(bookIds,mobile,mobileToken);
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
        //?????????????????????????????????????????????
        if (isInit) {
            isInit = false;
            mRvContent.post(
                    () -> mPresenter.updateCollBooks(mCollBookAdapter.getItems())
            );
        }
    }

    @Override
    public void finishUpdate() {
        //?????????????????????????????????
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCollBookAdapter.refreshItems(BookRepository
                        .getInstance().getCollBooks());
            }
        });

    }

    @Override
    public void finishSyncBook() {
        ToastUtils.show("??????????????????");
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
                        //??????RxBus??????
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
        MobPush.setShowBadge(true);
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
