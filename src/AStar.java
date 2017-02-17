import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;


public class AStar implements IntrusionAlgorithm
{
	public RunSim run;
	
        class Node
        {
            int x;
            int y;
            int travelCost;
            int distanceToGoal;
            int totalCost;
            Node parent;
                    
            public void updateCosts(){};
            public void setParent(Node parentNode){};
            public Node setPosition(int x, int y){return this;};
            
        }
        
        LinkedList<Node> open = new LinkedList<>();
        LinkedList<Node> closed = new LinkedList<>();
        
        int directions[][] = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
        int endX;
        
	public AStar(RunSim r)
	{
            run = r;
                

            // A*
            Node tempNode = new Node();
            closed.add(tempNode.setPosition((int)(run.mouse.getX()),(int)(run.mouse.getY())));

            while (!open.isEmpty() && newClosedNode.x < endX)
            {
                Node newClosedNode = findNextBlock(open);
                open.remove(newClosedNode); // remove from open list
                closed.add(newClosedNode);
                
                addSuccessors(newClosedNode); // add valid adjacent nodes and make newClosedNode their parents
                
            }
	}

        private void addSuccessors(Node newNode){
            for (int i = 0; i < 8; i++){
                Node tempNode = newNode;
                int tempX = newNode.x + directions[i][0];
                int tempY = newNode.y + directions[i][1];
                tempNode.setPosition(tempX, tempY);
                if (tempNode.isValid());
                {
                    tempNode.updateCosts;
                    open.add(tempNode);
                }
                tempNode.setParent(newNode);
            }
        }
                        
	@Override
	public String[] getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameters(double[] values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] doNextMove(double x, double y) {
		double[] ret = {x + 1, y};
		return ret;
	}
	
	private int minimumIndex(double[] array)
	{
		double temp = array[0];
		int tempIndex = 0;
		for(int i=0; i < array.length; i++)
		{
			if(temp < array[i])
			{
				temp = array[i];
				tempIndex = i;
			}
		}
		return tempIndex;
	}

	@Override
	public void resetAlg() {
		// TODO Auto-generated method stub
		
	}
}
