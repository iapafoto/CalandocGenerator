/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarclock.physic;

import solarclock.physic.Point3D;



/**
 *
 * @author durands
 * source mathlab:
 * http://www.mathworks.com/matlabcentral/fileexchange/39191-low-precision-ephemeris/content/jupiter.m
 */
public class Jupiter extends Planet {
    
    public Jupiter() {
        super(Planet.JUPITER);
    }
    
    
    @Override
    public double[] calculateHeliocentricPosAt(double jdate) {
        
        // fundamental time arguments
        final double 
                atr = Math.PI / 648000.,
                djd = jdate - 2451545.,  // nb days since 2000
                t = djd / 36525. + 1.;   // nb century since 2000 + 1

        // fundamental trig arguments (radians)
        final double 
                l5 = r2r(0.089608 + 0.00023080893 * djd),
                g5 = r2r(0.056531 + 0.00023080893 * djd),
                g6 = r2r(0.882987 + 0.00009294371 * djd),
                g7 = r2r(0.400589 + 0.00003269438 * djd);

        // heliocentric ecliptic longitude (radians)
        final double 
                pl = 19934 * Math.sin(g5) + 5023 * t + 2511 + 1093 * Math.cos(2 * g5 - 5 * g6)
                    + 601 * Math.sin(2 * g5) - 479 * Math.sin(2 * g5 - 5 * g6)
                    - 185 * Math.sin(2 * g5 - 2 * g6) + 137 * Math.sin(3 * g5 - 5 * g6)
                    - 131 * Math.sin(g5 - 2 * g6) + 79 * Math.cos(g5 - g6)
                    - 76 * Math.cos(2 * g5 - 2 * g6) - 74 * t * Math.cos(g5)
                    + 68 * t * Math.sin(g5) + 66 * Math.cos(2 * g5 - 3 * g6)
                    + 63 * Math.cos(3 * g5 - 5 * g6) + 53 * Math.cos(g5 - 5 * g6)
                    + 49 * Math.sin(2 * g5 - 3 * g6) - 43 * t * Math.sin(2 * g5 - 5 * g6)
                    - 37 * Math.cos(g5) + 25 * Math.sin(2 * l5) + 25 * Math.sin(3 * g5)
                    - 23 * Math.sin(g5 - 5 * g6) - 19 * t * Math.cos(2 * g5 - 5 * g6)
                    + 17 * Math.cos(2 * g5 - 4 * g6) + 17 * Math.cos(3 * g5 - 3 * g6)
                    - 14 * Math.sin(g5 - g6) - 13 * Math.sin(3 * g5 - 4 * g6)
                    - 9 * Math.cos(2 * l5) + 9 * Math.cos(g6) - 9 * Math.sin(g6)
                    - 9 * Math.sin(3 * g5 - 2 * g6) + 9 * Math.sin(4 * g5 - 5 * g6)
                    + 9 * Math.sin(2 * g5 - 6 * g6 + 3 * g7) - 8 * Math.cos(4 * g5 - 10 * g6)
                    + 7 * Math.cos(3 * g5 - 4 * g6) - 7 * Math.cos(g5 - 3 * g6)
                    - 7 * Math.sin(4 * g5 - 10 * g6) - 7 * Math.sin(g5 - 3 * g6)
                    + 6 * Math.cos(4 * g5 - 5 * g6) - 6 * Math.sin(3 * g5 - 3 * g6)
                    + 5 * Math.cos(2 * g6) - 4 * Math.sin(4 * g5 - 4 * g6)
                    - 4 * Math.cos(3 * g6) + 4 * Math.cos(2 * g5 - g6)
                    - 4 * Math.cos(3 * g5 - 2 * g6) - 4 * t * Math.cos(2 * g5)
                    + 3 * t * Math.sin(2 * g5) + 3 * Math.cos(5 * g6)
                    + 3 * Math.cos(5 * g5 - 10 * g6) + 3 * Math.sin(2 * g6)
                    - 2 * Math.sin(2 * l5 - g5) + 2. * Math.sin(2 * l5 + g5)
                    - 2 * t * Math.sin(3 * g5 - 5 * g6) - 2. * t * Math.sin(g5 - 5 * g6);

        double plon = l5 + atr * pl;

        // heliocentric ecliptic latitude (radians)
        double plat = 
                -4692 * Math.cos(g5) + 259 * Math.sin(g5) + 227 - 227 * Math.cos(2 * g5)
                + 30 * t * Math.sin(g5) + 21 * t * Math.cos(g5)
                + 16 * Math.sin(3 * g5 - 5 * g6) - 13 * Math.sin(g5 - 5 * g6)
                - 12 * Math.cos(3 * g5) + 12 * Math.sin(2 * g5)
                + 7 * Math.cos(3 * g5 - 5 * g6) - 5 * Math.cos(g5 - 5 * g6);
        plat = atr * plat;

        // heliocentric distance (kilometers)
        double r = 
             5.20883 - 0.25122 * Math.cos(g5) - 0.00604 * Math.cos(2 * g5)
           + 0.0026 * Math.cos(2 * g5 - 2 * g6) - 0.0017 * Math.cos(3 * g5 - 5 * g6)
           - 0.00106 * Math.sin(2 * g5 - 2 * g6) - 0.00091 * t * Math.sin(g5)
           - 0.00084 * t * Math.cos(g5) + .00069 * Math.sin(2 * g5 - 3 * g6)
           - 0.00067 * Math.sin(g5 - 5 * g6) + 0.00066 * Math.sin(3 * g5 - 5 * g6)
           + 0.00063 * Math.sin(g5 - g6) - 0.00051 * Math.cos(2 * g5 - 3 * g6)
           - 0.00046 * Math.sin(g5) - 0.00029 * Math.cos(g5 - 5 * g6)
           + 0.00027 * Math.cos(g5 - 2 * g6) - 0.00022 * Math.cos(3 * g5);
        
        r = 149597870.691 * (r - 0.00021 * Math.sin(2 * g5 - 5 * g6));
        
        return new double[] {plon, plat, r};
    }
}
