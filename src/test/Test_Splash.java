package test;

import java.awt.EventQueue;

import view.Load_Last_Saved_System_View;
import view.Splash_Screen;

public class Test_Splash {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String splashImageFilename_logo = "4G1_logo_rotating_bmf_320x240.gif";
		String splashImageFilename_loading = "alambicchi_480x360.gif";
		String splashImageFilename_searching = "alambicchi_rasp.gif";
		// Throw a nice little title page up on the screen first
		Splash_Screen splash = new Splash_Screen(5000,splashImageFilename_logo);
		splash.showSplash();
		splash = new Splash_Screen(5000,splashImageFilename_loading);
		splash.showSplashMessage("loading...");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					
					
					Load_Last_Saved_System_View frame = new Load_Last_Saved_System_View();
					frame.setVisible(true);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
