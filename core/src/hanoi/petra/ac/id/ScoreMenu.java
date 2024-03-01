
package hanoi.petra.ac.id;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class ScoreMenu implements Screen {
    
    final hanoi game;
    String username = "";
    String score = "";
    boolean classic = true;
    boolean move = false;
    float pos = 1920/2;
    float poss;
    GlyphLayout classicUsername = new GlyphLayout();
    GlyphLayout classicScore = new GlyphLayout();
    GlyphLayout randomUsername = new GlyphLayout();
    GlyphLayout randomScore = new GlyphLayout();
    GlyphLayout classicHeader = new GlyphLayout();
    GlyphLayout randomHeader = new GlyphLayout();
    
    public ScoreMenu(final hanoi game) {
        this.game = game;     
        game.scoreheader.getData().setScale(3);
        game.next.setPosition((1920/2 - game.next.getWidth()/2) + 800 , 1080/2 - game.next.getHeight()/2);
        game.prev.setPosition((1920/2 - game.prev.getWidth()/2) - 800, 1080/2 - game.prev.getHeight()/2);
        
        classicHeader.setText(game.scoreheader, "Classic Score");
        randomHeader.setText(game.scoreheader, "Random Score");
        
        for(int i=0; i<game.classic.size; i++){
            username += (i+1)+". "+game.classic.get(i).username+"\n";
            score += " - "+game.classic.get(i).score+"\n";
        }
        
        classicUsername.setText(game.font, username);
        classicScore.setText(game.font, score);
        
        username = "";
        score = "";
        
        for(int i=0; i<game.random.size; i++){
            username += (i+1)+". "+game.random.get(i).username+"\n";
            score += " - "+game.random.get(i).score+"\n";
        }
        randomUsername.setText(game.font, username);
        randomScore.setText(game.font, score);
    }
    
    public void drawButton(){
        game.exit.draw(game.batch);
        game.drawVolBtn();
        if(classic==true){
            game.next.draw(game.batch);
        }
        
        else{
            game.prev.draw(game.batch);
        }
    }
    
    public void buttonCheck(Rectangle rec){
        if(game.next.getBoundingRectangle().overlaps(rec) && classic==true){
            game.slide.play(game.vol);
            move = true;
            poss = pos - 1600;
        }
        
        else if(game.prev.getBoundingRectangle().overlaps(rec) && classic==false){
            game.slide.play(game.vol);
            move = true;
            poss = pos + 1600;
        }
    }
    
    public void next(){
        
        if(pos != poss){
            pos -= 80;
        }
        else{
            move = false;
            classic = false;
        }
    }
    
    public void prev(){
        
        if(pos != poss){
            pos += 80;
        }
        else{
            move = false;
            classic = true;
        }
    }
    
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.scoreboard, 0, 0);
        game.font.draw(game.batch, classicHeader, pos - classicHeader.width/2, 950);
        game.font.draw(game.batch, classicUsername, pos - classicUsername.width, 1080/2 + classicUsername.height/2 - 100);
        game.font.draw(game.batch, classicScore, pos, 1080/2 + classicUsername.height/2 - 100);
        
        game.font.draw(game.batch, randomHeader, pos - randomHeader.width/2 + 1600, 950);
        game.font.draw(game.batch, randomUsername, pos - randomUsername.width + 1600, 1080/2 + randomUsername.height/2 - 100);
        game.font.draw(game.batch, randomScore, pos + 1600, 1080/2 + randomUsername.height/2 - 100);
        drawButton();
        game.batch.end();
        if(move==true && classic==true){
            next();
        }
        if(move==true && classic==false){
            prev();
        }
        
        if (Gdx.input.justTouched()) {
            Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(tmp);
            Rectangle rec = new Rectangle(tmp.x-10, tmp.y-10, 20, 20);
            if(game.exit.getBoundingRectangle().overlaps(rec)){
                game.click.play(game.vol);
                game.setScreen(new MainMenu(game));
                dispose();
            }
            
            else if(game.volBtn.getBoundingRectangle().overlaps(rec)){
                game.click.play(game.vol);
                game.setVol();
            }  
             
            else if(move==false){
                buttonCheck(rec);
            }
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)){
            game.setScreen(new MainMenu(game));
            dispose();
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
