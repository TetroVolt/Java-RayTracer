import java.awt.*;
import java.util.ArrayList;

public class MirrorSphere extends Sphere {
    public MirrorSphere(float radius, Vec3 position)  {
        super(radius, position);
    }

    public float N = 1.3f;


    public Color colorHit(Ray ray, ArrayList<Renderable> renderables, int n_reflections) {
        if (n_reflections <= 0) return Color.BLACK;

        float t = super.touch(ray);
        if (Float.isNaN(t) || t <= 0) return Color.BLACK;

        Vec3 rhat = super.rhat(ray.point(t));
        Ray reflect = ray.reflect(rhat, t);

        float min_dist = Float.MAX_VALUE;
        Color c = Color.black;

        for (Renderable renderable : renderables) {
            if (renderable == this) continue;

            float dist = renderable.touch(reflect);
            if (!Float.isNaN(dist) && dist > 0 &&  dist < min_dist) {
                min_dist = dist;
                if (renderable instanceof LensSphere) {
                    c = ((LensSphere)renderable).colorHit(reflect, renderables, n_reflections-1);
                } else if (renderable instanceof MirrorSphere) {
                    c = ((MirrorSphere)renderable).colorHit(reflect, renderables, n_reflections-1);
                } else {
                    c = renderable.colorHit(reflect);
                }
            }
        }

        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        r = Math.min(r, 8 * (int)(r / (t + min_dist)));
        g = Math.min(g, 8 * (int)(g / (t + min_dist)));
        b = Math.min(b, 8 * (int)(b / (t + min_dist)));

        return new Color(r, g, b);
    }
}
