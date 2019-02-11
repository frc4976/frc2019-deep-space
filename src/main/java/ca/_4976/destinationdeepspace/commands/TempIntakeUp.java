package ca._4976.destinationdeepspace.commands;

import ca._4976.destinationdeepspace.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class TempIntakeUp extends Command {
    @Override
    protected void initialize(){
        Robot.intake.tempIntakeUp();
    }
    @Override
    protected boolean isFinished() {
        return true;
    }
}