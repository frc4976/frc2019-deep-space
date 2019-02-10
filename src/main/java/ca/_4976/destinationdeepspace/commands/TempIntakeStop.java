package ca._4976.destinationdeepspace.commands;

import ca._4976.destinationdeepspace.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class TempIntakeStop extends Command {
    @Override
    protected void initialize(){
        Robot.intake.tempIntakeStop();
    }
    @Override
    protected boolean isFinished() {
        return true;
    }
}
