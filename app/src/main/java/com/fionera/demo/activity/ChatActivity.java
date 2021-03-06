package com.fionera.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.fionera.demo.adapter.ChatMsgViewAdapter;
import com.fionera.demo.bean.ChatMsgBean;
import com.fionera.demo.util.DatabaseHelper;
import com.fionera.demo.view.ArcMenu;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author fionera
 */
public class ChatActivity
        extends BaseActivity
        implements OnClickListener, AbsListView.OnScrollListener {

    private ListView listView;
    private View footer;
    private EditText mEditTextContent;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this, "ChatEntity");

    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private int allRecorders = 0;
    private int pageCount = 1;

    private List<ChatMsgBean> dataList = new ArrayList<>();

    private Disposable disposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        databaseHelper.closeDb();
    }

    private void initView() {
        findViewById(R.id.iv_toggle_menu).setOnClickListener(this);
        findViewById(R.id.tv_send_msg).setOnClickListener(this);
        findViewById(R.id.tv_extra_function).setOnClickListener(this);
        ((ArcMenu) findViewById(R.id.arc_menu)).setOnMenuItemClickListener((view, pos) -> {
            int sendPos = 2;
            if(sendPos == pos){
                sendTestEmail();
            }
        });

        listView = findViewById(R.id.lv_chat_content);
        mEditTextContent = findViewById(R.id.et_send_message);
    }

    private void sendTestEmail() {
        disposable = Observable.create((ObservableOnSubscribe<String>) e -> {
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.qiye.163.com");
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");

            Session session = Session.getInstance(prop);
            session.setDebug(true);
            Transport ts = session.getTransport();
            ts.connect("smtp.qiye.163.com", "shell.yang@centling.com", "Fionera0");
            Message message = createAttachMail(session);
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            e.onNext("Send Mail Success");
            e.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                ShowToast::show, throwable -> ShowToast.show(throwable.getMessage()));
    }

    private static MimeMessage createAttachMail(Session session) throws Exception {
        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress("shell.yang@centling.com"));
        // 收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("devilinmind@qq.com"));
        // 邮件标题
        message.setSubject("JavaMail邮件发送测试");
        // 创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("使用JavaMail创建的带附件的邮件", "text/html;charset=UTF-8");
        // 创建邮件附件
        MimeBodyPart attach = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource(Environment.getExternalStorageDirectory() + "/DidiUuid"));
        attach.setDataHandler(dh);
        attach.setFileName("DidiUuid");
        // 创建容器描述数据关系
        MimeMultipart mp = new MimeMultipart();
        mp.addBodyPart(text);
        mp.addBodyPart(attach);
        mp.setSubType("mixed");
        message.setContent(mp);
        message.saveChanges();
        // 将创建的Email写入到E盘存储
        message.writeTo(new FileOutputStream(
                Environment.getExternalStorageDirectory() + "/attachMail.eml"));
        // 返回生成的邮件
        return message;
    }

    private void initData() {
        footer = View.inflate(mContext, R.layout.layout_load_more, null);
        listView.setOnScrollListener(this);
        showAllData();
    }

    /**
     * 读取显示数据
     */
    private void showAllData() {
        allRecorders = databaseHelper.getCount();
        if (allRecorders > PAGE_SIZE) {
            listView.addFooterView(footer);
        }

        pageCount = (allRecorders + PAGE_SIZE - 1) / PAGE_SIZE;
        dataList.addAll(databaseHelper.getSomeItems(currentPage, PAGE_SIZE));

        listView.setAdapter(new ChatMsgViewAdapter(mContext, dataList));
        listView.setSelection(dataList.size() - 1);
        databaseHelper.closeDb();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_msg:
                sendMsg();
                break;
            case R.id.tv_extra_function:
                startActivity(new Intent(mContext, CaptureActivity.class));
                break;
            default:
                break;
        }
    }

    private int firstItem = -1;

    @Override
    public void onScroll(AbsListView absView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        firstItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scorllState) {
        if (firstItem == 0 && currentPage < pageCount && scorllState == AbsListView
                .OnScrollListener.SCROLL_STATE_IDLE) {
            currentPage++;
            appendDate();
        }
    }

    /**
     * 增加数据
     */
    private void appendDate() {
        List<ChatMsgBean> addItems = databaseHelper.getSomeItems(currentPage, PAGE_SIZE);

        dataList.addAll(0, addItems);
        if (allRecorders == dataList.size()) {
            listView.removeHeaderView(footer);
        }

        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        listView.setSelection(addItems.size());
        databaseHelper.closeDb();
    }

    private void sendMsg() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgBean entry = new ChatMsgBean();
            entry.setDate(getDate());
            entry.setName("world");
            entry.setMsgType(false);
            entry.setText(contString);
            dataList.add(entry);
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            listView.setSelection(listView.getCount() - 1);
            databaseHelper.insertChatEntity(entry);
            new Handler().postDelayed(this::autoReply, 1000);
            mEditTextContent.setText("");
        }
    }

    private void autoReply() {
        ChatMsgBean entry = new ChatMsgBean();
        entry.setDate(getDate());
        entry.setName("hello");
        entry.setMsgType(true);
        entry.setText("请稍候联系！ " + mEditTextContent.getText().toString());
        dataList.add(entry);
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
        mEditTextContent.setText("");
        listView.setSelection(listView.getCount() - 1);
        databaseHelper.insertChatEntity(entry);
    }

    private static String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String min = String.valueOf(c.get(Calendar.MINUTE));
        return (year + "-" + month + "-" + day + " " + hour + ":" + min);
    }
}