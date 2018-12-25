package com.fionera.demo.fragment;

import android.view.View;
import android.widget.Button;

import com.fionera.base.fragment.BaseFragment;
import com.fionera.demo.R;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.TransitionManager;

/**
 * ConstraintSetFragment
 *
 * @author fionera
 * @date 15-10-3
 */

public class ConstraintSetFragment
        extends BaseFragment {

    private ConstraintLayout clContact;

    private ConstraintSet applyConstraintSet = new ConstraintSet();
    private ConstraintSet resetConstraintSet = new ConstraintSet();
    private ConstraintSet chainConstraintSet = new ConstraintSet();

    @Override
    public int setLayoutResource() {
        return R.layout.fragment_constraint_set;
    }

    @Override
    public void initViews(View rootView) {
        initView(rootView);

        applyConstraintSet.clone(clContact);
        resetConstraintSet.clone(clContact);
        chainConstraintSet.clone(clContact);
    }

    private void initView(View rootView) {
        clContact = rootView.findViewById(R.id.cl_contact);
        Button btnContactApply = rootView.findViewById(R.id.btn_contact_apply);
        btnContactApply.setOnClickListener(v -> {
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

            applyConstraintSet.constrainHeight(R.id.btn_contact1, ConstraintSet.WRAP_CONTENT);
            applyConstraintSet.connect(R.id.btn_contact1, ConstraintSet.START, R.id.cl_contact,
                    ConstraintSet.START, 0);
            applyConstraintSet.connect(R.id.btn_contact1, ConstraintSet.END, R.id.cl_contact,
                    ConstraintSet.END, 0);

            applyConstraintSet.setVisibility(R.id.btn_contact4, View.GONE);

            applyConstraintSet.applyTo(clContact);
        });
        Button btnContactChain = rootView.findViewById(R.id.btn_contact_chain);
        btnContactChain.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(clContact);

            chainConstraintSet.clear(R.id.btn_contact1);
            chainConstraintSet.clear(R.id.btn_contact2);
            chainConstraintSet.clear(R.id.btn_contact3);
            chainConstraintSet.clear(R.id.btn_contact4);

            chainConstraintSet.constrainWidth(R.id.btn_contact1, ConstraintSet.WRAP_CONTENT);
            chainConstraintSet.constrainWidth(R.id.btn_contact2, ConstraintSet.WRAP_CONTENT);
            chainConstraintSet.constrainWidth(R.id.btn_contact3, ConstraintSet.WRAP_CONTENT);
            chainConstraintSet.constrainWidth(R.id.btn_contact4, ConstraintSet.WRAP_CONTENT);

            chainConstraintSet.constrainHeight(R.id.btn_contact1, ConstraintSet.WRAP_CONTENT);
            chainConstraintSet.constrainHeight(R.id.btn_contact2, ConstraintSet.WRAP_CONTENT);
            chainConstraintSet.constrainHeight(R.id.btn_contact3, ConstraintSet.WRAP_CONTENT);
            chainConstraintSet.constrainHeight(R.id.btn_contact4, ConstraintSet.WRAP_CONTENT);

            chainConstraintSet.connect(R.id.btn_contact1, ConstraintSet.START, R.id.cl_contact,
                    ConstraintSet.START, 0);
            chainConstraintSet.connect(R.id.btn_contact4, ConstraintSet.END, R.id.cl_contact,
                    ConstraintSet.END, 0);
            chainConstraintSet.connect(R.id.btn_contact2, ConstraintSet.START,
                    R.id.btn_contact1, ConstraintSet.END, 0);
            chainConstraintSet.connect(R.id.btn_contact1, ConstraintSet.END, R.id.btn_contact2,
                    ConstraintSet.START, 0);
            chainConstraintSet.connect(R.id.btn_contact3, ConstraintSet.START,
                    R.id.btn_contact2, ConstraintSet.END, 0);
            chainConstraintSet.connect(R.id.btn_contact2, ConstraintSet.END, R.id.btn_contact3,
                    ConstraintSet.START, 0);
            chainConstraintSet.connect(R.id.btn_contact4, ConstraintSet.START,
                    R.id.btn_contact3, ConstraintSet.END, 0);
            chainConstraintSet.connect(R.id.btn_contact3, ConstraintSet.END, R.id.btn_contact4,
                    ConstraintSet.START, 0);

            /*
            rtl lose effect
             */
            chainConstraintSet.createHorizontalChain(R.id.btn_contact1, ConstraintSet.LEFT,
                    R.id.btn_contact4, ConstraintSet.RIGHT,
                    new int[]{R.id.btn_contact1, R.id.btn_contact4}, null,
                    ConstraintWidget.CHAIN_SPREAD);

            chainConstraintSet.applyTo(clContact);
        });
        Button btnContactReset = rootView.findViewById(R.id.btn_contact_reset);
        btnContactReset.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(clContact);

            resetConstraintSet.applyTo(clContact);
        });
    }
}
