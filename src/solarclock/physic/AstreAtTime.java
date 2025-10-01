package solarclock.physic;

import org.joda.time.DateTime;


public abstract class AstreAtTime {
    public static final double  EARTH_MEAN_RADIUS = 6371., // Km
                                pi2 = 2.*Math.PI,
                                toRad = Math.PI/180.,
                                toDeg = 180./Math.PI;
                                
    public double GMST, julianDay, 
                  rightAscention,
                  declination,  // in degres
                  sin_dec, cos_dec,
                  distance;

    public static double toJulianDay(final DateTime date) {
        return 2440587.5 + (date.getMillis()/ 86400000.0);
    }
    
    public final static double UA_IN_KM = 149597870.7;
    
    public static double kmToUA(double km) {
        return km/UA_IN_KM;
    }

    public static String deg2dms(double deg) {
        deg = Math.abs(deg);
        int d = (int) deg;
        int m = (int) ((deg - d) * 60);
        double s = ((deg - d - (m / 60.)) * 3600.);
//        if (m < 0)
//            m *= -1;
//        if (s < 0)
//            s *= -1;
        return (d>0 ? Integer.toString(d) + "°" : "") + 
               (d>0 || m>0 ? Integer.toString(m) + "'" : "") +
               (d>0 || m>0 || s>0 ? Double.toString(s) + "\"" : "0°");
    }
    
    public AstreAtTime(long timeUnixMs) {
        this(new DateTime(timeUnixMs));
    }
    
    public AstreAtTime(final DateTime date) {
        this((double)toJulianDay(date));
    }
    
    public AstreAtTime(final double jd) {
        julianDay = jd;
        double t  = julianDay-2451545.;
        double t0 = t/36525.;    // Nombre de siecles depuis 2000
        GMST = 280.46061837 + 360.98564736629*t + t0*t0*(0.000387933 - t0/38710000.); // Greenwich Mean Sidereal Time    
        initConstants();
        calculatePlanetPosition();
    }
    
    abstract void calculatePlanetPosition();
    
    final public double getRightAscention() {    
        return rightAscention;
    }

    final public double getDeclination() {    
        return declination;
    }

    final public double getDiss() {    
        return distance;
    }

    final public double[] getAzimuthElevation(final double latitude, final double longitude) {    
        final double 
            sin_lat = Math.sin(toRad*latitude),
            cos_lat = Math.cos(toRad*latitude);
        
        double lmst = (GMST + longitude)/15.; // degres vers horaire
        lmst = lmst %24;    
        if (lmst<0) lmst += 24.;
        lmst = lmst*15.*toRad; // Horaire vers Radian
        double ha = lmst - rightAscention;
        
        double elevation = Math.asin(sin_lat * sin_dec + cos_lat * cos_dec * Math.cos(ha)),
               azimuth   = Math.acos((sin_dec - (sin_lat*Math.sin(elevation))) / (cos_lat*Math.cos(elevation)));
        
        if (Math.sin(ha) > 0) azimuth = 2.*Math.PI - azimuth;

        return new double[] {toDeg*azimuth, toDeg*elevation};
    }
    
    final public double getElevation(final double latitude, final double longitude) {    
        return toDeg*Math.asin(Math.sin(toRad*latitude) * sin_dec + Math.cos(toRad*latitude) * cos_dec * Math.cos(toRad*(longitude + GMST) - rightAscention));
    }

    final public double getAzimuth(final double latitude, final double longitude) {             
        final double elevationRad = toRad*getElevation(latitude, longitude);
        return toDeg*Math.acos(sin_dec - Math.sin(toRad*latitude)*Math.sin(elevationRad) / Math.cos(toRad*latitude)*Math.cos(elevationRad));
    }

    
    protected double[] C;
    
    private void initConstants() {
        double t  = julianDay-2451545.;  // nombre de jours depuis le 01/01/2000 (1 January 2000 = 2451545 Julian Days)

        final double // Les planetes ont des influances les unes sur les autres => pour avoir quelques chose de precis on tiens compte de toute les planetes
              // Moon
              Lm = 0.606434+0.03660110129*t,
              Gm = 0.374897+0.03629164709*t, 
              Fm = 0.259091+0.03674819520*t, 
              D =  0.827362+0.03386319198*t,
              gm = 0.347343-0.00014709391*t,
              // Sun
              Ls = 0.779072+0.00273790931*t,
              Gs = 0.993126+0.00273777850*t, 
              // Mercury        
              L1 = 0.700695+0.01136771400*t, 
              G1 = 0.485541+0.01136759566*t,
              F1 = 0.566441+0.01136762384*t,
              // Venus        
              L2 = 0.505498+0.00445046867*t,
              G2 = 0.140023+0.00445036173*t,
              F2 = 0.292498+0.00445040017*t,
              // Mars
              L4 = 0.987573+0.00145575328*t,
              G4 = 0.053856+0.00145561327*t,
              F4 = 0.849694+0.00145569456*t,
              // Jupiter
              L5 = 0.089608+0.00023080893*t,
              G5 = 0.056531+0.00023080893*t,
              F5 = 0.814794+0.00023080893*t,
              // Saturn
              L6 = 0.133295+0.00009294371*t,
              G6 = 0.882987+0.00009294371*t,
              F6 = 0.821218+0.00009294371*t,
              // Uranus
              L7 = 0.870169+0.00003269438*t,
              G7 = 0.400589+0.00003269438*t,
              F7 = 0.664614+0.00003265562*t,
              // Neptune
              L8 = 0.846912+0.00001672092*t,
              G8 = 0.725368+0.00001672092*t,
              F8 = 0.480856+0.00001663715*t,
              // Pluto
              L9 = 0.663854+0.00001115482*t,
              G9 = 0.041020+0.00001104864*t,
              F9 = 0.357355+0.00001104864*t;
        
        C = new double[] {0.,  // Juste pour garder des indices cooerent avec le doc
            Lm, Gm, Fm, D, gm,  // 1 2 3 4 5 
            0., Ls, Gs, // (6) 7 8 
            L1, G1, F1, // 9 10 11
            L2, G2, F2, // 12 13 14
            L4, G4, F4, // 15 16 17
            L5, G5, F5, // 18 19 20
            L6, G6, F6, // 21 22 23
            L7, G7, F7, // 24 25 26
            L8, G8, F8, // 27 28 29
            0.,         // (30!) ???
            L9, G9, F9  // 31 32 33
        };
        
        for (int i=1; i<C.length; i++) {
            C[i] = (C[i] - Math.floor(C[i]))* pi2;
        }
    }
    
    protected void coords(double v, double u, double w, double xl) {
        double w1 = w / Math.sqrt(u-v*v);
        double v1 = v / Math.sqrt(u);
        // this should never happen
        if (Math.abs(w1) > 1.0) w1 = (w1<0.0) ? -1.0 : 1.0; 
        if (Math.abs(v1) > 1.0) v1 = (v1<0.0) ? -1.0 : 1.0; 
 /////////////////////////
  //      rightAscention = xl + Math.asin(w1);
  //      declination = Math.asin(v1);
//        if (rightAscention < 0.0) rightAscention = rightAscention+pi2;
     //   rightAscention = rightAscention*24.0/pi2;
     //   declination = declination*toDeg;
 /////////////////////////     
        rightAscention = xl + Math.atan(w1/Math.sqrt(1.-w1*w1));
        declination = Math.atan(v1/Math.sqrt(1.-v*v));
        // For small optim
        sin_dec = Math.sin(declination);
        cos_dec = Math.cos(declination);
    }
}

