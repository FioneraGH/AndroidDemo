package com.fionera.wechatdemo.extra;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

        listView = (ListView) findViewById(R.id.list_view_edit_text);
        choosePhoneNumAdapter = new ChoosePhoneNumAdapter(this, data);
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
            final EditText et = (EditText) convertView.findViewById(R.id.et_edit_phone_num);
            et.setText(data.get(position));
            et.requestFocus();
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.d("ETILV", position + " " + hasFocus);
                    if (!hasFocus) {
                        data.set(position, ((EditText) v).getText().toString());
                        Log.d("ETILV", position + " " + data.get(position));
                    }
                }
            });

            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_choose_phone_num);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 0);
                }
            });
            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {

                    if (intent == null) {
                        return;
                    }
                    data.set(1, backContact(intent));
                    Log.d("ETILV",data.get(1));
                    choosePhoneNumAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private String backContact(Intent intent) {
        String phoneNumber = null;
        Uri uri = intent.getData();
        // 得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        // 取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        // 向下移动光标
        while (cursor.moveToNext()) {
            // 取得联系人名字
            String contact = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // 取得电话号码
            String ContactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + "=" + ContactId, null, null);

            if (phone.moveToNext()) {
                phoneNumber = phone
                        .getString(phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
        }
        cursor.close();

        return phoneNumber;
    }
}
