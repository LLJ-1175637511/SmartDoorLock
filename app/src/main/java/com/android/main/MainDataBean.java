package com.android.main;

import com.llj.baselib.IOTInterfaceId;

public class MainDataBean {

    @IOTInterfaceId("23004")
    private int isHavePeople;

    @IOTInterfaceId("23023")
    private int isPwdErrorAlert;

    @IOTInterfaceId("23004")
    private int isHavePeopleAlert;

    public int getIsHavePeople() {
        return isHavePeople;
    }

    public void setIsHavePeople(int isHavePeople) {
        this.isHavePeople = isHavePeople;
    }

    public int getIsPwdErrorAlert() {
        return isPwdErrorAlert;
    }

    public void setIsPwdErrorAlert(int isPwdErrorAlert) {
        this.isPwdErrorAlert = isPwdErrorAlert;
    }

    public int getIsHavePeopleAlert() {
        return isHavePeopleAlert;
    }

    public void setIsHavePeopleAlert(int isHavePeopleAlert) {
        this.isHavePeopleAlert = isHavePeopleAlert;
    }

}
