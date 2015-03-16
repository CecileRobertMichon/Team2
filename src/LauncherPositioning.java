/*
 *  Team 2
 *  Cecile Robert-Michon 260552816
 *  Even Wang - 260633630
 *  Derek Yu - 260570997
 *  Ajan Ahmed - 260509046
 *  Georges Assouad - 260567730
 *  Chaohan Wang - 260516712
 */

public class LauncherPositioning {
 // requires (10, 10) in block units is (0, 0) for the launcher

 private Odometer odometer;
 private Robot robot;
 private Navigation nav;
 private int section;

 public LauncherPositioning(Odometer odometer, Navigation nav) {
  this.odometer = odometer;
  this.robot = new Robot();
  this.nav = nav;

  if (robot.TARGET_ONE_X < robot.SECTION_DIVIDER
    && robot.TARGET_ONE_Y > robot.SECTION_DIVIDER) {
   section = 1;
  } else if (robot.TARGET_ONE_X > robot.SECTION_DIVIDER
    && robot.TARGET_ONE_Y > robot.SECTION_DIVIDER) {
   section = 2;
  } else {
   section = 3;
  }
 }

 public void targetAcquisition(int x1, int y1, int x2, int y2) {
  travelToFiringPosition(x1, y1);
  lineUp(x1, y1);
  // fire command
  travelToFiringPosition(x2, y2);
  lineUp(x2, y2);
  // fire command
  // return to normal position
  nav.travelTo(10 * robot.TILE_LENGTH, 10 * robot.TILE_LENGTH);
 }

 // to move forwards and backwards until target is perfectly in optimal range

 private void lineUp(int x_target, int y_target) {
  double x, y;
  x = odometer.getX();
  y = odometer.getY();
  double target_distance = Math.sqrt(Math.pow((x - x_target), 2)
    + Math.pow((y - y_target), 2));
  double discrepancy = target_distance - robot.FIRING_DISTANCE;

  if (discrepancy > 0) {
   // move closer to target
   nav.goForward(discrepancy);
  } else {
   // move further away from target
   nav.goBackward(discrepancy);
  }

 }

 // given the coordinates of the target, travel to optimal area
 // distances given with respect to original origin
 private void travelToFiringPosition(int x, int y) {
  if (section == 1) {
   nav.travelTo(9 * robot.TILE_LENGTH, 10.5 * robot.TILE_LENGTH);
  } else if (section == 2) {
   nav.travelTo(10.5 * robot.TILE_LENGTH, 10.5 * robot.TILE_LENGTH);
  } else {
   nav.travelTo(10.5 * robot.TILE_LENGTH, 9 * robot.TILE_LENGTH);
  }
 }
}
