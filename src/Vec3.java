public class Vec3 {
    private float x, y, z;

    public Vec3(){
        x = y = z = 0f;
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(Vec3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3 scale(float scalar) {
        return new Vec3(x * scalar, y * scalar, z * scalar);
    }

    public Vec3 add(Vec3 vec, Vec3 ... vecs) {
        float X = x + vec.x, Y = y + vec.y, Z = z + vec.z;
        for (Vec3 v : vecs) {
            X += v.x;
            Y += v.y;
            Z += v.z;
        }
        return new Vec3(X,Y,Z);
    }

    public Vec3 sub(Vec3 vec, Vec3 ... vecs) {
        float X = x - vec.x, Y = y - vec.y, Z = z - vec.z;
        for (Vec3 v : vecs) {
            X -= v.x;
            Y -= v.y;
            Z -= v.z;
        }
        return new Vec3(X,Y,Z);
    }

    public Vec3 proj(Vec3 B) {
        return B.scale(dot(B) / B.dot(B));
    }

    public Vec3 negate() {
        return new Vec3(-x, -y, -z);
    }

    public float dot(Vec3 vec) {
        return (x * vec.x) + (y * vec.y) + (z * vec.z);
    }

    public Vec3 unit() {
        float mag = this.mag();
        return new Vec3(x / mag, y / mag, z / mag);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    public boolean isZero() { return x == 0 && y == 0 && z == 0; }

    public float mag() {
        return (float)(Math.sqrt(x * x + y * y + z * z));
    }

    public Vec3 cross(Vec3 vec) {
        Vec3 ret = new Vec3();
        ret.x = (y * vec.z - z * vec.y);
        ret.y = -(x * vec.z - z * vec.x);
        ret.z = (x * vec.y - y * vec.x);
        return ret;
    }

    public String toString() {
        return String.format("Vec3(%.2f, %.2f, %.2f)", x, y, z);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Vec3) {
            Vec3 v = (Vec3)obj;
            return v.x == x && v.y == y && v.z == z;
        }
        return false;
    }
}
