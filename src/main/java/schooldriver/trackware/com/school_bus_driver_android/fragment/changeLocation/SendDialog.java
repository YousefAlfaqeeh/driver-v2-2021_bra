package schooldriver.trackware.com.school_bus_driver_android.fragment.changeLocation;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import schooldriver.trackware.com.school_bus_driver_android.API.enumApi.EnumNameApi;
import schooldriver.trackware.com.school_bus_driver_android.MainActivity;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseDialog;
import schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo.RoundInfoFragment;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;


/**
 * Created by mohbader on 3/9/2017.
 */


public class SendDialog extends BaseDialog {


    ProgressBar progressBar;
    TextView labSend;
    CountDownTimer mCountDownTimer;

    Button btnOk;
    int timer = 0;

    public SendDialog(@NonNull Context context , String message, final EnumNameApi enumFragment) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.drawable.dialog_regtangel_round_shape);
        setCancelable(false);
        setContentView(R.layout.dialog_send_message);
        labSend=(TextView)findViewById(R.id.labSend);
        labSend.setText(message);
        btnOk=(Button)findViewById(R.id.btnOk);


        btnOk.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (enumFragment==EnumNameApi.CHANGE_LOCATION){
                   Bundle bundle = new Bundle();
                    bundle.putParcelable(UtilityDriver.ROUND, RoundInfoFragment.roundBean);
                    MainActivity.showFragmentRoundInfo(bundle);
                    dismiss();
                }else{
                    dismiss();
                }


            }
        });

      /*  btnOk=(Button)findViewById(R.id.btnOk);
        btnOk.setVisibility(View.INVISIBLE);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        labSend=(TextView)findViewById(R.id.labSend);
        labSend.setVisibility(View.INVISIBLE);
        progressBar=(ProgressBar)findViewById(R.id.prograss);
        if (FragmentType== EnumFragment.MAPS){
            labSend.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            labSend.setText(R.string.bus_near);
            btnOk.setVisibility(View.VISIBLE);
        }else if(FragmentType== EnumFragment.MYKIDS) {

            btnOk.setVisibility(View.INVISIBLE);
            mCountDownTimer = new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.v("Log_tag", "Tick of Progress" + timer + millisUntilFinished);
                    timer++;
                    progressBar.setProgress(timer);

                }

                @Override
                public void onFinish() {
                    //Do what you want
                    timer++;
                    progressBar.setProgress(timer);
                    progressBar.setVisibility(View.INVISIBLE);
                    labSend.setVisibility(View.VISIBLE);
                    btnOk.setVisibility(View.VISIBLE);
                }
            };
            mCountDownTimer.start();

        }
    }*/
    }

}
