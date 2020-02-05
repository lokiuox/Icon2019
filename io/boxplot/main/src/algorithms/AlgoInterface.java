package algorithms;

import java.util.List;

public interface AlgoInterface {
    List<Node> findPath();
    void setInitialNode(Node initialNode);
    void setFinalNode(Node finalNode);

}
