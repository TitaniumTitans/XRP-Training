package frc.robot.commands.shapes;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.XRPDrivetrain;

//TODO: This command should have the drivetrain move in a triangle fashion
public class TriangleCommand extends ParallelCommandGroup {
  private final XRPDrivetrain m_drivetrain;

  public TriangleCommand(XRPDrivetrain drivetrain) {
    m_drivetrain = drivetrain;
    // each subsystem used by the command must be passed into the
    // addRequirements() method (which takes a vararg of Subsystem)
    addRequirements(m_drivetrain);

    // Repeat the same motions 3 times (for a triangle)
    for (int i = 0; i < 3; i++) {
      // drive at half speed for 1 second
      addCommands(m_drivetrain.driveForTime(0.5, 0.0, 1));

      // turn 120 degrees (360 / 3 = 120)
      addCommands(m_drivetrain.rotateDegrees(120));
    }
  }
}
