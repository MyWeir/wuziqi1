package com.example.yls.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yls on 2017/6/9.
 */

public class WuziqiPanel extends View{
    private int mPanelWidth;
    private float mLineHight;
    private int MAX_LINE = 10;
    private  int MAX_IN_LINE=7;
    private Paint mPaint = new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float ratioPieceOfHeight=3 * 1.0f/4;
    private boolean mIsWhite=true;
    private ArrayList<Point> mWhiteArray=new ArrayList<>();
    private ArrayList<Point> mBlackArray=new ArrayList<>();
    private boolean mIsgameover;
    private boolean mIsWhiteWinner;

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        // setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mPaint.setColor(0x44ff0000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mWhitePiece= BitmapFactory.decodeResource(getResources(),R.drawable.blue);
       mBlackPiece= BitmapFactory.decodeResource(getResources(),R.drawable.black);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth=w;
        mLineHight=mPanelWidth*1.0f/MAX_LINE;
        int pieceWidth= (int) (mLineHight*ratioPieceOfHeight);

        mWhitePiece=Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
       mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsgameover){
            return false;
        }
        int action=event.getAction();
        if(action==MotionEvent.ACTION_UP){
            int x=(int) event.getX();
            int y= (int) event.getY();
            Point p=getVaildPoint(x,y);
            if(mWhiteArray.contains(p)||mBlackArray.contains(p)){
                return false;
            }
            if(mIsWhite){
                mWhiteArray.add(p);
            }else {
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite=!mIsWhite;
        }
        return true;
    }

    private Point getVaildPoint(int x, int y) {
        return new Point((int)(x/mLineHight),(int)(y/mLineHight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        Gameover();
    }

    private void Gameover() {
        boolean whiteWin=checkFiveInLine(mWhiteArray);
        boolean blackWin=checkFiveInLine(mBlackArray);
        if(whiteWin||blackWin){
            mIsgameover=true;
            mIsWhiteWinner=whiteWin;
            String text=mIsWhiteWinner?"蓝棋胜利":"黑棋胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for(Point p:points){
            int x=p.x;
            int y=p.y;
            boolean win=checkHorizontal(x,y,points);
            if(win){
                return true;
            }
            win=checkVertical(x,y,points);
            if(win){
                return true;
            }
            win=checkLeftLine(x,y,points);
            if(win){
                return true;
            }
            win=checkRightLine(x,y,points);
            if(win){
                return true;
            }
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count=1;
        for(int i=0;i<MAX_IN_LINE;i++){
            if(points.contains(new Point(x-i,y))){
                count++;
            }else {
                break;
            }
        }
        for(int i=0;i<MAX_IN_LINE;i++){
            if(points.contains(new Point(x+i,y))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_IN_LINE){
            return true;
        }
        return false;
    }
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count=1;
        for(int i=0;i<MAX_IN_LINE;i++){
            if(points.contains(new Point(x,y-i))){
                count++;
            }else {
                break;
            }
        }
        for(int i=0;i<MAX_IN_LINE;i++){
            if(points.contains(new Point(x,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_IN_LINE){
            return true;
        }
        return false;
    }

    private boolean checkLeftLine(int x, int y, List<Point> points) {
        int count=1;
        for(int i=0;i<MAX_IN_LINE;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;
            }else {
                break;
            }
        }
        for(int i=0;i<MAX_IN_LINE;i++){
            if(points.contains(new Point(x+i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_IN_LINE){
            return true;
        }
        return false;
    }

    private boolean checkRightLine(int x, int y, List<Point> points) {
        int count=1;
        for(int i=0;i<MAX_IN_LINE;i++){
            if(points.contains(new Point(x-i,y-i))){

                count++;
            }else {
                break;
            }
        }
        for(int i=0;i<MAX_IN_LINE;i++){
            if(points.contains(new Point(x+i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_IN_LINE){
            return true;
        }
        return false;
    }


    private void drawPiece(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - ratioPieceOfHeight) / 2) * mLineHight,
                    (whitePoint.y + (1 - ratioPieceOfHeight) / 2) * mLineHight, null);
        }
        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece, (blackPoint.x + (1 -ratioPieceOfHeight) / 2) * mLineHight,
                    (blackPoint.y + (1 - ratioPieceOfHeight) / 2) * mLineHight, null);
        }
    }


    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHight;
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);

            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }
    private static final String INSTANCE="instance";
    private static final String INSTANCE_Gameover="gameover";
    private static final String INSTANCE_WHITE_ARRAY="whitearray";
    private static final String INSTANCE_BLACK_ARRAY="blackarray";



    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_Gameover,mIsgameover);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle=(Bundle) state;
            mIsgameover=bundle.getBoolean(INSTANCE_Gameover);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
        }
            return;
    }
}


