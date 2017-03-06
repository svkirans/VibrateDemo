package com.codingsquare.cordova.vibrate.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutEvent;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutHandler;
import com.googlecode.gwtphonegap.client.notification.Notification;
import com.googlecode.gwtphonegap.client.notification.PromptCallback;
import com.googlecode.gwtphonegap.client.notification.PromptResults;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VibrateDemo implements EntryPoint {

	 private Logger log = Logger.getLogger(getClass().getName());
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		 GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

		      @Override
		      public void onUncaughtException(Throwable e) {
		        Window.alert("uncaught: " + e.getLocalizedMessage());
		        log.log(Level.SEVERE, "uncaught exception", e);
		      }
		    });

		    final PhoneGap phoneGap = GWT.create(PhoneGap.class);

		    phoneGap.addHandler(new PhoneGapAvailableHandler() {

		      @Override
		      public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {
		    	    createUI(phoneGap);
		      }
		    });

		    phoneGap.addHandler(new PhoneGapTimeoutHandler() {

		      @Override
		      public void onPhoneGapTimeout(PhoneGapTimeoutEvent event) {
		        Window.alert("can not load phonegap");

		      }
		    });
		    phoneGap.initializePhoneGap();
	}
	
	private void createUI(final PhoneGap phoneGap) {
		try {
			FlowPanel container = new FlowPanel();
			
			final Notification notification = phoneGap.getNotification();
			
			Button vibrateBtn = new Button("Vibrate");
			Button cancelBtn = new Button("Cancel");
			Button vibratePatternBtn = new Button("Vibrate With Pattern");
			
			cancelBtn.addTapHandler(new TapHandler() {
				
				@Override
				public void onTap(TapEvent event) {
					notification.vibrate(0);
				}
			});
			
			vibratePatternBtn.addTapHandler(new TapHandler() {
				
				@Override
				public void onTap(TapEvent event) {
				notification.prompt("Enter the vibration pattern.", new PromptCallback() {
					
					@Override
					public void onPrompt(PromptResults results) {
						ArrayList<Integer> alVibrationPattern = new ArrayList<Integer>();
						int index = results.getButtonIndex();
						if(index == 1){
							String pattern = results.getInput1();
							String parsedTokens[] = pattern.split(",");
							for (int i = 0; i < parsedTokens.length; i++) {
								String token = parsedTokens[i];
								Integer time2Vibrate = Integer.parseInt(token);
								int time2VibrateInMs = time2Vibrate *1000;
								alVibrationPattern.add(time2VibrateInMs);
							}
							int[] arrVibratePattern= new int[alVibrationPattern.size()];
							int indexNum =0;
							for (Integer iVibrationTime : alVibrationPattern) {
								arrVibratePattern[indexNum] = (int)iVibrationTime;
								indexNum++;
							}
							notification.vibrate(arrVibratePattern);
						}
					}
				}, "Vibrate with Pattern", "10,20,30");	
				}
			});
			
			vibrateBtn.addTapHandler(new TapHandler() {
				
				@Override
				public void onTap(TapEvent event) {
					notification.prompt("Enter the number of seconds to vibrate ?", new PromptCallback() {
						
						@Override
						public void onPrompt(PromptResults results) {
							int buttonIndex = results.getButtonIndex();
							if(buttonIndex == 1){
								String time2Vibrate = results.getInput1();
								Integer time2VibrateInS = Integer.parseInt(time2Vibrate);
								int time2VibrateInMs = time2VibrateInS *1000;
								notification.vibrate(time2VibrateInMs);
							}
							
						}
					}, "Vibrate", "10");
				}
			});
			
			container.add(vibrateBtn);
			container.add(vibratePatternBtn);
			container.add(cancelBtn);
			RootPanel.get("VibrateDemo").add(container);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
