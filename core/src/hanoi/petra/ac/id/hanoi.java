package hanoi.petra.ac.id;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Random;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class hanoi extends Game {
    
    SpriteBatch batch;
    BitmapFont font, scoreheader;
    Texture bg, layer, cloudTexture, volMuteBtnTexture, volPlayBtnTexture, pauseBtnTexture, confirmTexture, 
            yesTexture, noTexture, pausedTexture, retryiconTexture, playiconTexture, exiticonTexture, resultTexture,
            undoBtnTexture, nextTexture, prevTexture, exitTexture, scoreboardTexture;
    Sprite cloud, volBtn, pauseBtn, confirm, yes, no, exiticon, playicon, retryicon, paused, result, undoBtn, exit, next, prev,
            scoreboard;
    Array<Sprite> clouds, howto, howtotext;
    Viewport viewport;
    Camera camera;
    Array<Float> t1, t2;
    Array<Array<Integer>> question;
    Array<HighScore> classic, random;
    Music bgmGame, bgm1, bgm2, bgm3, bgm4, bgm5;
    Sound click, failed, pick, success, slide, win, move;
    User userLogin;
    
    int stackCount = 3;
    int gameMode = 0;
    float vol;
    boolean confirmation = false, loggedIn;
    long sounds;
    Preferences user;
    
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    GlyphLayout layout = new GlyphLayout(); 
    
    @Override
    public void create() {

        Gdx.input.setCatchBackKey(true);

        bg = new Texture("image/bg.png");
        layer = new Texture("image/layer.png");
        
        bgmGame = Gdx.audio.newMusic(Gdx.files.internal("sound/bgmGameMenu.mp3"));
        click = Gdx.audio.newSound(Gdx.files.internal("sound/click.mp3"));
        pick = Gdx.audio.newSound(Gdx.files.internal("sound/pick.mp3"));
        failed = Gdx.audio.newSound(Gdx.files.internal("sound/fail.mp3"));
        success = Gdx.audio.newSound(Gdx.files.internal("sound/success.mp3"));
        slide = Gdx.audio.newSound(Gdx.files.internal("sound/slide.mp3"));
        win =  Gdx.audio.newSound(Gdx.files.internal("sound/win.mp3"));
        move =  Gdx.audio.newSound(Gdx.files.internal("sound/move.mp3"));
        
        bgm1 = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm1.mp3"));
        bgm2 = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm2.mp3"));
        bgm3 = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm3.mp3"));
        bgm4 = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm4.mp3"));
        bgm5 = Gdx.audio.newMusic(Gdx.files.internal("sound/bgm5.mp3"));

        bgmGame.setLooping(true);
        
        bgm1.setLooping(true);
        bgm2.setLooping(true);
        bgm3.setLooping(true);
        bgm4.setLooping(true);
        bgm5.setLooping(true);
        
        clouds = new Array<Sprite>();
        
        for(int i=0; i<3; i++){
            cloudTexture = new Texture("image/cloud"+(i+1)+".png");
            cloud = new Sprite(cloudTexture);
            clouds.add(cloud);
        }
        
        for(int i=3; i<6; i++){
            cloudTexture = new Texture("image/cloud"+(i-2)+".png");
            cloud = new Sprite(cloudTexture);
            clouds.add(cloud);
        }
        
        volMuteBtnTexture = new Texture("image/volmute.png");
        volPlayBtnTexture = new Texture("image/volplay.png");
        
        volBtn = new Sprite(volMuteBtnTexture);
        volBtn.setPosition(1920 - volBtn.getWidth() - 50, 900);
        
        pauseBtnTexture = new Texture("image/pause.png");
        pauseBtn = new Sprite(pauseBtnTexture);
        pauseBtn.setPosition(1920 - pauseBtn.getWidth() - 50, 900);
        
        confirmTexture = new Texture("image/confirm.png");
        yesTexture = new Texture("image/yes.png");
        noTexture = new Texture("image/no.png");
        
        confirm = new Sprite(confirmTexture);
        yes = new Sprite(yesTexture);
        no = new Sprite(noTexture);       
        
        confirm.setPosition(1920/2 - confirm.getWidth()/2, 1080/2 - confirm.getHeight()/2);
        yes.setPosition(1920/2 - yes.getWidth() - 50, 1080/2 - confirm.getHeight()/3);
        no.setPosition(1920/2 + 50, 1080/2 - confirm.getHeight()/3);
        
        pausedTexture = new Texture("image/paused.png");
        playiconTexture = new Texture("image/playicon.png");
        retryiconTexture = new Texture("image/retryicon.png");
        exiticonTexture = new Texture("image/exiticon.png");
        resultTexture = new Texture("image/result.png");
        
        paused = new Sprite(pausedTexture);
        playicon = new Sprite(playiconTexture);
        retryicon = new Sprite(retryiconTexture);
        exiticon = new Sprite(exiticonTexture);
        result = new Sprite(resultTexture);
        
        paused.setPosition(1920/2 - paused.getWidth()/2, 1080/2 - paused.getHeight()/2);
        playicon.setPosition(1920/2 - playicon.getWidth()/2 - 200, 1080/2 - paused.getHeight()/4);
        
        result.setPosition(1920/2 - result.getWidth()/2, 1080/2 - result.getHeight()/2);
        
        undoBtnTexture = new Texture("image/undo.png");
        undoBtn = new Sprite(undoBtnTexture);
        undoBtn.setPosition(undoBtn.getWidth() - 100, 900);
        
        batch = new SpriteBatch();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("font/OpenSans.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.borderColor = Color.WHITE;
        parameter.size = 50;
        parameter.borderWidth = 3;
        font = new BitmapFont();
        font = generator.generateFont(parameter);
        scoreheader = new BitmapFont();
        scoreheader = generator.generateFont(parameter);
        generator.dispose();
        
        camera = new OrthographicCamera();
        viewport = new StretchViewport(bg.getWidth(), bg.getHeight(), camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
        
        nextTexture = new Texture("image/howto/next.png");
        prevTexture = new Texture("image/howto/prev.png");
        exitTexture = new Texture("image/howto/exit.png");
        next = new Sprite(nextTexture);
        prev = new Sprite(prevTexture);
        exit = new Sprite(exitTexture);
        
        howto = new Array<Sprite>();
        howtotext = new Array<Sprite>();
        
        exit.setPosition(exit.getWidth() - 100, 900);
        
        for(int i=0; i<7; i++){
            Texture howtoTexture = new Texture("image/howto/howto"+(i+1)+".png");
            Texture howtotextTexture = new Texture("image/howto/howtotext"+(i+1)+".png");
            Sprite howtos = new Sprite(howtoTexture);
            Sprite howtotexts = new Sprite(howtotextTexture);
            
            howto.add(howtos);
            howtotext.add(howtotexts);
        }
        
        scoreboardTexture = new Texture("image/scoreboard.png");
        scoreboard = new Sprite(scoreboardTexture);
        scoreboard.setPosition(1920/2 - scoreboard.getWidth()/2, 1080/2 - scoreboard.getHeight()/2);
        
        user = Gdx.app.getPreferences("User");
        
        loggedIn = user.getBoolean("loggedIn", false);
        
        vol = user.getBoolean("soundOn", true) ? 1 : 0;
        
        if(loggedIn==true){
            this.setScreen(new MainMenu(this));
        }
        else{          
            this.setScreen(new LoginMenu(this));
        }
        
    }
    
    public void drawPaused(){
        retryicon.setPosition(1920/2 - retryicon.getWidth()/2, 1080/2 - paused.getHeight()/4);
        exiticon.setPosition(1920/2 - exiticon.getWidth()/2 + 200, 1080/2 - paused.getHeight()/4);
        batch.draw(layer,0,0);
        paused.draw(batch);
        playicon.draw(batch);
        retryicon.draw(batch);
        exiticon.draw(batch);
    }
    
    public void drawConfirm(){
        batch.draw(layer,0,0);
        confirm.draw(batch);
        yes.draw(batch);
        no.draw(batch);
    }
    
    public void setVol(){
        if(vol == 1){
            vol = 0;
            user.putBoolean("soundOn", false);
            user.flush();
            
            bgmGame.setVolume(vol);
            click.setVolume(sounds, vol);
            pick.setVolume(sounds, vol);
            failed.setVolume(sounds, vol);
            success.setVolume(sounds, vol);
            slide.setVolume(sounds, vol);    
            win.setVolume(sounds, vol);
            move.setVolume(sounds, vol);
            
            bgm1.setVolume(vol);
            bgm2.setVolume(vol);
            bgm3.setVolume(vol);
            bgm4.setVolume(vol);
            bgm5.setVolume(vol);
        }
        else{
            vol = 1;
            user.putBoolean("soundOn", true);
            user.flush();
            
            bgmGame.setVolume(vol);
            click.setVolume(sounds, vol);
            pick.setVolume(sounds, vol);
            failed.setVolume(sounds, vol);
            success.setVolume(sounds, vol);
            slide.setVolume(sounds, vol);
            win.setVolume(sounds, vol);
            move.setVolume(sounds, vol);
            
            bgm1.setVolume(vol);
            bgm2.setVolume(vol);
            bgm3.setVolume(vol);
            bgm4.setVolume(vol);
            bgm5.setVolume(vol);
        }
    }
       
    public void drawVolBtn(){
        volBtn.draw(batch);
        
        if(vol==0){
            volBtn.setTexture(volMuteBtnTexture);
        }
        else{
            volBtn.setTexture(volPlayBtnTexture);
        }
    }
    
    public void drawResult(){
        retryicon.setPosition(1920/2 - retryicon.getWidth()/2 - 125, 1080/2 - paused.getHeight()/4 - 75);
        exiticon.setPosition(1920/2 - exiticon.getWidth()/2 + 125, 1080/2 - paused.getHeight()/4 - 75);
        batch.draw(layer,0,0);
        result.draw(batch);
        retryicon.draw(batch);
        exiticon.draw(batch);
    }
    
    public void cloudPos(){
        
        Random rand = new Random();
        t1 = new Array<Float>();
        t2 = new Array<Float>();
        
        t1.add((float)(rand.nextInt(50)+1)/100);
        t1.add((float)(rand.nextInt(50)+1)/100);
        t1.add((float)(rand.nextInt(50)+1)/100);
        
        t2.add((float)(rand.nextInt(50)+1)/-100);
        t2.add((float)(rand.nextInt(50)+1)/-100);
        t2.add((float)(rand.nextInt(50)+1)/-100);
        
        for(int i=0; i<3; i++){
            clouds.get(i).setScale((float)0.5);
            clouds.get(i).setPosition((rand.nextInt(2500)*-1)-clouds.get(i).getWidth(), rand.nextInt(400)+600);
        }
        
        for(int i=3; i<6; i++){
            clouds.get(i).setScale((float)0.5);
            clouds.get(i).setPosition((rand.nextInt(2500))+1920, rand.nextInt(400)+600);
        }
    }
    
    public void drawCloud(){
        for(int i=0; i<6; i++){
            clouds.get(i).draw(batch);
        }
    }   
    
    public void rePoscloud(){
        
        Random rand = new Random();
        
        for(int i=0; i<3; i++){
            if(clouds.get(i).getX() > 1920){
                clouds.get(i).setPosition((rand.nextInt(2500)*-1)-clouds.get(i).getWidth(), rand.nextInt(400)+600);
                t1.set(i, (float)(rand.nextInt(50)+1)/100);
            }
        }   
        
        for(int i=3; i<6; i++){
            if(clouds.get(i).getX()+clouds.get(i).getWidth() < 0){
                clouds.get(i).setPosition((rand.nextInt(2500))+1920, rand.nextInt(400)+600);
                t2.set(i-3, (float)(rand.nextInt(50)+1)/-100);
            }
        } 
    }
    
    public void moveCloud(){
        for(int i=0; i<3; i++){
            clouds.get(i).translateX(t1.get(i));            
        }
        for(int i=3; i<6; i++){
            clouds.get(i).translateX(t2.get(i-3));            
        }
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {
        batch.dispose();
        font.dispose();
    }
    
    @Override
    public void resize(int x, int y){
        viewport.update(x, y);
    }
}
