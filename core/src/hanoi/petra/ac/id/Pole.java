package hanoi.petra.ac.id;
  
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Pole {
    
    Texture plTexture;
    Sprite pl;
    Array<Stack> stacks = new Array<Stack>();
    boolean lastPick;
    int num;
    
    Pole(){
        lastPick = false;
    }
    
    void setNum(int _num){
        num = _num;
    }
    
    void setTexture(String texture){
        plTexture = new Texture(texture);
        pl = new Sprite(plTexture);
    }
    
    void setPosition(float x, float y){
        pl.setPosition(x, y);
    }
    
    void setStackPosition(){
        for(int i=0; i<getSize(); i++){
            stacks.get(i).getSprite().setAlpha(1);
            stacks.get(i).setPosition((pl.getX()+pl.getWidth()/2 - stacks.get(i).getWidth()/2), 56 * i + 250);
        }
    }
    
    int getNum(){
        return num;
    }
    
    Sprite getSprite(){
        return pl; 
    }
    
    Texture getTexture(){
        return pl.getTexture();
    }
    
    Rectangle getBounds(){
        return pl.getBoundingRectangle();
    }
    
    int getWidth(){
        return pl.getTexture().getWidth();
    }
    
    int getTop(){
       return stacks.peek().getNum();
    }
    
    int getSize(){
        return stacks.size;
    }
    
    float getAlpha(){
        return stacks.peek().getSprite().getColor().a;
    }
    
//    void pop(){
//        if(getSize()>0){
//            stacks.pop();
//        }
//    }
    
    Stack pop(){
        return stacks.pop();    
    }
    
    void push(Stack stk){
        stacks.add(stk);
    }       
    
    void setLastPick(boolean lastpick){
        if(getSize()>0){
            stacks.peek().getSprite().setAlpha(1);
        }
        lastPick = lastpick;
    }
    
    boolean getLastPick(){
        return lastPick;
    }
    
    void setAlpha(float a){
        stacks.peek().getSprite().setAlpha(a);
    }
    
    void moveStack(Pole pl){
        pl.push(stacks.pop());
        setLastPick(false);
        pl.setStackPosition();
    }

    void moveCancel(){
        if(getLastPick()==true){
            setLastPick(false);
        }
    }
}