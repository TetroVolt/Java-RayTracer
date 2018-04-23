import java.awt.Color;

public abstract class Renderable {
    protected Color color;
    protected Vec3 origin;

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) { this.color = color; }
    public Vec3 getOrigin() {
        return origin;
    }
    public void getOrigin(Vec3 origin) { this.origin = origin; }

    public abstract boolean hit(Ray ray);
    public abstract float touch(Ray ray);

    public abstract Color colorHit(Ray ray);

}
