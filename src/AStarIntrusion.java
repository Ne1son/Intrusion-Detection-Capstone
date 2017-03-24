import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;


public class AStarIntrusion implements IntrusionAlgorithm
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
                    
            public void updateCosts(Node parentNode){
                distanceToGoal = (run.w - this.x) * 15;
                if (travelCost > parentNode.travelCost)
                {
                    if ((parentNode.x - x == 0) || (parentNode.y - y == 0))
                    {
                        travelCost = parentNode.travelCost + 10;
                    }
                    else
                    {
                        travelCost = parentNode.travelCost + 14;
                    }
                    parent = parentNode;
                }
                totalCost = distanceToGoal + travelCost;
                System.out.println("(" + parentNode.x + "," + parentNode.y + ") f " + distanceToGoal + " g " + travelCost + " h " + totalCost);

            };
            
            public void setParent(Node parentNode){
                parent = parentNode;
            };
            
            public Node setPosition(int x, int y){
                this.x = x;
                this.y = y;
                return this;
            };

            private boolean isValid() {
                //TO DO: check for out of bounds
                boolean valid = true;
                Cat tempcat = new Cat();
                for (int i = 0; i < run.cats.length && valid; i++){
                    tempcat = run.cats[i];
                    valid = !tempcat.detect(x, y, tempcat.getSensingRange());
                }
                    
                for(Node tempnode : closed) {
                    if(tempnode.x == x && tempnode.y == y) {
                        valid = false;
                    }
                }
                
                if (y < 0 || y > run.h - 1)
                    valid = false;
                    
                if (valid){
                    System.out.println("valid @ (" + x + "," + y + ")");}
                else{
                    System.out.println("invalid @ (" + x + "," + y + ")");}
                    
                return valid;
            }
            
        }
        
        LinkedList<Node> open = new LinkedList<>();
        LinkedList<Node> closed = new LinkedList<>();
        
        int directions[][] = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
        int endX;
        
        Node newClosedNode = new Node();

        
	public AStarIntrusion(RunSim r)
	{

            open.clear();
            closed.clear();
            
            

            run = r;
            
            // A*            


            
	}

        private void addSuccessors(Node parentNode){
            for (int i = 0; i < 8; i++){
                Node tempNode = new Node();//parentNode;
                int tempX = parentNode.x + directions[i][0];
                int tempY = parentNode.y + directions[i][1];
                tempNode.setPosition(tempX, tempY);
                if (tempNode.isValid())
                {
                    if (findNode(tempX, tempY) == null){
                        tempNode.travelCost = parentNode.travelCost + 1;
                        tempNode.updateCosts(parentNode);
                        open.add(tempNode);
                        System.out.println("Adding x:" + tempX + " y:" + tempY);

                    }
                    else
                    {
                        findNode(tempX, tempY).updateCosts(parentNode);
                    }
                }

                //tempNode.setParent(parentNode);
            }
        }
        
        private Node findNode(int x, int y){
            for(Node tempnode : open){
                if (tempnode.x == x && tempnode.y == y){
                    return tempnode;
                }
            }
            return null;
        }
        
        private Node findNextBlock(LinkedList<Node> open) {
            Node tempNode = open.getFirst();
            for (int i = 0; i < open.size(); i++) {
                if (tempNode.totalCost > open.get(i).totalCost) {
                    tempNode = open.get(i);
                }
            }
            return (tempNode);
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
            //newClosedNode = findNextBlock(open);
            // we need to check if open is empty because that means there's no path to the end
            open.remove(newClosedNode); // remove from open list
            closed.add(newClosedNode);   
            addSuccessors(newClosedNode); // add valid adjacent nodes and make newClosedNode their parents

            newClosedNode = findNextBlock(open);
            
            //System.out.println(newClosedNode.x + " " + newClosedNode.y);
            double[] ret = {newClosedNode.x, newClosedNode.y};
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

            newClosedNode.setPosition((int)(run.mouse.getX()),(int)(run.mouse.getY()));
            closed.add(newClosedNode);
            
            addSuccessors(newClosedNode); // add valid adjacent nodes and make newClosedNode their parents
            newClosedNode = findNextBlock(open);
                
        }
}