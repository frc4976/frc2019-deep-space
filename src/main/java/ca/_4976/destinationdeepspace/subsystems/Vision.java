package ca._4976.destinationdeepspace.subsystems;

import ca._4976.destinationdeepspace.Robot;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Vision extends Subsystem implements Sendable {

    double distance, angA2, nx, ny, vpw, vph, actualX, actualY, x;

    NetworkTable visionValues = NetworkTableInstance.getDefault().getTable("limelight");

    NetworkTableEntry tx = visionValues.getEntry("tx");
    NetworkTableEntry ty = visionValues.getEntry("ty");
    NetworkTableEntry ta = visionValues.getEntry("ta");
    NetworkTableEntry ts = visionValues.getEntry("ts");

    Servo camera = new Servo(0);

    @Override
    protected void initDefaultCommand() {}

    //distance calculations
    public void periodicRead(){

        double area = ta.getDouble(0);
        //Most accurate value so far 1.365839252
        //Most second accurate value so far 1.340580252
        distance = 1.319004642/Math.sqrt(area);

    }
    //constantly reading x values from the Limelight
    public double readXValue(){
        x = tx.getDouble(0);
        return x;
    }

    public void center() {
        if (readXValue() < 10){
            Robot.drive.drive(0.6, -0.6);
        }
        else {
            Robot.drive.drive(-0.6 , 0.6);
        }
    }

    public boolean isCentered() {
        if (readXValue() > 5 && readXValue() < 15){
            return true;
        }
        else {
            return false;
        }
    }

    //looks for a vision target then returns true
    public boolean stopWithVision(){
        readXValue();
        if (x != 0){
            return true;
        }
        return false;
    }

    // Turns the camera to the left
    public void cameraLeft() {
        camera.setAngle(0);
    }

    // Turns the camera to the forwards position
    public void cameraForwards() {
        camera.setAngle(90);
    }

    // Turns the camera to the right
    public void cameraRight() {
        camera.setAngle(180);
    }

    //Turns the bot based on its angle in comparison to the target
    public void skewCorrection() {
        if (tx.getDouble(0) < 0){
            Robot.drive.drive(0.5, 0.5);
        }
        else {
            Robot.drive.drive(-0.5, -0.5);
        }
        System.out.println(tx.getDouble(0));
    }
}