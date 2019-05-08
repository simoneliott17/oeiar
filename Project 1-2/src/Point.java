public class Point {
    double x=0;
    double y=0;
    double z=0;

    public Point(){
    }
    public Point(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double getX(){
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY(){
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ(){
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    public void changeX(double change){
        x+=change;
    }
    public void changeY(double change){
        y+=change;
    }
    public void changeZ(double change){
        z+=change;
    }
}
