package schooldriver.trackware.com.school_bus_driver_android.fragment.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import schooldriver.trackware.com.school_bus_driver_android.R;
import schooldriver.trackware.com.school_bus_driver_android.bean.AttendantBean;
import schooldriver.trackware.com.school_bus_driver_android.bean.StudentBean;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;

/**
 * Created by muradtrac on 3/7/17.
 */

public class MapPresenter {
    Activity mActivity;

    public MapPresenter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public Bitmap MarkerWithOptionsBitmap() {
        View marker = LayoutInflater.from(mActivity).inflate(R.layout.marker_with_bus,
                null);

        marker.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());

        final Bitmap clusterBitmap = Bitmap.createBitmap(marker.getMeasuredWidth(),
                marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(clusterBitmap);
        marker.draw(canvas);

        return clusterBitmap;
    }


    CircleImageView imgStudent;

    public Bitmap MarkerWithStudentsBitmapSmall(StudentBean studentBean, Bitmap bitmap, int no) {
        View marker = LayoutInflater.from(mActivity).inflate(R.layout.marker_student_far,//marker_student_far
                null);
        TextView labNameStudent = (TextView) marker.findViewById(R.id.labNameStudent);
        TextView labNoStudent = (TextView) marker.findViewById(R.id.labNoStudent);
        labNameStudent.setText(studentBean.getNameStudent());
        labNoStudent.setText(""+no);

        imgStudent = (CircleImageView) marker.findViewById(R.id.imgStudent);

        if (StaticValue.G34) {
            imgStudent.setImageBitmap(bitmap);
        }

        marker.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());

        final Bitmap clusterBitmap = Bitmap.createBitmap(marker.getMeasuredWidth(),
                marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(clusterBitmap);
        marker.draw(canvas);

        return clusterBitmap;
    }

    public Bitmap MarkerWithAttendantBeanBitmapSmall(AttendantBean attendantBean, Bitmap bitmap, int no) {
        View marker = LayoutInflater.from(mActivity).inflate(R.layout.marker_student_far,//marker_student_far
                null);
        TextView labNameStudent = (TextView) marker.findViewById(R.id.labNameStudent);
        TextView labNoStudent = (TextView) marker.findViewById(R.id.labNoStudent);
        labNameStudent.setText(attendantBean.getName());
        if (no==-1){
            labNoStudent.setVisibility(View.INVISIBLE);
        }else {
            labNoStudent.setText(""+no);
        }


        imgStudent = (CircleImageView) marker.findViewById(R.id.imgStudent);

        if (StaticValue.G34) {
            imgStudent.setImageBitmap(bitmap);
        }

        marker.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());

        final Bitmap clusterBitmap = Bitmap.createBitmap(marker.getMeasuredWidth(),
                marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(clusterBitmap);
        marker.draw(canvas);

        return clusterBitmap;
    }


//    public Bitmap MarkerWithStudentsBitmapBig(StudentBean studentBean, Bitmap bitmap) {
//        View marker = LayoutInflater.from(mActivity).inflate(R.layout.marker_student_near,
//                null);
//        TextView textView = (TextView) marker.findViewById(R.id.labNameStudent);
//        textView.setText(studentBean.getNameStudent());
//
//        imgStudent = (CircleImageView) marker.findViewById(R.id.imgStudent);
////        imgStudent.setImageDrawable(UtilityDriver.getDrawableImage(mActivity, id));
//
//        imgStudent.setImageBitmap(bitmap);
//
//        marker.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());
//
//        final Bitmap clusterBitmap = Bitmap.createBitmap(marker.getMeasuredWidth(),
//                marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(clusterBitmap);
//        marker.draw(canvas);
//
//        return clusterBitmap;
//    }
}
