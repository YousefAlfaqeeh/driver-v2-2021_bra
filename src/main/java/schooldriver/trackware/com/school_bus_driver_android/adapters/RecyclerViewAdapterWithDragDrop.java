package schooldriver.trackware.com.school_bus_driver_android.adapters;

import java.util.Collections;
import java.util.List;

public abstract class RecyclerViewAdapterWithDragDrop<Type, Holder extends DraggableHolder> extends RecyclerViewAdapter<Type, Holder> implements DragDropHelper.ItemTouchHelperAdapter {


    private DragDropHelper.OnStartDragListener onStartDragListener;
    private DragDropHelper.OnListChangedListener onListChangedListener;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(getValues(), fromPosition, toPosition);
        getDraggableListChangedListener().onListChanged(getValues());

//        Collections.swap(getRootValues(), fromPosition, toPosition);
//        getDraggableListChangedListener().onListChanged(getRootValues());


        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {

    }


    public RecyclerViewAdapterWithDragDrop<Type, Holder> setOnStartDragListener(DragDropHelper.OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
        return this;
    }

    public RecyclerViewAdapterWithDragDrop<Type, Holder> setOnListChangedListener(DragDropHelper.OnListChangedListener onListChangedListener) {
        this.onListChangedListener = onListChangedListener;
        return this;
    }

//    public DragDropHelper.OnStartDragListener getDragDropListener() {
//        if (onStartDragListener == null)
//            onStartDragListener = new DragDropHelper.OnStartDragListener() {
//                @Override
//                public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
//
//                }
//            };
//
//        return onStartDragListener;
//    }


    public <StudentBean> DragDropHelper.OnListChangedListener getDraggableListChangedListener() {
        if (onListChangedListener == null)
            onListChangedListener = new DragDropHelper.OnListChangedListener<Type>() {
                @Override
                public void onListChanged(List<Type> items) {
                    setRootList(items);
                }

            };
        return onListChangedListener;
    }

}