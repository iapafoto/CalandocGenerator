/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solarclock.calendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import solarclock.physic.AstreAtTime;
import solarclock.physic.Planet;


/**
 *
 * @author sebastien.durand
 */
public class Astronomie {
    
    public static void main(String[] args) {
        // A comparer avec
        // https://pgj.pagesperso-orange.fr/position-planetes.htm 
        
        DateTime now  =DateTime.now(DateTimeZone.UTC);
        for (int i=0; i<Planet.PLUTO; i++) {
            Planet planet = Planet.getPlanet(i);
            double diam = planet.getApparentDiametreAt(now);
            double distanceUA = planet.getDistanceUAAt(now);
            
            System.out.println(planet.toString() + " - dist=" +  distanceUA + "UA - d=" + AstreAtTime.deg2dms(diam));
        }

    }
}
