
import java.awt.Color;

public class Sphere extends Renderable {
    private Color color;
    private float radius;

    public Sphere(float radius, Vec3 position) {
        this.radius = radius;
        this.origin = position;
        this.color = Color.RED;
    }

    public Sphere(float radius, Color color, Vec3 position) {
        this.radius = radius;
        this.color = color;
        this.origin = position;
    }

    @Override
    public boolean hit(Ray ray) {
        // uncomment if you allow hits from inside the sphere
        // if (ray.getOrigin().sub(position).mag() <= radius) return true;

        Vec3 OP = origin.sub(ray.getOrigin()); // vector pointing from ray origin to sphere position
        float dotprod = OP.dot(ray.getDirection());
        if (dotprod <= 0) return false;

        Vec3 proj = ray.getDirection().scale(dotprod);
        Vec3 OPproj = proj.sub(OP); // perp

        return OPproj.mag() <= radius;
    }

    public float[] intersect(Ray ray) {
        float[] ret = new float[2];

        Vec3 CO = ray.getOrigin().sub(this.origin);
        float base = (ray.getDirection().dot(CO));
        float disc = base*base - CO.dot(CO) + radius*radius;
        ret[0] = ret[1] = (float)Math.sqrt(disc);
        ret[0] = -base + ret[0];
        ret[1] = -base - ret[0];

        return ret;
    }

    public float touch(Ray ray) {
        if (ray.getOrigin().sub(getOrigin()).mag() <= radius) return Float.NaN;
        float[] T = intersect(ray);
        if (Float.isNaN(T[0])) return Float.NaN;
        float mins = Math.min(T[0], T[1]);
        float maxs = Math.max(T[0], T[1]);
        if (mins > 0) return mins;
        if (maxs > 0) return maxs;
        return Float.NaN;
    }

    public Vec3 rhat(Vec3 point) {
        return point.sub(origin);
    }

    @Override
    public Color colorHit(Ray ray) {
        float t = touch(ray);
        if (!hit(ray) || Float.isNaN(t)) return Color.black;

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        red = Math.min(red, 6*(int)(color.getRed() / (t)));
        green = Math.min(green, 6*(int)(color.getGreen() / (t)));
        blue = Math.min(blue, 6*(int)(color.getBlue() / (t)));

        //return color;
        return new Color(red, green, blue);
    }

    public Color getColor() {
        return color;
    }

    public float getRadius() { return radius; }

}
