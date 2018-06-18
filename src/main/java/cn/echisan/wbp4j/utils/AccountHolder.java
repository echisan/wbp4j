package cn.echisan.wbp4j.utils;

import java.util.List;

/**
 * Created by echisan on 2018/6/17
 */
public class AccountHolder {

    private static List<Account> accounts;

    public static void setAccounts(List<Account> accounts) {
        AccountHolder.accounts = accounts;
    }

    public static List<Account> getAccounts() {
        return accounts;
    }

    public static boolean isEmpty(){
        return accounts.isEmpty();
    }
}
