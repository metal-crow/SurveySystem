$(document).ready(function() {
	$('#createuser').click(function() {
		var em = $('#email').val();
		var pass = $('#password').val();
		var first = $('#firstname').val();
		var last = $('#lastname').val();
		
		$.ajax({
			type: "POST",
			url: "http://localhost:4567/createUser",
			data: JSON.stringify({ "email": em, "password": pass, "first_name": first, "last_name": last}),
			dataType: "json",
			success: function() {
				alert("true");
			},
			error: function(){
				alert("false");
			}
		});
	});
});
