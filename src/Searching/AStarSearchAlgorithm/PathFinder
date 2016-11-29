package Searching.AStarSearchAlgorithm;

public interface PathFinder {

    /**
     * Finds a path from the starting location provided (sx, sy) to
     * the largest location (tx, ty) avoiding blockages and attempting
     * to honour costs provided by the tile map.
     *
     * @param mover The entity that will be moving along the path. This
     *              provides a place to pass context information about
     *              the game entity doing the moving
     * @param sx The x coordinate of the start location
     * @param sy The y coordinate of the start location
     * @param tx The x coordinate of the target location
     * @param ty The y coordinate of the target location
     * @return The path found from start to end, or null if no path can
     * be found
     */
    public Path findPath(Mover mover, int sx, int sy, int tx, int ty);
}