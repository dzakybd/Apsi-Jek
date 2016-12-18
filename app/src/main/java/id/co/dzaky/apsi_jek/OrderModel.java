package id.co.dzaky.apsi_jek;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Zaki on 19/12/2016.
 */

@Table(name = "OrderModel")
public class OrderModel extends Model {
        @Column(name = "awal")
        public String awal;
        @Column(name = "tujuan")
        public String tujuan;
        @Column(name = "jarak")
        public String jarak;
        @Column(name = "durasi")
        public String durasi;
        @Column(name = "harga")
        public String harga;

        public OrderModel() {
                super();
        }
}
