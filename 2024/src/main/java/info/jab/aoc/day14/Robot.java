package info.jab.aoc.day14;

// Robot record to store position and velocity
record Robot(int x, int y, int vx, int vy) {

    // Parse a single robot's data from input string
    static Robot of(String line) {
        String[] parts = line.split(" ");
        String[] position = parts[0].substring(2).split(",");
        String[] velocity = parts[1].substring(2).split(",");

        int x = Integer.parseInt(position[0]);
        int y = Integer.parseInt(position[1]);
        int vx = Integer.parseInt(velocity[0]);
        int vy = Integer.parseInt(velocity[1]);

        return new Robot(x, y, vx, vy);
    }
}
