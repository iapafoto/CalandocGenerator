package solarclock.physic;



/**
 *
 * @author durands
 */
public class Venus extends Planet {
    
    public Venus() {
        super(Planet.VENUS);
    }
    
    @Override
    public double[] calculateHeliocentricPosAt(double jdate) {
        // fundamental time arguments
        final double 
                atr = Math.PI / 648000.,
                djd = jdate - 2451545.,
                t = djd / 36525. + 1.;
        
        // fundamental trig arguments (radians)
        final double
            gs = r2r(0.993126 + 0.0027377785 * djd),
            l2 = r2r(0.505498 + 0.00445046867 * djd),
            g2 = r2r(0.140023 + 0.00445036173 * djd),
            f2 = r2r(0.292498 + 0.00445040017 * djd);

        // heliocentric ecliptic longitude (radians)
        double pl = 2814. * Math.sin(g2) - 181. * Math.sin(2. * f2);
        pl = pl - 20. * t * Math.sin(g2) + 12. * Math.sin(2. * g2);
        pl = pl - 10. * Math.cos(2. * gs - 2. * g2) + 7. * Math.cos(3. * gs - 3. * g2);
        
        double plon = l2 + atr * pl;

        // heliocentric ecliptic latitude (radians)
        double plat = 12215. * Math.sin(f2) + 83. * Math.sin(g2 + f2) + 83. * Math.sin(g2 - f2);
        plat = atr * plat;

        // heliocentric distance (kilometers)
        double r = 149597870.691 * (0.72335 - 0.00493 * Math.cos(g2));

        // heliocentric ecliptic position vector (kilometers)
        return new double[] {plon, plat, r};
    }
}
