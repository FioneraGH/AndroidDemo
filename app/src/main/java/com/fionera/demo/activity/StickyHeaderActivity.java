package com.fionera.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.fionera.demo.R;
import com.fionera.demo.adapter.StickyHeaderAdapter;
import com.fionera.demo.bean.StickyHeaderBean;
import com.fionera.demo.util.HttpUtils;
import com.fionera.demo.view.SectionDecoration;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class StickyHeaderActivity
        extends BaseActivity {

    @ViewInject(R.id.rv_sticky_header)
    private RecyclerView recyclerView;

    private List<StickyHeaderBean.DataEntity.ComingEntity> comingEntityList = new ArrayList<>();

    public String URL = "http://api.meituan.com/mmdb/movie/v2/list/rt/order/coming" +
            ".json?ci=1&limit=12&token=&__vhost=api.maoyan" +
            ".com&utm_campaign=AmovieBmovieCD-1&movieBundleVersion=6801&utm_source=xiaomi" +
            "&utm_medium=android&utm_term=6.8.0&utm_content=868030022327462&net=255&dModel=MI%205" +
            "&uuid=0894DE03C76F6045D55977B6D4E32B7F3C6AAB02F9CEA042987B380EC5687C43&lat=40.100673" +
            "&lng=116.378619&__skck=6a375bce8c66a0dc293860dfa83833ef&__skts=1463704714271&__skua" +
            "=7e01cf8dd30a179800a7a93979b430b2&__skno=1a0b4a9b-44ec-42fc-b110-ead68bcc2824&__skcy" +
            "=sXcDKbGi20CGXQPPZvhCU3%2FkzdE%3D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_header);
        x.view().inject(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new SectionDecoration(mContext, comingEntityList, new
                SectionDecoration.DecorationCallback() {
                    @Override
                    public String getGroupId(int position) {
                        return comingEntityList.get(position).getNm();
                    }

                    @Override
                    public String getGroupFirstLine(int position) {
                        return comingEntityList.get(position).getNm();
                    }
                }));
        recyclerView.setAdapter(new StickyHeaderAdapter(mContext, comingEntityList));

        fetchData();
    }

    private void fetchData() {
        HttpUtils.get(URL, null, new HttpUtils.HttpRequestCallBack<String>(URL) {

            @Override
            public void onSucceed(String json) {
                processData(json);
            }

            @Override
            public void onFailed(String reason) {

            }

            @Override
            public void onNoNetwork() {

            }
        });
    }

    private void processData(String json) {
        StickyHeaderBean stickyHeaderBean = JSON.parseObject(json, StickyHeaderBean.class);
        comingEntityList.addAll(stickyHeaderBean.getData().getComing());
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
