
package com.vedas.spectrocare.PatientModule;

 import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.ToggleButton;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.cardview.widget.CardView;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.vedas.spectrocare.R;

        import java.util.ArrayList;

public class TestMenuStartactivity extends AppCompatActivity {
    ToggleButton creditBtn,paypalBtn;
    RelativeLayout rl_back;
    RecyclerView urineView;
    TextView txt_testType;
    UrineAllAdpter urineAllAdpter;
    int selectedPos=-1;
    ArrayList<String> urineList=new ArrayList<>();
    ArrayList<String> bloodList=new ArrayList<>();
    ArrayList<String> arrayList=new ArrayList<>();
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu_startactivity);
        loadIds();
        loadArrayList();

    }
    private void loadArrayList(){
        urineList.add("Urine Routine");
        urineList.add("Urine Routine");
        urineList.add("Urine Routine");

       /* urineList.add("12 Parameters");
        urineList.add("1 Parameter");
        urineList.add("10 Parameters");
*/
        bloodList.add("Lapid Panel");
        bloodList.add("Thyroid");
        bloodList.add("Total Cholesterol");
    }
    private void loadIds(){
        creditBtn=findViewById(R.id.toggle_1);
        paypalBtn=findViewById(R.id.toggle_2);
        urineView=findViewById(R.id.recycler_view);
        txt_testType=findViewById(R.id.txt_type);
        btn_next=findViewById(R.id.btn_next);

        rl_back=findViewById(R.id.back);
        arrayList=urineList;
        txt_testType.setVisibility(View.GONE);
        urineView.setVisibility(View.VISIBLE);
        loadRecyclerview();
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BleScanningActivity.class));
            }
        });
        creditBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txt_testType.setVisibility(View.GONE);
                urineView.setVisibility(View.VISIBLE);
                arrayList=urineList;
                if(isChecked){
                    paypalBtn.setChecked(true);
                    arrayList=bloodList;
                }else {
                    paypalBtn.setChecked(false);
                  //  arrayList=urineList;
                }
                urineAllAdpter.notifyDataSetChanged();
            }
        });
        paypalBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txt_testType.setVisibility(View.GONE);
                urineView.setVisibility(View.VISIBLE);
                arrayList=bloodList;
                if(isChecked){
                    creditBtn.setChecked(true);
                    arrayList=urineList;
                }else {
                    creditBtn.setChecked(false);
                   // arrayList=bloodList;
                }
                urineAllAdpter.notifyDataSetChanged();
            }
        });
    }
    private void loadRecyclerview(){
        urineAllAdpter = new UrineAllAdpter();
        urineView.setHasFixedSize(true);
        urineView.setLayoutManager(new LinearLayoutManager(this));
        urineView.setAdapter(urineAllAdpter);
        urineAllAdpter.notifyDataSetChanged();
    }
    public class UrineAllAdpter extends RecyclerView.Adapter<UrineAllAdpter.Holder> {
        // ArrayList<String> arrayList=new ArrayList<>();
        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View allMedical = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testmenu_items, parent, false);
            return new Holder(allMedical);
        }
        UrineAllAdpter(){
            //  this.arrayList=list;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            String myListData = arrayList.get(position);
            holder.txt_name.setText(myListData);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedPos !=position){
                        selectedPos=position;
                        notifyDataSetChanged();
                    }else {
                        selectedPos=-1;
                        notifyDataSetChanged();
                    }
                }
            });
            if(selectedPos==position){
                holder.rl_hiddenView.setVisibility(View.VISIBLE);
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#E9F9FB"));
            }else {
                holder.rl_hiddenView.setVisibility(View.GONE);
                holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            public Button downBtn;
            public TextView paramsTxt,txt_name;
            public CardView relativeLayout;
            RelativeLayout rl_hiddenView;
            public Holder(View itemView) {
                super(itemView);
                this.downBtn = (Button) itemView.findViewById(R.id.down);
                this.paramsTxt = (TextView) itemView.findViewById(R.id.params);
                this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                this. relativeLayout = (CardView)itemView.findViewById(R.id.relativeLayout);
                this. rl_hiddenView = (RelativeLayout) itemView.findViewById(R.id.rl_hiddenview);
            }
        }
    }
}
