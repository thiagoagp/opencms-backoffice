package com.mscg.virgilio.listener;

import org.apache.http.client.methods.HttpGet;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mscg.virgilio.VirgilioGuidaTvPrograms;
import com.mscg.virgilio.net.HttpClientManager;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.ContextAndHandlerAware;
import com.mscg.virgilio.util.ProgramLinearLayout;
import com.mscg.virgilio.util.net.AsynchResponseHandler;
import com.mscg.virgilio.util.net.ProgramsDetailsResponseHandler;
import com.mscg.virgilio.util.net.VirgilioURLUtil;

public class ProgramSelectionClickListener extends ContextAndHandlerAware implements OnItemClickListener {

	public ProgramSelectionClickListener(VirgilioGuidaTvPrograms context, Handler guiHandler) {
		super(context, guiHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ProgramLinearLayout pll = (ProgramLinearLayout) view;
		TVProgram program = pll.getTvProgram();

		String url = VirgilioURLUtil.DETAILS_URL.replace("${programID}", program.getStrId());
		HttpGet get = new HttpGet(url);
		AsynchResponseHandler<String> responseHandler = new ProgramsDetailsResponseHandler(context, guiHandler);
		HttpClientManager.executeAsynchMethod(get, responseHandler);
	}

}
