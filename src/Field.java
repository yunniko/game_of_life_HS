import java.awt.*;
import java.util.Random;

public class Field {
    protected boolean[][] field;
    final public int dimension;
    final public int generation;

    private Field nextGen;

    public Field(int dim) {
        this(dim, 0);
        init();
    }

    private Field(int dim, int gen) {
        this.dimension = dim;
        this.generation = gen;
        field = new boolean[dimension][dimension];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Field.class) {
            Field otherField = (Field) obj;
            if (dimension == otherField.dimension) {
                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        Point p = new Point(i,j);
                        if (isAlive(p) != otherField.isAlive(p)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void init() {
        Random rand = new Random();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                field[i][j] = rand.nextBoolean();
            }
        }
    }

    private int getLivingNeighboursCount(Point p) {
        int xLeft = (p.x == 0) ? dimension - 1 : p.x - 1;
        int xRight = (p.x == dimension - 1) ? 0 : p.x + 1;

        int yUp = (p.y == 0) ? dimension - 1 : p.y - 1;
        int yDown = (p.y == dimension - 1) ? 0 : p.y + 1;

        Point[] points = new Point[] {
                new Point(xLeft, yUp),
                new Point(xLeft, p.y),
                new Point(xLeft, yDown),
                new Point(p.x, yUp),
                new Point(p.x, yDown),
                new Point(xRight, yUp),
                new Point(xRight, p.y),
                new Point(xRight, yDown),
        };

        int count = 0;

        for (Point nP : points) {
            count += isAlive(nP) ? 1 : 0;
        }
        return count;
    }

    public Field nextGeneration() {
        if (nextGen == null) {
            Field newField = new Field(dimension, generation + 1);
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int nCount = getLivingNeighboursCount(new Point(i, j));
                    if (nCount == 3) {
                        newField.field[i][j] = true;
                    } else if (nCount == 2 && field[i][j]) {
                        newField.field[i][j] = true;
                    } else {
                        newField.field[i][j] = false;
                    }
                }
            }
            nextGen = newField;
        }
        return nextGen;
    }

    public int getAliveCount() {
        int aliveCount = 0;
        for (boolean[] row : field) {
            for (boolean isCellAlive: row) {
                aliveCount += isCellAlive ? 1 : 0;
            }
        }
        return aliveCount;
    }

    public boolean isAlive(Point p) {
        return field[p.x][p.y];
    }
}
