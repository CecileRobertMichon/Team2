/**
 * Auto Generated Java Class.
 */
public class LauncherPositioning {
  //requires (10, 10) in block units is (0, 0) for the launcher
  
   private Odometer odometer;
   private Robot robot;
   private double FIRING_DISTANCE = 1.00;
   private int SECTION_DIVIDER = 11; 
   private int SECTION;
   
   public LauncherPositioning(Odometer odometer) {
     this.odometer = odometer;
     this.robot = new Robot();
     
     if (TARGET_ONE_X < SECTION_DIVIDER 
           && TARGET_ONE_Y > SECTION_DIVIDER) {
       SECTION = 1;
     }
     else if (TARGET_ONE_X > SECTION_DIVIDER 
                && TARGET_ONE_Y > SECTION_DIVIDER) {
       SECTION = 2;
     }
     else {
       SECTION = 3;
     }
   }
   
   public static void targetAquisition(int x1, int y1, int x2, int y2) {
     travelToFiringPosition(x1, y1);
     lineUp(x1, y1);
     //fire command;
     travelToFiringPosition(x2, y2);
     lineUp(x2, y2);
     //fire command;
   }
   
   //to move forwards and backwards until target is perfectly in optimal range
                                       
   
   public static void lineUp(int x_target, int y_target) { 
     int x, y;
     x = odometer.getX();
     y = odometer.getY();
     int target_distance = sqrt( math.pow((x-x_target),2) + math.pow((y-y_target),2) );
     int discrepancy = target_distance - FIRING_DISTANCE;
     
     if (discrepancy > 0) {
       //move closer to target
       Navigation.goBackward(discrepancy);
     }
     else {
       //move further away from target
       Navigation.goForward(discrepancy);
     }
       
   }
   
   
   //given the coordinates of the target, travel to optimal area
   //distances given with respect to original origin
   public static void travelToFiringPosition(int x, int y) {
     if (SECTION == 1) {
       travelTo(9*TILE_LENGTH, 10.5*TILE_LENGTH);
     }
     else if (SECTION == 2) {
       travelTo(10.5*TILE_LENGTH, 10.5*TILE_LENGTH);
     }
     else {
       travelTo(10.5*TILE_LENGTH, 9*TILE_LENGTH);
     }
   }
}
