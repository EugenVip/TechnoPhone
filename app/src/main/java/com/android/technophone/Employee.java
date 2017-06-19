package com.android.technophone;

/**
 * Created by User on 15.06.2017.
 */

public class Employee {

    private String mNameEmployee;
    private String mPhoneNumber;

    public Employee(String nameEmployee, String phoneNumber){

        mNameEmployee = nameEmployee;
        mPhoneNumber = phoneNumber;

    }

    public void setmNameEmployee(String mNameEmployee) {
        this.mNameEmployee = mNameEmployee;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmNameEmployee() {
        return mNameEmployee;
    }

    public String getmPhoneNumber() {

        return mPhoneNumber;
    }

    @Override
    public String toString() {
        return ""+mNameEmployee + " - "+mPhoneNumber;
    }
}
