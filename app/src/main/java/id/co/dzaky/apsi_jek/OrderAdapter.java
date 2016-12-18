package id.co.dzaky.apsi_jek;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Zaki on 19/12/2016.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ReyclerViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<OrderModel> items;

    public OrderAdapter(Context context, List<OrderModel> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;

    }

    @Override
    public ReyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View acara = layoutInflater.inflate(R.layout.order_item, parent, false);
        return new ReyclerViewHolder(acara);
    }

    @Override
    public void onBindViewHolder(final ReyclerViewHolder holder, int position) {
        holder.awal.setText("Lokasi awal: "+items.get(position).awal);
        holder.tujuan.setText("Lokasi tujuan: "+items.get(position).tujuan);
        holder.harga.setText("Harga: "+items.get(position).harga);
        holder.jarak.setText("Jarak: "+items.get(position).jarak);
        holder.durasi.setText("Durasi: "+items.get(position).durasi);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ReyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView awal,tujuan,harga,jarak,durasi;

        private ReyclerViewHolder(final View v) {
            super(v);
            awal = (TextView) v.findViewById(R.id.awal);
            tujuan = (TextView) v.findViewById(R.id.tujuan);
            harga = (TextView) v.findViewById(R.id.harga);
            jarak = (TextView) v.findViewById(R.id.jarak);
            durasi = (TextView) v.findViewById(R.id.durasi);
        }
    }
}
