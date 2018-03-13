package com.example.subhamdivakar.alice.Listener;

import org.json.JSONObject;

/**
 * Created by Administrator on 7/8/2016.
 */
public interface AsyncListener<T> {

    public T onPreDownload();

    public void onPostDownload(JSONObject result);

    public void onCancel();

}
