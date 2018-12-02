package com.example.webviewPre;

import org.json.JSONException;
  




import com.example.webviewPre.R;
import com.example.webviewsuper.CardMainSuperActivity;
import com.example.webviewsuper.LoanMainSuperActivity;
import com.example.webviewsuper.MainSuperActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
 
public class TabHostActivity extends TabActivity {
    /** Called when the activity is first created. */
	private TabHost tabHost;
	private TextView main_tab_new_message;
  
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bottom);
        
        main_tab_new_message=(TextView) findViewById(R.id.main_tab_new_message);
        main_tab_new_message.setVisibility(View.GONE);
        main_tab_new_message.setText("");
        
        tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent=new Intent().setClass(this, MainSuperActivity.class);
        spec=tabHost.newTabSpec("主页").setIndicator("主页").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,CardMainSuperActivity.class);
        spec=tabHost.newTabSpec("办卡").setIndicator("办卡").setContent(intent);
        tabHost.addTab(spec);
        
//        intent=new Intent().setClass(this, LineLoanShowActivity.class);
//        spec=tabHost.newTabSpec("网贷").setIndicator("网贷").setContent(intent);
//        tabHost.addTab(spec);
        
     
        intent=new Intent().setClass(this, LoanMainSuperActivity.class);
        spec=tabHost.newTabSpec("贷款").setIndicator("贷款").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, WebViewBBSActivity.class);
        spec=tabHost.newTabSpec("社区").setIndicator("社区").setContent(intent);
        tabHost.addTab(spec);
        
        
        tabHost.setCurrentTab(0);
        
        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.main_tab_addExam: 
					tabHost.setCurrentTabByTag("主页");
					break;
				case R.id.main_tab_myExam: 
					tabHost.setCurrentTabByTag("办卡");
					break;
//				case R.id.main_tab_message: 
//					tabHost.setCurrentTabByTag("网贷");
//					break;
				case R.id.main_tab_settings: 
					tabHost.setCurrentTabByTag("贷款");
					break;
				case R.id.main_tab_bbs: 
					tabHost.setCurrentTabByTag("社区");
					break;
				default:
					//tabHost.setCurrentTabByTag("我的考试");
					break;
				}
			}
		});
      
    }
    
    protected void onResume() {  
        super.onResume();  
        
//        long carCount = db.GetShopingCarCount(); 
//         
//        main_tab_new_message.setText(carCount + "");
    }
 
 
    
   
}
