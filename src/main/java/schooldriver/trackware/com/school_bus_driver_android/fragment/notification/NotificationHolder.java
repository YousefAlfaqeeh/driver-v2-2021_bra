package schooldriver.trackware.com.school_bus_driver_android.fragment.notification;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import schooldriver.trackware.com.school_bus_driver_android.R;

/**
 * Created by Ibrahem Al-Betar on 3/2/2017.
 */

public class NotificationHolder extends RecyclerView.ViewHolder {

    TextView txtDetails;
    Button btnTime;
    CircleImageView imgStudent;

    public NotificationHolder(View itemView) {
        super(itemView);
        imgStudent = (CircleImageView) itemView.findViewById(R.id.imgStudent);
        txtDetails = (TextView) itemView.findViewById(R.id.txtDetails);
        btnTime = (Button) itemView.findViewById(R.id.btnTime);
        imgStudent = (CircleImageView) itemView.findViewById(R.id.imgStudent);

    }
}
