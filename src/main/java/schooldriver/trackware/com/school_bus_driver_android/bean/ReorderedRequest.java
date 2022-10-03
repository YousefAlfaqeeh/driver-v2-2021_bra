package schooldriver.trackware.com.school_bus_driver_android.bean;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReorderedRequest {

    @SerializedName("round_id")
    @Expose
    public Integer roundId;
    @SerializedName("ordered_students_ids")
    @Expose
    public List<Integer> orderedStudentsIds = new ArrayList<>();

}
