
package hanoi.petra.ac.id;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class LoginMenu implements Screen {
    
    final hanoi game;
    Form form;
    Texture loginTexture, registerTexture;
    Sprite loginBtn, registerBtn;
     
    boolean a, cancel, chooseLogin = false, wrong = false;
    int b;
    String confirm;
    
    public LoginMenu(final hanoi game) {
        this.game = game;   
        chooseLogin = true;
        
        game.userLogin = new User();
        
        loginTexture = new Texture("image/login.png");
        registerTexture = new Texture("image/signup.png");
       
        loginBtn = new Sprite(loginTexture);
        registerBtn = new Sprite(registerTexture);
       
        loginBtn.setPosition(1920/2 - loginBtn.getWidth()/2 - 300, 1080/2 - loginBtn.getHeight()/2);
        registerBtn.setPosition(1920/2 - registerBtn.getWidth()/2 + 300, 1080/2 - registerBtn.getHeight()/2);
       
    }
        
    public LoginMenu(final hanoi game, boolean mode, int state) {
        
        a = mode;
        b = state;
        
        this.game = game;    
        
        form = new Form() {
            @Override
            public void canceled() {
                cancel = true;
            }
        };
                
        if(mode==true){
            if(state==0){
                Gdx.input.getTextInput(form, "Login", "", "Username");
            }
            else{
                
                Gdx.input.getTextInput(form, "Login", "", "Password");
            }
        }
        else{
            if(state==0){
                Gdx.input.getTextInput(form, "Sign Up", "", "Username");
            }
            else if(state==1){
                Gdx.input.getTextInput(form, "Sign Up", "", "Password");
            }
            else{
                Gdx.input.getTextInput(form, "Sign Up", "", "Confirm Password");
            }
        }
    }
    
    public void formCheck(){
        if(a==true){
            if(form.clicked==true && b==0){
                game.userLogin.username = form.text;
                game.setScreen(new LoginMenu(game, true, 1));
                dispose();
            }
            else if(form.clicked==true && b==1){
                game.userLogin.password = form.text;
                game.setScreen(new RequestMenu(game, game.userLogin, true));
                dispose();
            }
        }
        
        else{
            if(form.clicked==true && b==0){
                game.userLogin.username = form.text;
                game.setScreen(new LoginMenu(game, false, 1));
                dispose();
            }
            else if(form.clicked==true && b==1){
                game.userLogin.password = form.text;
                game.setScreen(new LoginMenu(game, false, 2));
                dispose();
            }
            else if(form.clicked==true && b==2){
                confirm = form.text;
                if(!confirm.equals(game.userLogin.password)){
                    wrong = true;
                    game.layout.setText(game.font, "Password and Confirm Password didn't match!");
                }
                else{
                    game.setScreen(new RequestMenu(game, game.userLogin, false));
                    dispose();
                }
            }
        }
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.bg, 0, 0);
      
        if(chooseLogin == false && wrong==false){
            game.batch.draw(game.layer, 0, 0);
            formCheck();
        } 
        
        else if(chooseLogin == false && wrong==true){
            game.font.draw(game.batch, game.layout, 1920/2 - game.layout.width/2, 1080/2 + game.layout.height/2);
            if(Gdx.input.isTouched()){
                game.setScreen(new LoginMenu(game, false, 0));
                dispose();
            } 
        }
        
        else{
           loginBtn.draw(game.batch);
           registerBtn.draw(game.batch); 
        }    
        
          
        
        game.batch.end();
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)){
            Gdx.app.exit();
        }
        
        if (Gdx.input.justTouched() && chooseLogin == true) {
            Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(tmp);
            Rectangle temp = new Rectangle(tmp.x-10, tmp.y-10, 20, 20);
            
            if(loginBtn.getBoundingRectangle().overlaps(temp)){
                game.setScreen(new LoginMenu(game, true, 0));
            }
            
            else if(registerBtn.getBoundingRectangle().overlaps(temp)){
                game.setScreen(new LoginMenu(game, false, 0));
            }
        }
        
        if(cancel == true){
            game.setScreen(new LoginMenu(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
    }
    
    @Override
    public void show() {
        
    }

    @Override
    public void hide() {
       
    }

    @Override
    public void dispose() {
        
    }
    
}