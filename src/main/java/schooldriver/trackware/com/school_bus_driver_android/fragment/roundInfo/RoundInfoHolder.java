package schooldriver.trackware.com.school_bus_driver_android.fragment.roundInfo;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import schooldriver.trackware.com.school_bus_driver_android.R;

/**
 * Created   on 2/28/2017.
 */

public class RoundInfoHolder extends RecyclerView.ViewHolder {


    TextView labStudentName,labGrade,labSchoolName,labDone;
    ImageView imgCall,imgShow,imgCheck,imgChangeLocation,imgDone;

//    LinearLayout lliMain;
    CircleImageView imgStudent;

    public RoundInfoHolder(View itemView) {
        super(itemView);

        labStudentName = (TextView) itemView.findViewById(R.id.labStudentName);
        labGrade = (TextView) itemView.findViewById(R.id.labGrade);
        labSchoolName = (TextView) itemView.findViewById(R.id.labSchoolName);
        labDone = (TextView) itemView.findViewById(R.id.labDone);
        imgDone = (ImageView) itemView.findViewById(R.id.imgDone);

        imgCall = (ImageView) itemView.findViewById(R.id.imgCall);
        imgShow = (ImageView) itemView.findViewById(R.id.imgShow);
        imgCheck = (ImageView) itemView.findViewById(R.id.imgCheck);
        imgChangeLocation = (ImageView) itemView.findViewById(R.id.imgChangeLocation);
//        lliMain = (LinearLayout) itemView.findViewById(R.id.lliMain);
        imgStudent = (CircleImageView) itemView.findViewById(R.id.imgStudent);

    }
}
