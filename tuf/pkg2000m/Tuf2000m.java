/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuf.pkg2000m;

import com.sun.javaws.Main;
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
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    primaryStage.setResizable(false);
                    FXMLLoader viewLoader = new FXMLLoader(getClass().getResource("/view/FXML.fxml"));
                    
                    Scene scene = new Scene(viewLoader.load());
                    primaryStage.setScene(scene);
                    
                    primaryStage.show();
                    
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        primaryStage.show();
    }
    
}
