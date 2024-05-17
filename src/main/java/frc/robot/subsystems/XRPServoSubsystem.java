package frc.robot.subsystems;


import edu.wpi.first.wpilibj.xrp.XRPServo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class XRPServoSubsystem extends SubsystemBase {
  private final XRPServo m_servo;

  public XRPServoSubsystem() {
    m_servo = new XRPServo(4);
  }

  public Command setServoAngle(double angle) {
    return runOnce(() -> m_servo.setAngle(angle));
  }
}

