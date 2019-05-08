
import javafx.animation.KeyFrame;
import javafx.application.Application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;

import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.animation.Timeline;

import javafx.util.Duration;



public class Main extends Application {
    private static final float WIDTH = 1400;
    private static final float HEIGHT = 800;

    private static final int DRAW_CONSTANT = 1000000000;

    Group world = new Group();
    Group Moon = new Group();
    Group MoonGhost = new Group();
    Date date = new Date(2019, 07, 15);
    private CelestialBody[] planet = new CelestialBody[11];
    private Sphere[] pl;
    private Sphere rock;
    boolean freeze = false;
    private double counterTimer = 0;
    public double timeStep = 500;
    private CelestialBody[] moons = new CelestialBody[2];
    public boolean saturnCenter = false;
    public boolean earthCenter = false;
    public boolean sunCenter = true;

    //Tracks drag starting point for x and y
    private double anchorX, anchorY;
    //Keep track of current angle for x and y
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    //We will update these after drag. Using JavaFX property to bind with object
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    Label timeStepValue = showTimeStep();
    Label calendar = showDate();

    Slider slider = prepareSlider();


    public void start(Stage primaryStage) {

        //We instantiate all of the planets with their real data
        // found on https:// nasa Web horizons
        planet[0] = new CelestialBody("Sun", RealDistance.sunXDistance, RealDistance.sunYDistance, RealDistance.sunZDistance, 0, 0, 0, RealMasses.SUN_MASS);

        planet[1] = new CelestialBody("Mercury", RealDistance.mercuryXDistance, RealDistance.mercuryYDistance,
                RealDistance.mercuryZDistance, RealVelocities.mercuryXVel, RealVelocities.mercuryYVel,
                RealVelocities.mercuryZVel, RealMasses.MERCURY_MASS);

        planet[2] = new CelestialBody("Venus", RealDistance.venusXDistance, RealDistance.venusYDistance,
                RealDistance.venusZDistance, RealVelocities.venusXVel, RealVelocities.venusYVel,
                RealVelocities.venusZVel, RealMasses.VENUS_MASS);

        planet[3] = new CelestialBody("Earth", RealDistance.earthXDistance, RealDistance.earthYDistance,
                RealDistance.earthZDistance, RealVelocities.earthXVel, RealVelocities.earthYVel,
                RealVelocities.earthZVel, RealMasses.EARTH_MASS);

        planet[4] = new CelestialBody("Mars", RealDistance.marsXDistance, RealDistance.marsYDistance,
                RealDistance.marsZDistance, RealVelocities.marsXVel, RealVelocities.marsYVel, RealVelocities.marsZVel,
                RealMasses.MARS_MASS);

        planet[5] = new CelestialBody("Jupiter", RealDistance.jupiterXDistance, RealDistance.jupiterYDistance,
                RealDistance.jupiterZDistance, RealVelocities.jupiterXVel, RealVelocities.jupiterYVel,
                RealVelocities.jupiterZVel, RealMasses.JUPITER_MASS);

        planet[6] = new CelestialBody("Saturn", RealDistance.saturneXDistance, RealDistance.saturneYDistance,
                RealDistance.saturneZDistance, RealVelocities.saturnXVel, RealVelocities.saturnYVel,
                RealVelocities.saturnZVel, RealMasses.SATURNE_MASS);

        planet[7] = new CelestialBody("Titan", RealDistance.titanXDistance, RealDistance.titanYDistance,
                RealDistance.titanZDistance, RealVelocities.titanXVel, RealVelocities.titanYVel,
                RealVelocities.titanZVel, RealMasses.SATURNE_MASS);
        planet[8] = new CelestialBody("Uranus", RealDistance.uranusXDistance, RealDistance.uranusYDistance,
                RealDistance.uranusZDistance, RealVelocities.uranusXVel, RealVelocities.uranusYVel,
                RealVelocities.uranusZVel, RealMasses.URANUS_MASS);
        planet[9] = new CelestialBody("Neptune", RealDistance.neptuneXDistance, RealDistance.neptuneYDistance,
                RealDistance.neptuneZDistance, RealVelocities.neptuneXVel, RealVelocities.neptuneYVel,
                RealVelocities.neptuneZVel, RealMasses.NEPTUNE_MASS);
        planet[10] = new CelestialBody("Rocket", RealDistance.earthXDistance + 9371000000.0, RealDistance.earthYDistance + 9371000000.0, RealDistance.earthZDistance + 9371000000.0, 27900, -30000, 1000, 7500);


        //planet[11] = new CelestialBody("Earth Moon", RealDistance.moonXDistance, RealDistance.moonYDistance,
        //         RealDistance.moonZDistance, RealVelocities.moonXVel, RealVelocities.moonYVel, RealVelocities.moonZVel, RealMasses.MOON_MASS );

        //Use a method to rub the spheres with an image so we can differentiate the planets

        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/mercury.jpeg")));

        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/sun.jpeg")));

        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/venusmap.jpeg")));

        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/earth/earth.normal.jpeg")));

        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/MarsMap.jpeg")));

        PhongMaterial jupMaterial = new PhongMaterial();
        jupMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/jupitermap.jpeg")));

        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/saturnmap.jpeg")));

        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/uranusmap.jpeg")));

        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("images/neptunemap.jpeg")));

        pl = new Sphere[planet.length];

        /* this for loop iterates through all of the planets
         * and create a sphere for each of them at their initial positions
         * these positions are unique but will be changed in the animation method
         */
        for (int i = 0; i < pl.length; i++) {

            pl[i] = new Sphere();
            if (saturnCenter == true) {

                pl[i].setTranslateX(planet[i].getXPosition() / DRAW_CONSTANT - planet[6].getXPosition() / DRAW_CONSTANT);
                pl[i].setTranslateY(planet[i].getYPosition() / DRAW_CONSTANT - planet[6].getYPosition() / DRAW_CONSTANT);
                pl[i].setTranslateZ(planet[i].getZPosition() / DRAW_CONSTANT - planet[6].getZPosition() / DRAW_CONSTANT);

            } else if (earthCenter == true) {
                pl[i].setTranslateX(planet[i].getXPosition() / DRAW_CONSTANT - planet[3].getXPosition() / DRAW_CONSTANT);
                pl[i].setTranslateY(planet[i].getYPosition() / DRAW_CONSTANT - planet[3].getYPosition() / DRAW_CONSTANT);
                pl[i].setTranslateZ(planet[i].getZPosition() / DRAW_CONSTANT - planet[3].getZPosition() / DRAW_CONSTANT);
            } else {
                pl[i].setTranslateX(planet[i].getXPosition() / DRAW_CONSTANT);
                pl[i].setTranslateY(planet[i].getYPosition() / DRAW_CONSTANT);
                pl[i].setTranslateZ(planet[i].getZPosition() / DRAW_CONSTANT);
            }
            switch (i) {
                case 0:
                    pl[i].setMaterial(sunMaterial);
                    pl[i].setRadius(30);
                    break;
                case 1:
                    pl[i].setMaterial(mercuryMaterial);
                    pl[i].setRadius(7.5);
                    break;
                case 2:
                    pl[i].setMaterial(venusMaterial);
                    pl[i].setRadius(10);
                    break;
                case 3:
                    pl[i].setMaterial(earthMaterial);
                    pl[i].setRadius(12.5);
                    break;
                case 4:
                    pl[i].setMaterial(marsMaterial);
                    pl[i].setRadius(10);
                    break;
                case 5:
                    pl[i].setMaterial(jupMaterial);
                    pl[i].setRadius(40);
                    break;
                case 6:
                    pl[i].setMaterial(saturnMaterial);
                    pl[i].setRadius(50);
                    break;
                case 7:
                    //TITAN
                    pl[i].setMaterial(uranusMaterial);
                    pl[i].setRadius(0.1);
                    break;
                case 8:
                    pl[i].setMaterial(uranusMaterial);
                    pl[i].setRadius(30);
                    break;
                case 9:
                    pl[i].setMaterial(neptuneMaterial);
                    pl[i].setRadius(35);
                    break;
                case 10:
                    pl[i].setRadius(5);
            }

            world.getChildren().add(pl[i]);
        }


        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);


        Group root = new Group();
        //root.getChildren().add(prepareImageView());
        root.getChildren().add(MoonGhost);
        root.getChildren().add(world);
        root.getChildren().add(Moon);
        root.getChildren().add(slider);
        root.getChildren().add(timeStepValue);
        root.getChildren().add(calendar);


        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case SPACE:
                        freeze = true;
                        break;
                    case ENTER:
                        freeze = false;

                }

            }
        });
        scene.setFill(Color.BLACK);
        initMouseControl(world, scene, primaryStage);


        primaryStage.setTitle("Galaxy");
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareAnimation();


    }

    private void prepareAnimation() {

        /* TimerLine is an object which allow to user to repeat
         * an action as many time as he wants.
         * The method Keyframe specify the duration of each cycle
         * We iterate through all the planets, update their positions first and then re draw them
         * @timeStep shows how long each loop represents in real life
         */
        Timeline timer = new Timeline(
                new KeyFrame(Duration.millis(1), e -> {

                    if (freeze == true) {
                        timeStep = slider.getValue();
                        timeStepValue.setText("Time Step :" + (int) timeStep + " s ");
                        planet[0].computeGravityStep(planet, timeStep);
                        saturnPressed();
                        earthPressed();
                        sunPressed();
                        redraw();
                        date.updateTime(timeStep);
                        calendar.setText(date.getDate());
                        counterTimer++;

                    }


                }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();


    }

    //Background
    private ImageView prepareImageView() {
        Image image = new Image(Main.class.getResourceAsStream("galaxy.jpeg"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 800));
        return imageView;
    }

    //Slider for zooming in the GUI
    private Slider prepareSlider() {
        Slider slider = new Slider();
        slider.setMax(5000);
        slider.setMin(timeStep);
        slider.setPrefWidth(300d);
        slider.setLayoutX(-150);
        slider.setLayoutY(200);
        slider.setShowTickLabels(true);

        slider.setTranslateZ(5);
        slider.setStyle("-fx-base: black");
        return slider;
    }

    //Creates a label that shows what is the current timeStep used
    private Label showTimeStep() {
        Label showtime = new Label();
        showtime.setFont(Font.font("Verdana", 20));
        showtime.setTextFill(Color.WHITE);
        showtime.setTranslateY(-210);
        showtime.setTranslateX(280);
        return showtime;
    }

    //Creates a label that shows what day it is in the calendar
    private Label showDate() {
        Label showDay = new Label();
        showDay.setFont(Font.font("Verdana", 20));
        showDay.setTextFill(Color.WHITE);
        showDay.setTranslateX(300);
        showDay.setTranslateY(150);
        return showDay;
    }

    //This method uses the refreshed positions of the planets
    // and translates the sphere upon these
    private void redraw() {


        for (int i = 0; i < pl.length; i++) {
            if (saturnCenter == true) {

                pl[i].setTranslateX(planet[i].getXPosition() / DRAW_CONSTANT - planet[6].getXPosition() / DRAW_CONSTANT);
                pl[i].setTranslateY(planet[i].getYPosition() / DRAW_CONSTANT - planet[6].getYPosition() / DRAW_CONSTANT);
                pl[i].setTranslateZ(planet[i].getZPosition() / DRAW_CONSTANT - planet[6].getZPosition() / DRAW_CONSTANT);

            } else if (earthCenter == true) {
                pl[i].setTranslateX(planet[i].getXPosition() / DRAW_CONSTANT - planet[3].getXPosition() / DRAW_CONSTANT);
                pl[i].setTranslateY(planet[i].getYPosition() / DRAW_CONSTANT - planet[3].getYPosition() / DRAW_CONSTANT);
                pl[i].setTranslateZ(planet[i].getZPosition() / DRAW_CONSTANT - planet[3].getZPosition() / DRAW_CONSTANT);
            } else if(sunCenter == true){
                pl[i].setTranslateX(planet[i].getXPosition() / DRAW_CONSTANT);
                pl[i].setTranslateY(planet[i].getYPosition() / DRAW_CONSTANT);
                pl[i].setTranslateZ(planet[i].getZPosition() / DRAW_CONSTANT);
            }

        }


    }

    //Method which allow the user to control the camera upon the scene and the stage
    private void initMouseControl(Group group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            //Get how much scroll was done in Y axis.
            double delta = event.getDeltaY();
            //Add it to the Z-axis location.
            group.translateZProperty().set(group.getTranslateZ() + delta);

        });
    }

    public void saturnPressed() {
        pl[6].addEventFilter(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override

                    public void handle(MouseEvent event) {
                        saturnCenter = true;
                        earthCenter = false;
                        sunCenter  = false;
                    }

                });
    }

    public void earthPressed() {
        pl[3].addEventFilter(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override 

                    public void handle(MouseEvent event) {
                        earthCenter = true;
                        saturnCenter = false;
                        sunCenter = false;
                    }

                });
    }

    public void sunPressed() {
        pl[0].addEventFilter(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override

                    public void handle(MouseEvent event) {
                        sunCenter = true;
                        earthCenter = false;
                        saturnCenter = false;
                    }

                });
    }
}


