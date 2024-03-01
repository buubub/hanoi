package hanoi.petra.ac.id;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class TutorialMenu implements Screen {
    final hanoi game;    
    
    int page = 0;
    float pos;
    boolean moveNext = false;
    boolean movePrev = false;
    
    public TutorialMenu(final hanoi game){
        this.game = game;        
        
        game.next.setPosition((1920/2 - game.next.getWidth()/2) + 800 , 1080/2 - game.next.getHeight()/2);
        game.prev.setPosition((1920/2 - game.prev.getWidth()/2) - 800, 1080/2 - game.prev.getHeight()/2);
        
        for(int i=0; i<7; i++){
            game.howto.get(i).setPosition(1920/2 - game.howto.get(i).getWidth()/2 + 1600*i, (1080/2 - game.howto.get(i).getHeight()/2) + 125);
            game.howtotext.get(i).setPosition(1920/2 - game.howtotext.get(i).getWidth()/2 + 1600*i, (game.howto.get(i).getY() - game.howtotext.get(i).getHeight()) - 25);
        }
    }
    
    public void drawPage(){
        for(int i=0; i<7; i++){
            game.howto.get(i).draw(game.batch);
            game.howtotext.get(i).draw(game.batch);
        }
    }
    
    public void drawButton(){
        game.exit.draw(game.batch);
        game.drawVolBtn();
        if(page<6){
            game.next.draw(game.batch);
        }
        
        if(page>0){
            game.prev.draw(game.batch);
        }
    }
    
    public void PageCheck(Rectangle rec){
        if(game.next.getBoundingRectangle().overlaps(rec) && page<6){
            game.slide.play(game.vol);
            moveNext = true;
            pos = game.howto.get(page).getX() - 1600;
        }
        
        if(game.prev.getBoundingRectangle().overlaps(rec) && page>0){
            game.slide.play(game.vol);
            movePrev = true;
            pos = game.howto.get(page).getX() + 1600;
        }
    }
    
    public void next(){
        
        if(game.howto.get(page).getX() != pos){
            for(int i=0; i<7; i++){
                game.howto.get(i).translateX(-80);
                game.howtotext.get(i).translateX(-80);
            }
        }
        else{
            moveNext = false;
            page++;
        }
    }
    
    public void prev(){
        if(game.howto.get(page).getX() != pos){
            for(int i=0; i<7; i++){
                game.howto.get(i).translateX(80);
                game.howtotext.get(i).translateX(80);
            }
        }
        else{
            movePrev = false;
            page--;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.bg, 0, 0);
        game.batch.draw(game.layer, 0, 0);
        drawPage();
        drawButton();
        game.batch.end();
        if(moveNext==true){
            next();
        }
        if(movePrev==true){
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
             
             else if(moveNext==false && movePrev==false){
                PageCheck(rec);
             }
             
             if(game.volBtn.getBoundingRectangle().overlaps(rec)){
                game.click.play(game.vol);
                game.setVol();
            }  
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK) ){
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