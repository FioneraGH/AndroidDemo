package com.fionera.demo.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fionera.demo.R;
import com.fionera.demo.adapter.ContactsAdapter;
import com.fionera.demo.util.ShowToast;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by fionera on 15-10-3.
 */
public class ContactFragment extends BaseFragment {

    @ViewInject(R.id.rv_contact)
    private RecyclerView recyclerView;

    private ContactsAdapter contactsAdapter;

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    public void findViewInThisFunction(View rootView) {

        setTitleBarText("联系人");

        contactsAdapter = new ContactsAdapter(mContext);
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        contactsAdapter.setRvItemTouchListener((v, pos) -> ShowToast.show(pos + ""));
    }
}
