package frc.robot.commands.shapes;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveForTimeCommand;
import frc.robot.subsystems.XRPDrivetrain;

public class TriangleAutoCommand extends SequentialCommandGroup {
  public TriangleAutoCommand(XRPDrivetrain drivetrain) {
    // Draw a square using a for loop
    for (int i = 0; i < 4; i++) {
      // for each side drive forward and then turn to the left
      addCommands(
          new DriveForTimeCommand(drivetrain, 1.0, 1.0, 0.0),
          new DriveForTimeCommand(drivetrain, 1.25, 0.0, 1.0)
      );
    }
  }
}
