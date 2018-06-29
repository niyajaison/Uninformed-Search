
import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author Niya Jaison | UTA ID : 1001562701 | Net ID:nxj2701
 * References:    https://www.programcreek.com/2013/01/sort-linkedlist-of-user-defined-objects-in-java/
 *                 Artificial Intelligence: A Modern Approach, 3rd Edition by Stuart Russell, Peter Norvig.
 * The find_route class implements the Uninformed search.The implemented algorithm always:
 *   a. finds a route between the origin and the destination, as long as such a route exists.
 *   b. terminates and reports that no route can be found when indeed no route exists that connects source and destination.
 *   c. returns optimal routes.
 */
public class find_route {
    
    /**
     * The member variables of the class :
     *     1.inputSet        -A Map<String, Double> to store the data from the input file
     *  2.fringeList     -An ArrayList<Node> for implementing the fringe
     *  3.closedStates     -An ArrayList<String> for storing the visited states
     *  4.startNode        -A Node variable for storing the source node
     *  5.goalState        -A String variable for storing the destination state name(provided by the user)
     */
    public static Map<String, Double> inputSet;
    public static ArrayList<Node> fringeList;
    public static ArrayList<String> closedStates;
    public static Node startNode;
    public static String goalState;
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        String inputFileName = args[0];
        startNode = new Node(args[1],null,0,0);
        goalState=args[2];
        inputSet = new LinkedHashMap<String,Double>();
        readDataFromFile(inputFileName);
        fringeList=new ArrayList<Node>();
        fringeList.add(startNode);
        closedStates=new ArrayList<String>();
        Node goalNode = extractNodeFromFringe(); /**Call the user defined function for extracting the node from the fringe*/
        
        if (goalNode==null) {/**The if loop is executed if no route is found*/
            System.out.println("distance: infinty\nroute:\n none");
        }
        else {
            generatePath(goalNode);/**Calls the user defined function for retracing the path from the goal node*/
        }
    }
    
    /**
     * @author Niya Jaison | UTA ID : 1001562701
     * Input :  void
     * Output :  Node
     * Function: Get the successor of the extracted node.The function loops until a goal state is found or the fringe becomes empty.
     *              The function returns the node of the goal state.
     */
    
    public static Node extractNodeFromFringe() {
        Node extractedNode,goalNode;
        
        goalNode = null;
        
        while(fringeList.isEmpty()==false) {/**The below line of codes will loop until the fringeList becomes empty or a goal state is found*/
            extractedNode = fringeList.remove(0);/**Since Uniformed cost is implemented, the first element of the fringe is extracted.*/
            
            if((extractedNode.getNodeStateName()).equalsIgnoreCase(goalState)){        /**Check if the extracted node is the goal state */
                //System.out.println("Result found: "+extractedNode.getNodeStateName());
                goalNode= extractedNode;
                break; /**If the goal node is found the while condition is terminated.*/
            }
            else {
                if(closedStates.isEmpty()) {
                    fringeList.addAll(getSuccessor(extractedNode));
                    closedStates.add((extractedNode.getNodeStateName()));
                }
                /**We check if the closed state contains the extracted node state name. Only if it does not contain the node,successor is retrieved*/
                else if(!(closedStates.contains((extractedNode.getNodeStateName())))){ /**Check if the extracted node is not already added in to closed set*/
                    
                    fringeList.addAll(getSuccessor(extractedNode)); /**Calling the user defined function for getting the list of successors and then adding the node of successors into fringe */
                    closedStates.add((extractedNode.getNodeStateName()));/**Adding the extracted nodes state name into the closed set.*/
                }
                Collections.sort(fringeList); /**Sorting the fringe list*/
                //System.out.println("fringe size"+fringeList.size());
                /*    System.out.println();
                 for(Node d:fringeList) {
                 System.out.print(d.getNodeStateName()+"("+d.getNodePathCost()+")"+"-");
                 }*/
            }
        }
        return goalNode;
    }
    
    /**
     * @author Niya Jaison | UTA ID : 1001562701
     * Input : The extracted node from the fringe
     * Output :  ArrayList containing the successor nodes
     * Function: Get the successor of the extracted node and create a node for each of the successor.
     */
    public static ArrayList<Node> getSuccessor(Node parentNode){
        ArrayList<Node> successorList = new ArrayList<Node>();
        for(String pathKey:inputSet.keySet()) {/**Iterates over the map which contains the data from the input file*/
            Node tempNode=null;
            StringTokenizer pathString=new StringTokenizer(pathKey, " ");/**StringTokenizer is used for extracting each of the component in one input line*/
            String stateOne=pathString.nextToken();
            String stateTwo = pathString.nextToken();
            /**The below if else condition checks if the extracted nodes name is present in one of the token, if yes we generate the node for the other*/
            if((stateOne.equalsIgnoreCase(parentNode.getNodeStateName())) ){
                tempNode=new Node(stateTwo,parentNode,parentNode.getNodeDepth()+1,parentNode.getNodePathCost()+inputSet.get(pathKey));
                successorList.add(tempNode);
            }
            else if(stateTwo.equalsIgnoreCase(parentNode.getNodeStateName())) {
                tempNode=new Node(stateOne,parentNode,parentNode.getNodeDepth()+1,parentNode.getNodePathCost()+inputSet.get(pathKey));
                successorList.add(tempNode);
            }
        }
        //fringeList.sort(fringeList.get(index));
        return successorList;/**Returns an ArrayList containing the nodes of all the successor*/
        
    }
    
    /**
     * @author Niya Jaison | UTA ID : 1001562701
     * Input : The goal node
     * Output :  void
     * Function: Retraces the path from the goal node to the source node and displays the output in the required format.
     */
    public static void generatePath(Node goalNode) {
        Node currentNode;
        double totalDistance =goalNode.getNodePathCost();
        ArrayList<String>outputDisplay=new ArrayList<String>();
        currentNode=goalNode;
        /**Storing the path detail as we retrace the node back to the source node(for which parent = null)*/
        while(currentNode.getNodeParent()!=null) {
            String outputString=currentNode.getNodeParent().getNodeStateName()+ " to "+currentNode.getNodeStateName();
            double distance=currentNode.getNodePathCost()-(currentNode.getNodeParent().getNodePathCost());
            outputString+=" "+distance;
            outputDisplay.add(0, outputString);
            currentNode=currentNode.getNodeParent();
        }
        /**Display the stored output*/
        System.out.println("distance: "+totalDistance+"\n"+"route:");
        //System.out.println("");
        for(String outputLine:outputDisplay) {
            System.out.println(outputLine);
        }
    }
    
    
    /**
     * @author Niya Jaison | UTA ID : 1001562701
     * Input : String - User provided filename
     * Output :  void
     * Function: Read the data from the input file and store it into a map
     */
    public static void readDataFromFile(String inputFileName) {
        try {
            Scanner ioFile = new Scanner(new File(inputFileName));/**uses a scanner for accessing the .txt file*/
            String ioFileData;
            while(!(ioFileData=ioFile.nextLine().toString()).equals("END OF INPUT")) {/**iterate till the "END OF INPUT" line is reached*/
                //= file_scanner.nextLine();
                /**The below set of code is used to read the msg from the system line by line and then load it in to the queue(map) as key and value*/
                /**The message retrieved from the file is stored in the format of key: "<SourceCity>-<DestCity> and message: <PathCost>*/
                StringTokenizer fileStringToken = new StringTokenizer(ioFileData, " ");
                String keyForMap= fileStringToken.nextToken()+" "+fileStringToken.nextToken();
                double dataForMap = (double)Integer.parseInt(fileStringToken.nextToken());
                inputSet.put(keyForMap, dataForMap);/**Adding the message read into the map one by one.*/
            }
            ioFile.close();/**closing the file*/
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            //e.printStackTrace();
        }
    }
}

/**
 * @author Niya Jaison | UTA ID : 1001562701
 * The class is used to represent the node in a Uninformed search algorithm.
 * The class implements the Comparable interface so as to perform the uniformed cost search (sorting in the fringe).
 */
class Node implements Comparable<Node>{
    /**
     * The properties of a node:
     * 1. nodeStateName     - The name of the state for which the node is created. Type : String
     * 2. nodeParent     - The Parent of the current node. Type: Node
     * 3. nodeDepth        - The level of the current node. Type : Integer
     * 4. nodePathCost    - The cumulative cost to reach the current node. Type: Double*/
    private String nodeStateName;
    private Node nodeParent;
    private Integer nodeDepth;
    private double nodePathCost;
    
    /**
     * @author Niya Jaison | UTA ID : 1001562701
     * Input : The name of the state.
     * Output :  void
     * Function: The constructor for initializing the member variables of a node
     */
    public Node(String nodeStateName,Node nodeParent,Integer nodeDepth,double nodePathCost){
        this.nodeStateName = nodeStateName;
        this.nodeParent=nodeParent;
        this.nodeDepth=nodeDepth;
        this.nodePathCost=nodePathCost;
        
    }
    
    public String getNodeStateName() {
        return nodeStateName;
    }
    
    public Node getNodeParent() {
        return nodeParent;
    }
    
    public Integer getNodeDepth() {
        return nodeDepth;
    }
    
    public double getNodePathCost() {
        return nodePathCost;
    }
    @Override
    /**Overriding the CompareTo function for implementing the sort functionality needed in Uniformed cost search
     * Reference: https://www.programcreek.com/2013/01/sort-linkedlist-of-user-defined-objects-in-java/
     */
    public int compareTo(Node node) {
        // TODO Auto-generated method stub
        double tempCost=node.nodePathCost;
        if (this.nodePathCost > tempCost) {
            return 1;
        } else if (this.nodePathCost == tempCost) {
            return 0;
        } else {
            return -1;
        }
        //return 0;
    }
}

