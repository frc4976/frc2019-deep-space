package ca._4976.destinationdeepspace.subsystems;

import ca._4976.destinationdeepspace.commands.DriveWithJoystick;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

import static com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput;
import static com.ctre.phoenix.motorcontrol.ControlMode.Position;

// The DriveTrain subsystem controls the robot's chassis and reads in
// information about it's speed and posit ion.

public class Drive extends Subsystem {

    // Making a network table
    NetworkTable drive = NetworkTableInstance.getDefault().getTable("Drive");

    // Left drive motor controllers
    VictorSPX LF = new VictorSPX(45);
    VictorSPX RF = new VictorSPX(46);
    //test change
    // Right drive motor controllers
    VictorSPX LB = new VictorSPX(49);
    VictorSPX RB = new VictorSPX(44);

    // The deadband percentage value
    double deadband = 0.10;

    // Variables used in the drive calculations
    double throttle, turn, leftOutput, rightOutput;
    // Control flags
    public boolean userControlEnabled = true, visonOveride = false;
    //Error range
    public double errorRange = 0.05;


    // Applies the deadband to the joystick outputs
    public double applyDeadband(double x) {

        if (Math.abs(x) > deadband) {
            if (x > 0.0) {
                return (x - deadband) / (1.0 - deadband);
            } else {
                return (x + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

    // Checks if a value is above 1 or below -1 and sets them to 1 and -1 respectively
    public double regularize(double x) {
        if (x > 1.0) {
            return 1.0;
        } else if (x < -1.0) {
            return -1.0;
        } else {
            return x;
        }
    }

    // Sets the motor controllers to the calculated outputs
    public void drive(double leftOutput, double rightOutput) {
        LF.set(PercentOutput, leftOutput);

        RF.set(PercentOutput, rightOutput);
    }

    // Drive output calculations
    public void arcadeDrive(Joystick joy) {

        if (userControlEnabled) {
            // Save the left and right trigger values as a combined value
            throttle = applyDeadband(joy.getRawAxis(2) - joy.getRawAxis(3));
            // Save the left stick value
            turn = applyDeadband(joy.getRawAxis(0));

            // Save the throttle values as the left and right outputs
            if (visonOveride) {
                leftOutput = regularize(-throttle);
                rightOutput = regularize(throttle);
            }
            // Save the combined turn and throttle values as the left and right outputs
            else {
                leftOutput = regularize(-throttle + turn);
                rightOutput = regularize(throttle + turn);
            }

            drive(leftOutput, rightOutput);
        }
    }

    // Sets the drive outputs to zero
    public void stop() {
        drive(0, 0);
    }

    // Set vision override enabled or disabled
    public void setVisonOveride(boolean enabled) {
        visonOveride = enabled;
    }

    //Set user control enabled or disabled
    public void setUserControlEnabled(boolean enabled) {
        userControlEnabled = enabled;
    }

    // Used to create looping joystick input

    //Drives to an encoder position
    public void driveToEncoderPos(double RightPos, double LeftPos) {
        setUserControlEnabled(false);
        RF.set(Position, RightPos);
        LF.set(Position, LeftPos);
        if (LF.getClosedLoopError() >= LeftPos * (1 - errorRange) && LF.getClosedLoopError() <= LeftPos * (1 + errorRange)) {
            if (RF.getClosedLoopError() >= RightPos * (1 - errorRange) && RF.getClosedLoopError() <= RightPos * (1 + errorRange)) {
                LF.set(PercentOutput, 0);
                RF.set(PercentOutput, 0);
                setUserControlEnabled(true);
            }
        }
    }

    @Override
    protected void initDefaultCommand() {
        RB.follow(RF);
        LB.follow(LF);
        setDefaultCommand(new DriveWithJoystick());
    }
}