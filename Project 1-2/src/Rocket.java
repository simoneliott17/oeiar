public class Rocket {
    public final double GRAVITY_CONSTANT = 6.673 * Math.pow(10, -11);
    double mass = 0;
    double xLoc = 0;
    double yLoc = 0;
    double zLoc = 0;
    double xVel = 0;
    double yVel = 0;
    double zVel = 0;
    double accX = 0;
    double accY = 0;
    double accZ = 0;
    String name = "";

    public Rocket(String name, double x, double y, double z, double vx, double vy, double vz, double bodyMass){
        this.name = name;
        xLoc = x;
        yLoc = y;
        zLoc = z;
        xVel = vx;
        yVel = vy;
        zVel = vz;
        mass = bodyMass;
    }

    public double getXPosition(){
        return xLoc;
    }

    public double getYPosition(){
        return yLoc;
    }

    public double getZPosition(){
        return zLoc;
    }

    public double getVelX(){
        return xLoc;
    }

    public double getVelY(){
        return yLoc;
    }

    public double getVelZ(){
        return zLoc;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double xPos){
        xLoc = xPos;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double yPos){
        yLoc = yPos;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double zPos){
        zLoc = zPos;
    }

    public void move(double sunX, double sunY, double sunZ){
        xLoc += xVel;
        yLoc += yVel;
        zLoc += zLoc;
    }

    public void changeVelocity(double vx, double vy, double vz) {
        xVel += vx;
        yVel += vy;
        zVel += vz;
    }
    public void changePosition(double dx, double dy, double dz) {
        xLoc += dx;
        yLoc += dy;
        zLoc += dz;
    }

    public int posNeg(double num){
        if(num ==0)
            return 0;
        if(num>0)
            return 1;
        return -1;
    }

    public void gravity(CelestialBody[] celestialBodies){

        for(int i=0; i<9; i++){
            double difX = (xLoc - celestialBodies[i].getXPosition());
            double difY = (yLoc - celestialBodies[i].getYPosition());
            double force =0;


            double squareDistance = (difX*difX+difY*difY);
            double realDistance = Math.pow(((Math.round(squareDistance*100.0)/100.0) ),2);

            //if (i == 0) {
            //	 force = ((GRAVITY_CONSTANT*mass*(50000*Math.pow(10, 23)))/(realDistance));
            // }

            force = ((GRAVITY_CONSTANT*mass)/(realDistance));


            //May not work properly
            //Error if it goes exactly to the center of the planet

            //If on same vertical, then only the y velocity is affected
            if(difX == 0)
                yVel+=posNeg(difY)*force;
                //if on same horizontal only x velocity is affected
            else if(difY == 0)
                xVel-=posNeg(difX)*force;
                //Else both are affected, proportions may be off/ there may be something wrong with my calculations
            else {
                double proportion = Math.abs(difX /(difY+difX));
                xVel -= posNeg(difX) * proportion * force;
                yVel += posNeg(difY) * (1-proportion) * force;
            }
            //System.out.println(i +": "+force);
        }
    }
    public void calculateAcceleration(CelestialBody[] bodies){
        double accX = 0;
        double accY = 0;
        double accZ = 0;

        for(int i = 0; i < bodies.length; i++){
            double r = (Math.pow(getXPosition() - bodies[i].getXPosition(), 2) + Math.pow(getYPosition() - bodies[i].getYPosition(), 2) + Math.pow(getZPosition() - bodies[i].getZPosition(), 2));
            r = Math.sqrt(r);

            double tmp = GRAVITY_CONSTANT * bodies[i].getMass() / (Math.pow(r, 3));

            accX = accX + (tmp * (bodies[i].getXPosition() -  getXPosition()));
            accY = accY + (tmp * (bodies[i].getYPosition() -  getYPosition()));
            accZ = accZ + (tmp * (bodies[i].getZPosition() -  getZPosition()));


        }
        setAccX(accX);
        setAccY(accY);
        setAccZ(accZ);
    }

    public void orbitRocket(CelestialBody[] bodies, int timeStep){
        calculateAcceleration(bodies);
        changePosition(getVelX() * timeStep, getVelY() * timeStep, getVelZ() * timeStep);
        changeVelocity(getAccX() * timeStep,getAccY() * timeStep, getAccZ() * timeStep);

    }
}
