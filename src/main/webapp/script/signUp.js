$(document).ready(function(){
		
		$('#name').blur(function(){
			validateName();
         });
			
		$('#email').blur(function(){
			$('#email').filter(function(){
				validateEmail();
			});
        });
			
		$('#pass').blur(function(){
			validatePass()
		});
		
		$('#cpass').blur(function(){
			validateCPaas();
		});
		
		$('#mobile').blur(function(){
			validateMob();
		})
		
		function validateName()
		{
			var valid = true;
			$("#emsgName").text("");
			var $regexname=/^[a-zA-Z]{3,}/;
			if (!($('#name').val()).match($regexname)) 
			{
				$("#emsgName").text("Please Enter a Valid Name!");
				valid=false;
            }
			return valid;
		}
		
		function validateEmail()
		{
				var valid = true;
			   $("#emsgEmail").text("");
			   var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
               if( !emailReg.test( $('#email').val())) 
               {
            	   $("#emsgEmail").text("Please Enter a Valid Email!");
            	   valid=false;
               } 
               return valid;
		}
		function validatePass()
		{
			var valid = true;
			$("#emsgPass").text("");
			if(($('#pass').val()).length<8)
			{
				$('#emsgPass').text("Password must contain 8 characters!");
			}
			return valid;
		}
		function validateCPaas()
		{
			var valid = true;
			$("#emsgcpass").text("");
			var pass = $('#pass').val();
			var cpass = $('#cpass').val();
			if(pass != cpass)
				{
					$('#emsgcpass').text("Password didn't matched");
					valid=false;
				}
			return valid;
		}
		function validateMob()
		{
			var valid= true;
			var mob = $('#mobile').val();
			$("#emsgmob").text("");
			if( mob.length!=10)
				{
					$('#emsgmob').text("Enter valid 10 digit mobile number!");
					valid=false;
				}
			return valid;
		}
		
		function checkISExit()
		{
			var flag;
			$.ajax({
				type : "post",
				url : "EmailValidation",
				dataType :"json",
				async : false,
				data:{email:$('#email').val()},
				success : function(data) {
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
				$("#emsgEmail").text("Email already taken!");
			}
			else
			{
				$("#emsgEmail").text("");
			}
			return flag;
		}
		
		function formValidate()
		{
			var valid= true;
			if(!validateName()){
					valid=false;
				}
			if(!validateEmail()){
				valid=false;
			}else{
				if(!checkISExit()){
					valid=false;
					}
			}
			if(!validatePass()){
				valid=false;
			}
			if(!validateCPaas()){
				valid=false;
			}
			if(!validateMob()){
				valid=false;
			}
			return valid;
		}
		
		$("#submit").click(function(event){
			if(formValidate()){
				return;
			}
			else
				{
				event.preventDefault();
				
				}
		});
		
	});