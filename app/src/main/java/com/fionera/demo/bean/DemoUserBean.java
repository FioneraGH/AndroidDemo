package com.fionera.demo.bean;

import android.databinding.BaseObservable;

import com.fionera.demo.BR;
import com.fionera.demo.R;

/**
 * Created by fionera on 16-2-5.
 */
public class DemoUserBean extends BaseObservable{

    private boolean isAdult;
    private String firstName;
    private String lastName;

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR._all);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR._all);
    }
}
