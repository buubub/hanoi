package hanoi.petra.ac.id;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameMenu implements Screen {
    final hanoi game;
    
    Array<Pole> pl;
    Array<Integer> from, to;
    public boolean pick = false, move = false, pause = false, win = false, winsound = false;
    public boolean animate = false, skipped = false;
    long startTime = TimeUtils.millis();
    long elapsedTime, pauseTime, lastPause = 0, pausedTime = 0;
    int sec=0, min=0, hr=0, step=0, target;
    String time;
    
    public boolean moveTop = false, moveSide = false, moveBot = false, moveLeft = false, moveRight = false;
    public GameMenu(final hanoi game) {
        this.game = game;
        
        pl = new Array<Pole>();
        
        from = new Array<Integer>();
        to = new Array<Integer>();
        
        for(int i=0; i<3; i++){
            Pole pole = new Pole();
            pole.setNum(i);
            pole.setTexture("image/pl.png");
            pl.add(pole);
        }
        
        if(game.gameMode==0){
            for(int i=game.stackCount; i>0; i--){
                Stack stack = new Stack();
                stack.setTexture("image/stk"+i+".png");
                stack.setNum(i);
                pl.get(0).push(stack);
            }
        }
        
        else if(game.gameMode==1){
            
            for(int i=0; i<3; i++){
                while(game.question.get(i).size > 0){
                    Stack stack = new Stack();
                    stack.setTexture("image/stk"+game.question.get(i).peek()+".png");
                    stack.setNum(game.question.get(i).pop());
                    pl.get(i).push(stack);  
                }
            }
        }
        
        for(int i=0; i<3; i++){
            pl.get(i).setPosition(640 * i + 320 - pl.get(i).getWidth()/2, 0);
            pl.get(i).setStackPosition();
        }
        
        game.cloudPos();
        game.bgmGame.setVolume(game.vol);
        game.bgmGame.play();
        
    }
    
    public void drawPole(){
        for(int i=0; i<3; i++){
            pl.get(i).getSprite().draw(game.batch);
        }
    }
    
    public void drawStack(){
        for(int i=0; i<3; i++){
            for(int j=0; j<pl.get(i).getSize(); j++){
                pl.get(i).stacks.get(j).getSprite().draw(game.batch);
            }
        }
    }
    
    public void lastPicked(){
        for(int i=0; i<3; i++){
            if(pl.get(i).getSize()>0){
                if(pl.get(i).getLastPick()==true && pause==false){
                    if(pl.get(i).getAlpha() > 1){
                        pl.get(i).setAlpha(pl.get(i).getAlpha() + 8);
                    }
                    else if(pl.get(i).getAlpha() <= 1){
                        pl.get(i).setAlpha(pl.get(i).getAlpha() - 8);
                    }
                }
            }
        }
    }
    
    public void GameCheck(Rectangle rec){
        for(int i=0; i<3; i++){
            if(pl.get(i).getBounds().overlaps(rec) && pause == false && animate == false){
                if(pick == false){ //klo blm nge pick
                    game.pick.play(game.vol);
                    if(pl.get(i).getSize()>0){
                        pl.get(i).setLastPick(true);
                        pick = true;
                        from.add(pl.get(i).getNum());
                    }
                }
                
                else{ //klo udh nge pick
                    if(pl.get(i).getLastPick()==true){ //nge pick ke last pick
                        game.failed.play(game.vol);
                        pl.get(i).moveCancel();
                        from.pop();
                    }
                    else{// nge pick ke yg ngga last pick
                        if(pl.get(i).getSize()>0){ //klo pole ga kosongan
                            if(pl.get(from.peek()).getTop() > pl.get(i).getTop()){ //klo stack lebih gede
                                game.failed.play(game.vol);
                                for(int j=0; j<3; j++){
                                    pl.get(j).moveCancel();
                                }
                                from.pop();
                            }
                            else{ //klo stack ga lebih gede
                                to.add(pl.get(i).getNum());
                                target = i;
                                for(int j=0; j<3; j++){
                                    if(pl.get(j).getLastPick()==true){
                                        if(target < j){
                                            moveLeft = true;
                                        }
                                        else{
                                            moveRight = true;
                                        }
                                    }
                                }
                                game.move.play(game.vol);
                                animate = true;
                                moveTop = true;
                                pauseTime = TimeUtils.millis();
                            }
                        }
                        else{ //klo pole kosongan
                            to.add(pl.get(i).getNum());
                            target = i;
                            for(int j=0; j<3; j++){
                                if(pl.get(j).getLastPick()==true){
                                    if(target < j){
                                        moveLeft = true;
                                    }
                                    else{
                                        moveRight = true;
                                    }
                                }
                            }
                            game.move.play(game.vol);
                            animate = true;
                            moveTop = true;
                            pauseTime = TimeUtils.millis();
                        }
                    }
                    pick = false;
                }
            }
        }
    }  
    
    public void anim(){
        pausedTime = TimeUtils.timeSinceMillis(pauseTime) + lastPause;
        for(int i = 0; i < 3; i++){
            if(pl.get(i).getLastPick() == true){
                if(moveTop==true){
                    if(pl.get(i).stacks.peek().getSprite().getY() <= 810){
                        pl.get(i).stacks.peek().getSprite().translateY(28);
                    }
                    else{
                        game.move.play(game.vol);
                        moveTop=false;
                        moveSide=true;
                    }
                }
                else if(moveSide==true){
                    if(moveRight == true){
                        if(pl.get(i).stacks.peek().getSprite().getX() != (pl.get(target).getSprite().getX()+pl.get(target).getWidth()/2) - pl.get(i).stacks.peek().getWidth()/2){
                            pl.get(i).stacks.peek().getSprite().translateX(20);
                        }
                        else{
                            game.move.play(game.vol);
                            moveRight=false;
                            moveBot=true;
                        }
                    }
                    else if(moveLeft == true){
                        if(pl.get(i).stacks.peek().getSprite().getX() != (pl.get(target).getSprite().getX()+pl.get(target).getWidth()/2) - pl.get(i).stacks.peek().getWidth()/2){
                            pl.get(i).stacks.peek().getSprite().translateX(-20);
                        }
                        else{
                            game.move.play(game.vol);
                            moveLeft=false;
                            moveBot=true;
                        }
                    }
                                       
                    else if(moveBot==true){
                       if(pl.get(i).stacks.peek().getSprite().getY() != 56*pl.get(target).getSize()+250){
                            pl.get(i).stacks.peek().getSprite().translateY(-28);
                        }
                        else{
                            lastPause = pausedTime;
                            moveBot=false;
                            animate = false;
                            game.success.play(game.vol);
                            pl.get(i).moveStack(pl.get(target));
                            step++;
                        } 
                    }
                }
            }
        }
    }
    
    public void skip(){
        skipped = true;
        moveLeft = false;
        moveRight = false;
        moveSide = false;
        moveBot = false;
        moveTop = false;
        animate = false;
        lastPause = pausedTime;
        game.success.play(game.vol);
        for(int i = 0; i < 3; i++){
            if(pl.get(i).getLastPick() == true){
                pl.get(i).moveStack(pl.get(target));                
            }
        }
        step++;
    }
    
    public void undo(){
        if(to.size>0){
            if(pick==true){
                for(int i=0; i<3; i++){
                    pl.get(i).moveCancel();
                }
                from.pop();
                pick = false;
            }
            pl.get(to.pop()).moveStack(pl.get(from.pop()));
            step--;
        }
    }
    
    public void UndoCheck(Rectangle rec){
        
        if(game.undoBtn.getBoundingRectangle().overlaps(rec) && pause==false){
            game.click.play(game.vol);
            undo();
        }
    }
    
    public void WinCheck(){
        
        if(pl.get(2).getSize() == game.stackCount && win == false){
            winsound = true;
            win = true;
        }
        
        else if(win == true){
            game.drawResult();
            game.font.draw(game.batch, "Time: "+time, 1920/2 - 150, 1080/2 + 75);
            game.font.draw(game.batch, "Step: "+step, 1920/2 - 150, 1080/2);
        }
        
        else{
            game.undoBtn.draw(game.batch);
            game.font.draw(game.batch, "Time: "+time, 250, 1080 - 50);
            game.font.draw(game.batch, "Step: "+step, 250, 1080 - 100);
            
            if(pause==false){
                game.pauseBtn.setPosition(1920 - game.pauseBtn.getWidth() - 50, 900);
                game.pauseBtn.draw(game.batch);
            }
        
            else{
                game.pauseBtn.setPosition(0, 0);
                game.drawPaused();
                game.drawVolBtn(); 
                pausedTime = TimeUtils.timeSinceMillis(pauseTime) + lastPause; 
            }
        }
    }
    
    public void PauseCheck(Rectangle rec){
        if(game.volBtn.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            game.setVol();
        }
                
        else if(game.playicon.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            pause = false;
            if(animate==false){
                lastPause = pausedTime;
            }
        }

        else if(game.retryicon.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            game.bgmGame.stop();
            if(game.gameMode == 1){
                game.setScreen(new RequestMenu(game)); 
                dispose();
            }
            else{
                game.setScreen(new GameMenu(game)); 
                dispose();  
            }
        }

        else if(game.exiticon.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            game.bgmGame.stop();
            game.setScreen(new MainMenu(game));
            dispose();
        }
    }
    
    public void ResultCheck(Rectangle rec){
        
        if(game.retryicon.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            Score score = new Score();
            score.id = game.user.getInteger("id");
            score.stack = game.stackCount;
            score.time = (int)elapsedTime/1000;
            score.step = step;
            game.bgmGame.stop();
            
            if(game.gameMode == 1){
                game.setScreen(new RequestMenu(game, score, game.gameMode, true));
                dispose();
            }
            else{
                game.setScreen(new RequestMenu(game, score, game.gameMode, true));
                dispose();
            }
        }
        
        else if(game.exiticon.getBoundingRectangle().overlaps(rec)){
            game.click.play(game.vol);
            Score score = new Score();
            score.id = game.user.getInteger("id");
            score.stack = game.stackCount;
            score.time = (int)elapsedTime/1000;
            score.step = step;
            game.bgmGame.stop();
            game.setScreen(new RequestMenu(game, score, game.gameMode, false));
            dispose();
        }
    }
    
    public void timeFormat(){
        if(win == false){
            elapsedTime = TimeUtils.timeSinceMillis(startTime) - pausedTime; 
        }
        
        sec = (int)elapsedTime/1000 % 60;

        min = (int)elapsedTime/1000 / 60 % 60;

        hr =  (int)elapsedTime/1000 / 3600;
        
        time = Integer.toString(hr)+":"+Integer.toString(min)+":"+Integer.toString(sec);
    }
  
    
    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lastPicked();
        timeFormat();
        game.rePoscloud();
       
        if(pause==false){
            game.moveCloud();
        }
        if(animate == true && pause == false){
            anim();
            if(Gdx.input.justTouched()){
                Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                game.camera.unproject(tmp);
                Rectangle temp = new Rectangle(tmp.x-10, tmp.y-10, 20, 20);
                if(!game.pauseBtn.getBoundingRectangle().overlaps(temp)){
                    skip();
                }
            }
        }
        
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.bg, 0, 0);
        game.drawCloud();
        drawPole();
        drawStack(); 
        
        WinCheck();      
        if(winsound==true){
            game.bgmGame.stop();
            game.win.play(game.vol);
            winsound = false;
        }
        game.batch.end();
        if (Gdx.input.justTouched()) {
            Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(tmp);
            Rectangle rec = new Rectangle(tmp.x-100, tmp.y-100, 200, 200);
            Rectangle temp = new Rectangle(tmp.x-10, tmp.y-10, 20, 20);
           
            if(win==false){
                if(skipped == false){
                    GameCheck(rec); 
                }
                UndoCheck(temp);
                    
                if(game.pauseBtn.getBoundingRectangle().overlaps(temp)){
                    game.click.play(game.vol);
                    pause=true;
                    if(animate==false){
                        pauseTime = TimeUtils.millis();
                    }
                    
                }
                else if(pause==true){
                    PauseCheck(temp);
                }
            }
            
            else{
                ResultCheck(temp);
            }
            
        }

        if((Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)) && win==false){
            pause=true;
            if(animate==false){
                pauseTime = TimeUtils.millis();
            }
        }
        skipped = false;
    }
    
    @Override
    public void dispose(){
        for(int i=0; i<3; i++){
            pl.get(i).getTexture().dispose();
        }
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

