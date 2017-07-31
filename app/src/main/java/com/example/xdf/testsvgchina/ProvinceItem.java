package com.example.xdf.testsvgchina;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.v4.content.ContextCompat;

/**
 * Created by xdf on 2017/7/31.
 */

class ProvinceItem {
    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    private Path path;
    public void draw(Canvas canvas, Paint paint, boolean select){

        if(!select){
            paint.clearShadowLayer();
            canvas.drawPath(path,paint);
            Paint paint2=new Paint();
            paint2.setAntiAlias(true);
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeWidth(2);
            paint2.setColor(Color.CYAN);
            canvas.drawPath(path,paint2);
        }else{
            Paint paint3=new Paint();
            paint3.setAntiAlias(true);
            paint3.setStyle(Paint.Style.FILL);
            paint3.setColor(Color.RED);
            canvas.drawPath(path,paint3);
        }
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public int getDrawColor() {
        return drawColor;
    }

    private int drawColor;
    public boolean isTouch(int x, int y){
        RectF bounds=new RectF();
        path.computeBounds(bounds,true);
        Region region=new Region();
        region.setPath(path,new Region((int)bounds.left,(int)bounds.top,(int)bounds.right,(int)bounds.bottom));
        return region.contains(x,y);
    }
}
