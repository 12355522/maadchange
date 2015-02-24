package com.madchya;

/**
 * Created by steven on 2015/2/15.
 */
public class AccountContent {
    private String AccountName;
    private String AccountLogin;

    public AccountContent(String AccountName, String AccountLogic){
        this.AccountName  = AccountName;
        this.AccountLogin = AccountLogic;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getAccountLogin() {
        return AccountLogin;
    }

    public void setAccountLogin(String accountLogin) {
        AccountLogin = accountLogin;
    }
}
