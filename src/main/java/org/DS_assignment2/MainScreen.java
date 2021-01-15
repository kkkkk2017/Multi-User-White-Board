package org.DS_assignment2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class MainScreen{
    @FXML private ColorPicker colorPicker;
    @FXML private Canvas canvas;
    @FXML private VBox userbox;
    @FXML private Pane canvasPane;
    @FXML private Label youR;
    @FXML private TextField inputText;

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
        youR.setText(youR.getText() + ClientController.getClientController().getClient().getName());
        brush = canvas.getGraphicsContext2D();
        canvas.setWidth(WIDTH);
        canvas.setHeight(HEIGHT);
        updateClients();
    }

    public void updateCanvas(int[] imageData){
        if (imageData != null){
            this.canvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, WIDTH, HEIGHT,
                    PixelFormat.getIntArgbPreInstance(), imageData, 0, WIDTH);
        }
    }

    public void exit(MouseEvent mouseEvent) {
      if(ClientController.getClientController().quitServer()){
          System.exit(0);
      }
    }

    public void updateClients(){
        userbox.getChildren().clear();
        for(String c: ClientController.getClientController().getAllClients()){
            Label clientLabel = new Label(c);
            clientLabel.setFont(new Font("Comic Sans MS Bold", 25));
            clientLabel.setStyle("-fx-border-width: 2; -fx-border-color: cyan;");
            userbox.getChildren().add(clientLabel);
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

    private void saveCanvas() {
        WIDTH = (int)canvasPane.getWidth();
        HEIGHT = (int)canvasPane.getHeight();

        int[] imageData = new int[WIDTH * HEIGHT];
        WritableImage writableImage = new WritableImage(WIDTH, HEIGHT);
        this.canvas.snapshot(null, writableImage);
        writableImage.getPixelReader().getPixels(0, 0, WIDTH, HEIGHT,
                PixelFormat.getIntArgbPreInstance(), imageData, 0, WIDTH);

        if (imageData != null){
            ClientController.getClientController().sendImage(imageData);
        }
    }

    private void disselectAllBrushes(){
        this.freeBrush = false;
        this.lineBrush = false;
        this.circleBrush = false;
        this.rectBrush = false;
        this.textBrush = false;
    }

}
