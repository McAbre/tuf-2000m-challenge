/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuf.pkg2000m;

import com.sun.javaws.Main;
import controller.Controller;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author joel_
 */
public class Tuf2000m extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Stage customerStage = new Stage();
                    primaryStage.setResizable(false);
                    customerStage.setResizable(false);
                    FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("views/Tuf-2000_view.fxml"));
                    
                    Scene cashierScene = new Scene(viewLoader.load());
                    
                    primaryStage.setScene(cashierScene);
                    
                    Controller c = viewLoader.<Controller>getController();
                    
                    primaryStage.show();
                    
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
