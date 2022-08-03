

Provide an implementation for the OrderOptimiser interface, as per the following requirements


    1.   The orderInstructions list is the desired distribution on market.  The OrderOptimiser should new/cancel/amend orders so that match this requested distributions
    2.   Order maybe have their qty amended up as well as down. Price amend is not supported.
    3.   Only 1 order may exist at a price level
    4.   getLeavesQty is the remaining volume on market.  Quantity amend is performed in respect the getQuantity value
    5.   You may ignore tick and lot size
    6.   Assume the implementation for the other interfaces will be provided, only the implementation for the OrderOptimiser needs to be written
    
Assumptions:
    0. Assuming unchecked exception thrown if some sort of error occurs from createOrder, amendOrderqty, cancenOrder
    1. Assuming "best effort", failures of createOrder, amendOrderqty, cancenOrder from one orderInstruction will not prevent other orderInstructions from continuing.
    2. failure in cancenOrder during the DeleteNew operation e.g. for "side change", the new order will not be posted.
    3. Assuming no throttling is necessary
        
