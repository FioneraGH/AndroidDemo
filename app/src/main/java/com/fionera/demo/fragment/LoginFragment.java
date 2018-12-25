package com.fionera.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fionera.demo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author fionera
 */
public class LoginFragment
        extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TextInputEditText actvLoginEmail;
    private TextInputEditText etLoginPassword;
    private Button emailSignInButton;
    private TextInputLayout tilLoginEmail;
    private TextInputLayout tilLoginPassword;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        emailSignInButton.setOnClickListener(item -> {
            tilLoginEmail.setErrorEnabled(false);
            tilLoginPassword.setErrorEnabled(false);
            switch (adjustLoginInfo()) {
                case 0:
                    mListener.onFragmentInteraction("登录成功");
                    break;
                case 1:
                    tilLoginEmail.setError("email too short");
                    tilLoginEmail.setErrorEnabled(true);
                    mListener.onFragmentInteraction("登录失败");
                    break;
                case 2:
                    tilLoginPassword.setError("password too short");
                    tilLoginPassword.setErrorEnabled(true);
                    mListener.onFragmentInteraction("登录失败");
                    break;
                case 3:
                    tilLoginEmail.setError("email error");
                    tilLoginPassword.setError("password error");
                    tilLoginEmail.setErrorEnabled(true);
                    tilLoginPassword.setErrorEnabled(true);
                    mListener.onFragmentInteraction("登录失败");
                    break;
                    default:
                        break;
            }
        });
    }

    private int adjustLoginInfo() {
        if (actvLoginEmail.getText().toString().length() < 6) {
            return 1;
        }
        if (etLoginPassword.getText().toString().length() < 6) {
            return 2;
        }
        if (!TextUtils.equals("hello0", actvLoginEmail.getText().toString()) || !TextUtils.equals(
                "world1", etLoginPassword.getText().toString())) {
            return 3;
        }
        return 0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initView(View view) {
        actvLoginEmail = view.findViewById(R.id.et_login_email);
        etLoginPassword = view.findViewById(R.id.et_login_password);
        emailSignInButton = view.findViewById(R.id.btn_email_sign_in);
        tilLoginEmail = view.findViewById(R.id.til_login_email);
        tilLoginPassword = view.findViewById(R.id.til_login_password);
    }

    public interface OnFragmentInteractionListener {
        /**
         * callback
         * @param result result
         */
        void onFragmentInteraction(String result);
    }
}
