package hanoi.petra.ac.id;

import com.badlogic.gdx.Input.TextInputListener;


public abstract class Form implements TextInputListener {
    public String text;
    public boolean clicked = false;
    
   @Override
   public void input (String _text) {
       text = _text;
       clicked = true;
   }

   @Override
   public abstract void canceled ();
}