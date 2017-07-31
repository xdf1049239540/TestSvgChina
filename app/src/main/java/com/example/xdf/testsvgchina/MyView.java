package com.example.xdf.testsvgchina;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by xdf on 2017/7/31.
 */

public class MyView extends View{
    private int[] colorArray=new int[]{R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark};
    private List<ProvinceItem> listProvince=new ArrayList<>();
    Paint paint;
    private ProvinceItem selectProvinceItem;
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init() {
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        InputStream inputStream = getResources().openRawResource(R.raw.tai_wan_svg);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//得到DocumentBuilderFactory
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder(); //获取factory
            Document doc = builder.parse(inputStream);
            Element rootElement = doc.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("path");
            for(int i = 0;i<items.getLength();i++){
                Element element = (Element) items.item(i);
                String pathData = element.getAttribute("android:pathData");
                Path path = PathParser.createPathFromPathData(pathData);

                //得到整个地图的边框范围
                RectF rectF = new RectF();
                path.computeBounds(rectF,true);
                ProvinceItem item = new ProvinceItem();
                item.setPath(path);
                item.setDrawColor(colorArray[i%3]);
                listProvince.add(item);
            }
            handler.sendEmptyMessage(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.v("touch","length:"+listProvince.size());
            if(listProvince != null && listProvince.size()>0){
                postInvalidate();
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < listProvince.size(); i++) {
            ProvinceItem provinceItem = listProvince.get(i);
            paint.setColor(ContextCompat.getColor(getContext(),provinceItem.getDrawColor()));

            provinceItem.draw(canvas,paint,false);
        }
        if(null!=selectProvinceItem){
            selectProvinceItem.draw(canvas,paint,true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handleTouch(event.getX(),event.getY());
                break;
        }
        return true;
    }

    private void handleTouch(float x, float y) {
        for (int i = 0; i <listProvince.size() ; i++) {
            ProvinceItem provinceItem = listProvince.get(i);
            if(provinceItem.isTouch((int)x,(int)y)){
                selectProvinceItem=provinceItem;
                break;
            }
        }
        postInvalidate();
    }
}
