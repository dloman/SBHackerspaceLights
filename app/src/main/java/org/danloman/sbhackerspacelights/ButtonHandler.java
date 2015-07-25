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
    public ButtonHandler(MainLightsFragment mainLightsFragment, final String Url, String room) {
        mMainLightsFragment = mainLightsFragment;
        mRoom = room;
        mUrl = Url;
        mMainLightsFragment.setButtonText(mRoom, "Connecting to Server");
        makeRequest(Url + "status");
    }

    private void makeRequest(final String url) {
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
                    if (jsonObject.get("lights").equals("1")) {
                        setStatus("off");
                    } else if (jsonObject.get("lights").equals("0")) {
                        setStatus("on");
                    } else {
                        setStatus("on");
                        MainActivity.showToast();
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
            makeRequest(mUrl + "on");
        }
        else {
            makeRequest(mUrl + "off");
        }
    }


    private void setStatus(String status){
        if (status.equals("down")) {
            mMainLightsFragment.setButtonText(mRoom, "Cannot connect to " + mRoom + " Lights :(");
        } else {
            mMainLightsFragment.setButtonText(mRoom, "Turn " + mRoom + " Lights " + status);
        }
        mStatus = status;
    }

    private String mRoom, mUrl, mStatus;
    private MainLightsFragment mMainLightsFragment;
}
