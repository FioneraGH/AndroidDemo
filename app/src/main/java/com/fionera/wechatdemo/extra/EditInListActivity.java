package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.fionera.wechatdemo.R;

import java.util.ArrayList;
import java.util.List;

public class EditInListActivity extends Activity {

    private ListView listView;
    private List<String> data = new ArrayList<String>();
    private ChoosePhoneNumAdapter choosePhoneNumAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_in_list);

        data.add("1");
        data.add("11");
        data.add("111");
        data.add("11111");
        data.add("111111");
        data.add("1111111");
        data.add("11111111");
        data.add("111111111");
        data.add("1111111111");
        data.add("11111111111");
        data.add("111111111111");
        data.add("1111111111111");
        data.add("11111111111111");
        data.add("111111111111111");
        data.add("1111111111111111");
        data.add("11111111111111111");

        listView = (ListView) findViewById(R.id.list_view_edit_text);
        choosePhoneNumAdapter = new ChoosePhoneNumAdapter(this,data);
        listView.setAdapter(choosePhoneNumAdapter);
        listView.setItemsCanFocus(true);

    }

    public class ChoosePhoneNumAdapter extends BaseAdapter {

        private List<String> data;
        private LayoutInflater layoutInflater;

        public ChoosePhoneNumAdapter(Context context, List<String> data) {

            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.man_choose_phone_item, null);
            EditText et = (EditText) convertView.findViewById(R.id.et_edit_phone_num);
            et.setText(data.get(position));
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.d("ETILV",hasFocus + "");
                    if(!hasFocus){
                        data.remove(position);
                        data.add(position, ((EditText) v).getText().toString());
                        Log.d("ETILV", data.get(position));
                    }
                }
            });
            return convertView;
        }
    }
}
