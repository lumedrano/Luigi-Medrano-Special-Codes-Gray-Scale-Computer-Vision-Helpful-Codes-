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



@Autonomous(name = "NewTensorFlow", group = "Concept")
@Disabled
public class PatrickGrayScaleTensorFlow extends LinearOpMode{
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
Frame frame = null;
Image image = null;

    
   
    private static final String VUFORIA_KEY =
            "AUUmFe7/////AAABmfrACDmLpkwPoZBcpjPAgD81Dd54BbEwE+bWlxUPFMMkELMLgNDtnLZm+XzK22G9gFzhzAFWBxFKtcRD3QkU/SrRc6nIQOMLVN+TgY7oL0V7/a5zdjQs7YFaxwGbrrYtvfThBqLBVq7fHOYdiLb24HaGxQuX6mwbRYZfV9vVRH8WtHhsPTi3J/tX4IG+oiyT8H22KqLfIy5ab0R1FJ2CyrvVYsJ7ogjKCz7lkf6BUMkVn7D/8QXUSoVfkoV4Vf5E966V7YfuXsLjQ1AhvcQuDnrXzh0EE9C/U+Ckux4WYTx7kTpmu5z80gQVqwdLQC6UTXVhvz3+ImkWr08GxXk+5m20Futx7YgxbyFYrNJXiBb2\n";


    private VuforiaLocalizer vuforia;
    
   
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
