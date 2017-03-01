package sourabh.ichiapp.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

public  class ProgressWheel {


	ProgressDialog dialog;
	Context context;

	public ProgressWheel(Context context) {
		this.context = context;
	}

	public  void ShowWheel (String title, String message){

		dialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);

		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(true);
		dialog.show();
	}
	
	public  void DismissWheel (){
	
		dialog.dismiss();
	}
	
	public  void ShowDefaultWheel (){
		
		ShowWheel("Loading", "Please Wait");
	}
	
	
}
