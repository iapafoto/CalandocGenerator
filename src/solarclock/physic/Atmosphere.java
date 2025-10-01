package solarclock.physic;

//package com.kw.generatecalendarapp.tools;
//
///*
// * Classe permettant de calculer le rayonement solaire a travers l'atmosphere 
// * http://www.scratchapixel.com/lessons/3d-advanced-lessons/simulating-the-colors-of-the-sky/atmospheric-scattering/
// * ex d'utilisations : - couleur de la terre vue de l'espace
// *                     - simulation de couchés de soleil
// */
//
//import java.awt.Color;
//import solarclock.physic.Vector3D;
//
///**
// *
// * @author sebastien.durand
// */
//public class Atmosphere {
//    public static final double RADIUS_EARTH = 6360e3;           // Earth radius
//    public static final Vector3D v0 = new Vector3D(0,0,0);
//    private final int numSamples = 16;
//    private final int numSamplesLight = 8;
//    
//    double radiusPlanet = RADIUS_EARTH;     // Earth radius
//    double radiusAtmosphere = 6420e3;       // Atmosphere radius
//    
//    Vector3D betaR = new Vector3D(5.5e-6, 13.0e-6, 22.4e-6);    // Rayleigh scattering coefficients at sea level
//    Vector3D betaM = new Vector3D(21e-6);                       // Mie scattering coefficients at sea level
//    double Hr = 7994;                                           // Rayleigh scale height
//    double Hm = 1200;                                           // Mie scale height
//    Vector3D sunDirection = new Vector3D(0., 1., 0.);           // Sun direction
//    double sunIntensity = 20;                                   // Sun intensity
//    double g = 0.76;                                            // Mean cosine
//    
//    public Atmosphere(Vector3D sd) {
//        this.sunDirection = sd;
//    }
//    
//    public Atmosphere(Vector3D sd, double re, double ra, double hr, double hm, double si, double g) {
//        this.Hr = hr;
//        this.Hm = hm;
//        this.radiusPlanet = re;
//        this.radiusAtmosphere = ra;
//        this.sunDirection = sd;
//        this.sunIntensity = si;
//        this.g = g;
//    }
//
//    private static class Ray {
//        public double tmin = 0;
//        public double tmax = Double.MAX_VALUE;
//        final Vector3D origine;
//        final Vector3D direction = new Vector3D();
//        Ray(final Vector3D o, final Vector3D dir) {
//            origine = o;
//            Vector3D.normalize(dir, direction); 
//        }
//        Vector3D getPos(final double k) {
//            return new Vector3D(origine.x + k*direction.x,
//                                origine.y + k*direction.y,
//                                origine.z + k*direction.z);
//        }
//    }
//    
//    private static double[] intersectSphere(final Vector3D center, final double radius, final Ray ray) {
//        final double radius2 = radius*radius;
//        Vector3D dist = new Vector3D(center.x - ray.origine.x, center.y - ray.origine.y, center.z - ray.origine.z);
//        final double B = Vector3D.dot(dist, ray.direction);
//        final double D = B*B - Vector3D.dot(dist, dist) + radius2;
//        if (D < 0) return null;
//        final double thc = Math.sqrt(D);
//        return new double[] {B - thc, B + thc};
//    }
//    
//    public Vector3D computeIncidentLight(double altitude, Vector3D dir) {
//        return computeIncidentLight(new Ray(new Vector3D(0, radiusPlanet+altitude, 0), new Vector3D(dir.x,dir.y,dir.z)));
//    }   
//
//    private Vector3D computeIncidentLight(final Ray r) {
//        Vector3D    hrAttenuation = new Vector3D(),
//                    hmAttenuation = new Vector3D();
//
//        double[] intersect = intersectSphere(v0, radiusAtmosphere, r);
//        if (intersect == null || intersect[1] < 0)
//            return new Vector3D(0);
//        
//        double t0 = intersect[0];
//        double t1 = intersect[1];
//        
//        if (t0 > r.tmin && t0 > 0) r.tmin = t0;
//        if (t1 < r.tmax) r.tmax = t1;
//        
//        final double segmentLength = (r.tmax - r.tmin) / numSamples;
//        final Vector3D sumR = new Vector3D(0);
//        final Vector3D sumM = new Vector3D(0);   
//        final Vector3D attenuation = new Vector3D(0);
//        
//        final double  mu = Vector3D.dot(r.direction, sunDirection);
//        final double  phaseR = 3 / (16 * Math.PI) * (1 + mu * mu);
//
//        double opticalDepthR = 0, opticalDepthM = 0;
//        double tCurrent = r.tmin;
//
//        double  phaseM = 3 / (8 * Math.PI) * ((1 - g * g) * (1 + mu * mu))/((2 + g * g) * Math.pow(1 + g * g - 2 * g * mu, 1.5));
//
//        for (int i = 0; i < numSamples; ++i) {
//            final Vector3D samplePosition = r.getPos(tCurrent + 0.5 * segmentLength);
//            final double height = samplePosition.length() - radiusPlanet;
//            // compute optical depth for light
//            final double hr = Math.exp(-height / Hr) * segmentLength;
//            final double hm = Math.exp(-height / Hm) * segmentLength;
//            opticalDepthR += hr;
//            opticalDepthM += hm;
//            // light optical depth
//            final Ray lightRay = new Ray(samplePosition, sunDirection);
//            intersect = intersectSphere(v0, radiusAtmosphere, lightRay);
//            if (intersect != null) {
//                lightRay.tmin = intersect[0];
//                lightRay.tmax = intersect[1];
//                double segmentLengthLight = lightRay.tmax / numSamplesLight, tCurrentLight = 0;
//                double opticalDepthLightR = 0, opticalDepthLightM = 0;
//                int j = 0;
//                for (j = 0; j < numSamplesLight; ++j) {
//                    final Vector3D samplePositionLight = lightRay.getPos(tCurrentLight + 0.5 * segmentLengthLight);
//                    final double heightLight = samplePositionLight.length() - radiusPlanet;
//                    if (heightLight < 0) break;
//                    opticalDepthLightR += Math.exp(-heightLight / Hr) * segmentLengthLight;
//                    opticalDepthLightM += Math.exp(-heightLight / Hm) * segmentLengthLight;
//                    tCurrentLight += segmentLengthLight;
//                }
//                if (j == numSamplesLight) {
//                    final double taux = betaR.x * (opticalDepthR + opticalDepthLightR) + betaM.x * 1.1 * (opticalDepthM + opticalDepthLightM);
//                    final double tauy = betaR.y * (opticalDepthR + opticalDepthLightR) + betaM.y * 1.1 * (opticalDepthM + opticalDepthLightM);
//                    final double tauz = betaR.z * (opticalDepthR + opticalDepthLightR) + betaM.z * 1.1 * (opticalDepthM + opticalDepthLightM);
//                    attenuation.set(Math.exp(-taux), Math.exp(-tauy), Math.exp(-tauz));
//                    Vector3D.mult(attenuation, hr, hrAttenuation);
//                    Vector3D.add(sumR, hrAttenuation, sumR);
//                    Vector3D.mult(attenuation, hm, hmAttenuation);
//                    Vector3D.add(sumM, hmAttenuation, sumM);
//                }
//            }
//            tCurrent += segmentLength;
//        }
//        return new Vector3D(20 * (sumR.x * phaseR * betaR.x + sumM.x * phaseM * betaM.x),
//                        20 * (sumR.y * phaseR * betaR.y + sumM.y * phaseM * betaM.y),
//                        20 * (sumR.z * phaseR * betaR.z + sumM.z * phaseM * betaM.z));
//    }
//
//    public void renderSkydome(final Vector3D sunDirection) {
//        Atmosphere atmosphere = new Atmosphere(sunDirection);
//        final int width = 640, height = 480;
//        Vector3D[] image = new Vector3D[width * height];
//
//        double aspectRatio = width / (double)height;
//        double fov = 65;
//        double angle = Math.tan(fov * Math.PI / 180. * 0.5);
//        int numPixelSamples = 4;
//        for (int y = 0; y < height; ++y) {
//            for (int x = 0; x < width; ++x) {
//                for (int m = 0; m < numPixelSamples; ++m) {
//                    for (int n = 0; n < numPixelSamples; ++n) {
//                        double rayx = (2 * (x + (m + Math.random()) / numPixelSamples) / (double)(width) - 1) * aspectRatio * angle;
//                        double rayy = (1 - (y + (n + Math.random()) / numPixelSamples) / (double)(height)*2) * angle;
//                        Vector3D rayDirection = new Vector3D(rayx, rayy, -1, true);
//                        Ray ray = new Ray(new Vector3D(0, atmosphere.radiusPlanet + 1000, 30000), rayDirection);
//                        double intersect[] = intersectSphere(v0, atmosphere.radiusPlanet, ray);
//                        if (intersect != null && intersect[1] > 0)
//                            ray.tmax = Math.max(0., intersect[0]);
//                        final Vector3D light = atmosphere.computeIncidentLight(ray);
//                        image[y*width+height].x += light.x;
//                        image[y*width+height].y += light.y;
//                        image[y*width+height].z += light.z;
//                    }
//                }
//                image[y*width+height].x *= 1. / (numPixelSamples * numPixelSamples);
//                image[y*width+height].y *= 1. / (numPixelSamples * numPixelSamples);
//                image[y*width+height].z *= 1. / (numPixelSamples * numPixelSamples);
//            }
//        }
//        // Save as PPM
//    }
//    
//
//    public static Color[] renderSkydome(final Vector3D sunDirection, int width, int height) {
//       // clock_t t = clock();
//        final Atmosphere atmosphere = new Atmosphere(sunDirection);
//        final Color[] image = new Color[width * height];
//      //  memset(image, 0x0, sizeof(Vec3<T>) * width * height);
//        for (int j = 0; j < height; ++j) {
//            double y = 2 * (j + 0.5) / (double)(height - 1) - 1;
//            for (int i = 0; i < width; ++i) {
//                final double x = 2 * (i + 0.5) / (double)(width - 1) - 1;
//                final double z2 = x * x + y * y; 
//                if (z2 <= 1) {
//                    final double phi = Math.atan2(y, x);
//                    final double theta = Math.acos(1 - z2);
//                    final Vector3D dir = new Vector3D(Math.sin(theta) * Math.cos(phi), Math.cos(theta), Math.sin(theta) * Math.sin(phi));
//                    // 1 meter above sea level
//                    final Vector3D val = atmosphere.computeIncidentLight(1, dir);
////                    if (val.x != 0 || val.y != 0 ||val.z != 0) {
////                        int test = 1;
////                    }
//                    image[j*width + i] = new Color( Math.min(Math.max(0, (int)(255.*val.x)),255),
//                                                    Math.min(Math.max(0, (int)(255.*val.y)),255),
//                                                    Math.min(Math.max(0, (int)(255.*val.z)),255));
//                } else {
//                    image[j*width + i] = Color.black;
//                }
//            }
//        }
//        return image;
//    }
//    
//    /**
//     * 
//     * @param hSun hauteur angulaire en degres
//     * @return 
//     */
//     public static Color getColorFromSpace(final double hSun) {
//       // clock_t t = clock();
//         double a = (hSun)*Math.PI/180.;  // On triche pour couvrir mieux la palette
//         Vector3D sunDirection = new Vector3D(-Math.cos(a), Math.sin(a),0);
//         Vector3D dir = new Vector3D(-Math.cos(Math.PI/12)/**Math.cos(Math.PI/8)*/, -Math.sin(Math.PI/12),0/*Math.sin(Math.PI/8)*/); //sunDirection; //new Vector3D(0.,-1.,0.);
//        final Atmosphere atmosphere = new Atmosphere(sunDirection);
//        double k = 1;//atmosphere.radiusPlanet - atmosphere.radiusAtmosphere;
//        final Vector3D lookAt = new Vector3D(0, atmosphere.radiusAtmosphere+1, 0);
//        final Vector3D origine = new Vector3D(lookAt.x - k*dir.x, lookAt.y - k*dir.y, lookAt.z - k*dir.z);
//        Ray ray = new Ray(lookAt, dir);
//        double intersect[] = intersectSphere(v0, atmosphere.radiusPlanet, ray);
//        if (intersect != null && intersect[1] > 0)
//            ray.tmax = Math.max(0., intersect[0]);
//        final Vector3D val = atmosphere.computeIncidentLight(ray);
//        return new Color( Math.min(Math.max(0, (int)(255.*val.x)),255),
//                                   Math.min(Math.max(0, (int)(255.*val.y)),255),
//                                   Math.min(Math.max(0, (int)(255.*val.z)),255));
//    }
//};
// 
//
