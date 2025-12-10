// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
    final XboxController secondaryController = new XboxController(1);
  final WPI_VictorSPX primarySpx = new WPI_VictorSPX(26);
  final WPI_VictorSPX followerSPX = new WPI_VictorSPX(28);
  final WPI_TalonSRX outTalonSRX = new WPI_TalonSRX(25);
  final WPI_TalonSRX primarySrx = new WPI_TalonSRX(20);
  final WPI_TalonSRX followerSrx = new WPI_TalonSRX(21);
  private Command m_autonomousCommand;
  @SuppressWarnings("unused")
  private final RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    followerSPX.follow(primarySpx);
    outTalonSRX.configPeakCurrentLimit(40,500);
    outTalonSRX.enableCurrentLimit(true);
    outTalonSRX.configContinuousCurrentLimit(10);
    followerSrx.follow(primarySrx);
    primarySrx.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,10);
    primarySrx.setSelectedSensorPosition(0);
    primarySrx.configPeakCurrentLimit(40,200);
    followerSrx.configPeakCurrentLimit(40,200);
    primarySrx.configContinuousCurrentLimit(20);
    followerSrx.configContinuousCurrentLimit(20);
    primarySrx.enableCurrentLimit(true);
    followerSrx.enableCurrentLimit(true);
  
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
   if (secondaryController.getRightBumperButton()){
    outTalonSRX.set(.75);
   } else {
      if (secondaryController.getLeftBumperButton())
         outTalonSRX.set(-1);
      else
      {
        outTalonSRX.set(secondaryController.getRightTriggerAxis()*-1);
      }
    }
double arm = secondaryController.getRightY();
 arm = arm * arm * (arm > 0 ? 1 : -1);
    primarySpx.set(arm *.5);
    followerSPX.set(arm *.5);

    primarySrx.set(secondaryController.getLeftY()*.5);
    followerSrx.set(secondaryController.getLeftY() *.5);
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();}
  

    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
  
}
