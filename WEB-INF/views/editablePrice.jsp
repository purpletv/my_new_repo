<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="eStoreProduct.model.admin.output.stockSummaryModel,java.util.List" %>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="ISO-8859-1">
<title>Price Review</title>
<link rel="stylesheet" type="text/css" href="./css/editablePrice.css">
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">

<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
<script>

$(document).ready(function() {
  showCategoriesCatalog();

  $(document).on('change', '#categoryDropdown', function(event) {
    event.preventDefault();
    var catg = $("#categoryDropdown").val();
    console.log("selected categoryyyyyy" + catg);
    retrieveCategorySpecificRecords(catg);
  });

  $(document).on('click', '#editprice-button', function(event) {
    event.preventDefault();
    var $row = $(this).closest('tr');
    var prod_id = $(this).data('prod-id');
    var prod_gstc_id = $(this).data('gstc-id');

    var prod_mrp = parseFloat($(this).data('mrp-id'));
    var prod_price2 = parseFloat($(this).data('price-id'));
    var prod_price = parseFloat($('#prodPrice-input-' + prod_id).val());

    if (prod_price > prod_mrp) {
      toastr.error("Price cannot be greater than MRP!");
      console.log("price>mrp");
    } else {
      updatePriceReview(prod_id, prod_mrp, prod_price);
      console.log("price less than mrp");
    }
  });

  function showCategoriesCatalog() {
    $.ajax({
      url: "CategoriesDropdownList",
      method: 'GET',
      success: function(response) {
        $('#categoryDropdown').html(response);
        console.log('Categories are brought');
      },
      error: function(xhr, status, error) {
        console.log('AJAX Error: ' + error);
      }
    });
  }

  function retrieveCategorySpecificRecords(catg) {
    $.ajax({
      url: "CategorySpecificRecordsForPriceReview",
      method: "GET",
      data: { catg: catg },
      success: function(response) {
        $('#tableContent').html(response);
      },
      error: function(xhr, status, error) {
        console.log('AJAX Error: ' + error);
      }
    });
  }
  function updatePriceReview(prod_id, prod_mrp, prod_price) {
	  event.preventDefault(); // Add this line to prevent form submission
	  $.ajax({
	    url: "updatePriceReview",
	    method: 'GET',
	    data: {
	      id: prod_id,
	      price: prod_price,
	      mrp: prod_mrp
	    },
	    success: function(response) {
	      $('#tableContent').html(response);
	      console.log('In the updation of master entry function');
	      showCategoriesCatalog();
	    },
	    error: function(xhr, status, error) {
	      console.log('AJAX Error: ' + error);
	    }
	  });
	}

});


</script>
</head>
<body>

 
    <div class="container mt-5">
      <div class="text-left mb-3">
        <select id="categoryDropdown" class="form-control">
          <!-- Options for categories -->
        </select>
      </div>
    </div>
<div id="tableContent">
<table id="tableData" class="table table-bordered table-hover">
    <thead class="thead-dark">
        <tr>
            <th>Product ID</th>
            <th>Product Title</th>
            <th>Product Category ID</th>
            <th>Product GST Category ID</th>
            <th>Product Brand</th>
            <th>Product MRP</th>
            <th>Product Price</th>
            <th>EDIT</th>
        </tr>
    </thead>
    <tbody>
        <% List<stockSummaryModel> stock2=(List<stockSummaryModel>)request.getAttribute("stocks1"); %>
        <% for (stockSummaryModel stock : stock2) { %>
         <tr <% if (stock.getPrice() > stock.getMrp()) { %>class="warning" <% } %>>
            <td name="prod_id"><%=stock.getId()%></td>
            <td><%= stock.getTitle() %></td>
            <td><%= stock.getProductCategory() %></td>
            <td id="prodGstcId-input" name="prod_gstc_id">
                <%= stock.getHsnCode() %>
            </td>
            <td><%= stock.getBrand() %></td>
            <td id="prodMrp-input"  name="prod_mrp">
               <%= stock.getMrp() %>
            </td>
             <td>
                <input type="text" id="prodPrice-input-<%= stock.getId() %>"  name="prod_price" value="<%= stock.getPrice() %>">
            </td>
            <td>
            	 <% if (stock.getPrice() < stock.getMrp()) { %>
               <button id="editprice-button" class="btn btn-success"  
  data-price-id="<%= stock.getPrice() %>"
  data-prod-id="<%= stock.getId() %>"
  data-mrp-id="<%= stock.getMrp() %>"
  onclick="updatePriceReview(event, '<%= stock.getId() %>', '<%= stock.getMrp() %>', '<%= stock.getPrice() %>')">
  UPDATE
</button>

                        <%}
            	 else{
                        	%>
                        	  <button id="editprice-button" class="btn btn-danger"  
  data-price-id="<%= stock.getPrice() %>"
  data-prod-id="<%= stock.getId() %>"
  data-mrp-id="<%= stock.getMrp() %>"
  onclick="updatePriceReview(event, '<%= stock.getId() %>', '<%= stock.getMrp() %>', '<%= stock.getPrice() %>')">
  UPDATE
</button>

                      <%} %>  
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>



</body>
</html>
