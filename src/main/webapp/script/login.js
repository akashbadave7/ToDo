$(document).ready(function(){

	function validateLogin()
		{ 
			var flag;
			$.ajax({
				type: "post",
				url : "loginValidation",
				dataType : "json",
				async : false,
				data :{email : $('#email').val()},
				success : function(data){
					console.log(data.valid);
		            flag=data.valid;
				},
					error:function(error){
		            console.log(error);
		        }
			});
			console.log(flag);
			if(flag==false)
			{
				$("#errmsg").text("Email and Password dint't match!");
			}
			else
			{
				$("#errmsg").text("");
			}
			return flag;
		}
	
	$('#submit').click(function(event){
		if(validateLogin()){
			return;
		}
		else
			{
			event.preventDefault();
			
			}
	});
});