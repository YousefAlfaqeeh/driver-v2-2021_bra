package schooldriver.trackware.com.school_bus_driver_android.adapters;

import android.app.Activity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilDialogs;

public class DraggableHolder extends RecyclerView.ViewHolder implements DragDropHelper.ItemTouchHelperViewHolder {

    public DraggableHolder(View itemView) {
        super(itemView);
    }

//    @Override
//    public void onItemSelected() {
//        itemView.setScaleY(1.2f);
//        itemView.setScaleX(1.2f);
//        itemView.setRotation(2.0f);
//        ViewCompat.setTranslationZ(itemView, 2.0f);
//        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.color_green_trans));
//    }

    @Override
    public void onItemSelected() {
        ViewCompat.setTranslationZ(itemView, 2.0f);
    }


    @Override
    public void onItemClear() {
        itemView.setScaleY(1.0f);
        itemView.setScaleX(1.0f);
        ViewCompat.setTranslationZ(itemView, 0.0f);
        itemView.setRotation(0.0f);
    }

//    public UtilDialogs.StudentToolDialog studentToolDialog;

    public UtilDialogs.StudentToolDialog showToolsDialog(Activity activity, int type) {
//        if (studentToolDialog != null)
//            studentToolDialog.dismiss();

        UtilDialogs.StudentToolDialog studentToolDialog = new UtilDialogs.StudentToolDialog().show(activity, type);

        return studentToolDialog;
    }
}
