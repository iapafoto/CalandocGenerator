/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solarclock.calendar;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author durands
 */
public class SimpleSvgTools {
    
    /**
     * Create the "d=" part of svg
     * @param shape
     * @return 
     */
    public static String toSvgPath(final Shape shape) {
        if (shape == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        final PathIterator pi = shape.getPathIterator(null);
        final double[] coords = new double[6];
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    sb.append(" M ")
                            .append(format(coords[0])).append(" ").append(format(coords[1]));
                    break;
                case PathIterator.SEG_CLOSE:
                    sb.append(" Z");
                    break;
                case PathIterator.SEG_LINETO:
                    sb.append(" L ")
                            .append(format(coords[0])).append(" ").append(format(coords[1]));
                    break;
                case PathIterator.SEG_QUADTO:
                    sb.append(" Q ")
                            .append(format(coords[0])).append(" ").append(format(coords[1])).append(", ")
                            .append(format(coords[2])).append(" ").append(format(coords[3]));
                    break;
                case PathIterator.SEG_CUBICTO:
                    sb.append(" C ")
                            .append(format(coords[0])).append(" ").append(format(coords[1])).append(", ")
                            .append(format(coords[2])).append(" ").append(format(coords[3])).append(", ")
                            .append(format(coords[4])).append(" ").append(format(coords[5]));
                    break;
                default:
                    break;
            }
            pi.next();
        }
        String txt =  sb.toString();
        return txt.isEmpty() ? txt : txt.substring(1);
    }
    
    private static String format(double val) {
        if (val == (int)val) {
            return String.format("%d", (int)val);
        }
        return String.format(Locale.US,"%.2f", val);
    }
    /**
     * Parse the "d=" part of svg
     * @param svgPath
     * @return 
     */
    public static Path2D svgPathToPath2D(final String svgPath) {
        final Matcher matchPathCmd = Pattern.compile("([MmLlHhVvAaQqTtCcSsZz])|([-+]?((\\d*\\.\\d+)|(\\d+))([eE][-+]?\\d+)?)").matcher(svgPath);

        //Tokenize
        LinkedList<String> tokens = new LinkedList<>();
        while (matchPathCmd.find()) {
            tokens.addLast(matchPathCmd.group());
        }
        Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD);
        char curCmd = 'Z';
        double x1 = 0, y1 = 0, x2 = 0, y2 = 0, lastX = 0, lastY = 0,  firstX = 0, firstY =0;
        while (!tokens.isEmpty()) {
            String curToken = tokens.removeFirst();
            char initChar = curToken.charAt(0);
            if ((initChar >= 'A' && initChar <= 'Z') || (initChar >= 'a' && initChar <= 'z')) {
                curCmd = initChar;
            } else {
                tokens.addFirst(curToken);
            }

            switch (curCmd) {
                case 'M':
                    lastX = nextDouble(tokens);
                    lastY = nextDouble(tokens);
                    path.moveTo(lastX, lastY);
                    firstX =lastX;
                    firstY =lastY;
                    curCmd = 'L';
                    break;
                case 'm':
                    lastX += nextDouble(tokens);
                    lastY += nextDouble(tokens);
                    path.moveTo(lastX, lastY);
                    firstX =lastX;
                    firstY =lastY;
                    curCmd = 'l';
                    break;
                case 'L':
                    lastX = lastY = 0;
                    // fall into 
                case 'l':
                    lastX += nextDouble(tokens);
                    lastY += nextDouble(tokens);
                    path.lineTo(lastX, lastY);
                    break;
                case 'H':
                    lastX = 0;
                    // fall into 
                case 'h':
                    lastX += nextDouble(tokens);
                    path.lineTo(lastX, lastY);
                    break;
                case 'V':
                    lastY = 0;
                case 'v':
                    lastY += nextDouble(tokens);
                    path.lineTo(lastX, lastY);
                    break;
                case 'A':
                    // fall into 
                case 'a':
                    break;
                case 'Q':
                    lastX = lastY = 0;
                    // fall into 
                case 'q':
                    x1 = lastX + nextDouble(tokens);
                    y1 = lastY + nextDouble(tokens);
                    lastX += nextDouble(tokens);
                    lastY += nextDouble(tokens);
                    path.quadTo(x1, y1, lastX, lastY);
                    break;
                case 'C':
                    lastX = lastY = 0;
                    // fall into 
                case 'c':
                    x1 = lastX + nextDouble(tokens);
                    y1 = lastY + nextDouble(tokens);
                    x2 = lastX + nextDouble(tokens);
                    y2 = lastY + nextDouble(tokens);
                    lastX += nextDouble(tokens);
                    lastY += nextDouble(tokens);
                    path.curveTo(x1, y1, x2, y2, lastX, lastY);
                    break;
                case 'T':
                    x1 = lastX + lastX - x1;
                    y1 = lastY + lastY - y1;
                    lastX = nextDouble(tokens);
                    lastY = nextDouble(tokens);
                    path.quadTo(x1, y1, lastX, lastY);
                    break;
                case 't':
                    x1 = lastX + lastX - x1;
                    y1 = lastY + lastY - y1;
                    lastX += nextDouble(tokens);
                    lastY += nextDouble(tokens);
                    path.quadTo(x1, y1, lastX, lastY);
                    break;
                case 'S':
                    x1 = lastX + lastX - x2;
                    y1 = lastX + lastX - y2;
                    x2 = nextDouble(tokens);
                    y2 = nextDouble(tokens);
                    lastX = nextDouble(tokens);
                    lastY = nextDouble(tokens);
                    path.curveTo(x1, y1, x2, y2, lastX, lastY);
                    break;
                case 's':
                    x1 = lastX + lastX - x2;
                    y1 = lastX + lastX - y2;
                    x2 = lastX + nextDouble(tokens);
                    y2 = lastY + nextDouble(tokens);
                    lastX += nextDouble(tokens);
                    lastY += nextDouble(tokens);
                    path.curveTo(x1, y1, x2, y2, lastX, lastY);
                    break;
                case 'Z':
                    // fall into 
                case 'z':
                    path.closePath();
                    lastX = firstX;
                    lastY = firstY;
                    break;
                default:
                    throw new RuntimeException("Invalid path element");
            }
        }
        return path;
    }

    static protected double nextDouble(LinkedList<String> l) {
        String s = l.removeFirst();
        return Double.parseDouble(s);
    }
        

}
