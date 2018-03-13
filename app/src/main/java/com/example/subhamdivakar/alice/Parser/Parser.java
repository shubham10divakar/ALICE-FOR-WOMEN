package com.example.subhamdivakar.alice.Parser;

import android.os.AsyncTask;

import com.example.subhamdivakar.alice.Listener.AsyncListener;
import com.example.subhamdivakar.alice.UTILS.JParserAdv;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Administrator on 7/8/2016.
 */
public class Parser<T> extends AsyncTask<String, String, JSONObject> {

    AsyncListener<T> asyncListener;
    JSONObject jsonObject;
    T beanParameter;
    HashMap<String, String> params;
    String URLCONNECTION;
//Setting The Listener

    public void setListener(AsyncListener asyncListener) {

        this.asyncListener = asyncListener;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (asyncListener != null) {


            beanParameter = asyncListener.onPreDownload();
            Class c1 = beanParameter.getClass();
            params=new HashMap<String,String>();

            Field[] valueObjFields = c1.getFields();

            // compare values now from 0 to 3
            for (int i = 0; i< valueObjFields.length; i++)
            {

                String fieldName = valueObjFields[i].getName();
                if(fieldName.equals("URL"))
                {
                    try {
                        URLCONNECTION=  valueObjFields[i].get(beanParameter).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                else
                {

                    try {
                        params.put(valueObjFields[i].getName(),valueObjFields[i].get(beanParameter).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }


            }



        }

    }


    @Override
    protected JSONObject doInBackground(String... param) {

        JParserAdv jParserAdv=new JParserAdv();
      JSONObject jsonObject=  jParserAdv.makeHttpRequest(URLCONNECTION,"GET",params);


        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        super.onPostExecute(jsonObject);
        if (asyncListener != null) {
            asyncListener.onPostDownload(jsonObject);

        }

    }
}
