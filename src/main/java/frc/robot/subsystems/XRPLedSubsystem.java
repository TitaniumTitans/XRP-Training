package frc.robot.subsystems;

import edu.wpi.first.wpilibj.xrp.XRPOnBoardIO;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class XRPLedSubsystem extends SubsystemBase {
  private final XRPOnBoardIO m_io;
  private boolean m_ledState = false;

  public XRPLedSubsystem() {
    m_io = new XRPOnBoardIO();
    m_io.setLed(false);
  }

  public boolean getOnBoardButton() {
    return m_io.getUserButtonPressed();
  }

  public void setOnBoardLed(boolean setLed) {
    m_ledState = setLed;
    m_io.setLed(setLed);
  }

  public Command toggleLed() {
    return runOnce(() -> setOnBoardLed(!m_ledState));
  }
}
