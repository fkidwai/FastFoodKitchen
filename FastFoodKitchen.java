package fastfoodkitchen;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * ITSC 1213
 * University of North Carolina at Charlotte
 */

public class FastFoodKitchen {

    private ArrayList<BurgerOrder> orderList = new ArrayList<BurgerOrder>();
    private ArrayList<BurgerOrder> completeOrderList = new ArrayList<BurgerOrder>();

    private static int nextOrderNum = 1;
    private static String orderFile = "burgerOrders.csv";
    private static String orderFileNew = "burgerOrders2.csv";
    private static String reportFile = "dayReport.csv";
    private int totalHam = 0;
    private int totalCheese = 0;
    private int totalVeggie = 0;
    private int totalSoda = 0;
    private int totalOrder = 0;

    FastFoodKitchen() {
        readFromFile();
    }

    private void readFromFile() {
        try {
            BufferedReader fr = new BufferedReader(new FileReader(orderFile));
            String line;

            line = fr.readLine();
            int numHamburger = 0;
            int numCheesesbuergers = 0;
            int numVeggieburgers = 0;
            int numSodas = 0;
            Boolean toGO = false;

            while ((line = fr.readLine()) != null) {
                String[] data = line.split(",");
                numHamburger = Integer.valueOf(data[0]);
                numCheesesbuergers = Integer.valueOf(data[1]);
                numVeggieburgers = Integer.valueOf(data[2]);
                numSodas = Integer.valueOf(data[3]);
                toGO = Boolean.valueOf(data[4]);

                orderList.add(new BurgerOrder(
                        numHamburger, numCheesesbuergers, numVeggieburgers,
                        numSodas, toGO, getNextOrderNum()));
                completeOrderList.add(new BurgerOrder(
                        numHamburger, numCheesesbuergers, numVeggieburgers,
                        numSodas, toGO, getNextOrderNum()));
                incrementNextOrderNum();
            }

            fr.close();

        } catch (FileNotFoundException e) {
            System.out.println("File burgerOrders.csv Not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Cannot read burgerOrders.csv");
            e.printStackTrace();
        }

    }

    public void writeDayReport() {

        try {
            BufferedWriter fr = new BufferedWriter(new FileWriter(reportFile));
            String line = "";

            fr.write("orderID,numHamburgers,numCheesebuergers,numVeggieburgers,numSodas,toGo,status\n");
            int orderNum = 0;
            int numHamburger = 0;
            int numCheesesbuergers = 0;
            int numVeggieburgers = 0;
            int numSodas = 0;
            Boolean toGO = false;
            String status = "";

            for (int i = 0; i < completeOrderList.size(); i++) {
                BurgerOrder order = completeOrderList.get(i);
                orderNum = order.getOrderNum();
                numHamburger = order.getNumHamburger();
                numCheesesbuergers = order.getNumCheeseburgers();
                numVeggieburgers = order.getNumVeggieburgers();
                numSodas = order.getNumSodas();
                toGO = order.isOrderToGo();
                status = order.getStatus();
                line = String.format(
                        "%d,%d,%d,%d,%d,%s,%s\n", orderNum, numHamburger,
                        numCheesesbuergers, numVeggieburgers, numSodas,
                        String.valueOf(toGO), status);
                fr.write(line);
            }

            fr.write(String.format("Total %d orders today\n", totalOrder));
            line = String.format(",%d,%d,%d,%d\n", totalHam, totalCheese, totalVeggie, totalSoda);
            fr.write(line);

            fr.close();

        } catch (FileNotFoundException e) {
            System.out.println("File dayReport.csv Not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File dayReport.csv Not found");
            e.printStackTrace();
        }

    }

    public void savePendingOrder() {

        try {
            BufferedWriter fr = new BufferedWriter(new FileWriter(orderFileNew));
            String line = "";

            fr.write("orderID,numHamburgers,numCheesebuergers,numVeggieburgers,numSodas,toGo\n");
            int orderNum = 0;
            int numHamburger = 0;
            int numCheesesbuergers = 0;
            int numVeggieburgers = 0;
            int numSodas = 0;
            Boolean toGO = false;

            for (int i = 0; i < orderList.size(); i++) {
                BurgerOrder order = orderList.get(i);
                orderNum = order.getOrderNum();
                numHamburger = order.getNumHamburger();
                numCheesesbuergers = order.getNumCheeseburgers();
                numVeggieburgers = order.getNumVeggieburgers();
                numSodas = order.getNumSodas();
                toGO = order.isOrderToGo();
                line = String.format(
                        "%d,%d,%d,%d,%d,%s\n", orderNum, numHamburger,
                        numCheesesbuergers, numVeggieburgers, numSodas,
                        String.valueOf(toGO));
                fr.write(line);
            }

            fr.close();

        } catch (FileNotFoundException e) {
            System.out.println("File burgerOrders2.csv Not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File burgerOrders2.csv Not found");
            e.printStackTrace();
        }

    }

    public static int getNextOrderNum() {
        return nextOrderNum;
    }

    private void incrementNextOrderNum() {
        nextOrderNum++;
    }

    public int addOrder(int ham, int cheese, int veggie, int soda, boolean toGo) {
        int orderNum = getNextOrderNum();
        orderList.add(new BurgerOrder(ham, cheese, veggie, soda, toGo, orderNum));
        completeOrderList.add(new BurgerOrder(ham, cheese, veggie, soda, toGo, orderNum));
        incrementNextOrderNum();
        orderCallOut(orderList.get(orderList.size() - 1));
        return orderNum;

    }

    public boolean isOrderDone(int orderID) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderNum() == orderID) {
                return false;
            }
        }
        return true;
    }

    public boolean cancelOrder(int orderID) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderNum() == orderID) {
                orderList.remove(i);
                break;
            }
        }
        for (int i = 0; i < completeOrderList.size(); i++) {
            if (completeOrderList.get(i).getOrderNum() == orderID) {
                completeOrderList.get(i).setStatus("cancelled");
                return true;
            }
        }
        return false;
    }

    public int getNumOrdersPending() {
        return orderList.size();
    }

    public boolean cancelLastOrder() {
        int orderID = 0;
        if (!orderList.isEmpty()) { // same as if (orderList.size() > 0)
            orderID = orderList.get(orderList.size() - 1).getOrderNum();
            orderList.remove(orderList.size() - 1);
        }
        for (int i = 0; i < completeOrderList.size(); i++) {
            if (completeOrderList.get(i).getOrderNum() == orderID) {
                completeOrderList.get(i).setStatus("cancelled");
                return true;
            }
        }
        return false;
    }

    private void orderCallOut(BurgerOrder order) {
        if (order.getNumHamburger() > 0) {
            System.out.println("You have " + order.getNumHamburger() + " hamburgers");
        }
        if (order.getNumCheeseburgers() > 0) {
            System.out.println("You have " + order.getNumCheeseburgers() + " cheeseburgers");
        }
        if (order.getNumVeggieburgers() > 0) {
            System.out.println("You have " + order.getNumVeggieburgers() + " veggieburgers");
        }
        if (order.getNumSodas() > 0) {
            System.out.println("You have " + order.getNumSodas() + " sodas");
        }

    }

    public void completeSpecificOrder(int orderID) {
        BurgerOrder order = null;
        for (int i = 0; i < orderList.size(); i++) {
            order = orderList.get(i);
            if (orderList.get(i).getOrderNum() == orderID) {
                System.out.println("Order number " + orderID + " is done!");
                totalHam += order.getNumHamburger();
                totalCheese += order.getNumCheeseburgers();
                totalVeggie += order.getNumVeggieburgers();
                totalSoda += order.getNumSodas();
                totalOrder += 1;
                if (orderList.get(i).isOrderToGo()) {
                    orderCallOut(orderList.get(i));
                }
                orderList.remove(i);
                break;
            }
        }

        for (int i = 0; i < completeOrderList.size(); i++) {
            if (completeOrderList.get(i).getOrderNum() == orderID) {
                completeOrderList.get(i).setStatus("closed");
                ;
                break;
            }
        }

    }

    public void completeNextOrder() {
        int nextOrder = orderList.get(0).getOrderNum();
        completeSpecificOrder(nextOrder);

    }

    // Part 2
    public ArrayList<BurgerOrder> getOrderList() {
        return orderList;
    }

    public int findOrderSeq(int whatWeAreLookingFor) {
        for (int j = 0; j < orderList.size(); j++) {
            if (orderList.get(j).getOrderNum() == whatWeAreLookingFor) {
                return j;
            }
        }
        return -1;
    }

    // public int findOrderBin(int whatWeAreLookingFor) {
    // int left = 0;
    // int right = orderList.size() - 1;
    // while (left <= right) {
    // int middle = (left + right) / 2;
    // if (whatWeAreLookingFor < orderList.get(middle).getOrderNum()) {
    // right = middle - 1;
    // } else if (whatWeAreLookingFor > orderList.get(middle).getOrderNum()) {
    // left = middle + 1;
    // } else {
    // return middle;
    // }
    // }
    // return -1;
    // }

    public int findOrderBin(int orderID) {
        int left = 0;
        int right = orderList.size() - 1;
        while (left <= right) {
            int middle = ((left + right) / 2);
            if (orderID < orderList.get(middle).getOrderNum()) {
                right = middle - 1;
            } else if (orderID > orderList.get(middle).getOrderNum()) {
                left = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;

    }

    public void selectionSort() {
        for (int i = 0; i < orderList.size() - 1; i++) {
            int minIndex = i;
            for (int k = i + 1; k < orderList.size(); k++) {
                if (orderList.get(minIndex).getTotalBurgers() > orderList.get(k).getTotalBurgers()) {
                    minIndex = k;
                }
            }
            BurgerOrder temp = orderList.get(i);
            orderList.set(i, orderList.get(minIndex));
            orderList.set(minIndex, temp);
        }
    }

    public void insertionSort() {
        for (int j = 1; j < orderList.size(); j++) {
            BurgerOrder temp = orderList.get(j);
            int possibleIndex = j;
            while (possibleIndex > 0 && temp.getTotalBurgers() < orderList.get(possibleIndex - 1).getTotalBurgers()) {
                orderList.set(possibleIndex, orderList.get(possibleIndex - 1));
                possibleIndex--;
            }
            orderList.set(possibleIndex, temp);
        }
    }

    // public void selectionSort() { //weird method!
    //
    // for (int j = 0; j < orderList.size() - 1; j++) {
    // int minIndex = j;
    // for (int k = j + 1; k < orderList.size(); k++) {
    //
    // if (orderList.get(minIndex).getTotalBurgers() >
    // orderList.get(j).getTotalBurgers()){
    // minIndex = k;
    // }
    // }
    // BurgerOrder temp = orderList.get(j);
    // orderList.set(j, orderList.get(minIndex));
    // orderList.set(minIndex, temp);
    //
    // }
    // }

}
