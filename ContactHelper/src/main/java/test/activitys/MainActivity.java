package test.activitys;

import android.app.Activity;
import android.helper.contact.ContactDbHelper;
import android.helper.contact.ContactHelper;
import android.helper.contact.bean.Contact;
import android.helper.contact.bean.Email;
import android.helper.contact.bean.IContactData;
import android.helper.contact.bean.IM;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.witness.utils.LogUtil;
import com.witness.utils.db.ESQLiteDatabase;

import java.util.List;

import witness.ho1st.R;

/**
 * Created by danger on 15/3/29.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    TextView crateTable;
    TextView tvSelect;
    ContactDbHelper db;
    ContactHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        crateTable = (TextView) findViewById(R.id.create_table);
        tvSelect = (TextView) findViewById(R.id.select);

        crateTable.setOnClickListener(onClickCreateTable);
        tvSelect.setOnClickListener(this);

        db = new ContactDbHelper(this);
        helper = new ContactHelper(getBaseContext());

        db.createTables();
    }


    View.OnClickListener onClickCreateTable = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            Contact contact = new Contact();
            contact.setDisplayName("诸葛亮");

            Email email = new Email();
            email.setLabel("常用");
            email.setAddress("zhuge@163.com");
            contact.addData(email);

            IM im = new IM();
            im.setLabel("facebook");
            im.setData("fac33533535");
            contact.addData(im);

            db.insert(contact);

            contact = new Contact();
            contact.setDisplayName("刘备");


            email = new Email();
            email.setLabel("公司邮箱");
            email.setAddress("liubei@gmail.com");
            contact.addData(email);

            im = new IM();
            im.setLabel("QQ");
            im.setData("33838");
            contact.addData(im);

            db.insert(contact);

            query();
        }
    };


    private void insert(ESQLiteDatabase database, Contact contact) {
        database.insert(contact);
        List<IContactData> list = contact.getDatas();
        for (Object o : list) {
            database.insert(o);
        }
    }

    private void query() {
        List<Contact> contacts = db.querySimple();
        for (Contact contact : contacts) {
            db.query(contact.getCId(), contact);

            LogUtil.d(TAG, "cid:" + contact.getCId() + "," + contact.getDisplayName());


            List<IContactData> datas = contact.getDatas();
            for (int i = 0; i < datas.size(); i++) {
                IContactData data = datas.get(i);
                LogUtil.d(TAG, "  data:" + data);
            }
        }


    }

    @Override
    public void onClick(View v) {
        if (tvSelect == v) {
            long start = System.currentTimeMillis();
            List<Contact> contacts = helper.querySimple();

            Log.d("", "contact count:" + contacts.size() + ", use:" + (System.currentTimeMillis() - start));

            helper.queryGroups();
            helper.query(contacts);

            for (Contact contact : contacts) {
                Log.d("", "contact" + contact.getDisplayName() + ",d:" + contact.getDatas().size());
            }
            Log.d("", "contact used:" + (System.currentTimeMillis() - start));
        }
    }
}
