package solarclock.physic;




/**
 *
 * @author durands
 * source:
 * http://www.mathworks.com/matlabcentral/fileexchange/39191-low-precision-ephemeris/content/jupiter.m
 */
public class Neptune extends Planet {
    
    public Neptune() {
        super(Planet.NEPTUNE);
    }
    
    @Override
    public double[] calculateHeliocentricPosAt(double jdate) {
        // fundamental time arguments
        final double 
                atr = Math.PI / 648000.,
                djd = jdate - 2451545.,
                tjd = djd / 36525. + 1.;

        // fundamental trig arguments (radians)
        final double 
                g5 = r2r(0.056531 + 0.00023080893 * djd),
                g6 = r2r(0.882987 + 0.00009294371 * djd),
                l7 = r2r(0.870169 + 0.00003269438 * djd),
                g7 = r2r(0.400589 + 0.00003269438 * djd),
                l8 = r2r(0.846912 + 0.00001672092 * djd),
                g8 = r2r(0.725368 + 0.00001672092 * djd),
                f8 = r2r(0.480856 + 0.00001663715 * djd);

        // heliocentric ecliptic longitude (radians)
        double pl = 3523. * Math.sin(g8) - 50 * Math.sin(2 * f8);
        pl = pl - 43. * tjd * Math.cos(g8) + 29 * Math.sin(g5 - g8);
        pl = pl + 19. * Math.sin(2 * g8) - 18 * Math.cos(g5 - g8);
        pl = pl + 13. * Math.cos(g6 - g8) + 13 * Math.sin(g6 - g8);
        pl = pl - 9. * Math.sin(2 * g7 - 3 * g8) + 9 * Math.cos(2 * g7 - 2 * g8);
        pl = pl - 5. * Math.cos(2 * g7 - 3 * g8) - 4 * tjd * Math.sin(g8);
        pl = pl + 4. * Math.cos(g7 - 2 * g8) + 4 * tjd * tjd * Math.sin(g8);

        double plon = 18. + atr * pl;

        // heliocentric ecliptic latitude (radians)
        double pb = 6404 * Math.sin(f8) + 55 * Math.sin(g8 + f8);
        pb = pb + 55 * Math.sin(g8 - f8) - 33 * tjd * Math.sin(f8);

        double plat = atr * pb;

        // heliocentric distance (kilometers)
        double pr = 30.07175 - 0.25701 * Math.cos(g8);
        pr = pr - 0.00787 * Math.cos(2 * l7 - g7 - 2 * l8);
        pr = pr + 0.00409 * Math.cos(g5 - g8) - 0.00314 * tjd * Math.sin(g8);
        pr = pr + 0.0025 * Math.sin(g5 - g8) - 0.00194 * Math.sin(g6 - g8);
        pr = pr + 0.00185 * Math.cos(g6 - g8);
        
        double r = 149597870.66 * pr; // km

        // heliocentric ecliptic position vector (kilometers)
        return new double[] {plon, plat, r};
    }
    

    
}
