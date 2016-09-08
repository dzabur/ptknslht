package sk.patkan.fluffy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import sk.patkan.fluffy.Animator;
import sk.patkan.fluffy.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 600;
		new LwjglApplication(new MainGame(), config);
       // new LwjglApplication(new Animator(), config);
	}
}
