package com.example.corona_prevention;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean inexamCnt = false, indecideCnt = false, inclearCnt = false, indeathCnt = false, incareCnt = false;
        String examCnt ="", decideCnt="", clearCnt="", deathCnt="", careCnt="";

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String today = format.format(Calendar.getInstance().getTime());
        int yesterday = Integer.parseInt(today);
        --yesterday;

        StrictMode.enableDefaults();
        TextView status1 = (TextView) findViewById(R.id.result); //파싱된 결과확인!


        StringBuilder date11 = new StringBuilder();
        date11.append("startCreateDt=");
        date11.append(yesterday);
        date11.append("&endCreateDt=");
        date11.append(today);


        try {
            URL url = new URL("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson?"
                    + "&serviceKey="
                    + "rv5WpuUIU505RawLeknLVjElf7godyPNvYlU111EtdipRoajF9FhA2W5wwrQHr%2B6mfhCpAWbY4U3MEnv0dbiig%3D%3D"
                    + "&startCreateDt=" + yesterday + "&endCreateDt=" + today
            ); //검색 URL부분


            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("parsing");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행

                        if (parser.getName().equals("examCnt")) inexamCnt = true;
                        if (parser.getName().equals("decideCnt")) indecideCnt = true;
                        if (parser.getName().equals("clearCnt")) inclearCnt = true;
                        if (parser.getName().equals("deathCnt")) indeathCnt = true;
                        if (parser.getName().equals("careCnt")) incareCnt = true;

                        if (parser.getName().equals("message")) { //message 태그를 만나면 에러 출력
                            status1.setText(status1.getText() + "에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (inexamCnt) {
                            examCnt = parser.getText();
                            inexamCnt = false;
                        }
                        if (indecideCnt) {
                            decideCnt = parser.getText();
                            indecideCnt = false;
                        }
                        if (inclearCnt) {
                            clearCnt = parser.getText();
                            inclearCnt = false;
                        }
                        if (indeathCnt) {
                            deathCnt = parser.getText();
                            indeathCnt = false;
                        }
                        if (incareCnt) {
                            careCnt = parser.getText();
                            incareCnt = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            status1.setText(status1.getText() + "국내검사중: " + examCnt +
                                    "\n확진자수 : " + decideCnt +
                                    "\n격리해제수: " + clearCnt +
                                    "\n사망자수: " + deathCnt +
                                    "\n치료중환자수: " + careCnt
                            );
                        }
                        break;
                }

                parserEvent = parser.next();
            }
        } catch (Exception e) {
            status1.setText("error");
        }
    }
}
