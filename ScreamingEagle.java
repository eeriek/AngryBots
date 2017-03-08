package saxion;

import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

/**
 * Created by Matthijs on 15-2-2017.
 */
public class ScreamingEagle extends TeamRobot {

    /**
     * TO DO:
     * REMOVE +/- 0.4 and CHANGE it with a variable based on the distance and speed of the scanned enemy.
     * ADD wall detection and change drive direction when to close to a wall or when wall is hit.
     * ADD fire from far distance based on speed and distance of enemy (it now only fires when in close range.
     * ADD detector detecting if driving in the same line. If so. Change direction to avoid enemy fire damage.
     */

    /**
     * Run method. Executes on startup.
     */
    public void run() {
        while(true) {
            //Keep scanning with the radar to detect enemies
            turnRadarRightRadians(Double.POSITIVE_INFINITY);
        }
    }

    /**
     * When an enemy robot is scanned:
     * Track it
     * Go to it
     * Keep a distance of 140 units
     * FIre at the enemy with the fire system based on distance to the enemy. The closer the stronger
     * @param e the event detecting a robot is scanned.
     */
    public void onScannedRobot(ScannedRobotEvent e) {

        //When an enemy is scanned. Lock the radar on the enemy tank by getting its position and adjusting the radar towards that posistion.
        double radarTurn= getHeadingRadians()+e.getBearingRadians()-getRadarHeadingRadians();
        setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));

        //Get the absolute bearing of the enemy tank.
        double absoluteBearing=getHeadingRadians()+e.getBearingRadians();

        //If the nemy tank is driving in a positive direction. Adjust the gun with a positive of +0.4.
        if(e.getBearing() > 0) {
            setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(absoluteBearing - getGunHeadingRadians()) + 0.4);
        } else { //If it does not drive in a positive direction, it drives in a negative direction. So adjust gun with a negative of -0.4
            setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(absoluteBearing - getGunHeadingRadians()) - 0.4);
        }

        //Turn the vehicle of the tank towards the enemy tank
        setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absoluteBearing - getHeadingRadians()));

        //Drive towards the enemy, but keep a distance of 140 units. When to close to the enemy tank. This method will make sure it drives backwards. So it does not get to close.
        setAhead(e.getDistance() - 140);

        //Execute all the set commands
        execute();
        while(getDistanceRemaining() > 0 && getTurnRemaining() > 0) {
            execute();
        }

        //Fire system using distance to decide which firepower is used.
        if(e.getDistance() < 150) {
            if(e.getDistance() < 80) {
                setFire(5);
            } else if(e.getDistance() < 100) {
                setFire(4);
            } else if(e.getDistance() < 120) {
                setFire(3);
            } else if(e.getDistance() < 140) {
                setFire(2);
            } else {
                setFire(1);
            }
        }
    }

}
