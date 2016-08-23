/*
 * ----------------------------------------------
 * Author   : Varun Pius Felix Rodrigues       --
 * UnivEmail: varunpius@ufl.edu                --
 * Advanced Data Structures Project            --
 * Main class java file                        --
 * Created by VarunPius.                       --
 * ----------------------------------------------
 * Edit History:                               --
 * ----------------------------------------------
 * Create/ChangeLog:                Date:      --
 * -------------------------------  -------------
 * Project Created                  3-20-2016  --
 * ----------------------------------------------
 */

import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class bbst {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        ArrayList<Node> NodeList = new ArrayList<Node>();
        String ipData;
        String[] ipDataOp;
        int nosOfIp;
        Node root;

        TreeFunc tree = new TreeFunc();

        try {
            nosOfIp = Integer.parseInt(br.readLine());

            for (int i = 0; i<=nosOfIp; i++){
                while ((ipData = br.readLine()) != null) {
                    ipDataOp = ipData.split(" ");                    
                    NodeList.add(new Node(ipDataOp[0],ipDataOp[1]));
                }
            }

        }catch (IOException err){
            err.printStackTrace();
        }

        int nodeCount = NodeList.size();
        root = tree.sortedArrayToBST(NodeList, 0, nodeCount - 1);
        tree.setParent(root);
        //System.out.println("Root is : " + root.ID);
        int ht = tree.height(root);
        //System.out.println("Ht of tree is : " + ht);
        tree.convertRedNode(root, ht);        
        RedBlackFunc rb = new RedBlackFunc(root);        
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        String s;        
        while ((s = br1.readLine()) !=null){
            
            String[] CmdIp = s.split("\\s+");
            if (CmdIp[0].equals("increase")){
                int idStr = Integer.parseInt(CmdIp[1]);
                int ctStr = Integer.parseInt(CmdIp[2]);
                Node op = rb.modifyCount(idStr, ctStr, 'i');
                System.out.println(op.Counter);
            }
            else if(CmdIp[0].equals("reduce")){
                int idStr = Integer.parseInt(CmdIp[1]);
                int ctStr = Integer.parseInt(CmdIp[2]);
                Node op = rb.modifyCount(idStr, ctStr, 's');
                System.out.println(op.Counter);
            }
            else if(CmdIp[0].equals("count")){
                int idStr = Integer.parseInt(CmdIp[1]);                
                Node op = rb.searchNode(idStr);
                System.out.println(op.Counter);
            }
            else if(CmdIp[0].equals("next")){
                int idStr = Integer.parseInt(CmdIp[1]);                
                rb.next(idStr);                
            }
            else if(CmdIp[0].equals("previous")){
                int idStr = Integer.parseInt(CmdIp[1]);
                rb.previous(idStr);
            }
            else if(CmdIp[0].equals("inrange")){
                int idStr1 = Integer.parseInt(CmdIp[1]);
                int idStr2 = Integer.parseInt(CmdIp[2]);
                int op = rb.countRange(root, idStr1, idStr2);
                System.out.println( op);
            }
            else if(CmdIp[0].equals("printTree")){
                tree.printLevelOrder(root);
                tree.preOrder(root);
            }
            else if(CmdIp[0].equals("quit")){
                break;
            }
            
        }
    }
}

class RedBlackFunc {
    Node root;

    RedBlackFunc(Node root){
        this.root = root;
    }

    /********************Functions for Red Black Tree*************************/

    public void rotateLeft(Node x) {
        // assert (h != null) && isRed(h.right);
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        if (y.left != null){
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null){
            root = y;
        }
        else if (x == x.parent.left){
            x.parent.left = y;
        }
        else x.parent.right = y;
        y.left = x;
        //y.color = y.left.color;
        //y.left.color = "Red";
        //x.N = h.N;
        //h.N = size(h.left) + size(h.right) + 1;
        //return x;
    }

    public void rotateRight(Node x) {
        // assert (h != null) && isRed(h.right);
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        if (y.right != null){
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null){
            root = y;
        }
        else if (x == x.parent.right){
            x.parent.right = y;
        }
        else x.parent.left = y;
        y.right = x;
        //y.color = y.left.color;
        //y.left.color = "Red";
        //x.N = h.N;
        //h.N = size(h.left) + size(h.right) + 1;
        //return x;
    }

    public Node minNode(Node r){
        Node tmp = r;

        if (tmp.left == null)
            return tmp;
        else
            return minNode(tmp.left);
    }

    public Node maxNode(Node x){
        //Node x = root;
        if(x.right == null)
            return x;
        else
            return maxNode(x.right);
    }

    /********************Insert Functions*********************************/

    public void insertNode(Node z){
        Node y = new Node();
        Node x = root;

        while (x != null){
            y = x;

            if (z.ID < x.ID)
                x = x.left;
            else if (z.ID > x.ID)
                x = x.right;            
        }
        z.parent = y;
        if (y == null)
            root = z;
        else if (z.ID < y.ID)
            y.left = z;
        else
            y.right = z;
        z.left = null;
        z.right = null;
        z.color = "Red";

        insertFixUp(z);
    }

    public void insertFixUp(Node z){
        Node y;
        while (z.parent.color.equals("Red")){
            if (z.parent == z.parent.parent.left){
                y = z.parent.parent.right;

                if (y!=null && y.color.equals("Red")){
                    z.parent.color = "Black";
                    y.color = "Black";
                    z.parent.parent.color = "Red";
                    z = z.parent.parent;
                }
                else if (z == z.parent.right){
                        z = z.parent;
                        rotateLeft(z);
                    }
                else{
                    z.parent.color = "Black";
                    z.parent.parent.color = "Red";
                    rotateRight(z.parent.parent);
                }
            }
            else
            {
                y = z.parent.parent.left;

                if (y!=null && y.color.equals("Red")){
                    z.parent.color = "Black";
                    y.color = "Black";
                    z.parent.parent.color = "Red";
                    z = z.parent.parent;
                }
                else if (z == z.parent.left){
                    z = z.parent;
                    rotateRight(z);
                }
                else{
                    z.parent.color = "Black";
                    z.parent.parent.color = "Red";
                    rotateLeft(z.parent.parent);
                }
            }

            if (z == root){
                break;
            }
        }

        root.color = "Black";
    }

    /********************Delete Functions*********************************/

    public void transplant(Node u, Node v) {
        if (u.parent == null)
            root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;
        if (u!=null && v!=null)
            v.parent = u.parent;
    }

    public void delete(Node z){
        Node y;
        Node x = null;
        y = z;
        String y_org_color = y.color;
        Node p=z.parent;        
        if (z.left == null){
            x = z.right;
            if(z.right!=null) {
                transplant(z, z.right);
            }
        }
        else if (z.right == null){
            x = z.left;
            if(z.left!=null) {
                transplant(z, z.left);
            }
        }
        else{
            y = minNode(z.right);
            y_org_color = y.color;
            x = y.right;
            if ((y.parent == z) && x!= null)
                x.parent = y;
            if (y.parent != z){
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z,y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if ((y_org_color.equals("Black")) && (x != null))
            deleteFixUp(x);
    }

    public void deleteFixUp(Node x){
        Node w;
        while (x != root && x.color.equals("Black")){
            if (x == x.parent.left){
                w = x.parent.right;
                if (w.color.equals("Red")){
                    w.color = "Black";
                    x.parent.color = "Red";
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color.equals("Black") && w.right.color.equals("Black")){
                    w.color = "Red";
                    x = x.parent;
                }
                else{
                    if (w.right.color.equals("Black")){
                        w.left.color = "Black";
                        w.color = "Red";
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = "Black";
                    w.right.color = "Black";
                    rotateLeft(x.parent);
                    x = root;
                }
            }
            else{
                w = x.parent.left;
                if (w.color.equals("Red")){
                    w.color = "Black";
                    x.parent.color = "Red";
                    rotateRight(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color.equals("Black") && w.left.color.equals("Black")){
                    w.color = "Red";
                    x = x.parent;
                }
                else if (w.left.color.equals("Black")){
                    w.right.color = "Black";
                    w.color = "Red";
                    rotateLeft(w);
                    w = x.parent.left;
                }
                else{
                    w.color = x.parent.color;
                    x.parent.color = "Black";
                    w.left.color = "Black";
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = "Black";
    }

    /********************Main Assignment Functions*********************************/

    public Node searchNode(int nos){
        Node x = root;

        if (x == null){
            return null;
        }

        Node tmp = x;
        int diffMin = Integer.MAX_VALUE;

        while (x != null){
            if (x.ID == nos)
                return x;
            else{
                int diff = Math.abs(nos - x.ID);
                if (diff<diffMin){
                    diffMin = diff;
                    tmp = x;
                }
                if (nos > x.ID){
                    x = x.right;
                }
                else{
                    x = x.left;
                }
            }
            }            
        return tmp;
    }  
    
    public int countRange(Node x, int low, int high){
        if (x == null)
            return 0;

        if (x.ID == high && x.ID == low)
            return x.Counter;

        if (x.ID <= high && x.ID >= low) {
            //case if current node is in range
            return x.Counter + countRange(x.left, low, high) + countRange(x.right, low, high);
        }
        else if (x.ID < low){
            //case when current node smaller than low
            return countRange(x.right, low, high);
        }
        else{
            //case when current node greater than high
            return countRange(x.left, low, high);
        }
    }
    
    public Node modifyCount(int nos, int ct, char c){
        Node x = root;

        int del_flag = 0;

        while (x != null){
            if (x.ID == nos){
                if (c == 's'){
                    //System.out.println("for reduction");
                    x.Counter = x.Counter - ct;
                    if (x.Counter <= 0){
                        del_flag = 1;
                        delete(x);
                        break;
                    }
                }
                else if(c == 'i'){
                    x.Counter = x.Counter + ct;
                }
                return x;
            }
            else if (nos < x.ID)
                x = x.left;
            else if (nos > x.ID)
                x = x.right;
        }
        if (del_flag !=1 && c!='s'){
            String idStr = Integer.toString(nos);
            String ctStr = Integer.toString(ct);
            Node nd = new Node(idStr, ctStr);
            nd.color = "Red";
            insertNode(nd);
            return nd;
        }
        else if(del_flag !=1 && c=='s'){
            Node nd = new Node();
            nd.ID=0;
            nd.Counter=0;
            return nd;
        }
        else {
            if (x.Counter < 0){
                Node nd = new Node();
                return nd;
            }

            return x;
        }
    }

    Node successor(Node x){
        
        if (x.right!= null) {
            return minNode(x.right);
        } 

        Node p = x.parent;
        while (p != null && x == p.right){
            x = p;
            p = p.parent;
        }        
        
        return p;
    }

    Node predecessor(Node x){
        
        if (x.left != null)
            return maxNode(x.left);

        Node p = x.parent;
        while (p != null && x == p.left){
            x = p;
            p = p.parent;
        }
        
        return p;

    }

    void previous(int nos) {
        Node x = searchNode(nos);

        if(x != null && x.ID < nos){
            System.out.println(x.ID + " " + x.Counter);
        }
        else{
            if(x == null){
                System.out.println("0 0");
            }
            else {
                Node p = predecessor(x);
                if (p != null) {
                    System.out.println(predecessor(x).ID + " " + predecessor(x).Counter);
                }
                else {
                    System.out.println("0 0");
                }
            }
        }
    }

    void next(int nos){
        Node x = searchNode(nos);
        //If the node value is greater than the given value, then the node returned by search function is the successor
        if(x != null && x.ID > nos){
            System.out.println(x.ID + " " + x.Counter);
        }
        else{
            Node tmp = successor(x);
            if(tmp != null){
                System.out.println(tmp.ID + " " + tmp.Counter);
            }
            else{
                System.out.println("0 0");
            }
        }
    }
}

class TreeFunc {
    //Node root;

    public Node sortedArrayToBST(ArrayList<Node> ndlst, int start, int end) {
        /* Base Case */
        if (start > end) {
            return null;
        }

        /* Get the middle element and make it root */
        int mid = (start + end) / 2;
        Node node = ndlst.get(mid);

        /* Recursively construct the left subtree and make it
         left child of root */
        node.left = sortedArrayToBST(ndlst, start, mid - 1);
        //node.left.parent = node;

        /* Recursively construct the right subtree and make it
         right child of root */
        node.right = sortedArrayToBST(ndlst, mid + 1, end);
        //node.right.parent = node;

        return node;
    }

    public void setParent(Node root){
        if(root.left != null) {
            root.left.parent = root;
            setParent(root.left);
        }
        if(root.right != null) {
            root.right.parent = root;
            setParent(root.right);
        }
    }

    public void preOrder(Node node) {
        if (node == null) {
            return;
        }
        if(node.parent != null)
            System.out.print(node.ID + " " + node.Counter + " Parent: " + node.parent.ID + "\n");
        else
            System.out.print(node.ID + " " + node.Counter + " Parent: is null as root" + "\n");
        preOrder(node.left);
        preOrder(node.right);
    }

    public void inOrder(Node node){
        if (node != null){
            inOrder(node.left);
            System.out.println(node.ID + " " + node.Counter + "\n");
            inOrder(node.right);
        }
    }

    int height(Node node){
        if (node == null)
            return 0;
        else
        {
            /* compute  height of each subtree */
            int lheight = height(node.left);
            int rheight = height(node.right);

            /* use the larger one */
            if (lheight > rheight)
                return(lheight+1);
            else return(rheight+1);
        }
    }

    void printGivenLevel (Node node ,int level){
        if (node == null)
            return;
        if (level == 1)
            System.out.print("color details: " + node.ID + " " + node.color + "\n");
        else if (level > 1)
        {
            printGivenLevel(node.left, level-1);
            printGivenLevel(node.right, level-1);
        }
    }

    void convertRedNode (Node node ,int level){
        if (node == null)
            return;
        if (level == 1)
            node.color = "Red";
        else if (level > 1)
        {
            convertRedNode(node.left, level-1);
            convertRedNode(node.right, level-1);
        }
    }

    void printLevelOrder(Node node){
        int h = height(node);
        int i;
        for (i=1; i<=h; i++)
            printGivenLevel(node, i);
    }
}

class Node {
    int ID;
    int Counter;
    Node left;
    Node right;
    Node parent;
    String color;

    Node(){
        ID = 0;
        Counter = 0;
        color = "Black";
        left = right = parent = null;
    }

    Node(String id, String cntr){
        ID = Integer.parseInt(id);
        Counter = Integer.parseInt(cntr);
        color = "Black";
        left = right = parent = null;
    }
}
