package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.XRPServoSubsystem;


public class ServoControlCommand extends Command {
  private final XRPServoSubsystem m_xrpServoSubsystem;
  private final double m_angle;

  public ServoControlCommand(XRPServoSubsystem xRPServoSubsystem,
                             double angle) {
    m_xrpServoSubsystem = xRPServoSubsystem;
    m_angle = angle;

    // each subsystem used by the command must be passed into the
    // addRequirements() method (which takes a vararg of Subsystem)
    addRequirements(m_xrpServoSubsystem);
  }

  @Override
  public void initialize() {
    m_xrpServoSubsystem.setServoPosition(m_angle);
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
