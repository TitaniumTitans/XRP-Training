package frc.robot.commands.shapes;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.XRPDrivetrain;

//TODO: This should be able to draw a shape with any number of sides
public class NSidedShapeCommand extends SequentialCommandGroup {
  public NSidedShapeCommand(XRPDrivetrain drivetrain, int sides) {
    // each subsystem used by the command must be passed into the
    // addRequirements() method (which takes a vararg of Subsystem)
    addRequirements(drivetrain);

    for (int i = 0; i < sides; i++) {
      addCommands(drivetrain.driveForTime(0.5, 0.0, 0.5));
      addCommands(drivetrain.rotateDegrees(360.0 / sides));
    }
  }
}
