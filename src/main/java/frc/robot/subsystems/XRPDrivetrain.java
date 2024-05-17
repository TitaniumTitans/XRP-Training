// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.xrp.XRPGyro;
import edu.wpi.first.wpilibj.xrp.XRPMotor;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class XRPDrivetrain extends SubsystemBase {
  private static final double K_GEAR_RATIO =
      (30.0 / 14.0) * (28.0 / 16.0) * (36.0 / 9.0) * (26.0 / 8.0); // 48.75:1
  private static final double COUNTS_PER_MOTOR_SHAFT_REV = 12.0;
  private static final double COUNTS_PER_REVOLUTION = COUNTS_PER_MOTOR_SHAFT_REV * K_GEAR_RATIO; // 585.0
  private static final double WHEEL_DIAMETER_INCH = 2.3622; // 60 mm

  // The XRP has the left and right motors set to
  // channels 0 and 1 respectively
  private final XRPMotor m_leftMotor = new XRPMotor(0);
  private final XRPMotor m_rightMotor = new XRPMotor(1);

  // The XRP has onboard encoders that are hardcoded
  // to use DIO pins 4/5 and 6/7 for the left and right
  private final Encoder m_leftEncoder = new Encoder(4, 5);
  private final Encoder m_rightEncoder = new Encoder(6, 7);

  // Set up the differential drive controller
  private final DifferentialDrive m_diffDrive = new DifferentialDrive(m_leftMotor, m_rightMotor);

  // Set up the XRPGyro
  private final XRPGyro m_gyro = new XRPGyro();
  private double newHeading = 0;
  private final PIDController m_headingController = new PIDController(0.2, 0.0, 0.0);

  // Create a timer
  private final Timer m_timer = new Timer();

  /** Creates a new XRPDrivetrain. */
  public XRPDrivetrain() {
    // Use inches as unit for encoder distances
    m_leftEncoder.setDistancePerPulse((Math.PI * WHEEL_DIAMETER_INCH) / COUNTS_PER_REVOLUTION);
    m_rightEncoder.setDistancePerPulse((Math.PI * WHEEL_DIAMETER_INCH) / COUNTS_PER_REVOLUTION);
    resetEncoders();

    m_timer.reset();

    // Invert right side since motor is flipped
    m_rightMotor.setInverted(true);

    // Tell the PID that it can rotate 360 degrees
    m_headingController.enableContinuousInput(0, 360);

    // Start accumulating error when within 10 degrees of setpoint
    m_headingController.setTolerance(1.0);
  }

  public void arcadeDrive(double xaxisSpeed, double zaxisRotate) {
    m_diffDrive.arcadeDrive(xaxisSpeed, zaxisRotate);
  }

  public Command driveForTime(double xSpeed, double zSpeed, double seconds) {
    return runOnce(() -> {

      // reset and start the timer
      m_timer.reset();
      m_timer.start();

      // drive at the desired speed for so many seconds
      while (m_timer.get() < seconds) {
        arcadeDrive(xSpeed, zSpeed);
      }

      // stop motors when finished
      arcadeDrive(0, 0);
    });
  }

  public void resetEncoders() {
    m_leftEncoder.reset();
    m_rightEncoder.reset();
  }

  public double getLeftDistanceInch() {
    return m_leftEncoder.getDistance();
  }

  public double getRightDistanceInch() {
    return m_rightEncoder.getDistance();
  }

  public double getGyroHeading() {
    return m_gyro.getAngle();
  }

  // Neither of these periodic methods are needed, but you can add things to them
  // to tinker

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Heading", getGyroHeading());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  // rotates the robot a certain amount of degrees positive counterclockwise
  public Command rotateDegrees(double degrees) {
    return runOnce(() -> {
      // find heading goal to get to
      newHeading = getGyroHeading() + degrees;

      // normalize heading goal to be +-360
      while (newHeading > 360) {
        newHeading -= 360;
      }

      while (newHeading < 0) {
        newHeading += 360;
      }

      // Tell the PID controller to get there
      m_headingController.setSetpoint(newHeading);
      SmartDashboard.putNumber("New Heading", newHeading);
    })
        .andThen(runEnd(() -> {
              // calculate new output
              double power = m_headingController.calculate(getGyroHeading());
              power = MathUtil.clamp(power, -0.5, 0.5);
              double error = m_headingController.getPositionError();

              // log error and power outputs
              SmartDashboard.putNumber("Error", error);
              SmartDashboard.putNumber("Power", power);

              // while we're more than a degree away move towards the goal getting slower and slower
              if (!m_headingController.atSetpoint()) {
                arcadeDrive(0, power);
              } else {
                arcadeDrive(0.0, 0.0);
              }
            },
            // stop when finished
            () -> arcadeDrive(0, 0)
        ));
  }
}
