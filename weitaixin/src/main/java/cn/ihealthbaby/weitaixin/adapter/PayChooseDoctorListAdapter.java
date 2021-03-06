package cn.ihealthbaby.weitaixin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ihealthbaby.client.model.AdviceItem;
import cn.ihealthbaby.weitaixin.R;


public class PayChooseDoctorListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AdviceItem> datas;
    private LayoutInflater mInflater;

    public PayChooseDoctorListAdapter(Context context, ArrayList<AdviceItem> datas) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        setDatas(datas);
    }

    public void setDatas(ArrayList<AdviceItem> datas) {
        if (datas == null) {
            this.datas = new ArrayList<AdviceItem>();
        } else {
            this.datas.clear();
            this.datas = datas;
        }
    }


    public void addDatas(ArrayList<AdviceItem> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
        }
    }


    @Override
    public int getCount() {
        return this.datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pay_choose_doctor_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        AdviceItem adviceItem = this.datas.get(position);

        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.ivDoctorPic) ImageView ivDoctorPic;
        @Bind(R.id.tvDoctorName) TextView tvDoctorName;
        @Bind(R.id.tvDoctorLocation) TextView tvDoctorLocation;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }


    public DisplayImageOptions setDisplayImageOptions() {
        DisplayImageOptions options = null;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.button_monitor_helper)
                .showImageForEmptyUri(R.drawable.button_monitor_helper)
                .showImageOnFail(R.drawable.button_monitor_helper)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        return options;
    }


}
