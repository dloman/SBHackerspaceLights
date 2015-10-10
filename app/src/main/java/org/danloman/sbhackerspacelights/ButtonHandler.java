package org.danloman.sbhackerspacelights;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by dlom on 7/23/15.
 */
public class ButtonHandler {
    public ButtonHandler(ButtonFragment Fragment, final String Url, String room, String type, String field) {
        mFragment = Fragment;
        mRoom = room;
        mUrl = Url;
        mType = type;
        mField = field;
        mFragment.setButtonText(mRoom, "Connecting to Server");
        makeRequest(Url + "status", field);
    }

    private void makeRequest(final String url, final String field) {
        Thread requestThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 2000);
                    HttpConnectionParams.setSoTimeout(httpClient.getParams(), 3000);
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if (jsonObject.has(field)) {
                        if (jsonObject.getBoolean(field)) {
                            setStatus("off");
                        } else {
                            setStatus("on");
                        }
                    }
                } catch (Exception e) {
                    setStatus("down");
                }
            }
        });
        requestThread.start();
    }

    public void toggleButton() {
        if (mStatus.contains("on")) {
            makeRequest(mUrl + "on", mField);
        }
        else {
            makeRequest(mUrl + "off", mField);
        }
    }


    private void setStatus(String status){
        if (status.equals("down")) {
            mFragment.setButtonText(mRoom, "Cannot connect to " + mRoom + mType + " :(");
        } else {
            mFragment.setButtonText(mRoom, "Turn " + mRoom + " " + mType + " " + status);
        }
        mStatus = status;
    }

    private String mRoom, mUrl, mStatus, mType, mField;
    private ButtonFragment mFragment;
}
