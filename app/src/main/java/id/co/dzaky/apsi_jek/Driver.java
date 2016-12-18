package id.co.dzaky.apsi_jek;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Driver extends Fragment {
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;

    public Driver() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.driver, container, false);
        List<OrderModel> orderModels = new Select()
                .from(OrderModel.class)
                .execute();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        OrderAdapter adapter = new OrderAdapter(getActivity(), orderModels);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

}
