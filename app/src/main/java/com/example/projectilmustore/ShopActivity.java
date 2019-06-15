package com.example.projectilmustore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    TextView tvsname,tvsphone,tvsaddress,tvsloc;
    ImageView imgShop;
    ListView lvbook;
    Dialog myDialogWindow;
    ArrayList<HashMap<String, String>> booklist;
    String userid,shopid,userphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        shopid = bundle.getString("shopid");
        String sname = bundle.getString("name");
        String sphone = bundle.getString("phone");
        String saddress = bundle.getString("address");
        String slocation = bundle.getString("location");
        userid = bundle.getString("userid");
        userphone = bundle.getString("userphone");
        initView();
        tvsname.setText(sname);
        tvsaddress.setText(saddress);
        tvsphone.setText(sphone);
        tvsloc.setText(slocation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(this).load("https://projectilmustore.000webhostapp.com/member/images/"+shopid+".jpg")
                .fit().into(imgShop);
        //  .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)

        lvbook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showBookDetail(position);
            }
        });
        loadBooks(shopid);

    }

    private void showBookDetail(int p) {
        myDialogWindow = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);//Theme_DeviceDefault_Dialog_NoActionBar
        myDialogWindow.setContentView(R.layout.dialog_window);
        myDialogWindow.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvbname,tvbprice,tvbquan;
        final ImageView imgbook = myDialogWindow.findViewById(R.id.imageViewBook);
        final Spinner spquan = myDialogWindow.findViewById(R.id.spinner2);
        Button btnorder = myDialogWindow.findViewById(R.id.button2);
        final ImageButton btnfb = myDialogWindow.findViewById(R.id.btnfacebook);
        tvbname= myDialogWindow.findViewById(R.id.textView12);
        tvbprice = myDialogWindow.findViewById(R.id.textView13);
        tvbquan = myDialogWindow.findViewById(R.id.textView14);
        tvbname.setText(booklist.get(p).get("bookname"));
        tvbprice.setText(booklist.get(p).get("bookprice"));
        tvbquan.setText(booklist.get(p).get("bookquantity"));
        final String bookid =(booklist.get(p).get("bookid"));
        final String bookname = booklist.get(p).get("bookname");
        final String bookprice = booklist.get(p).get("bookprice");
        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bquan = spquan.getSelectedItem().toString();
                dialogOrder(bookid,bookname,bquan,bookprice);
            }
        });

        btnfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = ((BitmapDrawable)imgbook.getDrawable()).getBitmap();
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();


                ShareDialog shareDialog = new ShareDialog(ShopActivity.this);
                shareDialog.show(content);
            }
        });
        int quan = Integer.parseInt(booklist.get(p).get("bookquantity"));
        List<String> list = new ArrayList<String>();
        for (int i = 1; i<=quan;i++){
            list.add(""+i);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spquan.setAdapter(dataAdapter);

        Picasso.with(this).load("https://projectilmustore.000webhostapp.com/member/bookimages/"+bookid+".jpg")
                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .fit().into(imgbook);
        myDialogWindow.show();
    }

    private void dialogOrder(final String bookid, final String bookname, final String bquan, final String bookprice) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Order "+bookname+ " with quantity "+bquan);

        alertDialogBuilder
                .setMessage("Are you sure")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        insertCart(bookid,bookname,bquan,bookprice);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void insertCart(final String bookid, final String bookname, final String bquan, final String bookprice) {
        class InsertCart extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("bookid",bookid);
                hashMap.put("shopid",bookid);
                hashMap.put("bookname",bookname);
                hashMap.put("quantity",bquan);
                hashMap.put("bookprice",bookprice);
                hashMap.put("userid",userphone);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("https://projectilmustore.000webhostapp.com/member/insert_cart.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(ShopActivity.this,s, Toast.LENGTH_SHORT).show();
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(ShopActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    myDialogWindow.dismiss();
                    loadBooks(shopid);
                }else{
                    Toast.makeText(ShopActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

        }
        InsertCart insertCart = new InsertCart();
        insertCart.execute();
    }

    private void loadBooks(final String shopid) {
        class LoadBook extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("shopid",shopid);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("https://projectilmustore.000webhostapp.com/member/load_books.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                booklist.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray bookarray = jsonObject.getJSONArray("book");
                    for (int i = 0; i < bookarray.length(); i++) {
                        JSONObject c = bookarray.getJSONObject(i);
                        String jsid = c.getString("bookid");
                        String jsbname = c.getString("bookname");
                        String jsbprice = c.getString("bookprice");
                        String jsquan = c.getString("quantity");
                        HashMap<String,String> booklisthash = new HashMap<>();
                        booklisthash.put("bookid",jsid);
                        booklisthash.put("bookname",jsbname);
                        booklisthash.put("bookprice",jsbprice);
                        booklisthash.put("bookquantity",jsquan);
                        booklist.add(booklisthash);
                    }
                }catch(JSONException e){}
                ListAdapter adapter = new CustomAdapterBook(
                        ShopActivity.this, booklist,
                        R.layout.book_list_shop, new String[]
                        {"bookname","bookrice","bookquantity"}, new int[]
                        {R.id.textView,R.id.textView2,R.id.textView3});
                lvbook.setAdapter(adapter);

            }
        }
        LoadBook loadBook = new LoadBook();
        loadBook.execute();
    }

    private void initView() {
        imgShop = findViewById(R.id.imageView3);
        tvsname = findViewById(R.id.textView6);
        tvsphone = findViewById(R.id.textView7);
        tvsaddress = findViewById(R.id.textView8);
        tvsloc = findViewById(R.id.textView9);
        lvbook = findViewById(R.id.listviewbook);
        booklist = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ShopActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",userid);
                bundle.putString("phone",userphone);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

