package solarclock.physic;

import org.joda.time.DateTime;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// calcul des positions + chnagement de repere 
// http://www2.arnes.si/~gljsentvid10/tutorial_.html#11

/**
 *
 * @author durands
 */
public abstract class Planet {
    public static final int 
            MERCURY = 0,
            VENUS = 1,
            EARTH = 2,
            MARS = 3,
            JUPITER = 4,
            SATURN = 5,
            URANUS = 6,
            NEPTUNE = 7,    
            PLUTO = 8;
    
    private final static int 
            SECOND = 1000,
            MINUTE = 60*SECOND,
            HOUR = 60*MINUTE,
            DAY = 24*HOUR;
    private static final double YEAR = 365.25*DAY;
    
    public static final String[] planetNames = {"Mercure","Vénus","Terre","Mars","Jupiter","Saturne","Uranus","Neptune","Pluton"};
    public static final int[] planetNbSatellites = {0, 0, 1, 2, 65, 62, 27, 13, 3};
    public static final double[] grandAxeUA = {0.3870983, 0.7233298, 1.0000010, 1.5236793, 5.202603, 9.554909, 19.21845, 30.11039, 39.54470}; 
    public static final double[] grandAxeMillionKm = {57.909083, 108.2086, 149.5980, 227.9392, 778.2983, 1429.394, 2875.039, 4504.450, 5915.803};
    public static final double[] orbitExcentricity = {0.20563, 0.00677,	0.01671, 0.09340, 0.04850, 0.05555, 0.04638, 0.00946, 0.2490};
    public static final double[] diameterEquatorRatioEarth = { 0.3825, 0.9488, 1, 0.5326, 11.2089, 9.4335, 4.0073, 3.8826, 0.1874 };
    public static final double[] diameterEquatorKm = {4879.4, 12103.6, 12756.28, 6794, 142984, 120536, 51118, 49528, 2390 };
    public static final double[] aplatissement = { 0., 0., 1./298.257,	 1./154. , 1./15.4, 1./10.2, 1./44., 1./59., 0};
    public static final double[] volumeRatioEarth = {0.056, 0.85, 1, 0.15, 1317, 757, 62.9, 57.5, 0.007};
    public static final double[] masseKg = {3.3018e10+23, 4.8685e10+24, 5.9736e10+24, 6.4185e10+23, 1.8986e10+27, 5.6846e10+26,	8.6831e10+25, 1.0243e10+26, 1.238e10+22};
    public static final double[] masseRatioSun = {1./6023600, 1./408523.71, 1./332946.05, 1./3098708, 1./1047.5654, 1./3498.77, 1./22905.35, 1./19416.3, 1.56e10-8};
    public static final double[] gravityOnSurfaceRatioEarth = {0.38, 0.90, 1, 0.38, 2.53, 1.07, 0.90, 1.14, 0.06}; 
    public static final double[] revolutionPeriod = { 87.9693*DAY, 224.701*DAY, 365.256*DAY, YEAR+321.730*DAY, 11*YEAR+314.84*DAY, 29*YEAR+ 166.98*DAY, 84*YEAR+7.48*DAY, 164*YEAR+281.3*DAY, 247*YEAR+362*DAY}; 
    public static final double[] rotationPeriod = { 58.65*DAY, 243.02*DAY, 23.935*HOUR, 24.62*HOUR, 9.92*HOUR, 10.66*HOUR, 17.24*HOUR, 16.11*HOUR, 6.39*DAY};

    protected int planetPosition; 
    
    protected Planet(int pos) {
        planetPosition = pos;
    }
    
    public static Planet getPlanet(int i) {
        switch (i) {
            case MERCURY: return new Mercury();
            case VENUS: return new Venus();
            case EARTH: return new Earth();
            case MARS: return new Mars();
            case JUPITER: return new Jupiter();
            case SATURN: return new Saturn();
            case URANUS: return new Uranus();
            case NEPTUNE: return new Neptune();
       //     case PLUTO: return new Pluto();
        }
        return null;
    }
    
    @Override
    public String toString() {
        switch (planetPosition) {
            case MERCURY: return "Mercury";
            case VENUS: return "Venus";
            case EARTH: return "Earth";
            case MARS: return "Mars";
            case JUPITER: return "Jupiter";
            case SATURN: return "Saturn";
            case URANUS: return "Uranus";
            case NEPTUNE: return "Neptune";
            case PLUTO: return "Pluto";
        }
        return "";
    }
    
    public double getDiametreKm() {
        return diameterEquatorKm[planetPosition];
    } 
    
    public abstract double[] calculateHeliocentricPosAt(final double jdate);
    
    public double[] calculateHeliocentricPosAt(final DateTime dateUTC) {
        return calculateHeliocentricPosAt(AstreAtTime.toJulianDay(dateUTC));
    }
    
    public Point3D calculateVectorPosAt(final DateTime dateUTC) {
        double[] lngLatDist = calculateHeliocentricPosAt(AstreAtTime.toJulianDay(dateUTC));
                // heliocentric ecliptic position vector (kilometers)
        return new Point3D(
                lngLatDist[2] * Math.cos(lngLatDist[1]) * Math.cos(lngLatDist[0]),
                lngLatDist[2] * Math.cos(lngLatDist[1]) * Math.sin(lngLatDist[0]),
                lngLatDist[2] * Math.sin(lngLatDist[1]));
    }
    
    /** distance a la planete en UA
     * @param dateUTC
     * @return  **/
    public double getDistanceUAAt(DateTime dateUTC) {
        Point3D pte = earth.calculateVectorPosAt(dateUTC);
        Point3D pt = calculateVectorPosAt(dateUTC);
        double distanceKm = Math.sqrt((pt.x - pte.x) * (pt.x - pte.x) + (pt.y - pte.y) * (pt.y - pte.y) + (pt.z - pte.z) * (pt.z - pte.z));
        return AstreAtTime.kmToUA(distanceKm);
    //    double[] pt = calculateHeliocentricPosAt(AstreAtTime.toJulianDay(dateUTC));
    //    return AstreAtTime.kmToUA(pt[2]);
    }
    
    static Earth earth = new Earth();
    
    public double getApparentDiametreAt(DateTime dateUTC) {
        double distanceUA = getDistanceUAAt(dateUTC);
        double diametreKm = getDiametreKm();
        return Math.toDegrees(Math.atan(diametreKm/distanceUA));
    }
    
    
//    public Path3D getHeliocentricOrbit() {
//        final double period = revolutionPeriod[planetPosition];
//        final long
//            startTime = (new Date()).getTime(),
//            endTime = startTime + (long)period,
//            dt = (endTime-startTime)/380;
//
//        final Path3D orbit = new Path3D();
//        orbit.moveTo(calculateHeliocentricPosAt(JulianDay.convertToJulianDay(startTime)));
//        for (long t=startTime+dt; t<endTime; t+=dt) {
//            orbit.lineTo(calculateHeliocentricPosAt(JulianDay.convertToJulianDay(t)));
//        }
//        orbit.closePath();
//        
//        return orbit;
//    }
//
//    public static List<Path3D> getHeliocentricOrbits3D() {
//        final List<Path3D> orbits3D = new ArrayList<Path3D>();
//        orbits3D.add(new Mercury().getHeliocentricOrbit());
//        orbits3D.add(new Venus().getHeliocentricOrbit());
//        orbits3D.add(new Earth().getHeliocentricOrbit());
//        orbits3D.add(new Mars().getHeliocentricOrbit());
//        orbits3D.add(new Jupiter().getHeliocentricOrbit());
//        orbits3D.add(new Saturn().getHeliocentricOrbit());
//        orbits3D.add(new Uranus().getHeliocentricOrbit());
//        orbits3D.add(new Neptune().getHeliocentricOrbit());
//        return orbits3D;
//    }

//    public static List<Path2D> getHeliocentricOrbits2D() {
//        // Determinaison du point de vue dans l'espace (a faire en statique au final une fois qu'on a un bon reglage)
//        final Planet refPlanet = new Earth();
//        final double period = revolutionPeriod[refPlanet.planetPosition];
//        final long
//                t0 = (new Date()).getTime(),
//                t1 = t0 + (long)period/4;
//        final Point3D 
//                p0 = refPlanet.calculateHeliocentricPosAt(JulianDay.convertToJulianDay(t0)),
//                p1 = refPlanet.calculateHeliocentricPosAt(JulianDay.convertToJulianDay(t1));
//        final Vector3D 
//                v0 = new Vector3D(p0, new Point3D(0,0,0), true),
//                v1 = new Vector3D(p1, new Point3D(0,0,0), true),
//                vn = Vector3D.cross(v0, v1, new Vector3D());  // Vecteur perpendiculaire au 
//        //final double distance = v0.length()*2.;
//        final List<Path3D> lstOrbits = getHeliocentricOrbits3D();
//        return Geometry3D.path3DtoScreen(new Point3D(vn.x, vn.y, vn.z), new Point3D(0,0,0), v1, lstOrbits, Double.MAX_VALUE);
//    }
    
    public static double r2r (double x) {
        return 2.0 *Math.PI * (x - ((x<0)?(Math.floor(x)+1):Math.floor(x)));  // 2.0 * PI * (x - (fix)(x)); <= MathLab
    }

}
