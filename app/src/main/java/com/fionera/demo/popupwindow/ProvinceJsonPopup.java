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
import com.fionera.base.util.LogCat;
import com.fionera.demo.R;
import com.fionera.demo.view.wheelview.ArrayWheelAdapter;
import com.fionera.demo.view.wheelview.OnWheelChangedListener;
import com.fionera.demo.view.wheelview.ProvinceDataBean;
import com.fionera.demo.view.wheelview.WheelView;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fionera
 */
public class ProvinceJsonPopup
        extends PopupWindow implements OnWheelChangedListener {
    private Context context;

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    private GetValueCallback getValueCallback;

    public void setGetValueCallback(GetValueCallback getValueCallback) {
        this.getValueCallback = getValueCallback;
    }

    public interface GetValueCallback {
        void getValue(String province, String city, String district, String provinceId,
                      String cityId, String districtId);
    }

    /**
     * 所有省
     */
    private String[] mProvinceData;
    private String[] mProvinceDataId;
    /**
     * key - 省 value - 市
     */
    private Map<String, String[]> mCitiesDataMap = new HashMap<>();
    private Map<String, String[]> mCitiesDataIdMap = new HashMap<>();
    /**
     * key - 市 values - 区
     */
    private Map<String, String[]> mDistrictDataMap = new HashMap<>();
    private Map<String, String[]> mDistrictDataIdMap = new HashMap<>();

    /**
     * 当前省的名称
     */
    private String mCurrentProvinceName;
    private String mCurrentProvinceId;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    private String mCurrentCityId;
    /**
     * 当前区的名称
     */
    private String mCurrentDistrictName;
    private String mCurrentDistrictId;

    public ProvinceJsonPopup(Context context) {
        this.context = context;

        View view = View.inflate(context, R.layout.dialog_change_province_pop, null);
        setContentView(view);
        TextView tvCancel = view.findViewById(R.id.tv_city_picker_cancel);
        TextView tvSave = view.findViewById(R.id.tv_city_picker_confirm);
        mViewProvince = view.findViewById(R.id.wl_province_picker);
        mViewCity = view.findViewById(R.id.wl_city_picker);
        mViewDistrict = view.findViewById(R.id.wl_district_picker);

        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);

        tvCancel.setOnClickListener(v -> dismiss());
        tvSave.setOnClickListener(v -> {
            if (getValueCallback != null) {
                getValueCallback.getValue(mCurrentProvinceName, mCurrentCityName,
                        mCurrentDistrictName, mCurrentProvinceId, mCurrentCityId,
                        mCurrentDistrictId);
            }
            dismiss();
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
        if (TextUtils.isEmpty(address)) {
            return;
        }
        LogCat.d("Province Pop Set Address:" + address);
        String[] addresses = address.split(":");
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
        mCurrentProvinceName = mProvinceData[i];
        mCurrentProvinceId = mProvinceDataId[i];

        updateCities();

        i = 0;
        String[] cities = mCitiesDataMap.get(addresses[0]);
        String[] citiesId = mCitiesDataIdMap.get(addresses[0]);
        if (cities == null || citiesId == null) {
            return;
        }
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
        mCurrentCityName = cities[i];
        mCurrentCityName = citiesId[i];

        updateAreas();

        i = 0;
        String[] areas = mDistrictDataMap.get(addresses[1]);
        String[] areasId = mDistrictDataMap.get(addresses[1]);
        if (areas == null || areasId == null) {
            return;
        }
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
        mCurrentDistrictName = areas[i];
        mCurrentDistrictId = areasId[i];
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            String[] data;
            mCurrentDistrictName = (data = mDistrictDataMap.get(
                    mCurrentCityName)) == null || 0 == data.length ? "" : data[newValue];
        }
    }

    private void setUpData() {
        initProvinceDataByJson();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(context, mProvinceData));
        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        mViewDistrict.setVisibleItems(5);
        updateCities();
    }

    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        if (mProvinceData == null || mProvinceDataId == null || 0 == mProvinceData.length || 0 == mProvinceDataId.length) {
            return;
        }
        mCurrentProvinceName = mProvinceData[pCurrent];
        mCurrentProvinceId = mProvinceDataId[pCurrent];
        String[] cities = mCitiesDataMap.get(mCurrentProvinceName);
        String[] cityIds = mCitiesDataIdMap.get(mCurrentProvinceName);
        if (cities == null || cityIds == null || 0 == cities.length || 0 == cityIds.length) {
            cities = new String[]{""};
            cityIds = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(context, cities));
        mViewCity.setCurrentItem(0);

        updateAreas();
    }

    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        String[] cities = mCitiesDataMap.get(mCurrentProvinceName);
        String[] cityIds = mCitiesDataIdMap.get(mCurrentProvinceName);
        if (cities == null || cityIds == null || 0 == cities.length || 0 == cityIds.length) {
            return;
        }
        mCurrentCityName = cities[pCurrent];
        mCurrentCityId = cityIds[pCurrent];
        String[] areas = mDistrictDataMap.get(mCurrentCityName);
        String[] areaIds = mDistrictDataIdMap.get(mCurrentCityName);
        if (areas == null || areaIds == null || 0 == areas.length || 0 == areaIds.length) {
            areas = new String[]{""};
            areaIds = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(context, areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = areas[0];
        mCurrentDistrictId = areaIds[0];
    }

    private void initProvinceDataByJson(){
        List<ProvinceDataBean.ProvinceListEntity> provinceList;
        String res;
        AssetManager a = context.getAssets();
        try {
            InputStream in = a.open("province_data_id.json");
            int length = in.available();
            byte[] buffer = new byte[length];
            LogCat.d("Province Pop Read String:" + in.read(buffer));
            res = new String(buffer, Charset.defaultCharset());
            in.close();
            provinceList = JSON.parseObject(res, ProvinceDataBean.class).getProvinceList();

            parseDataList(provinceList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDataList(List<ProvinceDataBean.ProvinceListEntity> provinceList) {
        if (provinceList != null && !provinceList.isEmpty()) {
            mCurrentProvinceName = provinceList.get(0).getProvinceName();
            mCurrentProvinceId = provinceList.get(0).getProvinceId();
            List<ProvinceDataBean.ProvinceListEntity.CityListEntity> cityList = provinceList.get(0)
                    .getCityList();
            if (cityList != null && !cityList.isEmpty()) {
                mCurrentCityName = cityList.get(0).getCityName();
                mCurrentCityId = cityList.get(0).getCityId();
                List<ProvinceDataBean.ProvinceListEntity.CityListEntity.AreaListEntity>
                        districtList = cityList
                        .get(0).getAreaList();
                mCurrentDistrictName = districtList.get(0).getAreaName();
                mCurrentDistrictId = districtList.get(0).getAreaId();
            }
        }

        int provinceListCount = 0;
        if (provinceList != null) {
            provinceListCount = provinceList.size();
        }
        mProvinceData = new String[provinceListCount];
        mProvinceDataId = new String[provinceListCount];
        for (int i = 0; i < provinceListCount; i++) {
            mProvinceData[i] = provinceList.get(i).getProvinceName();
            mProvinceDataId[i] = provinceList.get(i).getProvinceId();
            List<ProvinceDataBean.ProvinceListEntity.CityListEntity> cityList = provinceList.get(i)
                    .getCityList();
            String[] cityNames = new String[cityList.size()];
            String[] cityIds = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                cityNames[j] = cityList.get(j).getCityName();
                cityIds[j] = cityList.get(j).getCityId();
                List<ProvinceDataBean.ProvinceListEntity.CityListEntity.AreaListEntity>
                        districtList = cityList
                        .get(j).getAreaList();
                String[] districtNameArray = new String[districtList.size()];
                String[] districtIdArray = new String[districtList.size()];
                for (int k = 0; k < districtList.size(); k++) {
                    districtNameArray[k] = districtList.get(k).getAreaName();
                    districtIdArray[k] = districtList.get(k).getAreaId();
                }
                mDistrictDataMap.put(cityNames[j], districtNameArray);
                mDistrictDataIdMap.put(cityNames[j], districtIdArray);
            }
            mCitiesDataMap.put(provinceList.get(i).getProvinceName(), cityNames);
            mCitiesDataIdMap.put(provinceList.get(i).getProvinceName(), cityIds);
        }
    }
}
