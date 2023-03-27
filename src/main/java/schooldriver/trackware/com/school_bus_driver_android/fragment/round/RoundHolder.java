package schooldriver.trackware.com.school_bus_driver_android.fragment.round;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import schooldriver.trackware.com.school_bus_driver_android.R;

/**
 * Created   on 3/1/2017.
 */

public class RoundHolder extends RecyclerView.ViewHolder {

    TextView txtNameRound;
    TextView txtTimeRound;
    ImageView imgStartRound;


    public RoundHolder(View itemView) {
        super(itemView);
        txtNameRound=(TextView) itemView.findViewById(R.id.txtNameRound);
        txtTimeRound=(TextView) itemView.findViewById(R.id.txtTimeRound);
        imgStartRound=(ImageView) itemView.findViewById(R.id.imgStartRound);
    }
}
