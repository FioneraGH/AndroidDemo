package com.fionera.demo.popupwindow;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fionera.demo.R;
import com.fionera.demo.util.LogCat;
import com.fionera.demo.view.wheelview.ArrayWheelAdapter;
import com.fionera.demo.view.wheelview.OnWheelChangedListener;
import com.fionera.demo.view.wheelview.ProvinceResultBean;
import com.fionera.demo.view.wheelview.WheelView;
import com.fionera.demo.view.wheelview.XmlParserHandler;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ProvincePopup
        extends PopupWindow implements OnWheelChangedListener {

    private Context context;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    private GetValueCallback getValueCallback;

    public interface GetValueCallback {
        void getValue(String province, String city, String district, String zipCode);
    }

    public void setGetValueCallback(GetValueCallback getValueCallback) {
        this.getValueCallback = getValueCallback;
    }

    /**
     * 所有省
     */
    private String[] mProvinceData;
    /**
     * key - 省 value - 市
     */
    private Map<String, String[]> mCitiesDataMap = new HashMap<>();
    /**
     * key - 市 values - 区
     */
    private Map<String, String[]> mDistrictDataMap = new HashMap<>();

    /**
     * key - 区 values - 邮编
     */
    private Map<String, String> mZipCodeDataMap = new HashMap<>();

    /**
     * 当前省的名称
     */
    private String mCurrentProvinceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    /**
     * 当前区的名称
     */
    private String mCurrentDistrictName;

    /**
     * 当前区的邮政编码
     */
    private String mCurrentZipCode = "";

    public ProvincePopup(Context context) {
        this.context = context;

        View view = View.inflate(context, R.layout.dialog_change_province_pop, null);
        setContentView(view);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_city_picker_cancel);
        TextView tv_save = (TextView) view.findViewById(R.id.tv_city_picker_confirm);
        mViewProvince = (WheelView) view.findViewById(R.id.wl_province_picker);
        mViewCity = (WheelView) view.findViewById(R.id.wl_city_picker);
        mViewDistrict = (WheelView) view.findViewById(R.id.wl_district_picker);
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getValueCallback != null) {
                    getValueCallback.getValue(mCurrentProvinceName, mCurrentCityName,
                            mCurrentDistrictName, mCurrentZipCode);
                }
                dismiss();
            }
        });

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setAnimationStyle(android.R.style.Animation_InputMethod);
        setBackgroundDrawable(new ColorDrawable(0));

        setUpData();

        update();
    }

    public void setValue(String address) {
        /*
          update province
         */
        if(TextUtils.isEmpty(address)){
            return;
        }
        LogCat.d("Province Pop Set Address:" + address);
        String addresses[] = address.split(":");
        LogCat.d("Province Pop Set Address List Size:" + addresses.length);
        if (addresses.length != 3) {
            return;
        }
        if (TextUtils.isEmpty(addresses[0])) {
            return;
        }
        int i = 0, length = mProvinceData.length;
        for (; i < length; i++) {
            if (addresses[0].equals(mProvinceData[i])) {
                break;
            }
        }
        if (i >= length) {
            return;
        }
        mViewProvince.setCurrentItem(i);
        mCurrentProvinceName = addresses[0];

        updateCities();

        i = 0;
        String cities[] = mCitiesDataMap.get(addresses[0]);
        length = cities.length;
        for (; i < length; i++) {
            if (addresses[1].equals(cities[i])) {
                break;
            }
        }
        if (i >= length) {
            return;
        }
        mViewCity.setCurrentItem(i);
        mCurrentCityName = addresses[1];

        updateAreas();

        i = 0;
        String[] areas = mDistrictDataMap.get(addresses[1]);
        length = areas.length;
        for (; i < length; i++) {
            if (addresses[2].equals(areas[i])) {
                break;
            }
        }
        if (i >= length) {
            return;
        }
        mViewDistrict.setCurrentItem(i);
        mCurrentDistrictName = addresses[2];
        mCurrentZipCode = mZipCodeDataMap.get(addresses[2]);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDataMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipCodeDataMap.get(mCurrentDistrictName);
        }
    }

    private void setUpData() {
//        initProvinceData();
        initProvinceDataByJson();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(context, mProvinceData));
        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        mViewDistrict.setVisibleItems(5);
        updateCities();
    }

    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProvinceName = mProvinceData[pCurrent];
        String[] cities = mCitiesDataMap.get(mCurrentProvinceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(context, cities));
        mViewCity.setCurrentItem(0);

        updateAreas();
    }

    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitiesDataMap.get(mCurrentProvinceName)[pCurrent];
        String[] areas = mDistrictDataMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(context, areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = mDistrictDataMap.get(mCurrentCityName)[0];
        mCurrentZipCode = mZipCodeDataMap.get(mCurrentDistrictName);
    }

    /**
     * init data method {use xml parser}
     */
    private void initProvinceData() {
        List<ProvinceResultBean.ProvinceModel> provinceList;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            provinceList = handler.getDataList();

            parseDataList(provinceList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initProvinceDataByJson(){
        List<ProvinceResultBean.ProvinceModel> provinceList;
        String res;
        AssetManager a = context.getAssets();
        try {
            InputStream in = a.open("province_data.json");
            int length = in.available();
            byte[] buffer = new byte[length];
            LogCat.d("Province Pop Read String:" + in.read(buffer));
            res = new String(buffer, Charset.defaultCharset());
            in.close();
            provinceList = JSON.parseObject(res, ProvinceResultBean.class).getProvinceModelList();

            parseDataList(provinceList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDataList(List<ProvinceResultBean.ProvinceModel> provinceList) {
        if (provinceList != null && !provinceList.isEmpty()) {
            mCurrentProvinceName = provinceList.get(0).getName();
            List<ProvinceResultBean.ProvinceModel.CityModel> cityList = provinceList.get(0).getCityList();
            if (cityList != null && !cityList.isEmpty()) {
                mCurrentCityName = cityList.get(0).getName();
                List<ProvinceResultBean.ProvinceModel.CityModel.DistrictModel> districtList = cityList.get(0).getDistrictList();
                mCurrentDistrictName = districtList.get(0).getName();
                mCurrentZipCode = districtList.get(0).getZipCode();
            }
        }
        int provinceListCount = 0;
        if (provinceList != null) {
            provinceListCount = provinceList.size();
        }
        mProvinceData = new String[provinceListCount];
        for (int i = 0; i < provinceListCount; i++) {
            mProvinceData[i] = provinceList.get(i).getName();
            List<ProvinceResultBean.ProvinceModel.CityModel> cityList = provinceList.get(i)
                    .getCityList();
            String[] cityNames = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                cityNames[j] = cityList.get(j).getName();
                List<ProvinceResultBean.ProvinceModel.CityModel.DistrictModel> districtList =
                        cityList
                        .get(j).getDistrictList();
                String[] districtNameArray = new String[districtList.size()];
                for (int k = 0; k < districtList.size(); k++) {
                    districtNameArray[k] = districtList.get(k).getName();
                    mZipCodeDataMap.put(districtList.get(k).getName(),
                            districtList.get(k).getZipCode());
                }
                mDistrictDataMap.put(cityNames[j], districtNameArray);
            }
            mCitiesDataMap.put(provinceList.get(i).getName(), cityNames);
        }
    }
}
