
<div class="container">
 			<form id="register-form" action="SignupPage" method="post" role="form">
 			<h3>Sign up</h3>
           
              <div class="form-group"> 	 
                <label for="name"><span class="req" style="color:red;">* </span> Name </label>
                    <input class="form-control" type="text" name="name" id ="name"  placeholder="Enter name" /> 
                 <!--        <p><span class="emsgName hidden" >Please Enter a Valid Name!</span></p>  --> 
                        <p><span id="emsgName" ></span></p>  
            	</div>

				<div class="form-group">
                <label for="email"><span class="req" style="color:red;">* </span> Email </label> 
                    <input class="form-control" type="text" name="email" id = "email" placeholder="Enter email" />   
                  		<p><span id="emsgEmail" ></span></p>
                  		<p><span id="emsgExit" ></span></p>
            	</div>
				 <div class="form-group">
                	<label for="password"><span class="req" style="color:red;">* </span> Password </label>
                    <input  name="password" type="password" class="form-control inputpass" minlength="8"  id="pass" name="pass" placeholder="Enter password"/> </p>
                 	<p><span id="emsgPass" ></span></p>  
                 </div>
                 
                 <div class="form-group">
                 	<label for="password"><span class="req" style="color:red;">* </span> Confirm-Password </label>
					<input type="password" class="form-control" placeholder="Retype Password" name="cpass" id="cpass" />
					<p><span id="emsgcpass" ></span></p>  
				</div>
                 
				<div class="form-group">
            	<label for="phonenumber"><span class="req" style="color:red;">* </span> Phone Number </label>
                    <input type="number" name="mobile" id="mobile" class="form-control phone" maxlength="13" min="0" placeholder="Enter 10 digit mobile number"/> 
            		<p><span id="emsgmob" ></span></p>  
            	</div>
				
				<br>
				      <button type="submit" id="submit" class="btn btn-primary"><b>Sign up</b></button>
				      <span></span>
				      <a  class="btn btn-primary" href="#!/login" role="button"  style="margin-right: 20%; width:80px"><b>Cancel</b></a>
				<br>
				
			<%-- 	<% String message= (String) session.getAttribute("error");
               if(message!=null)
               {          	   	
               		out.println("<center><font color='red'><h4>" + message);
               		session.removeAttribute("error");
               }
               %>  --%>
</div>
