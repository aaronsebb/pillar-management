package com.java.pillargroup.pillarmanagement;

import com.java.pillargroup.pillarmanagement.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    
    
    public static void main(String[] args) {

        launch(args);
        
    }
    
    @Override
    public void start(Stage stage){
        SceneManager.getInstance().init(stage);
        SceneManager.getInstance().showDashboardView();
    }
}
