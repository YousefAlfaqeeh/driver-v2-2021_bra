package schooldriver.trackware.com.school_bus_driver_android.fragment.notification;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import schooldriver.trackware.com.school_bus_driver_android.R;

/**
 * Created by Ibrahem Al-Betar on 3/2/2017.
 */

public class NotificationHolder_new extends RecyclerView.ViewHolder {

    public CircleImageView notification_image;
    public TextView notification_text, notification_time;

    public NotificationHolder_new(View itemView) {
        super(itemView);
        notification_image = (CircleImageView) itemView.findViewById(R.id.notification_image);
        notification_text = (TextView) itemView.findViewById(R.id.notification_text);
        notification_time = (TextView) itemView.findViewById(R.id.notification_time);

    }
}
