package com.patterson.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class IDAStarGraph<T extends Comparable<T>> {

    /*
     path              current search path (acts like a stack)
     node              current node (last node in current path)
     g                 the cost to reach current node
     f                 estimated cost of the cheapest path (root..node..goal)
     h(node)           estimated cost of the cheapest path (node..goal)
     cost(node, succ)  step cost function
     is_goal(node)     goal test
     successors(node)  node expanding function, expand nodes ordered by g + h(node)
     ida_star(root)    return either NOT_FOUND or a pair with the best path and its cost

     procedure ida_star(root)
       bound := h(root)
       path := [root]
       loop
         t := search(path, 0, bound)
         if t = FOUND then return (path, bound)
         if t = ∞ then return NOT_FOUND
         bound := t
       end loop
     end procedure

     function search(path, g, bound)
       node := path.last
       f := g + h(node)
       if f > bound then return f
       if is_goal(node) then return FOUND
       min := ∞
       for succ in successors(node) do
         if succ not in path then
           path.push(succ)
           t := search(path, g + cost(node, succ), bound)
           if t = FOUND then return FOUND
           if t < min then min := t
           path.pop()
         end if
       end for
       return min
     end function
     */

    final int VMAX = 10;
    Stack<Graph.Vertex<T>> path = new Stack<>();
    Graph.Vertex<T> goal;


    public IDAStarGraph(){}

    public List<Graph.Vertex<T>> idaStar(Graph.Vertex<T> start, Graph.Vertex<T> goal) {

        this.goal = goal;

        float max = heuristicCostEstimate(start,goal); //initial bound
        path.clear(); //delete old path
        path.push(start);


        while(true) {
            Result result = Search(path, 0, max );
            if (result.type == 1) { //found or not found
                    return path;
            }
            else if (result.type == 2) {
                if (result.value == Float.MAX_VALUE) {
                    System.out.println("Not found");
                    return path;
                }else{
                    max = result.value;
                }
            }
        }
    }

    public Result Search(Stack<Graph.Vertex<T>> path, float g, float max) {


        Result result;
        if(path.empty()){
            result = new Result(2, max+1);
            return result;
        }
        Graph.Vertex<T> node = path.peek();
        float f = heuristicCostEstimate(node,goal) + g;


        if (f>max) {
            result = new Result(2, f);
            return result;
        }

        if (heuristicCostEstimate(node,goal) == 0) {
            result = new Result(1, 0);
            return result;
        }

        float min = Float.MAX_VALUE;
        for (Graph.Edge<T> e : node.getEdges()) {
            path.push(e.getToVertex());
            Result result1 = Search(path, g + e.getCost(), max);
            if (result1.type == 1) {
                return result1;
            }
            else if (result1.type == 2) {
                float newMin =  result1.value;
                if (newMin < min) {
                    min = newMin;
                }
            }
            path.pop();
        }

        result = new Result(2, min);
        return result;
    }

    protected float heuristicCostEstimate(Graph.Vertex<T> start, Graph.Vertex<T> goal) {
        return  (Math.abs(start.getX() - goal.getX()) + Math.abs(start.getY() - goal.getY()) )/ VMAX;
    }

    private static class Result{
        int type;
        float value;
        public  Result(int t, float v){
            type = t;
            value = v;
        }

    }
}
