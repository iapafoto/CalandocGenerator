package solarclock.physic;


public class Saturn extends Planet {
    
    public Saturn() {
        super(Planet.SATURN);
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
                g5 = r2r(0.056531 + 0.00023080893 * djd),
                l6 = r2r(0.133295 + 0.00009294371 * djd),
                g6 = r2r(0.882987 + 0.00009294371 * djd),
                g7 = r2r(0.400589 + 0.00003269438 * djd);

// heliocentric ecliptic longitude (radians)

        double pl = 
                 23045 * Math.sin(g6) + 5014 * t - 2689 * Math.cos(2 * g5 - 5 * g6) + 2507
                + 1177 * Math.sin(2 * g5 - 5 * g6) - 826 * Math.cos(2 * g5 - 4 * g6)
                + 802 * Math.sin(2 * g6) + 425 * Math.sin(g5 - 2 * g6)
                - 229 * t * Math.cos(g6) - 153 * Math.cos(2 * g5 - 6 * g6)
                - 142 * t * Math.sin(g6) - 114 * Math.cos(g6)
                + 101 * t * Math.sin(2 * g5 - 5 * g6) - 70 * Math.cos(2 * l6)
                + 67 * Math.sin(2 * l6) + 66 * Math.sin(2 * g5 - 6 * g6)
                + 60 * t * Math.cos(2 * g5 - 5 * g6) + 41 * Math.sin(g5 - 3 * g6)
                + 39 * Math.sin(3 * g6) + 31 * Math.sin(g5 - g6)
                + 31 * Math.sin(2 * g5 - 2 * g6) - 29 * Math.cos(2 * g5 - 3 * g6)
                - 28 * Math.sin(2 * g5 - 6 * g6 + 3 * g7) + 28 * Math.cos(g5 - 3 * g6)
                + 22 * t * Math.sin(2 * g5 - 4 * g6) - 22 * Math.sin(g6 - 3 * g7)
                + 20 * Math.sin(2 * g5 - 3 * g6) + 20 * Math.cos(4 * g5 - 10 * g6)
                + 19 * Math.cos(2 * g6 - 3 * g7) + 19 * Math.sin(4 * g5 - 10 * g6)
                - 17 * t * Math.cos(2 * g6) - 16 * Math.cos(g6 - 3 * g7)
                - 12 * Math.sin(2 * g5 - 4 * g6) + 12 * Math.cos(g5)
                - 12 * Math.sin(2 * g6 - 2 * g7) - 11 * t * Math.sin(2 * g6)
                - 11 * Math.cos(2 * g5 - 7 * g6) + 10 * Math.sin(2 * g6 - 3 * g7)
                + 10 * Math.cos(2 * g5 - 2 * g6) + 9 * Math.sin(4 * g5 - 9 * g6)
                - 8 * Math.sin(g6 - 2 * g7) - 8 * Math.cos(2 * l6 + g6)
                + 8 * Math.cos(2 * l6 - g6) + 8 * Math.cos(g6 - g7)
                - 8 * Math.sin(2 * l6 - g6) + 7 * Math.sin(2 * l6 + g6)
                - 7 * Math.cos(g5 - 2 * g6) - 7 * Math.cos(2 * g6)
                - 6 * t * Math.sin(4 * g5 - 10 * g6) + 6 * Math.cos(4 * g5 - 10 * g6) * t
                + 6 * t * Math.sin(2 * g5 - 6 * g6) - 5 * Math.sin(3 * g5 - 7 * g6)
                - 5 * Math.cos(3 * g5 - 3 * g6) - 5 * Math.cos(2 * g6 - 2 * g7)
                + 5 * Math.sin(3 * g5 - 4 * g6) + 5 * Math.sin(2 * g5 - 7 * g6)
                + 4 * Math.sin(3 * g5 - 3 * g6) + 4 * Math.sin(3 * g5 - 5 * g6)
                + 4 * Math.cos(g5 - 2 * g6) * t + 3 * t * Math.cos(2 * g5 - 4 * g6)
                + 3 * Math.cos(2 * g5 - 6 * g6 + 3 * g7) - 3 * t * Math.sin(2 * l6)
                + 3 * Math.cos(2 * g5 - 6 * g6) * t - 3 * t * Math.cos(2 * l6)
                + 3 * Math.cos(3 * g5 - 7 * g6) + 3 * Math.cos(4 * g5 - 9 * g6)
                + 3 * Math.sin(3 * g5 - 6 * g6) + 3 * Math.sin(2 * g5 - g6)
                + 3 * Math.sin(g5 - 4 * g6) + 2 * Math.cos(3 * g6 - 3 * g7)
                + 2 * t * Math.sin(g5 - 2 * g6) + 2 * Math.sin(4 * g6)
                - 2 * Math.cos(3 * g5 - 4 * g6) - 2 * Math.cos(2 * g5 - g6)
                - 2 * Math.sin(2 * g5 - 7 * g6 + 3 * g7) + 2 * Math.cos(g5 - 4 * g6)
                + 2 * Math.cos(4 * g5 - 11 * g6) - 2 * Math.sin(g6 - g7);

        double plon = l6 + atr * pl;
 
// heliocentric ecliptic latitude (radians)

        double plat = 
                 8297 * Math.sin(g6) - 3346 * Math.cos(g6) + 462 * Math.sin(2 * g6)
                - 189 * Math.cos(2 * g6) + 185 + 79 * t * Math.cos(g6)
                - 71 * Math.cos(2 * g5 - 4 * g6) + 46 * Math.sin(2 * g5 - 6 * g6)
                - 45 * Math.cos(2 * g5 - 6 * g6) + 29 * Math.sin(3 * g6)
                - 20 * Math.cos(2 * g5 - 3 * g6) + 18 * t * Math.sin(g6)
                - 14 * Math.cos(2 * g5 - 5 * g6) - 11 * Math.cos(3 * g6) - 10 * t
                + 9 * Math.sin(g5 - 3 * g6) + 8 * Math.sin(g5 - g6)
                - 6 * Math.sin(2 * g5 - 3 * g6) + 5 * Math.sin(2 * g5 - 7 * g6)
                - 5 * Math.cos(2 * g5 - 7 * g6) + 4 * Math.sin(2 * g5 - 5 * g6)
                - 4 * Math.sin(2 * g6) * t - 3 * Math.cos(g5 - g6)
                + 3 * Math.cos(g5 - 3 * g6) + 3 * t * Math.sin(2 * g5 - 4 * g6)
                + 3 * Math.sin(g5 - 2 * g6) + 2 * Math.sin(4 * g6)
                - 2 * Math.cos(2 * g5 - 2 * g6);
        
        plat *= atr;

// heliocentric distance (kilometers)
        double r = 9.55774
                - 0.53252 * Math.cos(g6) - 0.01878 * Math.sin(2 * g5 - 4 * g6)
                - 0.01482 * Math.cos(2 * g6) + 0.00817 * Math.sin(g5 - g6)
                - 0.00539 * Math.cos(g5 - 2 * g6) - 0.00524 * t * Math.sin(g6)
                + 0.00349 * Math.sin(2 * g5 - 5 * g6) + 0.00347 * Math.sin(2 * g5 - 6 * g6)
                + 0.00328 * t * Math.cos(g6) - 0.00225 * Math.sin(g6)
                + 0.00149 * Math.cos(2 * g5 - 6 * g6) - 0.00126 * Math.cos(2 * g5 - 2 * g6)
                + 0.00104 * Math.cos(g5 - g6) + 0.00101 * Math.cos(2 * g5 - 5 * g6)
                + 0.00098 * Math.cos(g5 - 3 * g6) - 0.00073 * Math.cos(2 * g5 - 3 * g6)
                - 0.00062 * Math.cos(3 * g6) + 0.00042 * Math.sin(2 * g6 - 3 * g7)
                + 0.00041 * Math.sin(2 * g5 - 2 * g6) - 0.0004 * Math.sin(g5 - 3 * g6)
                + 0.0004 * Math.cos(2 * g5 - 4 * g6) - 0.00028 * t - 0.00023 * Math.sin(g5);
         
        r = 149597870.691 * (r + 0.0002 * Math.sin(2 * g5 - 7 * g6));

// heliocentric ecliptic position vector (kilometers)
return new double[] {plon, plat, r};
    }
}
