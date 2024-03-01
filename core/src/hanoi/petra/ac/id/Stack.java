package hanoi.petra.ac.id;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Stack {
    int num;
    Texture stackTexture;
    Sprite stack;
    
    Stack(){
    }

    void setTexture(String texture){
        stackTexture = new Texture(texture);
        stack = new Sprite(stackTexture);
        stack.setAlpha(1);
    }
    
    void setPosition(float x, float y){
        stack.setPosition(x, y);
    }

    void setNum(int _num){
        num = _num;
    }
    
    float getPositionX(){
        return stack.getX();
    }

    int getNum(){
        return num;
    }

    Texture getTexture(){
        return stack.getTexture();
    }
    
    Sprite getSprite(){
        return stack;
    }
    
    int getWidth(){
        return stackTexture.getWidth();
    }
    
}
