package com.andlightlight.autowork;

import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class GestureManager {
    public static void LogLong(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public GestureDescription RengQiu(){
//        Path dragRightPath = new Path();
//        dragRightPath.moveTo(200, 200);
//        dragRightPath.lineTo(400, 200);
//        long dragRightDuration = 500L; // 0.5 second
//
//        // The starting point of the second path must match
//        // the ending point of the first path.
//        Path dragDownPath = new Path();
//        dragDownPath.moveTo(400, 200);
//        dragDownPath.lineTo(400, 400);
//        long dragDownDuration = 500L;
//        GestureDescription.StrokeDescription rightThenDownDrag =
//                new GestureDescription.StrokeDescription(dragRightPath, 0L,
//                        dragRightDuration, true);
//        rightThenDownDrag.continueStroke(dragDownPath, dragRightDuration,
//                dragDownDuration, false);
//        GestureDescription.Builder clickBuilder = new GestureDescription.Builder();
//        return clickBuilder.addStroke(rightThenDownDrag).build();

        Path clickPath = new Path();
        clickPath.moveTo(475.960693f, 1431.914063f);
        clickPath.lineTo(469.801727f, 1428.765869f);
        clickPath.lineTo(458.997314f, 1419.245972f);
        clickPath.lineTo(436.542358f, 1403.677612f);
        clickPath.lineTo(426.456299f, 1395.410156f);
        clickPath.lineTo(403.170563f, 1364.283203f);
        clickPath.lineTo(389.970703f, 1347.392578f);
        clickPath.lineTo(359.948303f, 1310.651978f);
        clickPath.lineTo(318.466187f, 1249.921875f);
        clickPath.lineTo(303.432922f, 1207.767090f);
        clickPath.lineTo(299.959717f, 1176.002686f);
        clickPath.lineTo(299.959717f, 1169.414063f);
        clickPath.lineTo(299.959717f, 1161.916748f);
        clickPath.lineTo(299.959717f, 1158.896484f);
        clickPath.lineTo(299.959717f, 1142.622314f);
        clickPath.lineTo(324.464722f, 1111.904297f);
        clickPath.lineTo(342.250366f, 1104.977173f);
        clickPath.lineTo(351.474609f, 1094.912109f);
        clickPath.lineTo(390.913757f, 1078.293823f);
        clickPath.lineTo(404.472656f, 1069.453125f);
        clickPath.lineTo(453.074005f, 1062.005371f);
        clickPath.lineTo(531.974487f, 1053.925781f);
        clickPath.lineTo(551.252441f, 1053.925781f);
        clickPath.lineTo(582.504395f, 1053.925781f);
        clickPath.lineTo(589.965820f, 1053.925781f);
        clickPath.lineTo(611.549744f, 1047.753540f);
        clickPath.lineTo(657.960205f, 996.445313f);
        clickPath.lineTo(659.970703f, 985.491028f);
        clickPath.lineTo(659.970703f, 979.980469f);
        clickPath.lineTo(659.970703f, 965.559937f);
        clickPath.lineTo(650.459839f, 935.419189f);
        clickPath.lineTo(641.464233f, 932.431641f);
        clickPath.lineTo(635.571716f, 928.749084f);
        clickPath.lineTo(622.055481f, 924.960938f);
        clickPath.lineTo(606.031799f, 924.960938f);
        clickPath.lineTo(575.487366f, 924.960938f);
        clickPath.lineTo(562.972412f, 924.960938f);
        clickPath.lineTo(554.662720f, 924.960938f);
        clickPath.lineTo(537.664185f, 924.960938f);
        clickPath.lineTo(532.468872f, 924.960938f);
        clickPath.lineTo(527.549072f, 931.356873f);
        clickPath.lineTo(524.970703f, 944.443359f);
        clickPath.lineTo(524.970703f, 953.684448f);
        clickPath.lineTo(524.970703f, 961.464844f);
        clickPath.lineTo(524.970703f, 976.296997f);
        clickPath.lineTo(527.969971f, 1002.152710f);
        clickPath.lineTo(538.863525f, 1031.687866f);
        clickPath.lineTo(587.212646f, 1109.487305f);
        clickPath.lineTo(612.797913f, 1144.638672f);
        clickPath.lineTo(618.475342f, 1153.916016f);
        clickPath.lineTo(643.922546f, 1182.187500f);
        clickPath.lineTo(668.474121f, 1220.449219f);
        clickPath.lineTo(695.305664f, 1264.547607f);
        clickPath.lineTo(704.959717f, 1281.474609f);
        clickPath.lineTo(741.673035f, 1340.592407f);
        clickPath.lineTo(756.697021f, 1371.761353f);
        clickPath.lineTo(767.466431f, 1382.958984f);
        clickPath.lineTo(768.966064f, 1390.943848f);
        clickPath.lineTo(768.966064f, 1398.457031f);
        clickPath.lineTo(776.464233f, 1416.445313f);
        clickPath.lineTo(781.385559f, 1439.641113f);
        clickPath.lineTo(788.972168f, 1496.894531f);
        clickPath.lineTo(788.972168f, 1511.923828f);
        clickPath.lineTo(788.972168f, 1517.958984f);
        clickPath.lineTo(788.972168f, 1532.662598f);
        clickPath.lineTo(788.972168f, 1568.876831f);
        clickPath.lineTo(788.972168f, 1577.460938f);
        clickPath.lineTo(788.972168f, 1581.914063f);
        clickPath.lineTo(788.972168f, 1587.955200f);
        GestureDescription.StrokeDescription clickStroke = new GestureDescription.StrokeDescription(clickPath, 0, 1107);
        GestureDescription.Builder clickBuilder = new GestureDescription.Builder();
        return clickBuilder.addStroke(clickStroke).build();
    }
}
