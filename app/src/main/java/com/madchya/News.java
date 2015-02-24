package com.madchya;



import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class News extends Activity {
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.news);
	        /*****************************************/
	        String myURL = "http://fantasyapp.blogspot.tw/2013/09/blog-post.html";         
	        WebView myBrowser=(WebView)findViewById(R.id.mybrowser);  
	  
	        WebSettings websettings = myBrowser.getSettings();  
	        websettings.setSupportZoom(true);  
	        websettings.setBuiltInZoomControls(true);   
	        websettings.setJavaScriptEnabled(true);  
	         
	        myBrowser.setWebViewClient(new WebViewClient());  
	  
	        myBrowser.loadUrl(myURL);  
	 		        }	
	    
	    private TextView aa;
	    private TextView bb;
	    private TextView cc;
	    private TextView dd;
	    
	    private void ddd()
	    {
	    	aa =(TextView)findViewById(R.id.textView1);
	    	bb =(TextView)findViewById(R.id.textView2);
	    	cc =(TextView)findViewById(R.id.textView3);
	    	dd =(TextView)findViewById(R.id.textView4);
	    }
}
	    