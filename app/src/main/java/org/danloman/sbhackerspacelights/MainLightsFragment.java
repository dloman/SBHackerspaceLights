package org.danloman.sbhackerspacelights;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class MainLightsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mainlightslayout, container, false);

        makeSureWifiConnectionCorrect();

        mClassroomLightsButton = (Button) rootView.findViewById(R.id.ClassroomLightsButton);
        mWorkroomLightsButton = (Button) rootView.findViewById(R.id.WorkroomLightsButton);
        mClassroomLightsButtonHandler = new ButtonHandler(this, "http://classroom-lights.west.sbhackerspace.com/", "classroom");
        mWorkroomLightsButtonHandler = new ButtonHandler(this, "http://workroom-lights.west.sbhackerspace.com/", "workroom");
        mClassroomLightsButton.setOnClickListener(lightsListener);
        mWorkroomLightsButton.setOnClickListener(lightsListener);

        return rootView;

    }

    View.OnClickListener lightsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            makeSureWifiConnectionCorrect();
            int viewId = v.getId();
            switch (viewId) {
                case R.id.ClassroomLightsButton:
                    mClassroomLightsButtonHandler.toggleButton();
                    break;
                case R.id.WorkroomLightsButton:
                    mWorkroomLightsButtonHandler.toggleButton();
                    break;
            }
        }
    };

    public void setButtonText(final String room, final String buttonText)
    {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (room.equals("classroom")) {
                            mClassroomLightsButton.setText(buttonText);
                        } else {
                            mWorkroomLightsButton.setText(buttonText);
                        }
                    }
                });
    }

    private void makeSureWifiConnectionCorrect() {
        WifiManager wifiManager = (WifiManager) getActivity().getSystemService(getActivity().WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (!wifiInfo.getSSID().contains("SBHX")) {
            Toast.makeText(getActivity(), "Must be on SBHX Wifi Network", Toast.LENGTH_LONG).show();
            startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
        }
    }
    private ButtonHandler mClassroomLightsButtonHandler, mWorkroomLightsButtonHandler;
    private Button mClassroomLightsButton, mWorkroomLightsButton;
}