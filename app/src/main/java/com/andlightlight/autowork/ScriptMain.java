package com.andlightlight.autowork;

import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.Date;

public class ScriptMain extends ScriptInterface {
    Date LastRengQiuDate;
    @Override
    protected void startImp() throws InterruptedException{
        LastRengQiuDate = new Date();
        while (true){
            int DDelay = 5000;
            RoundClick();
            sleep(DDelay);
        }
    }

    void RoundClick() throws InterruptedException {
        int allDelay = 5000;
        int pressDelay = 0;
        click(new PointF[]{new PointF(559.000000f,1113.000000f)},33);
        sleep(1186);
        click(new PointF[]{new PointF(483.000000f,1202.000000f)},33);
        sleep(1200);
        click(new PointF[]{new PointF(448.000000f,1276.000000f)},32);
        sleep(1587);
        click(new PointF[]{new PointF(546.000000f,1382.000000f)},48);
        sleep(1622);
        click(new PointF[]{new PointF(672.000000f,1278.000000f)},49);
        sleep(2067);
        click(new PointF[]{new PointF(485.000000f,1195.000000f)},39);
    }

    public void CheckAndRengQiu(){
    Date NowRengQiuDate = new Date();
    long DateOffset = NowRengQiuDate.getTime() - LastRengQiuDate.getTime();
    if (DateOffset > 600){
        while (true){
//            Call Plugin.Media.Beep(784, 300)
//            Call Plugin.Media.Beep(578, 300)
//            Call Plugin.Media.Beep(988, 300)
//            Call Plugin.Media.Beep(440, 300)
//            Call Plugin.Media.Beep(1760, 300)
//            Call Plugin.Media.Beep(1397, 300)
//            Call Plugin.Media.Beep(698, 300)
//            Call Plugin.Media.Beep(523, 300)
//            Call Plugin.Media.Beep(1046, 300)
//            Call Plugin.Media.Beep(262, 300)
//            Call Plugin.Media.Beep(349, 300)
//            Call Plugin.Media.Beep(784, 300)
//            Call Plugin.Media.Beep(880, 300)
//            Call Plugin.Media.Beep(880, 300)
        }
    }

    int rengqiuDelay = 2000;
    FindPic 102, 668, 385, 1000, "C:\Users\andlightlight\Desktop\1.bmp", 0.4, intX, intY
            Rd = 3
    While intX > 0 And intY > 0
    If Rd = 1 Then
    Call RengQiu()
    Rd = 2
    ElseIf Rd = 2 Then
    Call RengQiu2()
    Rd = 3
    ElseIf Rd = 3 Then
    Call RengQiu3()
    //Rd = 1
    End If
    LastRengQiuDate = DateDiff("s", "1970-01-01 00:00:00", Now)
    Delay rengqiuDelay
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\1.bmp", 0.4, intX, intY
            Wend
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\2.bmp", 0.5, intX, intY
    While intX > 0 And intY > 0
    Call MoveAndClick(intX, intY, rengqiuDelay)
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\2.bmp", 0.5, intX, intY
            Wend
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\4.bmp", 0.5, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\5.bmp", 0.3, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\14.bmp", 0.3, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\6.bmp", 0.4, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\7.bmp", 0.4, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\15.bmp", 0.4, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\8.bmp", 0.4, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\9.bmp", 0.5, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\11.bmp", 0.6, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\12.bmp", 0.4, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\13.bmp", 0.4, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\16.bmp", 0.8, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\17.bmp", 0.4, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\18.bmp", 0.4, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\3.bmp", 0.5, intX, intY
    While intX > 0 And intY > 0
    Call MoveAndClick(intX, intY, rengqiuDelay)
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\3.bmp", 0.5, intX, intY
            Wend
    FindPic 0, 28, 489, 1005, "C:\Users\andlightlight\Desktop\19.bmp", 0.5, intX, intY
    If intX > 0 And intY > 0 Then
    Call MoveAndClick(intX, intY, rengqiuDelay)
    End If
    //Call FindGuaiWu()
    End Sub

}
