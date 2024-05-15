package utils;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class SATCollision {

    // Method to project polygon vertices onto an axis
    private static double[] projectPolygon(Point2D[] vertices, Point2D axis) {
        double min = dotProduct(vertices[0], axis);
        double max = min;
        for (int i = 1; i < vertices.length; i++) {
            double projection =  dotProduct(vertices[i], axis);
            if (projection < min) {
                min = projection;
            } else if (projection > max) {
                max = projection;
            }
        }
        return new double[] { min, max };
    }

    // Method to find the overlap between two projections
    private static double overlap(double[] proj1, double[] proj2) {
        return Math.min(proj1[1], proj2[1]) - Math.max(proj1[0], proj2[0]);
    }

    // Method to check for SAT collision between two polygons
    public static boolean checkCollision(Polygon poly1, Polygon poly2) {
        Point2D[] axes = getAxes(poly1, poly2);
        for (Point2D axis : axes) {
            double[] proj1 = projectPolygon(getVertices(poly1), axis);
            double[] proj2 = projectPolygon(getVertices(poly2), axis);
            if (overlap(proj1, proj2) <= 0) {
                return false;
            }
        }
        return true;
    }

    // Method to get vertices of a polygon
    private static Point2D[] getVertices(Polygon poly) {
        Point2D[] vertices = new Point2D[poly.npoints];
        for (int i = 0; i < poly.npoints; i++) {
            vertices[i] = new Point2D.Double(poly.xpoints[i], poly.ypoints[i]);
        }
        return vertices;
    }

    // Method to get axes for SAT
    private static Point2D[] getAxes(Polygon poly1, Polygon poly2) {
        List<Point2D> axes = new ArrayList<>();
        Point2D[] vertices1 = getVertices(poly1);
        Point2D[] vertices2 = getVertices(poly2);

        // Add axes for poly1
        for (int i = 0; i < vertices1.length; i++) {
            Point2D p1 = vertices1[i];
            Point2D p2 = vertices1[(i + 1) % vertices1.length];
            Point2D edge = new Point2D.Double(p2.getX() - p1.getX(), p2.getY() - p1.getY());
            Point2D normal = new Point2D.Double(-edge.getY(), edge.getX());
            axes.add(normal);
        }

        // Add axes for poly2
        for (int i = 0; i < vertices2.length; i++) {
            Point2D p1 = vertices2[i];
            Point2D p2 = vertices2[(i + 1) % vertices2.length];
            Point2D edge = new Point2D.Double(p2.getX() - p1.getX(), p2.getY() - p1.getY());
            Point2D normal = new Point2D.Double(-edge.getY(), edge.getX());
            axes.add(normal);
        }

        return axes.toArray(new Point2D[0]);
    }
    
    // Utility method for the dot product of two points
    private static double dotProduct(Point2D p1, Point2D p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }
    
    // Converting rectangle to polygon
    public static Polygon rectangleToPolygon(Rectangle rect) {
        int[] xpoints = { rect.x, rect.x + rect.width, rect.x + rect.width, rect.x };
        int[] ypoints = { rect.y, rect.y, rect.y + rect.height, rect.y + rect.height };
        return new Polygon(xpoints, ypoints, 4);
    }

}
