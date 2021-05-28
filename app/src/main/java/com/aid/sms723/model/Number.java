package com.aid.sms723.model;

import android.telephony.SmsManager;

import java.util.ArrayList;

public class Number {
    private String num;
    private int size;
    private int limitSize;

    private ArrayList<String> num_list;

    public Number() {
    }

    public Number(int size) {
           this.num_list = new ArrayList<>(size);
           this.size = size;
      }
      public void add(String number) {
              if(this.getNum_list().size() < this.getMaxSize()) {
                  this.num_list.add(String.valueOf(number));
              }

          }
    // Send message to each phone number of this Batch
    public void sendMessage(String message) {
        for (int i = 0; i < this.getNum_list().size(); i++) {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(this.num_list .get(i), null, message, null, null);
        }
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    public Number(ArrayList<String> num_list) {
        this.num_list = num_list;
    }

    public Number(String num) {
        this.num = num;
    }

    public ArrayList<String> getNum_list() {
        return this.num_list;
    }


    public int getMaxSize() {
        return this.size;
    }
     public static Number parse(ArrayList<String> num) {
          return new Number(num);
       }
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }



}








