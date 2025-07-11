package view;

import controller.Controller;
import integration.CustomerID;
import integration.DiscountHandler;
import integration.DiscountHandlerSelector;
import integration.InventoryHandler;
import model.Amount;
import util.FileLogger;
import util.Logger;
import util.TotalRevenueFileOutput;

/**
 * This is a placeholder for the real view. It contains a hardcoded execution with calls to all
 * system operations in this controller.
 */
public class View {
    private final Controller contr;
    private final ErrorMessageHandler errorMsgHandler = new ErrorMessageHandler();
    private final Logger logger = FileLogger.getLogger();

    /**
     * Creates a new instance that uses the specified controller for alls to other layers.
     *
     * @param contr The controller to use for all calls to other layers.
     */
    public View(Controller contr){
        this.contr = contr;
        contr.addTotalRevenueObserver(new TotalRevenueView());
        contr.addTotalRevenueObserver(new TotalRevenueFileOutput());
    }

    /**
     * Performs a fake sale by calling all system operations in the controller.
     */
    public void runFakeExecution(){
        contr.startSale();
        String unexpectedErrorMsg = "An unexpected error occurred.";
        try{
            contr.scanItem("abc123");
            contr.scanItem("abc123");
            contr.scanItem("def456");
            contr.scanItem("invalidID");
        } catch (Exception e){ //according to page 188?
            logger.log(e.getMessage());
            errorMsgHandler.showErrorMessage(e.getMessage());
        }
        try{
            contr.scanItem(InventoryHandler.DATABASE_FAILURE_ID); //used to throw a database failure exception (hardcoded)
        } catch (Exception e){
            logger.log(e.getMessage());
            errorMsgHandler.showErrorMessage(e.getMessage());
        }
        CustomerID customer1 = new CustomerID(70, false);
        //DiscountHandler discountHandler = DiscountHandlerSelector.getDiscountHandler(customer1); //wrong
        //contr.applyDiscount(discountHandler); //wrong
        //contr.applyDiscount(discountHandler, customer1);

        contr.endSaleAndPay(new Amount(100), customer1);
        //=========================SECOND SALE=================================
        contr.startSale();
        try{
            contr.scanItem("abc123");
        }catch (Exception e){
            logger.log(e.getMessage());
            errorMsgHandler.showErrorMessage(unexpectedErrorMsg);
        }
        CustomerID customer2 = null;
        contr.endSaleAndPay(new Amount(100),customer2);
    }
}
