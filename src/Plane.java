import java.awt.Color;

/**
 *  Infinite plane
 */
public class Plane extends Renderable {
    private Vec3 normal;

    public Plane() {
        origin = new Vec3();
        normal = new Vec3(0f, 0f, 1f);
        color = Color.RED;
    }

    public Plane(Vec3 normal, Vec3 origin, Color color) {
        if (normal.mag() == 0) throw new IllegalArgumentException("must have non zero normal");
        this.normal = new Vec3(normal).unit();
        this.origin = new Vec3(origin);
        this.color = color;
    }

    public Vec3 getNormal() {
        return normal;
    }
    @Override
    public Color colorHit(Ray ray) {
        float t = touch(ray);
        if (Float.isNaN(t)) return color.BLACK;

        Vec3 point = ray.point(t);
        int x = (int)Math.ceil(point.getX()), y = (int)Math.ceil(point.getY());
        x = Math.abs(x);
        y = Math.abs(y);
        Color c = Color.WHITE;
        if ((x%2 + y%2) == 1) {
            c = Color.BLACK;
        }

        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();

        red = Math.min(red, 6*(int)(red / (t)));
        green = Math.min(green, 6*(int)(green / (t)));
        blue = Math.min(blue, 6*(int)(blue / (t)));

        //return color;
        return new Color(red, green, blue);
    }

    @Override
    public float touch(Ray ray) {
        float ndot = normal.dot(ray.getDirection());
        if (ndot == 0f) return Float.NaN;
        float t = origin.sub(ray.getOrigin()).dot(normal) / ndot;
        if (t > 0) return t;
        return Float.NaN;
    }

    @Override
    public boolean hit(Ray ray) {
        float dot = normal.dot(ray.getDirection());
        if (dot >= 0f) return false;
        return true;
    }

}
