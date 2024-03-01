package hanoi.petra.ac.id.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hanoi.petra.ac.id.hanoi;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.fullscreen = false;
        //config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width; 
        //config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        LwjglApplication lwjglApplication = new LwjglApplication(new hanoi(), config);
    }
}
