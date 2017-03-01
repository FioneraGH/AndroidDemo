package com.fionera.demo.fragment;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.view.View;
import android.widget.Button;

import com.fionera.base.fragment.BaseFragment;
import com.fionera.demo.R;

/**
 * ContactFragment
 * Created by fionera on 15-10-3.
 */

public class ContactFragment
        extends BaseFragment {

    private ConstraintLayout clContact;
    private Button btnContactApply;
    private Button btnContactReset;
    private Button btnContact1;
    private Button btnContact2;
    private Button btnContact3;

    private ConstraintSet applyConstraintSet = new ConstraintSet();
    private ConstraintSet resetConstraintSet = new ConstraintSet();

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initViews(View rootView) {
        initView(rootView);

        applyConstraintSet.clone(clContact);
        resetConstraintSet.clone(clContact);
    }

    private void initView(View rootView) {
        clContact = (ConstraintLayout) rootView.findViewById(R.id.cl_contact);
        btnContactApply = (Button) rootView.findViewById(R.id.btn_contact_apply);
        btnContactApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(clContact);

                applyConstraintSet.setMargin(R.id.btn_contact1, ConstraintSet.START, 0);
                applyConstraintSet.setMargin(R.id.btn_contact1, ConstraintSet.TOP, 0);
                applyConstraintSet.setMargin(R.id.btn_contact1, ConstraintSet.END, 0);
                applyConstraintSet.setMargin(R.id.btn_contact1, ConstraintSet.BOTTOM, 0);
                applyConstraintSet.setMargin(R.id.btn_contact2, ConstraintSet.START, 0);
                applyConstraintSet.setMargin(R.id.btn_contact2, ConstraintSet.END, 0);
                applyConstraintSet.setMargin(R.id.btn_contact3, ConstraintSet.START, 0);
                applyConstraintSet.setMargin(R.id.btn_contact3, ConstraintSet.END, 0);

                applyConstraintSet.constrainWidth(R.id.btn_contact2, 500);

                applyConstraintSet.centerHorizontally(R.id.btn_contact1, R.id.cl_contact);
                applyConstraintSet.centerVertically(R.id.btn_contact1, R.id.cl_contact);
                applyConstraintSet.centerHorizontally(R.id.btn_contact2, R.id.cl_contact);
                applyConstraintSet.centerHorizontally(R.id.btn_contact3, R.id.cl_contact);

                applyConstraintSet.clear(R.id.btn_contact1);

                applyConstraintSet.connect(R.id.btn_contact1, ConstraintSet.START, R.id.cl_contact,
                        ConstraintSet.START, 0);
                applyConstraintSet.connect(R.id.btn_contact1, ConstraintSet.END, R.id.cl_contact,
                        ConstraintSet.END, 0);
                applyConstraintSet.connect(R.id.btn_contact1, ConstraintSet.TOP, R.id.cl_contact,
                        ConstraintSet.TOP, 0);
                applyConstraintSet.connect(R.id.btn_contact1, ConstraintSet.BOTTOM, R.id.cl_contact,
                        ConstraintSet.BOTTOM, 0);

                applyConstraintSet.setVisibility(R.id.btn_contact1, View.GONE);

                applyConstraintSet.applyTo(clContact);
            }
        });
        btnContactReset = (Button) rootView.findViewById(R.id.btn_contact_reset);
        btnContactReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetConstraintSet.applyTo(clContact);
            }
        });
        btnContact1 = (Button) rootView.findViewById(R.id.btn_contact1);
        btnContact2 = (Button) rootView.findViewById(R.id.btn_contact2);
        btnContact3 = (Button) rootView.findViewById(R.id.btn_contact3);
    }
}
