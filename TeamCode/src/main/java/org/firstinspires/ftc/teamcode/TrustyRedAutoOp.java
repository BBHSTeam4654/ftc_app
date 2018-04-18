package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by etill on 04 Jan 2018.
 */

@Autonomous(name="TrustyRedAutoOp")
public class TrustyRedAutoOp extends TrustyAutoOp {

	public TrustyRedAutoOp() {
		super(ColorMode.RED);
	}
	
}
