import java.awt.*;
import java.util.ArrayList;


public class LensSphere extends MirrorSphere {
    public LensSphere(float radius, Vec3 position)  {
        super(radius, position);
    }

    public Color colorHit(Ray ray, ArrayList<Renderable> renderables, int n_reflections) {
        if (n_reflections <= 0) return Color.BLACK;

        float t1 = super.touch(ray);
        if (Float.isNaN(t1) || t1 <= 0) return Color.BLACK;

        Vec3 rhat = super.rhat(ray.point(t1));
        Ray refract = ray.refract(rhat, t1, 1f, 1.1f);

        refract = new Ray(refract.point(0.01f), refract.getDirection());
        float t2 = super.intersect(refract)[1];
        if (Float.isNaN(t2) || t2 <= 0) return Color.BLACK;
        rhat = super.rhat(refract.point(t2));
        refract = refract.refract(rhat, t2, 1.1f, 1f);

        float min_dist = Float.MAX_VALUE;
        Color c = Color.black;

        for (Renderable renderable : renderables) {
            if (renderable == this) continue;

            float dist = renderable.touch(refract);
            if (!Float.isNaN(dist) && dist < min_dist) {
                min_dist = dist;
                if (renderable instanceof LensSphere) {
                    c = ((LensSphere) renderable).colorHit(refract, renderables, n_reflections-1);
                } else if (renderable instanceof MirrorSphere) {
                    c = ((MirrorSphere)renderable).colorHit(refract, renderables, n_reflections-1);
                } else {
                    c = renderable.colorHit(refract);
                }
            }
        }

        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        r = Math.min(r, 8 * (int)(r / (t1+t2)));
        g = Math.min(g, 8 * (int)(g / (t1+t2)));
        b = Math.min(b, 8 * (int)(b / (t1+t2)));

        return new Color(r, g, b);
    }
}
