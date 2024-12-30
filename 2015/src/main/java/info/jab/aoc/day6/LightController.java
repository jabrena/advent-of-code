package info.jab.aoc.day6;
public class LightController {
    private LightGrid lightGrid;

    public LightController() {
        lightGrid = new LightGrid();
    }

    public void executeCommand(String command, int x1, int y1, int x2, int y2) {
        switch (command.toLowerCase().trim()) {
            case "turn on":
                lightGrid.turnOn(x1, y1, x2, y2);
                break;
            case "turn off":
                lightGrid.turnOff(x1, y1, x2, y2);
                break;
            case "toggle":
                lightGrid.toggle(x1, y1, x2, y2);
                break;
            default:
                throw new IllegalArgumentException("Comando no válido: " + command);
        }
    }

    public int getTotalLightsOn() {
        return lightGrid.getNumberOfLightsOn();
    }
} 