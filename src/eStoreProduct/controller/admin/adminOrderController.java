package eStoreProduct.controller.admin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eStoreProduct.DAO.common.OrderDAO;
import eStoreProduct.model.admin.entities.orderModel;
import eStoreProduct.model.admin.output.stockSummaryModel;

@Controller
public class adminOrderController {

	private OrderDAO od;
	private orderModel om;
	private static final Logger logger = LoggerFactory.getLogger(adminOrderController.class);

	@Autowired
	public adminOrderController(OrderDAO ord, orderModel omd) {
	    od = ord;
	    om = omd;
	}


	   
	
	//retriveing the specifc page orders adn show in ordersList page
	@GetMapping("/listOrders")
	public String showOrders(Model model, @RequestParam(defaultValue = "0") int page) {
		logger.info("adminOrderController url: listOrders returns: orderList.jsp ");
		System.out.println("\n page:" + page);
		int pageSize = 5; // Number of records per page
		List<orderModel> orders = od.getOrders(page, pageSize);
		int totalPages = (int) Math.ceil(od.getTotalOrders() / (double) pageSize); // Calculate total pages
		if(totalPages==0) 
			totalPages=1;
		System.out.println("totalpages:\n" + totalPages);
		model.addAttribute("orders", orders);
		model.addAttribute("token", "All");
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages); // Add totalPages to the model

		return "orderList";
	}

	// navigating through the pages in the orderList page
	@GetMapping("/listOrdersForPagination")
	public String showOrdersForPagination(Model model, @RequestParam(value = "nextPage") Integer page,
			@RequestParam(value = "token") String token,@RequestParam(value = "token1") String token1) {
		logger.info("adminOrderController url: listOrders returns: orderList.jsp ");
		System.out.println("\n page:" + page);
		int pageSize = 5; // Number of records per page
		if (token.equals("ProcessedPaginationToken")) {
			List<orderModel> orders = od.getProcessedOnlyOrders(page, pageSize);
			int totalPages = (int) Math.ceil(od.getTotalProcessedRecords() / (double) pageSize); // Calculate total //
																								// pages
			System.out.println("totalpages:\n" + totalPages);
			model.addAttribute("token", "ProcessedPaginationToken");
			model.addAttribute("orders", orders);
			model.addAttribute("page", page);
			model.addAttribute("totalPages", totalPages); // Add totalPages to the model

		} else if (token.equals("UnProcessedPaginationToken")) {
			List<orderModel> orders = od.getUnprocessedOrders(page, pageSize);
			int totalPages = (int) Math.ceil(od.getTotalUnprocessedOrders() / (double) pageSize); // Calculate total // pages//
			
			System.out.println("totalpages:\n" + totalPages);
			model.addAttribute("token", "UnProcessedPaginationToken");
			model.addAttribute("orders", orders);
			model.addAttribute("page", page);
			model.addAttribute("totalPages", totalPages); // Add totalPages to the model
		} 
		else if(token1.equals("dateFilter")) {
			
		}
		else {
			model.addAttribute("token", "All");
			List<orderModel> orders = od.getOrders(page, pageSize);
			int totalPages = (int) Math.ceil(od.getTotalOrders() / (double) pageSize); // Calculate total pages
			
			System.out.println("totalpages:\n" + totalPages);
			model.addAttribute("orders", orders);
			model.addAttribute("page", page);
			model.addAttribute("totalPages", totalPages); // Add totalPages to the model
		}

		return "orderList";
	}

	
	

	// get the unprocessed orders\
	@GetMapping("/unprocessedOrders")
	public String unprocessedOrders(Model model, @RequestParam(defaultValue = "0") int page) {

		logger.info("adminOrderController  url: unprocessedOrders  returns: orderList.jsp ");

		int pageSize = 5; // Number of records per page
		List<orderModel> orders = od.getUnprocessedOrders(page, pageSize);
		int totalPages = (int) Math.ceil(od.getTotalUnprocessedOrders() / (double) pageSize); // Calculate total pages
		if(totalPages==0) 
			totalPages=1;
		System.out.println("totalpages:\n" + totalPages);
		model.addAttribute("orders", orders);
		model.addAttribute("token", "UnProcessedPaginationToken");
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages); // Add totalPages to the model

		return "orderList";
	}

	// get the processed orders\
	@GetMapping("/processedOnlyOrders")
	public String processedOnlyOrders(Model model, @RequestParam(defaultValue = "0") int page) {

		logger.info("adminOrderController  url: processedOnlyOrders  returns: orderList.jsp ");

		int pageSize = 5; // Number of records per page
		List<orderModel> orders = od.getProcessedOnlyOrders(page, pageSize);
		int totalPages = (int) Math.ceil(od.getTotalProcessedRecords() / (double) pageSize); // Calculate total pages
		if(totalPages==0) 
			totalPages=1;
		System.out.println("totalpages:\n" + totalPages);
		model.addAttribute("orders", orders);
		model.addAttribute("page", page);
		model.addAttribute("token", "ProcessedPaginationToken");
		model.addAttribute("totalPages", totalPages); // Add totalPages to the model

		return "orderList";
	}
	
	// processed the unprocessed orders\
		@GetMapping("/processOrders")
		public String processOrders(@RequestParam(value = "orderId") long orderId,
				@RequestParam(value = "adminId") int adminId,@RequestParam(value = "token") String token,
				@RequestParam(value = "page") int page,Model model) {

			logger.info("adminOrderController  url: processOrders  returns: filteredOrderList.jsp ");
			int pageSize = 5; // Number of records per page
			System.out.println("processing");
			System.out.println(orderId + "" + adminId);
			od.updateOrderProcessedBy(orderId, adminId);
			
			
			if (token.equals("ProcessedPaginationToken")) {
				List<orderModel> orders = od.getProcessedOnlyOrders(page, pageSize);
				int totalPages = (int) Math.ceil(od.getTotalProcessedRecords() / (double) pageSize); // Calculate total //
																										// pages
				System.out.println("totalpages:\n" + totalPages);
				if(totalPages==0) 
					totalPages=1;
				model.addAttribute("token", "ProcessedPaginationToken");
				model.addAttribute("orders", orders);
				model.addAttribute("page", page);
				model.addAttribute("totalPages", totalPages); // Add totalPages to the model

			} else if (token.equals("UnProcessedPaginationToken")) {
				List<orderModel> orders = od.getUnprocessedOrders(page, pageSize);
				int totalPages = (int) Math.ceil(od.getTotalUnprocessedOrders() / (double) pageSize); // Calculate total //
						
				// pages
				
				if(totalPages==0) 
					totalPages=1;System.out.println("totalpages:\n" + totalPages);
				model.addAttribute("token", "UnProcessedPaginationToken");
				model.addAttribute("orders", orders);
				model.addAttribute("page", page);
				model.addAttribute("totalPages", totalPages); // Add totalPages to the model
			} else {
				model.addAttribute("token", "All");
				List<orderModel> orders = od.getOrders(page, pageSize);
				int totalPages = (int) Math.ceil(od.getTotalOrders() / (double) pageSize); // Calculate total pages
				if(totalPages==0) 
					totalPages=1;
				System.out.println("totalpages:\n" + totalPages);
				model.addAttribute("orders", orders);
				model.addAttribute("page", page);
				model.addAttribute("totalPages", totalPages); // Add totalPages to the model
			}
			
			return "orderList";
		}

		//displaying statistics for clear understanding between stocks and reorderLevel
		@GetMapping("/ordersForStatistics")
		@ResponseBody
		public List<orderModel> showStatistics(Model model) {
			logger.info("adminStockController url: listStocksForPagination returns: stockSummary.jsp ");
			
			List<orderModel> orders = od.getAllOrders();
			
			model.addAttribute("orders", orders);
			return orders;
		}
		
		
		@PostMapping("/FilterOrdersThroughDates")
		public String filterOrdersThroughDates(@RequestParam(value = "startDate") String sdate,
		        @RequestParam(value = "endDate") String edate, Model model,
		        @RequestParam(defaultValue = "0") int page) {
		    logger.info("adminOrderController url: FilterOrdersThroughDates returns: orderList.jsp");
		    logger.info("generating the Orders filtered by dates");
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Date startDate = null;
		    Date endDate = null;
		    
		    try {
		        startDate = sdf.parse(sdate);
		        endDate = sdf.parse(edate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    
		    int pageSize = 5; // Number of records per page
		    
		    List<orderModel> orders = od.getOrdersByDate(startDate, endDate, page, pageSize);
		    
		    int totalPages = (int) Math.ceil(od.getTotalOrdersByDate(startDate, endDate) / (double) pageSize);
		    if (totalPages == 0) {
		        totalPages = 1;
		    }
		    
		    System.out.println("totalpages:\n" + totalPages);
		    model.addAttribute("orders", orders);
		    model.addAttribute("page", page);
		    model.addAttribute("totalPages", totalPages);
		    model.addAttribute("token1","dateFilter");
		    model.addAttribute("token","All");
		    return "orderList";
		}

		
}
