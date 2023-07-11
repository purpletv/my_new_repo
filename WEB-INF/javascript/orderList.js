

function applyDateFilter() {
          console.log("Filter orders through dates");
            var startDate = document.getElementById("startDate").value;
            var endDate = document.getElementById("endDate").value;
            $.ajax({
                url: "FilterOrdersThroughDates",
                method: 'POST',
                data: { startDate: startDate,
                		endDate: endDate
                	 }, // Pass the orderId as data
                success: function (response) {
                    $('#content').html(response); // Set the response HTML as the inner HTML of the orderproducts container
                },
                error: function (xhr, status, error) {
                    console.log('AJAX Error: ' + error);
                }
            });

          
        }
        function setMinEndDate() {
    var startDate = document.getElementById("startDate").value;
    document.getElementById("endDate").min = startDate;
}


var tokenElement = document.getElementById("tokenHolder");
var token = tokenElement.innerText;
// Use the "token" variable in your JavaScript code
console.log("Token: " + token);

var tokenElement1 = document.getElementById("tokenHolder1");
var token1 = tokenElement1.innerText;
// Use the "token" variable in your JavaScript code
console.log("Token1: " + token1);


var pageElement = document.getElementById("pageHolder");
var page = pageElement.innerText;
// Use the "page" variable in your JavaScript code
console.log("page: " + page);

$(document).on('click', '#red-button', function(event) {
	    event.preventDefault();
		var orderId = $(this).data('order-id'); 
	    
	    console.log('Clicked button with order ID:', orderId);
	   processOrder(orderId,token,page);
	   });
	 
	 function processOrder(orderId,token,page){
	    toastr.options = {
		  "closeButton": false,
		  "debug": false,
		  "newestOnTop": false,
		  "progressBar": false,
		  "positionClass": "toast-bottom-right",
		  "preventDuplicates": false,
		  "onclick": null,
		  "showDuration": "300",
		  "hideDuration": "1000",
		  "timeOut": "5000",
		  "extendedTimeOut": "1000",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
		}; 
	 //showSpinner();
	 	 $.ajax({ 	 
		      url: "processOrders",
		      method: 'GET',
		      data: { orderId: orderId,
		    	  adminId:1,
		    	  token:token,
		    	  page:page
		    	 },
		      success: function(response) {
		     // //hideSpinner();
		    	  $('#content').html(response);
		    	  toastr.success('Order has been processed');
		      },
		      error: function(xhr, status, error) {
		        console.log('AJAX Error: ' + error);
		      }
		    });
	 	}
	 
	 






function processedStatusFilter() {
  var processedStatusFilter = $('#processedStatusFilter').val();
	console.log('im here in status filtering');
  if (processedStatusFilter === 'unprocessed') {
    getUnprocessedOrders();
  } else if (processedStatusFilter === 'processed') {
    getProcessedOnlyOrders();
  } else {
    getAllOrders();
  }
}


function getAllOrders() {
//showSpinner();
  // Send an AJAX request to retrieve unprocessed orders
  $.ajax({
    url: 'listOrders',
    method: 'GET',
    success: function(response) {
      // Handle the response and update the order list
     // hideSpinner();
      $('#content').html(response);
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
}

function getUnprocessedOrders() {
//showSpinner();
  // Send an AJAX request to retrieve unprocessed orders
  $.ajax({
    url: 'unprocessedOrders',
    method: 'GET',
    success: function(response) {
      // Handle the response and update the order list
     // hideSpinner();
      $('#content').html(response);
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
}



function getProcessedOnlyOrders() {
//showSpinner();
  // Send an AJAX request to retrieve processed orders
  $.ajax({
    url: 'processedOnlyOrders',
    method: 'GET',
    success: function(response) {
      // Handle the response and update the order list
      //hideSpinner();
      $('#content').html(response);
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
}

var tokenElement = document.getElementById("tokenHolder");
var token = tokenElement.innerText;
// Use the "token" variable in your JavaScript code
console.log("Token: " + token);


var tokenElement1 = document.getElementById("tokenHolder1");
var token1 = tokenElement1.innerText;
// Use the "token" variable in your JavaScript code
console.log("Token1: " + token1);

var pageElement = document.getElementById("pageHolder");
var page = pageElement.innerText;
// Use the "page" variable in your JavaScript code
console.log("page: " + page);



function navigateToPage(page) {
//showSpinner();
  var nextPage = parseInt(page) + 1;
  nextPage -= 1;
  console.log('in navigation next', nextPage);
  /*window.location.href = "/listOrders?page=" + nextPage;*/

  $.ajax({
    url: "listOrdersForPagination",
    method: 'GET',
    data: { nextPage: nextPage,
    token:token,
    token1:token1 },
    success: function(response) {
     // hideSpinner();
      $('#content').html(response);
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
}




$(document).on('click', '#viewStatisticsBtn', function() {
  // Retrieve the order data from the server
  $.ajax({
    url: 'ordersForStatistics', // Replace with your actual server endpoint URL
    method: 'GET',
    success: function(data) {
      // Create the order statistics
      console.log('statistics inside');
      createOrderChart(data);

      // Show the statistics modal
      $('#statisticsModal').modal('show');
    },
    error: function(xhr, status, error) {
      console.log('AJAX Error: ' + error);
    }
  });
});

function createOrderChart(data) {
  const orderDates = data.map(order => order.orderDate);
  const orderCounts = countOrdersByDate(data);

  const formattedDates = orderDates.map(date => formatDateLabel(date));

  const orderChartCtx = document.getElementById('orderChart').getContext('2d');
  const orderChart = new Chart(orderChartCtx, {
    type: 'line',
    data: {
      labels: formattedDates,
      datasets: [
        {
          label: 'Number of Orders',
          data: orderCounts,
          backgroundColor: 'rgba(75, 192, 192, 0.5)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }
      ]
    },
    options: {
      scales: {
        x: {
          ticks: {
            maxTicksLimit: 10
          }
        },
        y: {
          beginAtZero: true
        }
      }
    }
  });
}
function formatDateLabel(value, format) {
  const date = new Date(value);
  let day = date.getDate().toString();
  let month = (date.getMonth() + 1).toString();
  const year = date.getFullYear().toString();

  if (format === 'mm/dd/yy') {
    // Swap day and month
    [day, month] = [month, day];
  }

  // Pad with leading zero if needed
  if (day.length === 1) {
    day = '0' + day;
  }
  if (month.length === 1) {
    month = '0' + month;
  }

  return `${day}/${month}/${year}`;
}

function countOrdersByDate(data) {
  const orderCounts = {};
  data.forEach(order => {
    const orderDate = order.orderDate;
    if (orderCounts[orderDate]) {
      orderCounts[orderDate]++;
    } else {
      orderCounts[orderDate] = 1;
    }
  });
  return Object.values(orderCounts);
}
