package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import java.util.Locale;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@TeleOp(name="New Demo", group="Iterative Opmode")
public class NewDemoOpMode extends BaseOpMode {
    
    enum State {
        DRIVE,
        MECANUM,
        TANK,
        MECANUM2
    }

    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    private State state = State.DRIVE;

    private long lastTime;
    private double clawPos, clawPitchPos;

    @Override
    public void start() {
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        long time = System.currentTimeMillis();
        long deltaTime = time - lastTime;
        lastTime = time;
        
        double armMult = gamepad2.left_bumper ? 0.3 : (gamepad2.right_bumper ? 0.15 : 0.5);
        double clawMult = gamepad2.left_bumper ? -0.8 : (gamepad2.right_bumper ? -0.6 : -1.0);
        
        // Claw open is .8, closed is .2
        clawPos = Math.min(0.9, Math.max(0.25, gamepad2.right_stick_y * clawMult * deltaTime / 1000D + clawPos));
        claw.setPosition(clawPos);
        
        clawPitchPos = Math.min(1.0, Math.max(0.0, 0.5 * gamepad2.left_stick_y * clawMult * deltaTime / 1000D + clawPitchPos));
        clawPitch.setPosition(clawPitchPos);
        
        telemetry.addData("Claw Position: ", String.format("(%.2f, %.2f)", clawPitch.getPosition(), claw.getPosition()));

        
       // telemetry.addData("Arm Position", arm.getCurrentPosition());
        
        //arm.setPower(armMult * (gamepad2.right_trigger - gamepad2.left_trigger));

        
        
        
        /*if (gamepad2.dpad_left) {
            glyph.setPosition(1);
        } else if (gamepad2.dpad_right) {
            glyph.setPosition(0);
        }*/
        
        glyph.setPosition(gamepad1.left_trigger);
        telemetry.addData("GlyphPos", glyph.getPosition());
       
        if (gamepad1.dpad_up)
            state = State.DRIVE;
        else if (gamepad1.dpad_right)
            state = State.MECANUM;
        else if (gamepad1.dpad_down)
            state = State.TANK;
        else if (gamepad1.dpad_left)
            state = State.MECANUM2;
        
        if (gamepad1.x || gamepad2.x)
            colorArm.setPosition(0.9);
        if (gamepad1.y || gamepad2.y)
            colorArm.setPosition(0.3);
        
        double mult = gamepad1.left_bumper ? 0.5 : (gamepad1.right_bumper ? 0.2 : 1.0);
        double x = gamepad1.left_stick_x, y = gamepad1.left_stick_y;
        switch (state) {
            case DRIVE:
                setPowers(mult, y - x, y - x, y + x, y + x);
                break;
            case MECANUM:
                double power = Math.sqrt(x * x + y * y);
                double angle = Math.atan2(y, x);
                double sin = Math.sin(angle - Math.PI / 4);
                double cos = Math.cos(angle - Math.PI / 4);

                setPowers(mult * power, sin, cos, cos, sin);
                break;
            case MECANUM2:
                double power2 = Math.sqrt(x * x + y * y);
                double angle2 = Math.atan2(y, x);
                double sin2 = Math.sin(angle2 - Math.PI / 4);
                double cos2 = Math.cos(angle2 - Math.PI / 4);
                
                double turn = -gamepad1.right_stick_x;

                setPowers(mult, power2 * sin2 + turn, power2 * cos2 + turn, power2 * cos2 - turn, power2 * sin2 - turn);
                break;
            case TANK:
                //left is y 
                double left = gamepad1.left_stick_y;
                double right = gamepad1.right_stick_y;
                setPowers(mult, left,left,right,right);
                break;
        }
        telemetry.addData("State", state);
        telemetry.addData("Color", color.argb() & 0b11111111);
    }
    
    private void setPowers(double mult, double leftFront, double leftBack, double rightFront, double rightBack) {
        setPowers(leftFront * mult, leftBack * mult, rightFront * mult, rightBack * mult);
    }
}