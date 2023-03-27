package schooldriver.trackware.com.school_bus_driver_android.dialog;

import android.app.Activity;

import androidx.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseDialog;

/**
 * Created by   3/8/17.
 */

public class SpeedDialog extends BaseDialog {

    Timer timerPhoenix;
    TimerTask timerTaskPhoenix;
    private  int realSpeed;
    private  int limitSpeed;
    TextView txtLimitSpeed, txtRealSpeed;
    View btnOk;
    private static SpeedDialog speedDialog;
//    Animation performAnimation;
    protected SpeedDialog(Activity mActivity, int limitSpeed, int realSpeed) {
        super(mActivity);
        super.mActivity = mActivity;
        this.limitSpeed = limitSpeed;
        this.realSpeed = realSpeed;
        createDialog();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    public static SpeedDialog getInstanceAndShow(Activity activity, int limitSpeed, int realSpeed){
        try{
            if (speedDialog!=null && speedDialog.isShowing()){
                speedDialog.dismiss();
                speedDialog.cancel();
                speedDialog=null;
            }
        }catch (Exception e){

        }



        speedDialog=new SpeedDialog(activity,limitSpeed,realSpeed);
        speedDialog.show();

        return speedDialog;
    }



    private void createDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_speed);
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_round_shape);
//        performAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.animation);
//        performAnimation.setRepeatCount(4);

        btnOk =  findViewById(R.id.btnOk);
        txtLimitSpeed = (TextView) findViewById(R.id.txtLimitSpeed);
        txtRealSpeed = (TextView) findViewById(R.id.txtRealSpeed);
        txtLimitSpeed.append(" "+limitSpeed+" ");
        txtRealSpeed.setText(" "+realSpeed+" ");

//        txtRealSpeed.startAnimation(performAnimation);
//        startTimerPhoenix();


        txtRealSpeed.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fadeout));

    }


//    public void startTimerPhoenix() {
//        //set a new Timer
//        timerPhoenix = new Timer();
//
//        //initialize the TimerTask's job
//        try {
////            timerOpenPhoenix();
//        } catch (OutOfMemoryError outOfMemoryError) {
//
//        }


        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//        timerPhoenix.schedule(timerTaskPhoenix, 1 * 1000 , 1* 1000); //
//    }

//    boolean value = false;
//    private void timerOpenPhoenix() {
//        timerTaskPhoenix = new TimerTask() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void run() {
////                performAnimation.start();
////                txtRealSpeed.startAnimation(performAnimation);
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                final int sdk = android.os.Build.VERSION.SDK_INT;
//                if (value) {
////                    txtRealSpeed.setBackgroundDrawable(R.drawable.speed_max_circle_shape);
//
//                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        txtRealSpeed.setBackgroundDrawable( mActivity.getResources().getDrawable(R.drawable.speed_max_circle_shape) );
//                    } else {
//                        txtRealSpeed.setBackground( mActivity.getResources().getDrawable(R.drawable.speed_max_circle_shape));
//                    }
//                    value = false;
//                }else{
//                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        txtRealSpeed.setBackgroundDrawable( mActivity.getResources().getDrawable(R.drawable.speed_max_circle_shape_white) );
//                    } else {
//                        txtRealSpeed.setBackground( mActivity.getResources().getDrawable(R.drawable.speed_max_circle_shape_white));
//                    }
////                        txtRealSpeed.setBackgroundResource(R.drawable.speed_max_circle_shape_white);
//                        value = true;
//
//                }
//                    }
//                });
//            }
//        };
//    }


//    public void stopTimerPhoenix() {
//        //stop the timer, if it's not already null
//        if (timerPhoenix != null) {
//            timerPhoenix.cancel();
//            timerPhoenix = null;
//        }
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        stopTimerPhoenix();
//    }
}
