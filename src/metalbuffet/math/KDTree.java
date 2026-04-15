package metalbuffet.math;

import arc.math.*;
import arc.struct.*;

public class KDTree<T> {
    public int k;
    public KDNode<T> root;

    public KDTree(int k, Seq<NodeConstructionData<T>> points) {
        this.k = k;
        root = construct(points, 0);
    }

    private KDNode<T> construct(Seq<NodeConstructionData<T>> points, int depth) {
        if (points.isEmpty()) return null;
        if (points.size == 1) return new KDNode<>(points.first());

        int dimension = depth % k;
        points.sort(point -> point.coordinates[dimension]);

        int median = points.size / 2;
        KDNode<T> node = new KDNode<>(points.get(median));

        NodeConstructionData<T>[] pointsArray = points.toArray(NodeConstructionData.class);
        NodeConstructionData<T>[] leftPoints = new NodeConstructionData[median];
        NodeConstructionData<T>[] rightPoints = new NodeConstructionData[(points.size - median) - 1];
        System.arraycopy(pointsArray, 0, leftPoints, 0, leftPoints.length);
        System.arraycopy(pointsArray, median + 1, rightPoints, 0, rightPoints.length);

        node.left = construct(new Seq<>(leftPoints), depth + 1);
        node.right = construct(new Seq<>(rightPoints), depth + 1);

        return node;
    }

    public KDNode<T> nearestNode(float[] position) {
        return recursiveNearest(root, position, 0);
    }

    private KDNode<T> recursiveNearest(KDNode<T> currentNode, float[] target, int depth) {
        if (currentNode == null) return null;

        int dimension = depth % k;
        boolean leftNext = target[dimension] < currentNode.coordinates[dimension];
        KDNode<T> nextNode = leftNext ? currentNode.left : currentNode.right;
        KDNode<T> otherNode = leftNext ? currentNode.right : currentNode.left;

        KDNode<T> temp = recursiveNearest(nextNode, target, depth + 1);
        KDNode<T> best = temp == null ? currentNode : (distanceSquared(temp.coordinates, target) < distanceSquared(currentNode.coordinates, target) ? temp : currentNode);

        float bestDistance = distanceSquared(best.coordinates, target);
        float planeDistance = Mathf.sqr(target[dimension] - currentNode.coordinates[dimension]);
        if (bestDistance >= planeDistance) {
            temp = recursiveNearest(otherNode, target, depth + 1);
            best = temp == null ? best : (distanceSquared(temp.coordinates, target) < distanceSquared(best.coordinates, target) ? temp : best);
        }

        return best;
    }

    public static float distanceSquared(float[] point1, float[] point2) {
        if (point1.length != point2.length) return 0f;

        float distance2 = 0f;
        for (int i = 0; i < point1.length; i++) {
            distance2 += Mathf.sqr(point2[i] - point1[i]);
        }

        return distance2;
    }

    public static class NodeConstructionData<T> {
        public float[] coordinates;
        public T object;

        public NodeConstructionData(float[] coordinates, T object) {
            this.coordinates = coordinates;
            this.object = object;
        }
    }

    public static class KDNode<T> {
        public float[] coordinates;
        public T object;
        public KDNode<T> left, right;

        public KDNode(float[] coordinates) {
            this.coordinates = coordinates;
        }

        public KDNode(NodeConstructionData<T> data) {
            this.coordinates = data.coordinates;
            this.object = data.object;
        }

        public boolean leaf() {
            return left == null && right == null;
        }
    }
}
