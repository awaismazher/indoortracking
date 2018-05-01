package demo.vend.wifidistancecalculator.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import demo.vend.wifidistancecalculator.R;
import demo.vend.wifidistancecalculator.callbacks.IItemClickListener;
import demo.vend.wifidistancecalculator.utils.Constants;

public class FloorListAdapter extends RecyclerView.Adapter<FloorListAdapter.MyViewHolder> {
    private final HashMap<String, String> floorNamesMap;
    private final HashMap<String, String> regionNamesMap;
    List<String> floorNames;
    IItemClickListener iItemClickListener;
    private String bssid;

    public FloorListAdapter(List<String> floorNames, HashMap<String, String> floornamesMap,
                            HashMap<String, String> regionNamesMap, IItemClickListener iItemClickListener) {
        this.floorNames = floorNames;
        this.floorNamesMap = floornamesMap;
        this.regionNamesMap = regionNamesMap;
        this.iItemClickListener = iItemClickListener;
    }

    @NonNull
    @Override
    public FloorListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.floor_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FloorListAdapter.MyViewHolder holder, int position) {
        if (floorNames.get(holder.getAdapterPosition()).equals(floorNamesMap.get(bssid))) {
            holder.ivStanding.setVisibility(View.VISIBLE);
            holder.rlFloor.setSelected(true);
        } else {
            holder.ivStanding.setVisibility(View.GONE);
            holder.rlFloor.setSelected(false);
        }
        holder.tvFloorName.setText(getFloorNameAndRegion(holder.getAdapterPosition()));

        holder.itemView.setOnClickListener(v -> iItemClickListener.onItemClicked(floorNames.get(holder.getAdapterPosition())));
    }

    private String getFloorNameAndRegion(int adapterPosition) {
        String  str = Constants.EMPTY_STRING;
        if(floorNamesMap.containsKey(bssid)) {
            if(floorNames.get(adapterPosition).equals(floorNamesMap.get(bssid))) {
                str = floorNamesMap.get(bssid);
                if(regionNamesMap.containsKey(bssid))
                    str += ", " + regionNamesMap.get(bssid);
            } else
                str = floorNames.get(adapterPosition);
        } else
            str = floorNames.get(adapterPosition);
        return str;
    }


    @Override
    public int getItemCount() {
        return floorNames.size();
    }

    public void updateData(String bssid) {
        this.bssid = bssid;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFloorName;
        public ImageView ivStanding;
        private RelativeLayout rlFloor;

        public MyViewHolder(View view) {
            super(view);
            tvFloorName = view.findViewById(R.id.tv_floor_name);
            ivStanding = view.findViewById(R.id.iv_standing);
            rlFloor = view.findViewById(R.id.rl_layout);
        }
    }
}
