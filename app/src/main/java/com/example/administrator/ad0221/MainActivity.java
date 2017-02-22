package com.example.administrator.ad0221;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtCompany,edtNum;
    private Button btnQuery;
    private TextView result;
    private LibraryBean mLibraryBean;
    String urlstr="http://v.juhe.cn/exp/index?key=92ecbf57fca21aae89cfbcde582c24a0&";
    /*String urlstr="http://a1.gdcp.cn/index.shtml";*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


    }
    private void initViews() {
        edtCompany= (EditText) findViewById(R.id.edt_company);
        edtNum= (EditText) findViewById(R.id.edt_num);
        btnQuery= (Button) findViewById(R.id.btn_query);
        result= (TextView) findViewById(R.id.tv_result);


        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String company=edtCompany.getText().toString().trim();
                            String num=edtNum.getText().toString().trim();
                            urlstr=urlstr + "com=" + company +"&no=" + num;
                            URL url = new URL(urlstr);
                            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                            InputStream input=conn.getInputStream();
                            BufferedReader buffer=new BufferedReader(new InputStreamReader(input));

                            final StringBuffer string=new StringBuffer();
                            String line=buffer.readLine();

                            while(line!=null){
                                string.append(line);
                                line=buffer.readLine();
                            }
                            final StringBuffer str=new StringBuffer();
                            Gson gson=new Gson();
                            mLibraryBean=gson.fromJson(string.toString(),LibraryBean.class);
                            List<LibraryBean.ResultBean.ListBean> in=mLibraryBean.getResult().getList();

                            for (int i = 0; i <in.size() ; i++) {
                                str.append( in.get(i).getDatetime()+"\n"+in.get(i).getRemark()+"\n");
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    result.setText(str.toString());
                                }

                            });
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}




