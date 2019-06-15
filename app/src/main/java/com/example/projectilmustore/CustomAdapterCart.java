package com.example.projectilmustore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapterCart extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomAdapterCart(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.user_cart_list, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            TextView tvbookname = vi.findViewById(R.id.textView);
            TextView tvbookprice = vi.findViewById(R.id.textView2);
            TextView tvquantity = vi.findViewById(R.id.textView3);
            TextView tvstatus = vi.findViewById(R.id.textView4);
            CircleImageView imgbook =vi.findViewById(R.id.imageView2);
            String dbname = (String) data.get("bookname");//hilang
            String dbookprice =(String) data.get("bookprice");
            String dbookquan =(String) data.get("quantity");
            String dbid=(String) data.get("bookid");
            String dbst=(String) data.get("status");
            String dborderid=(String) data.get("orderid");
            tvbookname.setText(dbname);
            tvbookprice.setText(dbookprice);
            tvquantity.setText(dbookquan);
            tvstatus.setText(dbst);
            String image_url = "https://projectilmustore.000webhostapp.com/member/bookimages/"+dbid+".jpg";
            Picasso.with(mContext).load(image_url)
                    .fit().into(imgbook);
//                    .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)

        }catch (IndexOutOfBoundsException e){

        }

        return vi;
    }
}
