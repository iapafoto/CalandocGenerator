package solarclock.physic;

/**
 *
 * @author durands
 */
public class Mercury extends Planet {
    
    public Mercury() {
        super(Planet.MERCURY);
    }
    
    @Override
    public double[] calculateHeliocentricPosAt(final double jdate) {
        // fundamental time arguments
        final double 
                atr = Math.PI / 648000.,
                djd = jdate - 2451545.,
                t = djd / 36525. + 1.;
        
// fundamental trig arguments (radians)
        final double
            l1 = r2r(0.700695 + 0.011367714 * djd),
            g1 = r2r(0.485541 + 0.01136759566 * djd),
            f1 = r2r(0.566441 + 0.01136762384 * djd),
            g2 = r2r(0.140023 + 0.00445036173 * djd);

        // heliocentric ecliptic longitude (radians)

        double pl = 84378 * Math.sin(g1) + 10733 * Math.sin(2 * g1);
        pl = pl + 1892 * Math.sin(3 * g1) - 646 * Math.sin(2 * f1);
        pl = pl + 381 * Math.sin(4 * g1) - 306 * Math.sin(g1 - 2 * f1);
        pl = pl - 274 * Math.sin(g1 + 2 * f1) - 92 * Math.sin(2 * g1 + 2 * f1);
        pl = pl + 83 * Math.sin(5 * g1) - 28 * Math.sin(3 * g1 + 2 * f1);
        pl = pl + 25 * Math.sin(2 * g1 - 2 * f1) + 19 * Math.sin(6 * g1);
        pl = pl - 9 * Math.sin(4 * g1 + 2 * f1) + 8 * t * Math.sin(g1);
        pl = pl + 7 * Math.cos(2 * g1 - 5 * g2);

        double plon = l1 + atr * pl;

        // heliocentric ecliptic latitude (radians)

        double pb = 24134 * Math.sin(f1) + 5180 * Math.sin(g1 - f1);
        pb = pb + 4910 * Math.sin(g1 + f1) + 1124 * Math.sin(2 * g1 + f1);
        pb = pb + 271 * Math.sin(3 * g1 + f1) + 132 * Math.sin(2 * g1 - f1);
        pb = pb + 67 * Math.sin(4 * g1 + f1) + 18 * Math.sin(3 * g1 - f1);
        pb = pb + 17 * Math.sin(5 * g1 + f1) - 10 * Math.sin(3 * f1);
        pb = pb - 9 * Math.sin(g1 - 3 * f1);

        double plat = atr * pb;

        // heliocentric distance (kilometers)

        double pr = 0.39528 - 0.07834 * Math.cos(g1) - 0.00795 * Math.cos(2 * g1);
        pr = pr - 0.00121 * Math.cos(3 * g1) - 0.00022 * Math.cos(4 * g1);

        double r = 149597870.691 * pr;    

// heliocentric ecliptic position vector (kilometers)
        return new double[] {plon, plat, r};
    }
}
