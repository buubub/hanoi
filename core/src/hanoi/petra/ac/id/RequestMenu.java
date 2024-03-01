
package hanoi.petra.ac.id;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;


public final class RequestMenu implements Screen {
    
    final hanoi game;  
    int state = 0;
    Json json;
    String jsonString, message;
    HttpRequest request;
    HttpResponseListener response;
            
    public RequestMenu(final hanoi game, User user, boolean login) {
        this.game = game;    
        
        if(login==true){
            login();
        }
        
        else{
            register();
        }
    }
    
    public RequestMenu(final hanoi game, Score score, int mode, boolean retry) {
        this.game = game;    
        post_score(score, mode, retry);
    }
    
    public RequestMenu(final hanoi game){
        this.game = game;
        game.question = new Array<Array<Integer>>();
        get_question();
               
    }
    
    public RequestMenu(final hanoi game, boolean a){
        this.game = game;
        
        if(a==false){
            get_classic_score();
        }
        else{
            get_random_score();
        }
    }
    
    public void login(){
        
        json = new Json();
        
        state = 3;
        message = "Please wait...";
        game.layout.setText(game.font, message);
        
        request = new HttpRequest(HttpMethods.POST);
        
        response = new HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                
                String responseJson = httpResponse.getResultAsString();
                final int statusCode = httpResponse.getStatus().getStatusCode();
                
                if(statusCode == 200){
                    JsonValue temp = new JsonReader().parse(responseJson);
                    game.user.putBoolean("loggedIn", true);
                    game.user.putInteger("id", temp.getInt("id"));
                    game.user.putString("username", temp.getString("username"));
                    game.user.putInteger("classicScore", temp.getInt("classic_score"));
                    game.user.putInteger("randomScore", temp.getInt("random_score"));
                    game.user.flush();
                    message = "Login Success!";
                    game.layout.setText(game.font, message);
                    state = 1;  
                }
                else if(statusCode == 422){   
                    JsonValue temp = new JsonReader().parse(responseJson);
                    
                    message = "";
                    
                    for(int i=0; i<temp.size; i++){
                        message += temp.get(i).child().toString()+"\n";
                    }
                    game.layout.setText(game.font, message);
                                     
                    state = 2;
                }
                
                else if(statusCode == 404){
                    message = "Login Error\nPlease try again!";
                    game.layout.setText(game.font, message);
                    state = 2;
                }
            }

            @Override
            public void failed(Throwable t) {
                message = "Connection Error\nPlease check connection!";
                game.layout.setText(game.font, message);
                state = 2;
            }
            @Override
            public void cancelled() {
                game.setScreen(new LoginMenu(game));
            }
        };
        
        
        json.setOutputType(OutputType.json);
        jsonString = json.toJson(game.userLogin);
        
        request.setUrl("http://epoll.pe.hu/hanoi/login");
        request.setHeader("Content-Type", "application/json");
        request.setContent(jsonString);
        
        Gdx.net.sendHttpRequest(request, response);
    }
    
    public void register(){
        
        json = new Json();
        
        state = 3;
        message = "Please wait...";
        game.layout.setText(game.font, message);
        
        request = new HttpRequest(HttpMethods.POST);
        
        response = new HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String responseJson = httpResponse.getResultAsString();
                final int statusCode = httpResponse.getStatus().getStatusCode();
                
                if(statusCode == 200){
                    game.user.putBoolean("loggedIn", true);
                    game.user.putInteger("id", Integer.parseInt(responseJson));
                    game.user.putString("username", game.userLogin.username);
                    game.user.putInteger("classicScore", 0);
                    game.user.putInteger("randomScore", 0);
                    game.user.flush();
                    message = "Register Success!";
                    game.layout.setText(game.font, message);
                    state = 1;
                }
                else if(statusCode == 422){   
                    JsonValue temp = new JsonReader().parse(responseJson);
                    
                    message = "";
                    
                    
                    for(int i=0; i<temp.size; i++){
                        message += temp.get(i).child().toString()+"\n";
                    }
                    game.layout.setText(game.font, message);
                                     
                    state = 2;
                }    
            }

            @Override
            public void failed(Throwable t) {
                message = "Connection Error\nPlease check connection!";
                game.layout.setText(game.font, message);
                state = 2;
            }

            @Override
            public void cancelled() {
                game.setScreen(new LoginMenu(game));
            }
        };
        
        
        json.setOutputType(OutputType.json);
        jsonString = json.toJson(game.userLogin);
        
        request.setUrl("http://epoll.pe.hu/hanoi/register");
        request.setHeader("Content-Type", "application/json");
        request.setContent(jsonString);
        
        Gdx.net.sendHttpRequest(request, response);
    }
    
    public void get_question(){
        final Array<Integer> pl1 = new Array<Integer>();
        final Array<Integer> pl2 = new Array<Integer>();
        final Array<Integer> pl3 = new Array<Integer>();
        
        state = 3;
        message = "Please wait...";
        game.layout.setText(game.font, message);
        request = new HttpRequest(HttpMethods.GET);
        
        response = new HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String responseJson = httpResponse.getResultAsString();
                final int statusCode = httpResponse.getStatus().getStatusCode();
                
                if(statusCode == 200){
                    JsonValue temp = new JsonReader().parse(responseJson);
                    String a = temp.getString("pole_1");
                    
                    if(a !=null){
                        for (String val: a.split(";")) {
                           pl1.add(Integer.parseInt(val));
                        }
                    }
                    
                    a = temp.getString("pole_2");
                    if(a !=null){
                        for (String val: a.split(";")) {
                           pl2.add(Integer.parseInt(val));
                        }
                    }
                    
                    a = temp.getString("pole_3");
                    if(a !=null){
                        for (String val: a.split(";")) {
                           pl3.add(Integer.parseInt(val));
                        }
                    }
                    
                    game.question.add(pl1);
                    game.question.add(pl2);
                    game.question.add(pl3);
                    
                    state = 4;
                }
                else{
                    
                }
            }

            @Override
            public void failed(Throwable t) {
                message = "Connection Error\nPlease check connection!";
                game.layout.setText(game.font, message);
                state = 1;
            }

            @Override
            public void cancelled() {
                game.setScreen(new MainMenu(game));
            }
        };
                
        request.setUrl("http://epoll.pe.hu/hanoi/get_question/"+game.stackCount);
        Gdx.net.sendHttpRequest(request, response);
    }
    
    public void post_score(Score score, final int mode, final boolean retry){
        
        json = new Json();
        
        state = 3;
        message = "Please wait...";
        game.layout.setText(game.font, message);
        
        request = new HttpRequest(HttpMethods.POST);
        
        response = new HttpResponseListener(){
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String responseJson = httpResponse.getResultAsString();
                final int statusCode = httpResponse.getStatus().getStatusCode();
                if(statusCode == 200){
                    if(responseJson.equals("0")){
                        message = "Your new Score didn't exceed your last score!";
                        game.layout.setText(game.font, message);
                        if(retry==false){
                            state = 1;
                        }
                        else{
                            if(mode == 0){
                                state = 5;
                            }
                            else{
                                state = 6;
                            }
                            
                        }
                    }
                    else{
                        JsonValue temp = new JsonReader().parse(responseJson);
                        game.user.putInteger("classicScore", temp.getInt("classic_score"));
                        game.user.putInteger("randomScore", temp.getInt("random_score"));
                        game.user.flush();
                        message = "Your new Score has been accumulated!";
                        game.layout.setText(game.font, message);
                        if(retry==false){
                            state = 1;
                        }
                        else{
                            if(mode == 0){
                                state = 5;
                            }
                            else{
                                state = 6;
                            }
                            
                        }
                    }
                }
            }

            @Override
            public void failed(Throwable t) {
                message = "Connection Error\nPlease check connection!";
                game.layout.setText(game.font, message);
                state = 1;
            }

            @Override
            public void cancelled() {
                game.setScreen(new MainMenu(game));
            }
            
        };
        json.setOutputType(OutputType.json);
        
        jsonString = json.toJson(score);
        
        if(mode==0){
            request.setUrl("http://epoll.pe.hu/hanoi/post_classic");
        }
        else{
            request.setUrl("http://epoll.pe.hu/hanoi/post_random");
        }
        
        request.setHeader("Content-Type", "application/json");
        request.setContent(jsonString);
        Gdx.net.sendHttpRequest(request, response);
    }
    
    public void get_classic_score(){
        json = new Json();
        game.classic = new Array();
        
        state = 3;
        message = "Please wait...";
        game.layout.setText(game.font, message);
        
        request = new HttpRequest(HttpMethods.GET);
        
        response = new HttpResponseListener(){
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String responseJson = httpResponse.getResultAsString();
                final int statusCode = httpResponse.getStatus().getStatusCode();
                if(statusCode == 200){
                    JsonValue temp = new JsonReader().parse(responseJson);
                    for(int i=0; i<temp.size; i++){
                        HighScore score = new HighScore();
                        score.username = temp.get(i).getString("username");
                        score.score = temp.get(i).getInt("classic_score");
                        game.classic.add(score);
                    }
                    
                    game.setScreen(new RequestMenu(game, true));
                }
            }

            @Override
            public void failed(Throwable t) {
                message = "Connection Error\nPlease check connection!";
                game.layout.setText(game.font, message);
                state = 1;
            }

            @Override
            public void cancelled() {
                game.setScreen(new MainMenu(game));
            }
            
        };
        
        request.setUrl("http://epoll.pe.hu/hanoi/get_classic");
        
        Gdx.net.sendHttpRequest(request, response);
    }
    
    public void get_random_score(){
        json = new Json();
        game.random = new Array();
        
        state = 3;
        message = "Please wait...";
        game.layout.setText(game.font, message);
        
        request = new HttpRequest(HttpMethods.GET);
        
        response = new HttpResponseListener(){
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String responseJson = httpResponse.getResultAsString();
                final int statusCode = httpResponse.getStatus().getStatusCode();
                if(statusCode == 200){
                    JsonValue temp = new JsonReader().parse(responseJson);
                    for(int i=0; i<temp.size; i++){
                        HighScore score = new HighScore();
                        score.username = temp.get(i).getString("username");
                        score.score = temp.get(i).getInt("random_score");
                        game.random.add(score);
                    }
                    
                    game.setScreen(new ScoreMenu(game));
                }
            }

            @Override
            public void failed(Throwable t) {
                message = "Connection Error\nPlease check connection!";
                game.layout.setText(game.font, message);
                state = 1;
            }

            @Override
            public void cancelled() {
                game.setScreen(new MainMenu(game));
            }
            
        };
        
        request.setUrl("http://epoll.pe.hu/hanoi/get_random");
        
        Gdx.net.sendHttpRequest(request, response);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.bg, 0, 0);        
        switch (state) {
            case 1:
                game.font.draw(game.batch, game.layout, 1920/2 - game.layout.width/2, 1080/2 + game.layout.height/2);
                if(Gdx.input.isTouched()){
                    game.setScreen(new MainMenu(game));
                    dispose();
                }   
                break;
                
            case 2:
                game.font.draw(game.batch, game.layout, 1920/2 - game.layout.width/2, 1080/2 + game.layout.height/2);
                 if(Gdx.input.isTouched()){
                    game.setScreen(new LoginMenu(game));
                    dispose();
                }  
                break;
                
            case 3:
                game.font.draw(game.batch, game.layout, 1920/2 - game.layout.width/2, 1080/2 + game.layout.height/2);
                if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)){
                    response.cancelled();
                    dispose();
                }
                    
                break;
                
            case 4:
                    game.bgm1.stop();
                    game.bgm2.stop();
                    game.bgm3.stop();
                    game.bgm4.stop();
                    game.bgm5.stop();
                    game.setScreen(new GameMenu(game));
                    dispose();
                break;
                
            case 5:
                    game.font.draw(game.batch, game.layout, 1920/2 - game.layout.width/2, 1080/2 + game.layout.height/2);
                    if(Gdx.input.isTouched()){
                        game.setScreen(new GameMenu(game));
                        dispose();
                    }   
                break;    
                
            case 6:
                    game.font.draw(game.batch, game.layout, 1920/2 - game.layout.width/2, 1080/2 + game.layout.height/2);
                    if(Gdx.input.isTouched()){
                        game.setScreen(new RequestMenu(game));
                        dispose();
                    }
                break;
                
            case 7:
                    game.font.draw(game.batch, game.layout, 1920/2 - game.layout.width/2, 1080/2 + game.layout.height/2);
                    if(Gdx.input.isTouched()){
                        game.setScreen(new ScoreMenu(game));
                        dispose();
                    }
                break;  
                    
            default:
                game.batch.draw(game.layer, 0, 0);
                break;
        }
        game.batch.end();
        
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
