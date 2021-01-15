package org.DS_assignment2;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ServerMainScreen {
    @FXML private ColorPicker colorPicker;
    @FXML private Pane canvasPane;
    @FXML private Canvas canvas;
    @FXML private VBox userbox;
    @FXML private Label youR;
    @FXML private TextField inputText;
    @FXML private BorderPane pane;

    FileChooser fileChooser = new FileChooser();
    private GraphicsContext brush;
    private int HEIGHT = 400;
    private int WIDTH = 500;

    private boolean freeBrush = false;
    private boolean lineBrush = false;
    private boolean circleBrush = false;
    private boolean rectBrush = false;
    private boolean textBrush = false;

    private boolean start = false;
    private boolean end = false;

    private Line line = new Line();
    private Circle circle = new Circle();
    private Rectangle rectangle = new Rectangle();

    @FXML
    protected void initialize(){
        youR.setText(youR.getText() + AdminController.getAdmin().getAdminClient().getName());
        brush = canvas.getGraphicsContext2D();
        updateClients();
    }

    public void updateCanvas(){
        if (AdminController.getAdmin().getImageData() != null){
            this.canvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, WIDTH, HEIGHT,
                    PixelFormat.getIntArgbPreInstance(), AdminController.getAdmin().getImageData(), 0, WIDTH);
        }
    }

    //admin user exit, all users exit
    public void exit(MouseEvent mouseEvent) {
        AdminController.getAdmin().closeServer();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Server Terminated.");
        alert.showAndWait();
        System.exit(0);
    }

    public void updateClients(){
        userbox.getChildren().clear();
        for(String c: AdminController.getAdmin().getClientName()){
            Label clientLabel = new Label(c);
            clientLabel.setFont(new Font("Comic Sans MS Bold", 25));
            clientLabel.setStyle("-fx-border-width: 2; -fx-border-color: cyan");
            userbox.getChildren().add(clientLabel);
        }
    }

    public void saveFile(MouseEvent mouseEvent) {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*"),
                new FileChooser.ExtensionFilter("PNG", ".png"));
        fileChooser.setTitle("Save Image");
        File file = fileChooser.showSaveDialog(null);
        if (file != null){
            try {
                WritableImage writableImage = new WritableImage(WIDTH, HEIGHT);
                canvas.snapshot(null, writableImage);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(bufferedImage, "png", file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save Image");
                alert.setHeaderText("Image saved successfully.");
                alert.showAndWait();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("File save failed");
            alert.setHeaderText("No file name");
            alert.showAndWait();
        }
    }

    public void newCanvas(MouseEvent mouseEvent) {
        AdminController.getAdmin().setImageData(new int[WIDTH*HEIGHT]);
        updateCanvas();
        AdminController.getAdmin().updateClientCanvas(null);
    }

    public void removeClient(MouseEvent mouseEvent) {
        ArrayList<String> clientList = AdminController.getAdmin().getClientName();

        ChoiceDialog<String> dialog = new ChoiceDialog<>("", clientList);
        dialog.setTitle("Kick out Client");
        dialog.setContentText("Choose the Client:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            if(AdminController.getAdmin().removeUser(result.get())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("[Admin] CLIENT " + result.get() + " has been removed.");
                alert.showAndWait();

                updateClients();
            }
        }
    }

    public void openFile(MouseEvent mouseEvent) {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*"),
                new FileChooser.ExtensionFilter("PNG", ".png"));
        fileChooser.setTitle("Open Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null){
            try {
                WritableImage saveImage = new WritableImage(WIDTH, HEIGHT);
                BufferedImage bufferedImage = ImageIO.read(file);
                saveImage = SwingFXUtils.toFXImage(bufferedImage, saveImage);
                int[] imageData = new int[WIDTH*HEIGHT];
                saveImage.getPixelReader().getPixels(0, 0, WIDTH, HEIGHT,
                        PixelFormat.getIntArgbPreInstance(), imageData, 0, WIDTH);

                AdminController.getAdmin().setImageData(imageData);
                updateCanvas();
                AdminController.getAdmin().updateClientCanvas(null);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("File open failed");
            alert.setHeaderText("No select file.");
            alert.showAndWait();
        }
    }

    @FXML
    public void selectBrush(ActionEvent actionEvent){
        disselectAllBrushes();
        freeBrush = true;
    }

    @FXML
    public void selectLine(ActionEvent actionEvent) {
        disselectAllBrushes();
        lineBrush = true;
        start = true;
    }

    @FXML
    public void selectCircle(ActionEvent actionEvent) {
        disselectAllBrushes();
        circleBrush = true;
        start = true;
    }

    @FXML
    public void selectRect(ActionEvent actionEvent) {
        disselectAllBrushes();
        rectBrush = true;
        start = true;
    }

    @FXML
    public void enterText(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input Text Tip");
        alert.setHeaderText("Tip");
        alert.setContentText("Enter the text in the textField\nPress any position on the canvas to insert.");
        disselectAllBrushes();
        textBrush = true;
        start = true;
    }

    @FXML
    public void draw(MouseEvent e) {
        if (start){
            startDraw(e.getX(), e.getY());
            start = false;
            end = true;
        } else if (end){
            endDraw(e.getX(), e.getY());
            end=false;
            start=true;
        }
        saveCanvas();
    }

    @FXML
    public void brushDraw(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();

        if (freeBrush){
            brush.setFill(colorPicker.getValue());
            brush.fillRoundRect(x, y, 10, 10, 10, 10);
        }
    }

    @FXML
    public void endDrawBrush(MouseEvent mouseEvent) {
        saveCanvas();
    }

    private void startDraw(double x, double y){
        if (lineBrush){
            brush.fillRoundRect(x, y, 1, 1, 1, 1);
            brush.setStroke(colorPicker.getValue());
            brush.setFill(colorPicker.getValue());
            line.setStartX(x);
            line.setStartY(y);

        } else if (circleBrush){
            brush.setStroke(colorPicker.getValue());
            brush.setFill(colorPicker.getValue());
            circle.setCenterX(x);
            circle.setCenterY(y);

        } else if (rectBrush){
            brush.fillRoundRect(x, y, 1, 1, 1, 1);
            brush.setStroke(colorPicker.getValue());
            brush.setFill(colorPicker.getValue());
            rectangle.setX(x);
            rectangle.setY(y);

        } else if (textBrush){
            brush.fillRoundRect(x, y, 1, 1, 1, 1);
            brush.setLineWidth(1);
            brush.setFont(youR.getFont());
            brush.setStroke(colorPicker.getValue());
            brush.setFill(colorPicker.getValue());
            brush.fillText(inputText.getText(), x, y);
        }
    }

    private void endDraw(double x, double y) {
        if (lineBrush){
            line.setEndX(x);
            line.setEndY(y);
            brush.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        } else if (circleBrush){
            double r = Math.abs(x - circle.getCenterX()) + Math.abs(y - circle.getCenterY())/2;
            circle.setRadius(r);
            brush.strokeOval(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), circle.getRadius());
        } else if (rectBrush){
            rectangle.setWidth(Math.abs(x-rectangle.getX()));
            rectangle.setHeight(Math.abs(y-rectangle.getY()));
            brush.strokeRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        }
    }

    private void saveCanvas(){
        WIDTH = (int)canvas.getWidth();
        HEIGHT = (int)canvas.getHeight();

        int[] imageData = new int[WIDTH * HEIGHT];
        WritableImage writableImage = new WritableImage(WIDTH, HEIGHT);
        this.canvas.snapshot(null, writableImage);
        writableImage.getPixelReader().getPixels(0, 0, WIDTH, HEIGHT,
                PixelFormat.getIntArgbPreInstance(), imageData, 0, WIDTH);

        AdminController.getAdmin().setImageData(imageData);
        AdminController.getAdmin().updateClientCanvas(null);
    }

    private void disselectAllBrushes(){
        this.freeBrush = false;
        this.lineBrush = false;
        this.circleBrush = false;
        this.rectBrush = false;
        this.textBrush = false;
    }

}
