<%-- 
    <%
	
		if (session.getAttribute("email") == null) 
		{
			response.sendRedirect("login");
		}    
	%>
	 --%>
	
	<div class="container" style="position:relative;">
		 
			 <form action="logout" method="post">	   			 
	   			
				  <div class="row">
			 		<div class="col-sm-4">
					    <div class="well well-sm" style="background-color:lavender;">Email : <%=session.getAttribute("email") %></div>
					</div>
					<div class="col-sm-6">
					    <div class="well well-sm" style="background-color:lavenderblush;width:30%;">Name : <%= session.getAttribute("name") %></div>
					</div>
					<div class="col-sm-2">    
					    <button type="submit" class="btn btn-success">Logout</button>

				  	</div>
				 </div>
	   			
	    	</form>
	</div>
