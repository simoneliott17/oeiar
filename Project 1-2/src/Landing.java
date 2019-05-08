public class Landing {

    private final double dragCoefficient = 0.001;
    private final double GRAVITY_CONSTANT = 6.673* Math.pow(10,-23);
    private final double RADIUS_TITAN = 2574.73*Math.pow(10,3);
    private final double MASS_TITAN = 1.3452*Math.pow(10,23);

    private double massRocket;


    private CelestialBody[] bodies = new CelestialBody[2];
    private Rocket rocket = new Rocket("Rocket", RealDistance.earthXDistance, RealDistance.earthYDistance, RealDistance.earthZDistance, RealVelocities.earthXVel, RealVelocities.earthYVel, RealVelocities.earthZVel, 2.75*Math.pow(10,6));
    private CelestialBody titan  = new CelestialBody("Titan", RealDistance.saturneXDistance, RealDistance.saturneYDistance, RealDistance.saturneZDistance, RealVelocities.saturnXVel, RealVelocities.saturnYVel, RealVelocities.saturnZVel, 10000);

    private double timeStep;
    private double acceleration;
    private Point dragForce;
    private Point weight;
    private double rocketArea;

    public Landing(double timeStep){
        this.timeStep = timeStep;

        bodies[1] = titan;
        dragForce = new Point(0, 0, 0 );
        weight = new Point(0, 0, 0);
    }
    public void calculate(){

        // distance calculation --->  ((x1-x0)^2 + (y1-y0)^2 + (z1-z0)^2)^0.5
        double distance =0;
        distance = Math.pow(titan.getXPosition()-rocket.getXPosition(),2)+ Math.pow(titan.getYPosition()-rocket.getYPosition(),2)+ Math.pow(titan.getZPosition()-rocket.getZPosition(),2);
        distance = Math.sqrt(distance);

        double altitude = distance-RADIUS_TITAN;

        // Drag force formula
        dragForce.setX(-1*(java.lang.Math.signum(rocket.getVelX())*dragCoefficient*getDensity(altitude)*Math.pow(rocket.getVelX(),2)* rocketArea)/2);
        dragForce.setY(-1*(java.lang.Math.signum(rocket.getVelY())*dragCoefficient*getDensity(altitude)*Math.pow(rocket.getVelY(),2)* rocketArea)/2);
        dragForce.setX(-1*(java.lang.Math.signum(rocket.getVelZ())*dragCoefficient*getDensity(altitude)*Math.pow(rocket.getVelZ(),2)* rocketArea)/2);

        //Towards to planet
        weight.setX(GRAVITY_CONSTANT*MASS_TITAN*massRocket/(Math.pow(distance,2)));
        weight.setY(GRAVITY_CONSTANT*MASS_TITAN*massRocket/(Math.pow(distance,2)));
        weight.setZ(GRAVITY_CONSTANT*MASS_TITAN*massRocket/(Math.pow(distance,2)));

        //Net force towards to surface
        Point netForce = new Point();
        netForce.setX(weight.getX()-dragForce.getX());
        netForce.setY(weight.getY()-dragForce.getY());
        netForce.setZ(weight.getZ()-dragForce.getZ());



    }


    private double getDensity(double altitude){

        //Density doubles for every 39 km decrease in distance

        //Density is 0 when the altitude is 1500 or another assumption is that the thickness of the atmosphere is 600 km
        //
        //This is the equation I got from a table that displays a linear correlation between the density and the distance
        //So the values are approximate
        //Probably needs to be changed
        double density = (-9/1300*altitude+5.867692308)*Math.pow(10,-9);   //kg/m^3

        return density;
    }


    //Rocket mass decreases because of the fuel used ( depends on several parameters )
    private double getRocketMass(){


        return massRocket;
    }





}
