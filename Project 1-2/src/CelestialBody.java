public class CelestialBody {
    private final double GRAVITY_CONSTANT = 6.673 * Math.pow(10, -11);
    double xLoc = 0;
    double yLoc = 0;
    double zLoc = 0;
    double velX = 0;
    double velY = 0;
    double velZ = 0;
    double mass = 0;
    double accX = 0;
    double accY = 0;
    double accZ = 0;
    String name = "";

    public CelestialBody(String name, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity,
                         double bodyMass) {
        xLoc = x;
        yLoc = y;
        zLoc = z;
        velX = xVelocity;
        velY = yVelocity;
        velZ = zVelocity;
        mass = bodyMass;
        this.name = name;
    }
    public double getMass() {
        return mass;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double d) {
        velX = d;

    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double d) {
        velY = d;
    }

    public double getVelZ() {
        return velZ;
    }

    public void setVelZ(double d) {
        velZ = d;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double d) {
        accX = d;

    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double d) {
        accY = d;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double d) {
        accZ = d;
    }

    public double getXPosition() {

        return xLoc;
    }

    public void setXPosition(double X) {
        xLoc = X;
    }

    public void changeVelocity(double vx, double vy, double vz) {
        velX += vx;
        velY += vy;
        velZ += vz;
    }

    public void changePosition(double dx, double dy, double dz) {
        xLoc += dx;
        yLoc += dy;
        zLoc += dz;
    }

    public Point getLocation() {
        Point pt = new Point(xLoc, yLoc, zLoc);
        return pt;
    }

    public Point getVelocity() {
        Point pt = new Point(velX, velY, velZ);
        return pt;
    }

    public double getYPosition() {
        return yLoc;
    }

    public void setYPosition(double Y) {
        yLoc = Y;
    }

    public double getZPosition() {
        return zLoc;
    }

    public void setZPosition(double Z) {
        zLoc = Z;
    }

    public String getName() {
        return this.name;
    }

    /*This method calculates the acceleration of the planet at each instance of time
    * It uses a basic physical formula
    * By using gravitational force and the variation of position between two object we can predict their next state
    */
    public void calculate_single_body_acceleration(CelestialBody[] bodies, int index) {

        //Point acceleration = new Point();
        CelestialBody targetBody = bodies[index];
        double accX =0;
        double accY =0;
        double accZ =0;




        for (int i = 0; i < bodies.length; i++) {
           //So a planet doesn't get calculated with itself
            if (i != index) {
                //Euclidean equation for the distance between two objects in 3d
                double r = (Math.pow((bodies[i].getXPosition() - targetBody.getXPosition()), 2) + Math.pow((bodies[i].getYPosition() - targetBody.getYPosition()), 2) + Math.pow((bodies[i].getZPosition() - targetBody.getZPosition()), 2));

                r = Math.sqrt(r);

                double tmp = GRAVITY_CONSTANT * bodies[i].getMass() / (Math.pow(r, 3));

                //Set each acceleration to this force times the variation of position
                accX = accX + (tmp * (bodies[i].getXPosition() -  targetBody.getXPosition()));
                accY = accY + (tmp * (bodies[i].getYPosition() -  targetBody.getYPosition()));
                accZ = accZ + (tmp * (bodies[i].getZPosition() -  targetBody.getZPosition()));

            }
        }
        targetBody.setAccX(accX);
        targetBody.setAccY(accY);
        targetBody.setAccZ(accZ);

    }


    // Combines the location and velocity update
    public void computeGravityStep(CelestialBody[] bodies, double timeStep) {

        for (int i = 0; i < bodies.length; i++) {
            calculate_single_body_acceleration(bodies, i);
        }

        for( int i = 0; i < bodies.length;i++){
            bodies[i].changeVelocity(bodies[i].getAccX() * timeStep,bodies[i].getAccY() * timeStep, bodies[i].getAccZ() * timeStep );
        }

        for (int i = 0; i < bodies.length; i++) {

            bodies[i].changePosition(bodies[i].getVelX() * timeStep, bodies[i].getVelY() * timeStep, bodies[i].getVelZ() * timeStep);

        }



    }
    /*


        private RealForce[] bodies;


        public RungeKutta(int timestep, CelestialBody[] bodies){
            this.timestep = timestep;
            this.bodies = bodies;
        }

        public Point partialStep(Point point1, Point point2, double time_step) {

            Point point = new Point();

            point.changeX(point1.getX() + point2.getX() * time_step);
            point.changeY(point1.getY() + point2.getY() * time_step);
            point.changeZ(point1.getZ() + point2.getZ() * time_step);

            return point;
        }

        public Point getAcceleration(int index) {
            Point acceleration = new Point();
            CelestialBody[] bodies;
            CelestialBody targetBody = bodies[index];

            Point k1 = new Point();
            Point k2 = new Point();
            Point k3 = new Point();
            Point k4 = new Point();
            Point tmpLoc = new Point();
            Point tmpVel = new Point();

            for(int i = 0; i<bodies.length; i++) {
                if (i != index) {
                    double r = Math.pow(targetBody.getXPosition() - bodies[i].getXPosition(), 2) + Math.pow(targetBody.getYPosition() - bodies[i].getYPosition(), 2) + Math.pow(targetBody.getZPosition() - bodies[i].getZPosition(), 2);
                    r = Math.sqrt(r);
                    double tmp = GRAVITY_CONSTANT * bodies[i].getMass() / Math.pow(r, 3);

                    k1.setX(tmp * (bodies[i].getXPosition() - targetBody.getXPosition()));
                    k1.setY(tmp * (bodies[i].getYPosition() - targetBody.getYPosition()));
                    k1.setZ(tmp * (bodies[i].getZPosition() - targetBody.getZPosition()));

                    tmpVel = partialStep(targetBody.getVelocity(), k1, 0.5);
                    tmpLoc = partialStep(targetBody.getLocation(), tmpVel, 0.5);

                    k2.setX((tmpLoc.getX() - (tmpLoc.getX() + tmpVel.getX() * 0.5 * timestep)) * tmp);
                    k2.setY((tmpLoc.getY() - (tmpLoc.getY() + tmpVel.getY() * 0.5 * timestep)) * tmp);
                    k2.setZ((tmpLoc.getZ() - (tmpLoc.getZ() + tmpVel.getZ() * 0.5 * timestep)) * tmp);


                    tmpVel = partialStep(targetBody.getVelocity(), k2, 0.5);


                    k3.setX((tmpLoc.getX() - (tmpLoc.getX() + tmpVel.getX() * 0.5 * timestep)) * tmp);
                    k3.setY((tmpLoc.getY() - (tmpLoc.getY() + tmpVel.getY() * 0.5 * timestep)) * tmp);
                    k3.setZ((tmpLoc.getZ() - (tmpLoc.getZ() + tmpVel.getZ() * 0.5 * timestep)) * tmp);


                    tmpVel = partialStep(targetBody.getVelocity(), k1, 1);
                    tmpLoc = partialStep(targetBody.getLocation(), tmpVel, 0.5);

                    k4.setX(bodies[i].getXPosition() - (tmpLoc.getX() + tmpVel.getX() * this.timestep) * tmp);
                    k4.setY(bodies[i].getYPosition() - (tmpLoc.getY() + tmpVel.getY() * this.timestep) * tmp);
                    k4.setZ(bodies[i].getZPosition() - (tmpLoc.getZ() + tmpVel.getZ() * this.timestep) * tmp);

                    acceleration.changeX((k1.getX() + k2.getX() * 2 + k3.getX() * 2 + k4.getX()) / 6);
                    acceleration.changeY((k1.getY() + k2.getY() * 2 + k3.getY() * 2 + k4.getY()) / 6);
                    acceleration.changeZ((k1.getZ() + k2.getZ() * 2 + k3.getZ() * 2 + k4.getZ()) / 6);
                }
            }
            return acceleration;
        }
    */
}






