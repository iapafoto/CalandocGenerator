package solarclock.physic;



/**
 *
 * @author durands
 * source mathlab:
 * http://www.mathworks.com/matlabcentral/fileexchange/39191-low-precision-ephemeris/content/earth.m
 */
public class Earth extends Planet {

    public Earth() {
        super(Planet.EARTH);
    }
    
// true-of-date heliocentric, ecliptic
// position vector of Mars
// input : jdate = julian day
// output : position vector of mars (kilometers)
    @Override
    public double[] calculateHeliocentricPosAt(final double jdate) {
        double atr = Math.PI / 648000.;

        // fundamental time arguments
        final double 
                djd = jdate - 2451545.,
                t = djd / 36525. + 1.;
        
// fundamental trig arguments (radians)
        final double
            gs = r2r(0.993126 + 0.0027377785 * djd),
            lm = r2r(0.606434 + 0.03660110129 * djd),
            ls = r2r(0.779072 + 0.00273790931 * djd),
            g2 = r2r(0.140023 + 0.00445036173 * djd),
            g4 = r2r(0.053856 + 0.00145561327 * djd),
            g5 = r2r(0.056531 + 0.00023080893 * djd);

// heliocentric ecliptic longitude (radians)
        double pl = 6910. * Math.sin(gs) + 72. * Math.sin(2. * gs) - 17. * t * Math.sin(gs);
        pl = pl - 7. * Math.cos(gs - g5) + 6. * Math.sin(lm - ls);
        pl = pl + 5. * Math.sin(4. * gs - 8. * g4 + 3. * g5);
        pl = pl - 5. * Math.cos(2. * (gs - g2));
        pl = pl - 4. * Math.sin(gs - g2) + 4. * Math.cos(4. * gs - 8. * g4 + 3. * g5);
        pl = pl + 3. * Math.sin(2 * (gs - g2)) - 3 * Math.sin(g5) - 3. * Math.sin(2 * (gs - g5));

        double plon = ls + atr * pl;

// heliocentric ecliptic latitude (radians)
        //double plat = 6603 * Math.sin(f4) + 622 * Math.sin(g4 - f4) + 615 * Math.sin(g4 + f4);
        //plat = atr * (plat + 64 * Math.sin(2 * g4 + f4));
        double plat = 0;
// heliocentric distance (kilometers)
        //double r = 1.53031 - 0.1417 * Math.cos(g4) - 0.0066 * Math.cos(2 * g4);
        //r = 149597870.691 * (r - 0.00047 * Math.cos(3 * g4));
        double r = 149597870.691 * (1.00014 - 0.01675 * Math.cos(gs) - 0.00014 * Math.cos(2 * gs));
        
// heliocentric ecliptic position vector (kilometers)
        // negatif car en fait on a calculé la position du soleil par rapport a la terre et pas l'inverse
        return new double[] {plon, plat, -r};
    }

}
