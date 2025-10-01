package solarclock.physic;

import solarclock.physic.Point3D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author durands
 */
public class Mars extends Planet {
    
    public Mars() {
        super(Planet.MARS);
    }

    @Override
    public double[] calculateHeliocentricPosAt(double jdate) {
        double atr = Math.PI / 648000.;

        // fundamental time arguments

        final double 
                djd = jdate - 2451545,
                t = djd / 36525. + 1.;
        
// fundamental trig arguments (radians)
        final double
            gs = r2r(0.993126 + 0.0027377785 * djd),
            g2 = r2r(0.140023 + 0.00445036173 * djd),
            g4 = r2r(0.053856 + 0.00145561327 * djd),
            g5 = r2r(0.056531 + 0.00023080893 * djd),
            f4 = r2r(0.849694 + 0.00145569465 * djd),
            l4 = r2r(0.987353 + 0.00145575328 * djd);

// heliocentric ecliptic longitude (radians)
        double pl = 38451 * Math.sin(g4) + 2238 * Math.sin(2 * g4) + 181 * Math.sin(3 * g4);
        pl = pl - 52 * Math.sin(2 * f4) + 37 * t * Math.sin(g4) - 22 * Math.cos(g4 - 2 * g5);
        pl = pl - 19 * Math.sin(g4 - g5) + 17 * Math.cos(g4 - g5) + 17 * Math.sin(4 * g4);
        pl = pl - 16 * Math.cos(2 * g4 - 2 * g5) + 13 * Math.cos(gs - 2 * g4);
        pl = pl - 10 * Math.sin(g4 - 2 * f4) - 10 * Math.sin(g4 + 2 * f4);
        pl = pl + 7 * Math.cos(gs - g4) - 7 * Math.cos(2 * gs - 3 * g4);
        pl = pl - 5 * Math.sin(g2 - 3 * g4) - 5 * Math.sin(gs - g4) - 5 * Math.sin(gs - 2 * g4);
        pl = pl - 4 * Math.cos(2 * gs - 4 * g4) + 4 * t * Math.sin(2 * g4) + 4 * Math.cos(g5);
        pl = pl + 3 * Math.cos(g2 - 3 * g4) + 3 * Math.sin(2 * g4 - 2 * g5);
   
        double plon = l4 + atr * pl;

// heliocentric ecliptic latitude (radians)
        double plat = 6603 * Math.sin(f4) + 622 * Math.sin(g4 - f4) + 615 * Math.sin(g4 + f4);
        plat = atr * (plat + 64 * Math.sin(2 * g4 + f4));

// heliocentric distance (kilometers)
        double r = 1.53031 - 0.1417 * Math.cos(g4) - 0.0066 * Math.cos(2 * g4);
        r = 149597870.691 * (r - 0.00047 * Math.cos(3 * g4));

// heliocentric ecliptic position vector (kilometers)
        return new double[] {plon, plat, r};
    }
    
    
}
