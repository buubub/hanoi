package hanoi.petra.ac.id;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import java.util.Random;

public class MainMenu implements Screen {
    final hanoi game;
    
    Texture logo, playBtnTexture, exitBtnTexture, hsBtnTexture, howtoBtnTexture,
            gamemode, classicTexture, randomTexture,
            stackCount, plusTexture, minusTexture, okTexture, stackNum, logoutTexture;
    
    Sprite playBtn, exitBtn, scoreBtn, howtoBtn, classic, random, plus, minus, ok, logout;
    
    boolean chooseMode = false;
    boolean chooseStack = false;
    boolean exit = false;
    
    public MainMenu(final hanoi game){
        this.game = game;

        playBtnTexture = new Texture("image/play.png");
        hsBtnTexture = new Texture("image/sb.png");
        exitBtnTexture = new Texture("image/exit.png");
        howtoBtnTexture = new Texture("image/howto.png");
        logo = new Texture("image/logo.png");
        gamemode = new Texture("image/gamemode.png");
        classicTexture = new Texture("image/classic.png");
        randomTexture = new Texture("image/random.png");
        stackCount = new Texture("image/stackcount.png");
        plusTexture = new Texture("image/plus.png");
        minusTexture = new Texture("image/minus.png");
        okTexture = new Texture("image/ok.png");
        stackNum = new Texture("image/"+game.stackCount+".png");
        
        playBtn = new Sprite(playBtnTexture);
        playBtn.setPosition(1920/2 - playBtn.getWidth()/2, 600);
        
        scoreBtn = new Sprite(hsBtnTexture);
        scoreBtn.setPosition(1920/2 - scoreBtn.getWidth()/2, 350);
        
        howtoBtn = new Sprite(howtoBtnTexture);
        howtoBtn.setPosition(howtoBtn.getWidth() - 100, 900);
        
        exitBtn = new Sprite(exitBtnTexture);
        exitBtn.setPosition(1920/2 - exitBtn.getWidth()/2, 100);
             
        classic = new Sprite(classicTexture);
        classic.setPosition(1920/2 - classic.getWidth()/2, 550);
                
        random = new Sprite(randomTexture);
        random.setPosition(1920/2 - random.getWidth()/2, 300);
        
        plus = new Sprite(plusTexture);
        plus.setPosition(1920/2 - plus.getWidth()/2 + 300, 450);
        
        minus = new Sprite(minusTexture);
        minus.setPosition(1920/2 - minus.getWidth()/2 - 300, 450);
        
        ok = new Sprite(okTexture);
        ok.setPosition(1920/2 - ok.getWidth()/2, 200);
        
        logoutTexture = new Texture("image/logout.png");
        logout = new Sprite(logoutTexture);
        
        logout.setPosition(game.volBtn.getX() - logout.getWidth() - 50, 900);
        
        game.cloudPos();
        
        game.bgm1.setVolume(game.vol);
        game.bgm2.setVolume(game.vol);
        game.bgm3.setVolume(game.vol);
        game.bgm4.setVolume(game.vol);
        game.bgm5.setVolume(game.vol);
        
        boolean play = false;
        if(game.bgm1.isPlaying()){
            play = true;
        }
        else if(game.bgm2.isPlaying()){
            play = true;
        }
        else if(game.bgm3.isPlaying()){
            play = true;
        }
        else if(game.bgm4.isPlaying()){
            play = true;
        }
         else if(game.bgm5.isPlaying()){
            play = true;
        }
        
        if(play==false){
            Random rand = new Random();
            int a = rand.nextInt(5);
            if(a==0){
                game.bgm1.play();
            }
            else if(a==1){
                game.bgm2.play();
            }
            else if(a==2){
                game.bgm3.play();
            }
            else if(a==3){
                game.bgm4.play();
            }
            else{
                game.bgm5.play();
            }
        }
    }
    
    void drawUserProfile(){
        game.font.draw(game.batch, "Welcome, "+game.user.getString("username"), 50, 200);
        game.font.draw(game.batch, "Your Classic Score: "+game.user.getInteger("classicScore"), 50, 150);
        game.font.draw(game.batch, "Your Random Score: "+game.user.getInteger("randomScore"), 50, 100);
    }
    
    void drawMainMenu(){
        game.batch.draw(logo, 1920/2 - logo.getWidth() / 2, 825);
        playBtn.draw(game.batch);
        scoreBtn.draw(game.batch);
        exitBtn.draw(game.batch);
        logout.draw(game.batch);
    }
    
    void MainMenuCheck(Rectangle rec){
        
        //play
        if(playBtn.getBoundingRectangle().overlaps(rec) && game.confirmation == false){   
            game.click.play(game.vol);
            chooseMode = true;
        }
        
        //tutorial
        else if(howtoBtn.getBoundingRectangle().overlaps(rec) && game.confirmation == false){
            game.click.play(game.vol);
            game.setScreen(new TutorialMenu(game));
            dispose();
        }
        
        //scoreboard
        else if(scoreBtn.getBoundingRectangle().overlaps(rec) && game.confirmation == false){
            game.click.play(game.vol);
            game.setScreen(new RequestMenu(game, false));
            dispose();
        }
        
        //exit
        else if(exitBtn.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            game.confirmation = true;
            exit = true;
        }    
        
        //exit yes
        else if(game.yes.getBoundingRectangle().overlaps(rec) && game.confirmation==true && exit == true){
            game.click.play(game.vol);
            Gdx.app.exit();
            dispose();
        }
        
        //exit no
        else if(game.no.getBoundingRectangle().overlaps(rec) && game.confirmation==true && exit == true){
            game.click.play(game.vol);
            game.confirmation = false;
            exit  = false;
        }
        
        //logout
        else if(logout.getBoundingRectangle().overlaps(rec) && game.confirmation == false){
            game.click.play(game.vol);
            game.confirmation = true;
        }
        
        //logout yes
        else if(game.yes.getBoundingRectangle().overlaps(rec) && game.confirmation == true && exit == false){
            game.click.play(game.vol);
            game.confirmation = false;
            game.user.putBoolean("loggedIn", false);
            game.user.remove("id");
            game.user.remove("username");
            game.user.remove("classicScore");
            game.user.remove("randomScore");
            game.user.flush();
            
            game.bgm1.stop();
            game.bgm2.stop();
            game.bgm3.stop();
            game.bgm4.stop();
            game.bgm5.stop();
            game.setScreen(new LoginMenu(game));
            dispose();
        }
        
        //logout no
        else if(game.no.getBoundingRectangle().overlaps(rec) && game.confirmation == true && exit == false){
            game.click.play(game.vol);
            game.confirmation = false;
        }
    }
    
    void drawChooseMode(){
        game.batch.draw(gamemode, 1920/2 - gamemode.getWidth() / 2, 825);
        classic.draw(game.batch);
        random.draw(game.batch);
    }
    
    void ChooseModeCheck(Rectangle rec){
        
        //choose mode classic
        if(classic.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            game.gameMode = 0;
            chooseMode = false;
            chooseStack = true;
        }
        
        //choose mode random
        else if(random.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            game.gameMode = 1;
            chooseMode = false;
            chooseStack = true;
        }    
        
        //back
        else if(game.exit.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            chooseMode = false;
        }
    }
    
    void drawChooseStack(){
        game.batch.draw(stackCount, 1920/2 - stackCount.getWidth() / 2, 825);
        
        game.batch.draw(stackNum, 1920/2 - stackNum.getWidth()/2, 435);
        
        if(game.stackCount<8){
            plus.draw(game.batch);
        }
        if(game.stackCount>3){
            minus.draw(game.batch);
        }
        ok.draw(game.batch);
    }
    
    void ChooseStackCheck(Rectangle rec){
        //choose stack +
        if(plus.getBoundingRectangle().overlaps(rec) && game.stackCount<8){
            game.click.play(game.vol);
            game.stackCount++;
            stackNum = new Texture("image/"+game.stackCount+".png");
        }
        
        //choose stack -
        else if(minus.getBoundingRectangle().overlaps(rec) && game.stackCount>3){
            game.click.play(game.vol);
            game.stackCount--;
            stackNum = new Texture("image/"+game.stackCount+".png");
        }
        
        
        //play
        else if(ok.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            if(game.gameMode == 1){
                game.setScreen(new RequestMenu(game));
            }
            else{
                game.bgm1.stop();
                game.bgm2.stop();
                game.bgm3.stop();
                game.bgm4.stop();
                game.bgm5.stop();
                
                game.setScreen(new GameMenu(game));
                dispose();
            }
        }
        
        
        //back
        else if(game.exit.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            chooseStack = false;
            chooseMode = true;
        }
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.rePoscloud();
        game.moveCloud();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.bg, 0, 0);
        game.drawCloud();
        
        if(chooseMode == false && chooseStack==false){
            drawMainMenu();
            drawUserProfile();
            howtoBtn.draw(game.batch);
        }
        
        else if(chooseMode == true){
            drawChooseMode();
        }
        
        else if(chooseStack == true){
            drawChooseStack();
        }        
        
        if(game.confirmation == true){
            game.drawConfirm();
        }
        
        if(chooseMode == true || chooseStack == true){
            game.exit.draw(game.batch);
        }
        game.drawVolBtn();
        game.batch.end();
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK) ){
            
            if(chooseMode == true){
                chooseMode = false;
            }
            
            else if(chooseStack == true){
                chooseStack = false;
                chooseMode = true;
            }
            
            else if(chooseMode == false && chooseStack == false){
                exit = true;
                game.confirmation = true;
            }
        }
        
        if (Gdx.input.justTouched()) {
             Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
             game.camera.unproject(tmp);
             Rectangle rec = new Rectangle(tmp.x-10, tmp.y-10, 20, 20);
             if(chooseMode == false && chooseStack == false){
                MainMenuCheck(rec);        
             }
             else if(chooseMode == true){
                ChooseModeCheck(rec);
             }
             
             else if(chooseStack == true){
                 ChooseStackCheck(rec);
             }
                          
            if(game.volBtn.getBoundingRectangle().overlaps(rec)){
                game.click.play(game.vol);
                game.setVol();
            }  
            
            
        }


    }

    @Override
    public void dispose(){
        
    }

    @Override
    public void show(){
    }

    @Override
    public void hide(){

    }

    @Override
    public void pause(){
        
    }

    @Override
    public void resume(){

    }
    
    @Override
    public void resize(int x, int y){
        game.viewport.update(x, y);
    }

}

