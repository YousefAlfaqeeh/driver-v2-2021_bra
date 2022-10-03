package schooldriver.trackware.com.school_bus_driver_android.fragment.round;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import schooldriver.trackware.com.school_bus_driver_android.R;

/**
 * Created by Ibrahem Al-Betar on 3/1/2017.
 */

public class RoundHolderNew extends RecyclerView.ViewHolder {

    public TextView nameRound_tv, timeRound_tv;
    public View startRound_view, showRound_view, resumeRound_view;


    public RoundHolderNew(View itemView) {
        super(itemView);
        nameRound_tv = (TextView) itemView.findViewById(R.id.nameRound_tv);
        timeRound_tv = (TextView) itemView.findViewById(R.id.timeRound_tv);
        startRound_view = itemView.findViewById(R.id.startRound_view);
        showRound_view = itemView.findViewById(R.id.showRound_view);
        resumeRound_view = itemView.findViewById(R.id.resumeRound_view);
    }


    public RoundHolderNew roundNew() {
        startRound_view.setVisibility(View.VISIBLE);
        showRound_view.setVisibility(View.GONE);
        resumeRound_view.setVisibility(View.GONE);
        return this;
    }


    public RoundHolderNew roundEnded() {
        startRound_view.setVisibility(View.GONE);
        showRound_view.setVisibility(View.VISIBLE);
        resumeRound_view.setVisibility(View.GONE);
        return this;
    }


    public RoundHolderNew roundResume() {
        startRound_view.setVisibility(View.GONE);
        showRound_view.setVisibility(View.GONE);
        resumeRound_view.setVisibility(View.VISIBLE);
        return this;
    }

    public RoundHolderNew roundEmpty() {
        startRound_view.setVisibility(View.INVISIBLE);
        showRound_view.setVisibility(View.GONE);
        resumeRound_view.setVisibility(View.GONE);
        return this;
    }

}
