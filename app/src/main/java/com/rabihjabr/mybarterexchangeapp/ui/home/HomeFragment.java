package com.rabihjabr.mybarterexchangeapp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rabihjabr.mybarterexchangeapp.R;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setCategoriesAttributes(root);
        return root;
    }


    private void setCategoriesAttributes(View root) {
        View applianceCategory = root.findViewById(R.id.appliancesCategory);
        ImageView applianceImgView = applianceCategory.findViewById(R.id.myImageView);
        applianceImgView.setImageResource(R.drawable.appliances);
        TextView applianceTxtView = applianceCategory.findViewById(R.id.myImageViewText);
        applianceTxtView.setTextSize(30);
        applianceTxtView.setText("Appliances");

        View clothesCategory = root.findViewById(R.id.clothesCategory);
        ImageView clothesImgView = clothesCategory.findViewById(R.id.myImageView);
        clothesImgView.setImageResource(R.drawable.clothes);
        TextView clothesTxtView = clothesCategory.findViewById(R.id.myImageViewText);
        clothesTxtView.setText("Clothes");

        View devicesCategory = root.findViewById(R.id.devicesCategory);
        ImageView devicesImgView = devicesCategory.findViewById(R.id.myImageView);
        devicesImgView.setImageResource(R.drawable.devices);
        TextView devicesTxtView = devicesCategory.findViewById(R.id.myImageViewText);
        devicesTxtView.setText("Devices");

        View vehiclesCategory = root.findViewById(R.id.vehiclesCategory);
        ImageView vehiclesImgView = vehiclesCategory.findViewById(R.id.myImageView);
        vehiclesImgView.setImageResource(R.drawable.vehicles);
        TextView vehiclesTxtView = vehiclesCategory.findViewById(R.id.myImageViewText);
        vehiclesTxtView.setText("Vehicles");

        View toysCategory = root.findViewById(R.id.toysCategory);
        ImageView toysImgView = toysCategory.findViewById(R.id.myImageView);
        toysImgView.setImageResource(R.drawable.toys);
        TextView toysTxtView = toysCategory.findViewById(R.id.myImageViewText);
        toysTxtView.setText("Toys");

        View otherCategory = root.findViewById(R.id.otherCategory);
        ImageView otherImgView = otherCategory.findViewById(R.id.myImageView);
//        otherImgView.setImageResource(R.color.white);
        TextView otherTxtView = otherCategory.findViewById(R.id.myImageViewText);
        otherTxtView.setTextColor(R.color.black);
        otherTxtView.setText("Others");

    }
}