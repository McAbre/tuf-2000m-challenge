/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuf.pkg2000m;

import controller.Controller;
import java.io.IOException;
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
                    scene.getStylesheets().add("/view/style.css");
                    primaryStage.setScene(scene);
                    
                    primaryStage.show();
                    
                    Controller c = Controller.getInstance();
                    c.refreshPressed();
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        primaryStage.show();
    }
    
}
