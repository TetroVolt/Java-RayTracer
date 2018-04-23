
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

public class PinholeCamera {
    private BufferedImage image;
    private Vec3 position;
    private Vec3 direction;
    private Vec3 UP;
    private Vec3 RIGHT;

    private int FOV;
    private int width, height;

    public void move_right(float t) {
        position = position.add(RIGHT.scale(t));
    }
    public void move_up(float t) {
        position = position.add(UP.scale(t));
    }
    public void move_forward(float t) {
        position = position.add(direction.scale(t));
    }

    public void space_default() {
        direction = new Vec3(1f, 0f, 0f);
        UP = new Vec3(0f, 0f, 1f);
        RIGHT = direction.cross(UP);
        position = new Vec3(0f, 0f, 2f);
    }

    public void pitch(float t) {
        float sin = (float)Math.sin(t);
        float cos = (float)Math.cos(t);
        direction = direction.scale(cos).add(UP.scale(-sin)).unit();
        UP = direction.scale(sin).add(UP.scale(cos)).unit();
    }

    public void yaw(float t) {
        float sin = (float)Math.sin(t);
        float cos = (float)Math.cos(t);
        direction = direction.scale(cos).add(RIGHT.scale(sin)).unit();
        RIGHT = direction.scale(-sin).add(RIGHT.scale(cos)).unit();
    }

    public void roll(float t) {

    }

    public PinholeCamera(int width, int height, Vec3 position, Vec3 direction, Vec3 UP) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
        FOV = 70;

        this.position = position;
        this.direction = direction.unit();
        this.UP = UP.unit();

        if (direction.dot(UP) != 0 || direction.isZero() || UP.isZero())
            throw new IllegalArgumentException("Direction and UP must be perpendicular and non-zero");

        this.RIGHT = direction.cross(UP).unit();
    }

    public void render_perspective(ArrayList<Renderable> renderables) {

        float canvas_distance = (float)(1.0 / Math.tan( Math.toRadians(FOV) / 2 ));
        Vec3 canvas_center = position.add(direction.scale(-canvas_distance));

        float pixel_len = 2f / width;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float UPScale = -(y - height / 2) * pixel_len;
                float RIGHTScale = (x - width / 2) * pixel_len;

                Vec3 P = canvas_center.sub(
                        UP.scale(UPScale),
                        RIGHT.scale(RIGHTScale)
                );
                Vec3 D = position.sub(P);
                Ray ray = new Ray(position, D);

                float min_dist = Float.MAX_VALUE;
                Color color = Color.black;

                for (Renderable renderable : renderables) {
                    float dist = renderable.touch(ray);
                    if (!Float.isNaN(dist) && dist < min_dist) {
                        min_dist = dist;
                        if (renderable instanceof MirrorSphere) {
                            color = ((MirrorSphere)renderable).colorHit(ray, renderables, 3);
                        } else {
                            color = renderable.colorHit(ray);
                        }
                    }
                }

                //image.setRGB(width - x - 1, y, color.getRGB());
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    public void render_projection(ArrayList<Renderable> renderables) {
        float pixel_len = 2f / width;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float UPscale = (y - height / 2) * pixel_len;
                float RIGHTscale = (x - width / 2) * pixel_len;
                Vec3 P = position.add(
                        UP.scale(UPscale),
                        RIGHT.scale(RIGHTscale)
                );
                Vec3 D = direction;
                Ray ray  = new Ray(P, D);
                for (Renderable renderable : renderables) {
                   if (renderable.hit(ray)) {
                       image.setRGB(x, y, renderable.getColor().getRGB());
                   } else {
                       image.setRGB(x, y, Color.black.getRGB());
                   }
                }
            }
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
