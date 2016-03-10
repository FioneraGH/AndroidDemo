package com.fionera.demo.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fionera.demo.R;
import com.fionera.demo.adapter.ContactsAdapter;
import com.fionera.demo.bean.ContactBean;
import com.fionera.demo.util.ShowToast;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fionera on 15-10-3.
 */
public class ContactFragment
        extends BaseFragment {

    @ViewInject(R.id.rv_contact)
    private RecyclerView recyclerView;

    private List<ContactBean> contactBeanList;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initViews(View rootView) {

        setTitleBarText("人脉");

        contactBeanList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ContactBean bean = new ContactBean();
            bean.name.set("aaaa");
            bean.phone.set("1111");
            contactBeanList.add(bean);
        }

        ContactsAdapter contactsAdapter = new ContactsAdapter(mContext, contactBeanList,
                                                              recyclerView);
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        contactsAdapter.setRvItemTouchListener((v, pos) -> {
            if (pos > 5) {
                contactBeanList.get(pos).name.set(contactBeanList.get(pos).name.get() + " " + pos);
                recyclerView.getAdapter().notifyItemChanged(pos);
            } else {
                ShowToast.show(pos);
            }
        });
    }
}
