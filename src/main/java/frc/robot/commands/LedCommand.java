package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.XRPLed;

public class LedCommand extends Command {
  private final XRPLed m_led;
  private final boolean m_ledOn;

  public LedCommand(XRPLed led, boolean ledOn) {
    m_led = led;
    m_ledOn = ledOn;

    addRequirements(m_led);
  }

  @Override
  public void initialize() {
    if (m_ledOn) {
      m_led.setLedOn();
    } else {
      m_led.setLedOff();
    }
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
