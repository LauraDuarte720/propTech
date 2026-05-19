package co.edu.uniquindio.com.proptech.structures.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GraphEdge<T> {
    private GraphNode<T> target;
    private double weight;
}