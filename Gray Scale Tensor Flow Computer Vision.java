/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package PracticeCodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.vuforia.Frame;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "NewTensorFlow", group = "Concept")
@Disabled
public class PatrickGrayScaleTensorFlow extends LinearOpMode{
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
Frame frame = null;
Image image = null;

    
    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AUUmFe7/////AAABmfrACDmLpkwPoZBcpjPAgD81Dd54BbEwE+bWlxUPFMMkELMLgNDtnLZm+XzK22G9gFzhzAFWBxFKtcRD3QkU/SrRc6nIQOMLVN+TgY7oL0V7/a5zdjQs7YFaxwGbrrYtvfThBqLBVq7fHOYdiLb24HaGxQuX6mwbRYZfV9vVRH8WtHhsPTi3J/tX4IG+oiyT8H22KqLfIy5ab0R1FJ2CyrvVYsJ7ogjKCz7lkf6BUMkVn7D/8QXUSoVfkoV4Vf5E966V7YfuXsLjQ1AhvcQuDnrXzh0EE9C/U+Ckux4WYTx7kTpmu5z80gQVqwdLQC6UTXVhvz3+ImkWr08GxXk+5m20Futx7YgxbyFYrNJXiBb2\n";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;
    
    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
   

    public void runOpMode() throws InterruptedException{
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
       

        /** Wait for the game to begin */
      
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();
            
            
        }

        if (opModeIsActive()) {
            
            while (opModeIsActive()) {
             tfod.activate();
                
                if (tfod != null) {
                    
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
frame = vuforia.getFrameQueue().take();
image = frame.getImage(0);
ByteBuffer pixels = image.getPixels();
byte[] pixelArray = new byte[pixels.remaining()];
pixels.get(pixelArray, 0, pixelArray.length);
                      // step through the list of recognitions and display boundary info.

                      for (Recognition recognition : updatedRecognitions) {
                          
                        // "recognition" contains a stone located by TensorFlow, see
                        // ConceptTensorFlowObjectDetection for how to get recognition.
                        int row = (int)(( recognition.getTop() + recognition.getBottom() ) / 2);
int l = row * 640; // starting index of row of pixels to examine
    int sum1 = 0;
    int sum2 = 0;
    int sum3 = 0;

    for (int i = 0; i < 213; i++) {
       sum1 += pixelArray[l + 0 + i] & 0xFF;
       sum2 += pixelArray[l + 213 + i] & 0xFF;
       sum3 += pixelArray[l + 426 + i] & 0xFF;
    }
if(sum1 < sum2 && sum1 < sum3 ){
    telemetry.addData("Skystone Position:", "Left");
}
else if(sum2 < sum1 && sum2 < sum3){
    telemetry.addData("Skystone Position:", "Center");
}
else if(sum3 <  sum1 && sum3 < sum2){
    telemetry.addData("Skystone Position:", "Right");
                     }
telemetry.update();
                      }
                      
                      
                    }
                    
                }
            }
        }
    }



    /**
     * Initialize the Vuforia localization engine.
     */
   private void initVuforia() {
   VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
   parameters.cameraName = hardwareMap.get(WebcamName.class, "12398Webcam");
   parameters.vuforiaLicenseKey = VUFORIA_KEY;
   parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
   vuforia = ClassFactory.getInstance().createVuforia(parameters);
   Vuforia.setFrameFormat(PIXEL_FORMAT.GRAYSCALE, true);
   vuforia.setFrameQueueCapacity(1);
  
   
}


    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
       tfodParameters.minimumConfidence = 0.2;
       tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
       tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
