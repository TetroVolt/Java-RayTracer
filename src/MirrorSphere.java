import java.awt.*;
import java.util.ArrayList;

public class MirrorSphere extends Sphere {
    public MirrorSphere(float radius, Vec3 position)  {
        super(radius, position);
    }

    public Color colorHit(Ray ray, ArrayList<Renderable> renderables) {
        float t = super.touch(ray);
        if (Float.isNaN(t)) return Color.BLACK;

        Vec3 rhat = super.rhat(ray.point(t));
        Ray reflect = ray.reflect(rhat, t);

        float min_dist = Float.MAX_VALUE;
        Color c = Color.black;

        for (Renderable renderable : renderables) {
            if (renderable == this) continue;

            float dist = renderable.touch(reflect);
            if (!Float.isNaN(dist) && dist < min_dist) {
                min_dist = dist;
                c = renderable.colorHit(reflect);
            }
        }

        return c;
    }
}
