package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;


import exception.CaractereInterditException;
import exception.LigneIncompleteException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import outil.Analyseur;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;

/**
 * Classe definissant le contenu de l'interface graphique
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class EditeurFenetre {
	String contenu = "";
	
	public EditeurFenetre (Stage stage) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		

		
		/*LABEL */
		
		Label texteTrame = new Label("");
		texteTrame.setFont(Font.font("Consolas", FontWeight.SEMI_BOLD, 15));
		
	
		
		/*LABEL STATUS*/
		
		Label status = new Label("Bonjour, veuillez selectionner un fichier");
		status.setFont(Font.font("Consolas", 14));
		status.setTextFill(Color.WHITE);
		
		
		/*SEPARATEUR*/
		
		Separator s = new Separator();
		
		/*LA BARRE DE BOUTON*/
		
		Button save = new Button ("Save");
		save.setDisable(true);
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FileChooser fc = new FileChooser();
				fc.setTitle("Veuillez sélectionner le répertoire où enregistrer votre fichier");
				File file = fc.showSaveDialog(stage);
				if (file == null) {
					return;
				}
				 if (file != null) {
					 try {
						FileWriter fw = new FileWriter(file, Charset.forName("UTF8"));
						fw.write(contenu);
						fw.close();
						status.setTextFill(Color.GREENYELLOW);
						status.setText("Analyse sauvegardé dans " + file.getAbsolutePath());
						
					} catch (IOException e1) {
						texteTrame.setText("Erreur avec l'ecriture du fichier resultat");
						
					}
				 }		
				
			}
		});
		
		Button clear = new Button("Clear");
		clear.setDisable(true);
		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				status.setText("Veuillez selectionner un autre fichier pour lancer une autre analyse");
				texteTrame.setText("");
				save.setDisable(true);
				clear.setDisable(true);
				status.setTextFill(Color.WHITE);
			}
		});
		
		
		
		Button open = new Button("Open a frame file");
		open.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				FileChooser fc = new FileChooser();
				fc.setTitle("Veuillez selectionner un fichier...");
				File file = fc.showOpenDialog(stage);
				
				texteTrame.setTextFill(Color.RED);
				
				if (file == null) {	
					return;
				}

				try {
		
					contenu = Analyseur.affichage(file.getAbsolutePath());
					
				} catch ( CaractereInterditException | LigneIncompleteException ex) {
					texteTrame.setText(ex.getMessage());
					return;
					
				}
				catch (IOException ex) {
					texteTrame.setText("Erreur avec l'ouverture du fichier");
				}
				
				catch (Exception ex) {
					texteTrame.setText("Erreur avec ce fichier, veuillez réessayer avec un fichier trame correct");
				}

				
				if (contenu.equals("")) {
					texteTrame.setText("Le fichier ne contient pas de trame");
				}
				
				else {
					texteTrame.setTextFill(Color.WHITE);
					status.setTextFill(Color.WHITE);
					texteTrame.setText(contenu);
					
					status.setText("Fichier : " + file.getAbsolutePath());
					save.setDisable(false);
					clear.setDisable(false);
				}
			}
		});
		
		Button exit = new Button("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});
		
		
		/*TOOLBAR*/
		ToolBar tb = new ToolBar(open, clear, save, exit);
		tb.setPrefWidth(screenSize.getWidth());
		
		
		
		/*SCROLL*/
		ScrollPane scroll = new ScrollPane(texteTrame);
		scroll.setStyle("-fx-background: #2F2F2F; -fx-border-color: #2F2F2F;");
		final double SPEED = 0.002;
		   scroll.getContent().setOnScroll(scrollEvent -> {
		        double deltaY = scrollEvent.getDeltaY() * SPEED;
		        scroll.setVvalue(scroll.getVvalue() - deltaY);
		    });
		scroll.setPrefSize( screenSize.getWidth() - 100, screenSize.getHeight() - tb.getHeight() - 100 );
		 
		
		
		/*CONTENEUR*/
		VBox vbox = new VBox();
		vbox.setBackground(new Background(new BackgroundFill(Color.rgb(47, 47, 47), CornerRadii.EMPTY, Insets.EMPTY)));
		vbox.getChildren().addAll(tb, scroll, status);
		vbox.getChildren().add(2, s);
		

		
		
		
		/*MISE EN PLACE DE LA SCENE*/
		stage.setScene(new Scene(vbox, screenSize.getWidth()/2, screenSize.getHeight()-100));
		stage.setTitle("Analyseur de trames");
		
		stage.show();
	}

}
