package com.aid.sms723.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

public class LocalStorage {
    private static final String KEY_FIREBASE_TOKEN = "firebaseToken";
    public static final String KEY_USER = "LimitOld";

    public static final String KEY_USER1 = "LimitNEW";
    public static final String KEY_USER2 = "pass";
    public static final String SELLCOUNT = "sotibOlishlar";
    public static final String KEY_USER_ADDRESS = "user_address";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String LIST = "list";
    private static LocalStorage instance = null;
    public SharedPreferences mySharedPref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
     SharedPreferences permissionStatus;
    Context _context;
    public LocalStorage(Context context) {
        mySharedPref = context.getSharedPreferences("Preferences", 0);
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalStorage.class) {
                if (instance == null) {
                    instance = new LocalStorage(context);
                }
            }
        }
        return instance;
    }
    public void setArray(ArrayList<String>array){
        ArrayList<String>  setlist = new ArrayList<>();
        setlist.addAll(array);
        editor.putStringSet("key", (Set<String>) setlist);
        editor.apply();

    }
    public ArrayList<String> getArray(){

    ArrayList<String> set = (ArrayList<String>) mySharedPref.getStringSet("key", null);
        return set;
    }

    public void setPermissionState(Boolean state){
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(Manifest.permission.SEND_SMS, state);
        editor.apply();
    }
    public Boolean getPermissionState(){
        Boolean state = mySharedPref.getBoolean(Manifest.permission.SEND_SMS, false);
        return state;
    }


    public void createUserLoginSession(String user) {
        editor = mySharedPref.edit();
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USER, user);
        editor.apply();
    }

    public String getUserLogin() {
        return mySharedPref.getString(KEY_USER, "");

    }


    public void logoutUser() {
        editor = mySharedPref.edit();
        editor.clear();
        editor.apply();
    }
    public void savePassword(String password) {
        editor = mySharedPref.edit();
        editor.putString(KEY_USER2, password);
        editor.apply();
    }
    public String getSavePassword() {
        return mySharedPref.getString(KEY_USER2, "");
    }
    public void saveLimit(String limit) {
        editor = mySharedPref.edit();
        editor.putString(KEY_USER, limit);
        editor.apply();
    }
    public String getLimit() {
        return mySharedPref.getString(KEY_USER, "");
    }
    public void saveLimitNewSell(String limit) {
        editor = mySharedPref.edit();
        editor.putString(KEY_USER1, limit);
        editor.apply();
    }
    public String getLimitNewSell() {
        return mySharedPref.getString(KEY_USER1, "");
    }
    public void saveSEllCount(int count) {
        editor = mySharedPref.edit();
        editor.putInt(SELLCOUNT, count);
        editor.apply();
    }
    public String getSellcount() {
        return String.valueOf(mySharedPref.getInt(SELLCOUNT, 1));
    }





    public void saveListSize(String listSize) {
        editor = mySharedPref.edit();
        editor.putString(KEY_USER, listSize);
        editor.apply();
    }




    public String getListSize() {
        return mySharedPref.getString(KEY_USER, "");
    }

    public boolean checkLogin() {
        // Check login status
        return !this.isUserLoggedIn();
    }


    public boolean isUserLoggedIn() {
        return mySharedPref.getBoolean(IS_USER_LOGIN, false);
    }

    public String getUserAddress() {
        if (mySharedPref.contains(KEY_USER_ADDRESS))
            return mySharedPref.getString(KEY_USER_ADDRESS, null);
        else return null;
    }


    public void setUserAddress(String user_address) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putString(KEY_USER_ADDRESS, user_address);
        editor.apply();
    }

    public String getCart() {
        if (mySharedPref.contains("CART"))
            return mySharedPref.getString("CART", null);
        else return null;
    }


    public void setCart(String cart) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putString("CART", cart);
        editor.apply();
    }

    public void deleteCart() {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.remove("CART");
        editor.apply();
    }


    public String getOrder() {
        if (mySharedPref.contains("ORDER"))
            return mySharedPref.getString("ORDER", null);
        else return null;
    }


    public void setOrder(String order) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putString("ORDER", order);
        editor.apply();
    }

    public void deleteOrder() {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.remove("ORDER");
        editor.apply();
    }


    public String getFirebaseToken() {
        return mySharedPref.getString(KEY_FIREBASE_TOKEN, null);
    }

    public void setFirebaseToken(String firebaseToken) {
        editor = mySharedPref.edit();
        editor.putString(KEY_FIREBASE_TOKEN, firebaseToken);
        editor.apply();
    }
    //Bu method tungi rejimni saqlaydi, True yoki False
    public void setNightModeState (Boolean state){
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }
    //Bu method Tungi rejimni load qiladi
    public Boolean loadNightMode(){
        Boolean state = mySharedPref.getBoolean("NightMode", false);
        return state;
    }

}

