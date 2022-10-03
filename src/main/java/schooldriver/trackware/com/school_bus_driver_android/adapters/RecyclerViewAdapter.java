package schooldriver.trackware.com.school_bus_driver_android.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class RecyclerViewAdapter<Type, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder>  {

    private Type removedItem = null;
    private ArrayList<Type> rootList = new ArrayList<Type>();
    private ArrayList<Type> originalList = new ArrayList<Type>();


    public void reverseAllItems(){
        Collections.reverse(rootList);
        Collections.reverse(originalList);
        notifyDataSetChanged();
    }


    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return cViewHolder(viewGroup, i, layoutInflater);
    }

    @Override
    public final void onBindViewHolder(ViewHolder viewHolder, int i) {
        bViewHolder(viewHolder, i, rootList.get(i));
    }

    @Override
    public int getItemCount() {
        return rootList.size();
    }

    public Type getItem(int position) {
        return rootList.get(position);
    }

//    public void add(Type... items) {
//        for (Type item : items) {
//            add(item);
//        }
//    }

    public void add(Type item) {
        add(item,true);
    }

    public void add(Type item,boolean animate) {
        if (item == null) {
            return;
        }
        rootList.add(item);
        originalList.add(item);
        if (animate)
        notifyItemInserted(rootList.size() - 1);
    }


    public void addFirst(Type item) {
        add(item,true);
    }

    public void addFirst(Type item,boolean animate) {
        if (item == null) {
            return;
        }
        rootList.add(0, item);
        originalList.add(0, item);
        if (animate)
        notifyItemInserted(0);
    }

    public void addAll(List<Type> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (Type type : list) {
            add(type);
        }
    }

    public void newList(List<Type> list) {
        clear();
        addAll(list);
    }

    public void clear() {
        while (!rootList.isEmpty()) {
            remove(0,true);
        }
        originalList.clear();
    }


    public void remove(int position) {
        remove(position,true);
    }

    public Type remove(int position,boolean animate) {
        removedItem = rootList.remove(position);
        originalList.remove(position);
        if (animate)
        notifyItemRemoved(position);
        return removedItem;
    }

    public void undoRemove() {
        if (removedItem != null) {
            add(removedItem);
            removedItem = null;
        }
    }

    public void sort(Comparator<Type> comparator) {
        Collections.sort(rootList, comparator);
        Collections.sort(originalList, comparator);
        notifyDataSetChanged();
    }

    public List<Type> getValues() {
        return originalList;
    }






    public boolean contain(Object object) {
        return rootList.contains(object);
    }

    public void filter(AdapterFilter<Type> adapterFilter) {
        rootList.clear();
        if (adapterFilter == null) {
            rootList.addAll(originalList);
        } else {
            for (Type type : originalList) {
                if (adapterFilter.filter(type)) {
                    rootList.add(type);
                }
            }
        }

        notifyDataSetChanged();
    }

    public abstract ViewHolder cViewHolder(ViewGroup viewGroup, int i, LayoutInflater layoutInflater);

    public abstract void bViewHolder(ViewHolder viewHolder, int i, Type item);

    public interface AdapterFilter<T> {
        boolean filter(T type);
    }


    public void setRootList(List<Type> list){
        rootList.clear();
        rootList.addAll(list);
    }

}